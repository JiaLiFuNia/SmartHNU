package com.smart.htu.api.module

import com.smart.htu.screens.news.entity.NewsType

data class NewsItemEntity(
    val label: NewsType,
    val title: String,
    private val _url: String,
    val imgUrlWithoutHttp: String? = "",
    val time: String
) {
    val url: String
        get() = if (_url.startsWith("http")) {
            if (_url.contains("web."))
                _url.replace("http://web", "https://www")
                    .replace("psp", "htm")
            else _url
        } else {
            "https://www.htu.edu.cn$_url"
        }
    val imgUrl: String
        get() = if (imgUrlWithoutHttp?.startsWith("http") == true) imgUrlWithoutHttp else "https://www.htu.edu.cn$imgUrlWithoutHttp"
}