package com.xhand.hnu.model.entity

data class ArticleListEntity(
    val title: String,
    var time: String,
    var url: String,
    var type: String,
    val isTop: Boolean
)