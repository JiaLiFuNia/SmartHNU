package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.R
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseGradeDetailPost
import com.smart.htu.api.module.CourseGradeDetailRes
import com.smart.htu.api.module.CourseGradeRes
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.CourseSchedulePost
import com.smart.htu.api.module.EvaluationQuestion
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessageRes
import com.smart.htu.api.module.SelectEntity
import com.smart.htu.api.module.TEDetailPost
import com.smart.htu.api.module.TEEntity
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.api.module.TextbookSelectPost
import com.smart.htu.api.module.TodayCourseRes
import com.smart.htu.api.network.JWCService
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
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

    private val loginStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    suspend fun getTEDetailService(
        syllabusEvaluateCode: String,
        teacherCode: String
    ): List<EvaluationQuestion>? {
        val res = jwcService.getTeacherEvaluationDetail(
            TEDetailPost(
                dgksdm = syllabusEvaluateCode,
                teadm = teacherCode
            )
        )
        return when (res.code) {
            200 -> res.evaluationQuestionList
            else -> null
        }
    }

    suspend fun getCourseScheduleService(
        week: String = "",
        section: String = ""
    ): CourseScheduleEntity? {
        val res = jwcService.getCourseSchedule(CourseSchedulePost(week, section))
        return when (res.code) {
            200 -> res
            else -> null
        }
    }


    suspend fun getPersonalMessageService(): PersonalMessageRes? {
        val res = jwcService.getPersonalMessage()
        return when (res.code) {
            200 -> res
            else -> null
        }
    }

    suspend fun getTodayCourseService(): TodayCourseRes? {
        val res = jwcService.getTodayCourse()
        return when (res.code) {
            200 -> res
            else -> null
        }
    }

    suspend fun getSelectableTextbookService(
        termCode: String,
        courseTaskCode: String
    ): SelectEntity? {
        val res = jwcService.getSelectableTextbook(TextbookSelectPost(termCode, courseTaskCode))
        return when (res.code) {
            200 -> res
            else -> null
        }
    }

    suspend fun getSelectedTextbookService(
        termCode: String,
        courseTaskCode: String
    ): SelectEntity? {
        val res = jwcService.getSelectedTextbook(TextbookSelectPost(termCode, courseTaskCode))
        return when (res.code) {
            200 -> res
            else -> null
        }
    }

    // 教材选订
    suspend fun getTextbookService(termCode: GlobalTerm): TextbookEntity? {
        val res = jwcService.getTextbook(termCode)
        return when (res.code) {
            200 -> res
            else -> null
        }
    }

    // 教师评价
    suspend fun getTeacherListService(termCode: GlobalTerm): TEEntity? {
        try {
            val res = jwcService.teacherEvaluation(termCode)
            return when (res.code) {
                200 -> res
                else -> null
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return null
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
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception("获取失败"))
        }
    }

    // 成绩查询
    suspend fun getCourseGradeService(termCode: GlobalTerm): Result<CourseGradeRes> {
        try {
            val res = jwcService.grade(termCode)
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 成绩详情查询
    suspend fun getCourseGradeDetailService(
        gradeCode: String
    ): Result<CourseGradeDetailRes> {
        try {
            val res = jwcService.gradeDetail(CourseGradeDetailPost(gradeCode))
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception("获取失败"))
        }
    }

    // 智慧教务登录
    suspend fun jwcLogin(
        username: String,
        password: String
    ): Result<LoginJWCEntity> {
        try {
            if (username == "" || password == "") {
                return Result.failure(Exception("学号或密码不能为空"))
            }
            val publicKey = RSAUtil.getPublicKeyFromRaw(context, R.raw.public_key)
            val passwordEncrypt = publicKey?.let { RSAUtil.encryptText(password, it) }
            val logState = jwcService.login(LoginPost(username, passwordEncrypt ?: ""))
            Log.i("TAG666 jwcLogin", logState.toString())
            return when (logState.code) {
                200 -> {
                    dataStoreRepo.setTokenValidity(true)
                    Result.success(logState)
                }

                else -> {
                    dataStoreRepo.setTokenValidity(false)
                    Result.failure(Exception(logState.msg.ifEmpty { "智慧教务登录失败" }))
                }
            }
        } catch (e: Exception) {
            Log.e("TAG666 jwcLogin", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun checkJWCTokenService(): Result<Boolean> {
        try {
            val res = jwcService.checkToken()
            return when (res.code) {
                200 -> Result.success(true)
                else -> {
                    if (reLogin()) Result.success(true)
                    else Result.failure(Exception(res.msg))
                }
            }
        } catch (e: Exception) {
            Log.e("TAG666 check token", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun reLogin(): Boolean {
        val password = passwordRepo.getPassword(JWC_PASSWORD) ?: DEFAULT_PASSWORD
        val res = jwcLogin(studentIdStateFlow.value, password)
        Log.i("TAG666 reLogin", res.toString())
        res.onSuccess {
            dataStoreRepo.setTokenValidity(true)
            dataStoreRepo.setJWCToken(it.user?.token ?: DEFAULT_TOKEN)
        }
        return res.isSuccess
    }

}