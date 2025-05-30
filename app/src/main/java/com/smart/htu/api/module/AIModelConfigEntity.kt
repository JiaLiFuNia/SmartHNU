package com.smart.htu.api.module

data class AIModelConfigEntity(
    val url: String = "https://chat.htu.edu.cn/api/chat/completions",
    val module: String = "DeepSeek-R1-Distill-Llama-70B",
    val key: String = ""
)

data class AIModulePostEntity(
    val messages: List<AIMessageEntity> = emptyList(),
    val model: String
)

data class AIMessageEntity(
    val content: String,
    val role: String
)

enum class AIRole(val value: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant")
}


data class AIResponseEntity(
    val id: String,
    val exampleGenerateObject: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    val promptLogprobs: Any? = null,
    val detail: String? = null
)

data class Choice(
    val index: Long,
    val message: Message,
    val logprobs: Any? = null,
    val finishReason: String,
    val stopReason: Any? = null
)

data class Message(
    val role: String,
    val reasoningContent: Any? = null,
    val content: String,
    val toolCalls: List<Any?>
) {
    val res: List<String>
        get() = content.split("</think>")
    val think: String
        get() = res.firstOrNull()?.trim() ?: ""
    val result: String
        get() = res.lastOrNull()?.trim() ?: ""
}

data class Usage(
    val promptTokens: Long,
    val totalTokens: Long,
    val completionTokens: Long,
    val promptTokensDetails: Any? = null
)
