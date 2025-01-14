package com.smart.htu.api.network

import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import retrofit2.http.Body
import retrofit2.http.POST

interface JWCService {

    @POST("dev-api/appapi/applogin")
    suspend fun login(
        @Body body: LoginPost
    ): LoginJWCEntity

}