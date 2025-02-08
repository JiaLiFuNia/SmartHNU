package com.smart.htu.api.network

import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface JWCService {

    @POST("dev-api/appapi/applogin")
    suspend fun login(
        @Body body: LoginPost
    ): LoginJWCEntity

    @GET("dev-api/appapi/getIstoken")
    suspend fun checkToken(
        @Header("Token") token: String
    ): LoginJWCEntity

    @POST("dev-api/appapi/appkxjs/classroom")
    suspend fun classroomOccupation(
        @Body body: BuildingEntity,
        @Header("Token") token: String
    ): ClassroomOccupationEntity

}