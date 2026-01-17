package com.smart.htu.api.module

data class NewsArticleEntity(
    var title: String?,
    val publishDate: String?,
    val visitCount: String?,
    var articleContent: String?,
    val attachment: List<AttachmentEntity>? = emptyList()
)

data class AttachmentEntity(
    val fileName: String,
    val url: String,
    val fileType: String,
    val isNeedOnlineView: Boolean = false
)