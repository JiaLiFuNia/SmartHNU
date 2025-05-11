package com.smart.htu.api.module

data class NoticeEntity(
    val code: Int,
    val message: String,
    val data: List<Notice>,
    val airConditionCookie: LoginCookie,
)

data class Notice(
    val id: Int,
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