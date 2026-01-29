package com.smart.htu.api.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AIModelEntity(
    val name: String,
    val model: String,
    val type: AIModelType,
    val token: String? = null
)

enum class AIModelType {
    Text,
    Image
}

@Serializable
data class Message(
    val role: String,
    val content: String? = null,
    @SerialName("reasoning_content") val reasoningContent: String? = null,
    @SerialName("finish_reason") val finishReason: String? = null
)

enum class AIRole(val value: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant")
}


@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false,
    val enable_thinking: Boolean = false,
)

@Serializable
data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage?,
    val created: Long,
    val model: String,
    val message: String? = null,
    val code: Int? = null
) {
    @Serializable
    data class Choice(
        val index: Int,
        val message: Message? = null,
        val delta: Message? = null
    )
}

@Serializable
data class Usage(
    @SerialName("prompt_tokens") val promptTokens: Int? = null,
    @SerialName("completion_tokens") val completionTokens: Int? = null,
    @SerialName("total_tokens") val totalTokens: Int? = null,
)