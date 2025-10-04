package com.smart.htu.api.module

data class NoticeRes(
    val code: Int,
    val message: String,
    val data: List<NoticeEntity>
)

data class NoticeEntity(
    val id: String,
    val time: String,
    val title: String,
    val content: String,
    val action: String,
    val type: NoticeType
)

enum class NoticeType {
    COMMON,
    URL,
    UPDATE,
    SCREEN,
    QUESTIONNAIRE,
}