package com.smart.htu.api.network

import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseGradeDetailPost
import com.smart.htu.api.module.CourseGradeDetailRes
import com.smart.htu.api.module.CourseGradeRes
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.CourseSchedulePost
import com.smart.htu.api.module.CreditEntity
import com.smart.htu.api.module.EvaluationDetail
import com.smart.htu.api.module.GPAEntity
import com.smart.htu.api.module.GPAPost
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessageRes
import com.smart.htu.api.module.SelectEntity
import com.smart.htu.api.module.TEDetailPost
import com.smart.htu.api.module.TEEntity
import com.smart.htu.api.module.TermIndexEntity
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.api.module.TextbookSelectPost
import com.smart.htu.api.module.TodayCoursePost
import com.smart.htu.api.module.TodayCourseRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JWCAppService {

    @POST("dev-api/appapi/applogin")
    suspend fun login(@Body body: LoginPost): LoginJWCEntity

    @GET("dev-api/appapi/getIstoken")
    suspend fun checkToken(): LoginJWCEntity

    @POST("dev-api/appapi/Studentxszc/index")
    suspend fun getTermIndex(@Body body: GlobalTerm): TermIndexEntity

    @POST("dev-api/appapi/appkxjs/classroom")
    suspend fun classroomOccupation(@Body body: BuildingEntity): ClassroomOccupationEntity

    @POST("dev-api/appapi/Studentcj/data")
    suspend fun getCourseGrade(@Body body: GlobalTerm): CourseGradeRes

    @POST("dev-api/appapi/Studentcj/detail")
    suspend fun getGradeDetail(@Body body: CourseGradeDetailPost): CourseGradeDetailRes

    @POST("dev-api/appapi/Studentpjwj/teacher")
    suspend fun teacherEvaluation(@Body body: GlobalTerm): TEEntity

    @POST("dev-api/appapi/Studentxsxdjc/xdjcdatas")
    suspend fun getTextbook(@Body body: GlobalTerm): TextbookEntity

    @POST("dev-api/appapi/Studentxsxdjc/kxjcdatas")
    suspend fun getSelectableTextbook(@Body body: TextbookSelectPost): SelectEntity

    @POST("dev-api/appapi/Studentxsxdjc/yxjcdatas")
    suspend fun getSelectedTextbook(@Body body: TextbookSelectPost): SelectEntity

    @POST("dev-api/appapi/appqxkb/datagrkb")
    suspend fun getTodayCourse(@Body body: TodayCoursePost = TodayCoursePost()): TodayCourseRes

    @POST("dev-api/appapi/Studentxjkp/index")
    suspend fun getPersonalMessage(@Body body: Any = Object()): PersonalMessageRes

    @POST("dev-api/appapi/Studentkb/index")
    suspend fun getCourseSchedule(@Body body: CourseSchedulePost): CourseScheduleEntity

    @POST("dev-api/appapi/Studentpjwj/pjTea")
    suspend fun getTeacherEvaluationDetail(@Body body: TEDetailPost): EvaluationDetail

    @POST("dev-api/appapi/Studentcj/cjjdDatas")
    suspend fun getCourseGPA(@Body body: GPAPost): GPAEntity

    @POST("dev-api/appapi/Studentcj/kcdlxfDatas")
    suspend fun getAllCredit(@Body body: Any = Object()): CreditEntity

    @GET("/new/welcome.page")
    suspend fun getWelcomePage(): Response<ResponseBody>


}