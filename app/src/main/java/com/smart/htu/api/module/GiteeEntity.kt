package com.smart.htu.api.module

data class GiteeEntity(
    val version: String,
    val versionCode: String,
    val termCode: String,
    val airConditionCookie: LoginCookie,
    val notice: List<Notice>
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