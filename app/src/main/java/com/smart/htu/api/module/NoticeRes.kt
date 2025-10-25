package com.smart.htu.api.module

import com.smart.htu.utils.DateUtil.convertStringDateTimeToLocalDateTime
import java.time.LocalDateTime

data class NoticeRes(
    val code: Int,
    val message: String,
    val data: List<NoticeEntity>
)

data class NoticeEntity(
    val id: String,
    private val publishTime: String,
    private val expireTime: String,
    val title: String,
    val content: String,
    val action: String,
    val type: NoticeType
) {
    val publishDate: LocalDateTime
        get() = convertStringDateTimeToLocalDateTime(publishTime, "yyyy-MM-dd HH:mm")

    val expireDate: LocalDateTime
        get() = convertStringDateTimeToLocalDateTime(expireTime, "yyyy-MM-dd HH:mm")
}

enum class NoticeType {
    COMMON,
    URL,
    UPDATE,
    SCREEN,
    QUESTIONNAIRE,
}