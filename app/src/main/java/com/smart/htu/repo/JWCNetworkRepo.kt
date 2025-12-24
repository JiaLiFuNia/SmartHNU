package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smart.htu.R
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseGradeDetailPost
import com.smart.htu.api.module.CourseGradeDetailRes
import com.smart.htu.api.module.CourseGradeRes
import com.smart.htu.api.module.CourseItemEntity
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.CourseSchedulePost
import com.smart.htu.api.module.CourseTimeEntity
import com.smart.htu.api.module.CreditItemEntity
import com.smart.htu.api.module.EvaluationQuestion
import com.smart.htu.api.module.GPAData
import com.smart.htu.api.module.GPAPost
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessageRes
import com.smart.htu.api.module.SelectEntity
import com.smart.htu.api.module.SelectableCourseTypeEntity
import com.smart.htu.api.module.TEDetailPost
import com.smart.htu.api.module.TEEntity
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.api.module.TextbookSelectPost
import com.smart.htu.api.module.TodayCourseRes
import com.smart.htu.api.network.JWCAppService
import com.smart.htu.api.network.JWCService
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_PASSWORD
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.PasswordRepo.Companion.JWC_PASSWORD
import com.smart.htu.utils.DateUtil.convertStringDateTimeToLocalDateTime
import com.smart.htu.utils.RSAUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JWCNetworkRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val jwcAppService: JWCAppService,
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

    private val tokenStateFlow = dataStoreRepo.observeJWCToken()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeJWCToken().first()
            }
        )

    suspend fun getCourseInfo(
        termCode: String,
        courseCode: String
    ): Result<List<CourseTimeEntity>> {
        try {
            val res = jwcService.getCourseInfo(
                termCode = termCode,
                courseCode = courseCode
            )
            return when (res.code()) {
                200 -> {
                    val pattern = """data\s*:\s*(\[\s*[\s\S]*?\s*])""".toRegex()
                    val match =
                        pattern.find(res.body()?.string() ?: "")?.groups?.get(1)?.value ?: ""
                    val type = object : TypeToken<List<CourseTimeEntity>>() {}.type
                    val courseInfo: List<CourseTimeEntity> = Gson().fromJson(match, type)
                    Result.success(courseInfo)
                }

                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getCourseRepo(courseTypeId: String): Result<List<CourseItemEntity>> {
        try {
            val res = jwcService.getCourseRepo(courseTypeId = courseTypeId)
            return when (res.code()) {
                200 -> Result.success(res.body()?.courseRepo ?: emptyList())
                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getSelectableCourseType(): Result<List<SelectableCourseTypeEntity>> {
        try {
            val res = jwcService.getSelectableCourseType()
            val courseTypeList = parseSelectableCourse(res.body()?.string() ?: "")
            return when (res.code()) {
                200 -> Result.success(courseTypeList)
                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getWebCookie(): Result<String> {
        try {
            val res = jwcAppService.getWelcomePage()
            return when (res.code()) {
                200 -> {
                    val cookie = res.headers()["Cookie"] ?: ""
                    Result.success(cookie)
                }

                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getCourseCreditService(): Result<List<CreditItemEntity>> {
        try {
            val res = jwcAppService.getAllCredit()
            return when (res.code) {
                200 -> Result.success(res.list)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getCourseGPAService(
        statisticalMethod: String, //fs
        type: String,
    ): Result<List<GPAData>> {
        try {
            val res = jwcAppService.getCourseGPA(GPAPost(statisticalMethod, type))
            return when (res.code) {
                200 -> Result.success(res.list)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getTEDetailService(
        syllabusEvaluateCode: String,
        teacherCode: String
    ): Result<List<EvaluationQuestion>> {
        try {
            val res = jwcAppService.getTeacherEvaluationDetail(
                TEDetailPost(
                    dgksdm = syllabusEvaluateCode,
                    teadm = teacherCode
                )
            )
            return when (res.code) {
                200 -> Result.success(res.evaluationQuestionList)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getCourseScheduleService(
        week: String = "",
        section: String = ""
    ): Result<CourseScheduleEntity> {
        try {
            val res = jwcAppService.getCourseSchedule(CourseSchedulePost(week, section))
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }


    suspend fun getPersonalMessageService(): Result<PersonalMessageRes> {
        try {
            val res = jwcAppService.getPersonalMessage()
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getTodayCourseService(): Result<TodayCourseRes> {
        try {
            val res = jwcAppService.getTodayCourse()
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getSelectableTextbookService(
        termCode: String,
        courseTaskCode: String
    ): Result<SelectEntity> {
        try {
            val res =
                jwcAppService.getSelectableTextbook(TextbookSelectPost(termCode, courseTaskCode))
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun getSelectedTextbookService(
        termCode: String,
        courseTaskCode: String
    ): Result<SelectEntity> {
        try {
            val res =
                jwcAppService.getSelectedTextbook(TextbookSelectPost(termCode, courseTaskCode))
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 教材选订
    suspend fun getTextbookService(termCode: GlobalTerm): Result<TextbookEntity> {
        try {
            val res = jwcAppService.getTextbook(termCode)
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 教师评价
    suspend fun getTeacherListService(termCode: GlobalTerm): Result<TEEntity> {
        try {
            val res = jwcAppService.teacherEvaluation(termCode)
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 教室查询
    suspend fun getClassroomOccupationService(
        building: BuildingEntity
    ): Result<ClassroomOccupationEntity> {
        try {
            val res = jwcAppService.classroomOccupation(building)
            return when (res.code) {
                200 -> Result.success(res)
                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 成绩查询
    suspend fun getCourseGradeService(termCode: GlobalTerm): Result<CourseGradeRes> {
        try {
            val res = jwcAppService.getCourseGrade(termCode)
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
            val res = jwcAppService.getGradeDetail(CourseGradeDetailPost(gradeCode))
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
            val logState = jwcAppService.login(LoginPost(username, passwordEncrypt ?: ""))
            Log.i("TAG666 jwcLogin", logState.toString())
            return when (logState.code) {
                200 -> {
                    dataStoreRepo.changeLoginJWCState(1)
                    Result.success(logState)
                }

                else -> {
                    dataStoreRepo.changeLoginJWCState(-1)
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
            if (tokenStateFlow.value.isEmpty())
                return Result.success(false)
            val res = jwcAppService.checkToken()
            return when (res.code) {
                200 -> {
                    Log.i("TAG666 check token", "valid")
                    Result.success(true)
                }

                else -> {
                    Log.i("TAG666 check token", "invalid, try reLogin")
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
        res.onSuccess {
            Log.i("TAG666 check token", "reLogin success ${it.msg}")
            dataStoreRepo.changeLoginJWCState(1)
            dataStoreRepo.setJWCToken(it.user?.token ?: DEFAULT_TOKEN)
        }.onFailure {
            Log.i("TAG666 check token", "reLogin failed ${it.message}")
            dataStoreRepo.changeLoginJWCState(-2)
        }
        return res.isSuccess
    }

}


fun parseSelectableCourse(html: String): List<SelectableCourseTypeEntity> {
    try {
        val courseTypeList = mutableListOf<SelectableCourseTypeEntity>()
        val doc = Jsoup.parse(html)
        val types =
            doc.select("div.layui-container ul div#bb1") + doc.select("div.layui-container ul div#bb2")
        types.forEach {
            val description = (it.selectFirst("div")?.attr("lay-tips") ?: "").split("<br>")
            val courseTypeId = it.selectFirst("div")?.attr("data-href") ?: ""
            val courseTypeName = it.selectFirst("div div.content div.text span")?.text() ?: ""
            val timeInfo = it.selectFirst("div div.content div.description")?.text()?.split(" ")
                ?: listOf("", "")
            val startTimeStr = "${timeInfo.getOrNull(0)} ${timeInfo.getOrNull(1)}"
            val endTimeStr = "${timeInfo.getOrNull(2)} ${timeInfo.getOrNull(3)}"
            courseTypeList.add(
                SelectableCourseTypeEntity(
                    courseTypeName = courseTypeName,
                    courseTypeId = courseTypeId.substringAfter("="),
                    courseTermString = description.first().split(":").last(),
                    description = description.takeLast(2).joinToString("，"),
                    startTime = convertStringDateTimeToLocalDateTime(
                        startTimeStr,
                        "yyyy-MM-dd HH:mm:ss"
                    ),
                    endTime = convertStringDateTimeToLocalDateTime(
                        endTimeStr,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                )
            )
        }
        return courseTypeList
    } catch (e: Exception) {
        Log.e("TAG666", "${e.message}")
        return emptyList()
    }
}