package com.smart.htu.api.network

import com.smart.htu.api.module.AIModulePostEntity
import com.smart.htu.api.module.AIResponseEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatService {

    @POST("api/chat/completions")
    suspend fun chatService(
        @Header("Authorization") authorization: String,
        @Body data: AIModulePostEntity
    ): Response<AIResponseEntity>

}