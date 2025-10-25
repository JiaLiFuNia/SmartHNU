package com.smart.htu.repo

import com.smart.htu.api.module.ChatRequest
import com.smart.htu.api.module.ChatResponse
import com.smart.htu.api.network.AIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AIChatNetworkRepo @Inject constructor(
    private val aiService: AIService
) {

    val jsonParser = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun streamChat(key: String, data: ChatRequest): Flow<ChatResponse> = flow {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val requestBody = jsonParser.encodeToString(data)
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.siliconflow.cn/v1/chat/completions")
            .addHeader("Authorization", "Bearer $key")
            .addHeader("Accept", "text/event-stream")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return@flow

        body.byteStream().bufferedReader().useLines { lines ->
            lines.forEach { line ->
                if (line.startsWith("data: ")) {
                    val json = line.removePrefix("data: ").trim()
                    if (json == "[DONE]" || json.isEmpty()) return@forEach
                    val chunk = jsonParser.decodeFromString<ChatResponse>(json)
                    emit(chunk)
                }
            }
        }
    }.flowOn(Dispatchers.IO)


    suspend fun chatService(
        key: String,
        data: ChatRequest
    ): Result<ChatResponse> {
        try {
            val response = aiService.chatService("Bearer $key", data)
            response.body()?.let { body ->
                val chatResponse = jsonParser.decodeFromString<ChatResponse>(body.string())
                return Result.success(chatResponse)
            }
            return Result.failure(Exception("请求失败"))
        } catch (e: Exception) {
            return Result.failure(Exception("请求失败, $e"))
        }
    }

}