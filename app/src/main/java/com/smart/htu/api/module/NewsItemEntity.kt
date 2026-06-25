package com.smart.htu.api.module

import com.smart.htu.screens.news.entity.NewsType
import kotlinx.serialization.Serializable

@Serializable
data class NewsItemEntity(
    val label: NewsType,
    val title: String,
    val url: String,
    val imgUrl: String = "",
    val time: String
)

@Serializable
data class NewsMarkEntity(
    val title: String,
    val url: String,
    val time: String,
    val source: String
)