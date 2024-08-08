package com.xhand.hnu.network

import android.content.Context
import com.xhand.hnu.model.entity.HourEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SecondClassService {

    @GET("/syslogin")
    suspend fun scLoginService(): Response<ResponseBody>

    @POST("/syslogin")
    @FormUrlEncoded
    suspend fun scLoginPostService(
        @Header("Cookie") cookie: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String,
        @Field("tk") tk: String,
        @Field("verifycode") verifycode: String,
    ): Response<ResponseBody>

    @GET("/sys/stu/dataanalysis/plate")
    suspend fun getHourList(
        @Header("Cookie") cookie: String
    ): HourEntity

    companion object {
        fun instance(context: Context): SecondClassService {
            return scRetrofitCreater(SecondClassService::class.java, context)
        }
    }

}