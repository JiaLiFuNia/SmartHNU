package com.xhand.htu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.htu.model.entity.HomeArticle.ArticleEntity
import com.xhand.htu.model.entity.HomeArticle.OtherArticleService

//新闻列表
class OtherArticleViewModel: ViewModel() {
    private val otherearticleService = OtherArticleService.instance()
    var list by mutableStateOf(
        listOf(
            ArticleEntity(
                title = "正在加载中...",
                time = "正在加载中..."
            )
        )
    )
        private set
    suspend fun fetchOtherArticleList() {
        val res = otherearticleService.otherlist()
        if (res.code == 200) {
            list = res.data
        }
    }
}