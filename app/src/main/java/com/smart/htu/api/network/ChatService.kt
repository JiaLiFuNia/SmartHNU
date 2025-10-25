package com.smart.htu.api.network

import com.smart.htu.api.module.ChatRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface AIService {

    @Streaming
    @POST("v1/chat/completions")
    @Headers("user-agent: SmartHNU-Android-App")
    suspend fun chatService(
        @Header("Authorization") authorization: String,
        @Body data: ChatRequest
    ): Response<ResponseBody>

}