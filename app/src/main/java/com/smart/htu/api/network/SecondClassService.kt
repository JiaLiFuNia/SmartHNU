package com.smart.htu.api.network

import com.smart.htu.api.module.SCHourEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SecondClassService {

    @POST("syslogin")
    @FormUrlEncoded
    suspend fun scLogin(
        @Header("Cookie") sid: String,
        @Field("username") username: String,
        @Field("password") password: String = "",
        @Field("password2") password2: String?,
        @Field("tk") tk: String,
        @Field("verifycode") verifyCode: String,
    ): Response<ResponseBody>

    @GET("sys/stu/dataanalysis/plate")
    suspend fun getHourList(
        @Header("Cookie") sid: String
    ): SCHourEntity

}