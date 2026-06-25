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
    suspend fun authServer(
        @Query("service") service: String = "http://authserver2.htu.edu.cn/authserver/mobile/callback?appId=537288889"
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("authserver/login")
    suspend fun authLogin(
        @Query("service") service: String = "http://authserver2.htu.edu.cn/authserver/mobile/callback?appId=537288889",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("execution") execution: String,
        @Field("captcha") captcha: String = "",
        @Field("_eventId") eventId: String = "submit",
        @Field("cllt") cllt: String = "userNameLogin",
        @Field("dllt") dllt: String = "mobileLogin",
        @Field("lt") lt: String = ""
    ): Response<ResponseBody>

}