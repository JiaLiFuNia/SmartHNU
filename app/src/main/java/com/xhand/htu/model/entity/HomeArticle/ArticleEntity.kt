package com.xhand.htu.model.entity.HomeArticle

import com.xhand.htu.model.entity.BaseResponse

//文章数据类
data class ArticleEntity(
    val title: String,
    var time: String
)

data class ArticleListResponse(var data: List<ArticleEntity>): BaseResponse()
data class ActiveArticleListResponse(var data: List<ArticleEntity>): BaseResponse()
