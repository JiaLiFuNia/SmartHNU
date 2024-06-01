package com.xhand.hnu.network

import com.xhand.hnu.model.entity.LoginEntity
import com.xhand.hnu.model.entity.LoginPostEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("dev-api/appapi/applogin")
    suspend fun loginPost(
        @Body body: LoginPostEntity?
    ): LoginEntity

    companion object {
        fun instance(): LoginService {
            return Network.loginService(LoginService::class.java)
        }
    }

}