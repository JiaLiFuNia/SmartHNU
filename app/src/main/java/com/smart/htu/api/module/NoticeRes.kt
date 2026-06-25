package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
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
    JWC
}

data class JWCNoticeRes(
    val msg: String,
    val newsList: List<JWCNoticeEntity>? = null,
    val news: JWCNoticeEntity? = null,
    val code: Int
)

data class JWCNoticeEntity(
    @SerializedName("cjsj") val publishDateTime: String,
    @SerializedName("bt") val content: String,
    @SerializedName("ggtzdm") val noticeId: String,
    @SerializedName("nrhtml") val detail: String? = null
)

data class JWCNoticeDetailPost(
    @SerializedName("ggtzdm") val noticeId: String,
    @SerializedName("downloadapi") val downloadApi: String = "/dev-api/"
)