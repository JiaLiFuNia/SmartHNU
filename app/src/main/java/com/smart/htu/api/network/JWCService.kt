package com.smart.htu.api.network

import com.smart.htu.api.module.CourseRepoRes
import com.smart.htu.api.module.CourseScheduleJWCEntity
import com.smart.htu.api.module.CourseTypeInfoRes
import com.smart.htu.api.module.SelectCourseEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JWCService {

    @GET("/new/student/xsxk")
    suspend fun getSelectableCourseType(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop"
    ): Response<ResponseBody>

    @POST("/new/student/xsxk/xklx/{courseTypeId}/kxkc")
    @FormUrlEncoded
    suspend fun getCourseRepo(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop",
        @Path("courseTypeId") courseTypeId: String,
        @Field("page") page: Int = 1,
        @Field("rows") rows: Int = 500,
        @Field("sort") sort: String = "kcdlmc",
        @Field("order") order: String = "asc"
    ): Response<CourseRepoRes>

    @POST("/new/student/xsxk/jxrl")
    @FormUrlEncoded
    suspend fun getCourseInfo(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop",
        @Field("xnxqdm") termCode: String,
        @Field("kcrwdm") courseCode: String,
        @Field("_") t: Long = System.currentTimeMillis(),
    ): Response<ResponseBody>

    @POST("/new/student/xsxk/xklx/{courseTypeId}/add")
    @FormUrlEncoded
    suspend fun selectCourse(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop",
        @Path("courseTypeId") courseTypeId: String,
        @Field("kcrwdm") courseTaskCode: String,
        @Field("kcmc") courseName: String,
        @Field("qz") qz: String = "-1",
        @Field("hlct") hlct: String = "0",
        @FieldMap dynamicParam: Map<String, String>
    ): Response<SelectCourseEntity>

    @GET("/new/student/xsxk/xklx/{courseTypeId}/config")
    suspend fun getCourseTypeConfig(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop",
        @Path("courseTypeId") courseTypeId: String
    ): Response<CourseTypeInfoRes>

    @POST("/new/student/xsgrkb/getCalendarWeekDatas")
    @FormUrlEncoded
    suspend fun getCourseSchedule(
        @Header("referer") referer: String = "https://jwc.htu.edu.cn/new/desktop",
        @Field("xnxqdm") termCode: String,
        @Field("zc") week: String
    ): Response<CourseScheduleJWCEntity>

}