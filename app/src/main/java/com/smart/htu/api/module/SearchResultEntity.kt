package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.utils.ParseNewsUtil.parseNewsHTML

data class SearchResultEntity(
    @SerializedName("data") private val data: String,
    @SerializedName("total") private val totalString: String
) {
    val total: Int
        get() = totalString.toIntOrNull() ?: 0

    val dataList: List<NewsItemEntity>
        get() = parseNewsHTML(data, NewsType.SEARCH)
}