package com.smart.htu.api.module

data class GiteeEntity(
    val version: String,
    val versionCode: String,
    val airConditionCookie: LoginCookie,
    val notice: List<Notice>
)

data class Notice(
    val id: String,
    val time: String,
    val title: String,
    val content: String,
    val type: NoticeType
)

enum class NoticeType {
    COMMON,
    URL,
    SCREEN
}