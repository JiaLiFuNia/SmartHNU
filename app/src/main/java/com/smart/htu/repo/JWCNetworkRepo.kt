package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.R
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseGrade
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.SelectEntity
import com.smart.htu.api.module.TEEntity
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.api.module.TextbookSelectPost
import com.smart.htu.api.module.TodayCourseResponse
import com.smart.htu.api.network.JWCService
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.PasswordRepo.Companion.JWC_PASSWORD
import com.smart.htu.utils.RSAUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JWCNetworkRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val jwcService: JWCService,
    private val dataStoreRepo: DataStoreRepo,
    private val passwordRepo: PasswordRepo
) {

    val scope = CoroutineScope(Dispatchers.IO)

    private val studentIdStateFlow = dataStoreRepo.observeStudentId()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeStudentId().first()
            }
        )

    suspend fun getTodayCourseService(): TodayCourseResponse? {
        val call = jwcService.getTodayCourse()
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getTodayCourseService()
                else
                    null
            }

            else -> null
        }
    }

    suspend fun getSelectableTextbookService(
        termCode: String,
        courseTaskCode: String
    ): SelectEntity? {
        val call = jwcService.getSelectableTextbook(TextbookSelectPost(termCode, courseTaskCode))
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getSelectableTextbookService(termCode, courseTaskCode)
                else
                    null
            }

            else -> null
        }
    }

    suspend fun getSelectedTextbookService(
        termCode: String,
        courseTaskCode: String
    ): SelectEntity? {
        val call = jwcService.getSelectedTextbook(TextbookSelectPost(termCode, courseTaskCode))
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getSelectedTextbookService(termCode, courseTaskCode)
                else
                    null
            }

            else -> null
        }
    }

    // 教材选订
    suspend fun getTextbookService(termCode: GlobalTerm): TextbookEntity? {
        val call = jwcService.getTextbook(termCode)
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getTextbookService(termCode)
                else
                    null
            }

            else -> null
        }
    }

    // 教师评价
    suspend fun getTeacherListService(termCode: GlobalTerm): TEEntity? {
        val call = jwcService.teacherEvaluation(termCode)
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getTeacherListService(termCode)
                else
                    null
            }

            else -> null
        }
    }

    // 教室查询
    suspend fun getClassroomOccupationService(
        building: BuildingEntity
    ): Result<ClassroomOccupationEntity> {
        try {
            val res = jwcService.classroomOccupation(building)
            return when (res.code) {
                200 -> Result.success(res)
                401 -> {
                    if (reLogin())
                        return getClassroomOccupationService(building)
                    else
                        Result.failure(Exception("获取失败"))
                }

                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception("获取失败"))
        }
    }

    // 成绩查询
    suspend fun getCourseGradeService(termCode: GlobalTerm): CourseGrade? {
        val call = jwcService.grade(termCode)
        val res = call.awaitResponse().body()
        return when (res?.code) {
            200 -> res
            401 -> {
                if (reLogin())
                    return getCourseGradeService(termCode)
                else
                    null
            }

            else -> null
        }
    }

    // 智慧教务登录
    suspend fun jwcLogin(
        username: String,
        password: String
    ): Result<LoginJWCEntity> {
        try {
            if (username == "" || password == "") {
                return Result.failure(Exception("用户名或密码不能为空"))
            }
            val publicKey = RSAUtil.getPublicKeyFromRaw(context, R.raw.public_key)
            val passwordEncrypt = publicKey?.let { RSAUtil.encryptText(password, it) }
            val logState = jwcService.login(LoginPost(username, passwordEncrypt ?: ""))
            Log.i("TAG666 jwclogin", logState.toString())
            return when (logState.code) {
                200 -> {
                    dataStoreRepo.setTokenValid(true)
                    Result.success(logState)
                }

                401 -> {
                    dataStoreRepo.setTokenValid(false)
                    Result.failure(Exception("智慧教务登录失败"))
                }

                else -> Result.failure(Exception("智慧教务登录失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun checkJWCTokenService(): Result<Boolean> {
        try {
            val res = jwcService.checkToken()
            return when (res.code) {
                200 -> Result.success(true)
                401 -> {
                    if (reLogin()) Result.success(true)
                    else Result.failure(Exception("false"))
                }

                else -> Result.failure(Exception("false"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun reLogin(): Boolean {
        val password = passwordRepo.getPassword(JWC_PASSWORD) ?: ""
        // val studentId = dataStoreRepo.observeStudentId().first()
        val res = jwcLogin(studentIdStateFlow.value, password)
        Log.i("TAG666 relogin", res.toString())
        res.onSuccess {
            dataStoreRepo.setJWCToken(it.user?.token ?: DEFAULT_TOKEN)
        }
        return res.isSuccess
    }

}