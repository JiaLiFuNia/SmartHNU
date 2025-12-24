package com.smart.htu.api.network

import com.smart.htu.api.module.CourseRepoRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
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
        @Field("_") timeStamp: Long = System.currentTimeMillis(),
    ): Response<ResponseBody>

}