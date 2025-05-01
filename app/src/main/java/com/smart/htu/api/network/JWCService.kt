package com.smart.htu.api.network

import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.CourseGrade
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.CourseSchedulePost
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessageRes
import com.smart.htu.api.module.SelectEntity
import com.smart.htu.api.module.TEEntity
import com.smart.htu.api.module.TermIndex
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.api.module.TextbookSelectPost
import com.smart.htu.api.module.TodayCoursePost
import com.smart.htu.api.module.TodayCourseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JWCService {

    @POST("dev-api/appapi/applogin")
    suspend fun login(@Body body: LoginPost): LoginJWCEntity

    @GET("dev-api/appapi/getIstoken")
    suspend fun checkToken(): LoginJWCEntity

    @POST("dev-api/appapi/Studentxszc/index")
    suspend fun getTermIndex(@Body body: GlobalTerm): TermIndex

    @POST("dev-api/appapi/appkxjs/classroom")
    suspend fun classroomOccupation(@Body body: BuildingEntity): ClassroomOccupationEntity

    @POST("dev-api/appapi/Studentcj/data")
    fun grade(@Body body: GlobalTerm): Call<CourseGrade>

    @POST("dev-api/appapi/Studentpjwj/teacher")
    fun teacherEvaluation(@Body body: GlobalTerm): Call<TEEntity>

    @POST("dev-api/appapi/Studentxsxdjc/xdjcdatas")
    fun getTextbook(@Body body: GlobalTerm): Call<TextbookEntity>

    @POST("dev-api/appapi/Studentxsxdjc/kxjcdatas")
    fun getSelectableTextbook(@Body body: TextbookSelectPost): Call<SelectEntity>

    @POST("dev-api/appapi/Studentxsxdjc/yxjcdatas")
    fun getSelectedTextbook(@Body body: TextbookSelectPost): Call<SelectEntity>

    @POST("dev-api/appapi/appqxkb/datagrkb")
    fun getTodayCourse(@Body body: TodayCoursePost = TodayCoursePost()): Call<TodayCourseResponse>

    @POST("dev-api/appapi/Studentxjkp/index")
    fun getPersonalMessage(@Body body: Any = Object()): Call<PersonalMessageRes>

    @POST("dev-api/appapi/Studentkb/index")
    fun getCourseSchedule(@Body body: CourseSchedulePost): Call<CourseScheduleEntity>

}