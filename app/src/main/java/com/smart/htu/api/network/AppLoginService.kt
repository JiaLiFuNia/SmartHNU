package com.smart.htu.api.network

import com.smart.htu.api.module.AppTokenResEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AppLoginService {

    @FormUrlEncoded
    @POST("auth/ids/login")
    suspend fun appLogin(
        @Field("mobileCode") mobileCode: String?
    ): AppTokenResEntity

}