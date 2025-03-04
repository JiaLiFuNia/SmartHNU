package com.smart.htu.api.module

import androidx.annotation.StringRes
import com.smart.htu.screens.news.entity.NewsType

data class NewsItemEntity(
    val label: NewsType,
    val title: String,
    private val _url: String,
    private val _imgUrl: String? = "",
    val time: String
){
    val url: String
        get() = if (_url.startsWith("http")) _url else "https://www.htu.edu.cn$_url"
    val imgUrl: String
        get() = if (_imgUrl?.startsWith("http") == true) _imgUrl else "https://www.htu.edu.cn$_imgUrl"
}