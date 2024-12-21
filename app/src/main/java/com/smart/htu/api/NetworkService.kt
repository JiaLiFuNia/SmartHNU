package com.smart.htu.api

import com.smart.htu.api.module.PersonalMessageRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkService {

    @GET("authserver/login")
    suspend fun authServer(): Response<ResponseBody>

    @FormUrlEncoded
    @POST("authserver/login")
    suspend fun authLogin(
        @Query("service") service: String = "http%3A%2F%2Fehall2.htu.edu.cn%2Flogin%3Fservice%3Dhttps%3A%2F%2Fehall2.htu.edu.cn%2Fywtb-portal%2Fofficial%2Findex.html",
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

    @GET("psfw/sys/pubbiinfaapphtu/api/select_xsjbxx.do")
    suspend fun getStudentInfo(): Response<PersonalMessageRes>

    companion object {
        const val BASE_URL = "https://authserver2.htu.edu.cn/"
        const val E_HALL_BASE_URL = "https://ehall2.htu.edu.cn/"
    }

}