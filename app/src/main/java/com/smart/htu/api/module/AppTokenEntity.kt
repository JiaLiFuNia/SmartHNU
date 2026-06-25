package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class AppTokenResEntity(
    val code: Int,
    val message: String,
    val data: AppToken? = null
)

data class AppToken(
    @SerializedName("tokenHeader") val tokenHeader: String,
    @SerializedName("tokenHead") val tokenHead: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenPeriodTime") val tokenPeriodTime: Long,
    @SerializedName("tokenJz") val tokenJz: String,
    @SerializedName("studentId") val userNo: String
)
