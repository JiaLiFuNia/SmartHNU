package com.smart.htu.api.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthLoginService {

    @GET("authserver/login")
    suspend fun authServer(): Response<ResponseBody>

    @FormUrlEncoded
    @POST("authserver/login")
    suspend fun authLogin(
        @Query("service") service: String = "https://authserver2.htu.edu.cn:443/personalInfo/personCenter/index.html",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("captcha") captcha: String,
        @Field("execution") execution: String,
        @Field("rememberMe") rememberMe: String = "true",
        @Field("_eventId") eventId: String = "submit",
        @Field("cllt") cllt: String = "userNameLogin",
        @Field("dllt") dllt: String = "generalLogin",
        @Field("lt") lt: String = ""
    ): Response<ResponseBody>

}