package com.smart.htu.api.network

import com.smart.htu.api.module.GiteeEntity
import retrofit2.http.GET

interface GiteeService {

    @GET("jialifunia/SmartHNU/raw/v3/config.json")
    suspend fun getGiteeConfig(): GiteeEntity

}