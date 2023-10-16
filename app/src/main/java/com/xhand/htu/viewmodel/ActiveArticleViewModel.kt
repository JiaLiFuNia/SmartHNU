package com.xhand.htu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.htu.model.entity.ActiveArticle.ActiveArticleService
import com.xhand.htu.model.entity.HomeArticle.ArticleEntity
//新闻列表
class ActiveArticleViewModel: ViewModel() {
    private val activearticleService = ActiveArticleService.instance()
    var list by mutableStateOf(
        listOf(
            ArticleEntity(
                title = "正在加载中...",
                time = "正在加载中..."
            )
        )
    )
        private set
    suspend fun fetchActiveArticleList() {
        val res = activearticleService.activelist()
        if (res.code == 200) {
            list = res.data
        }
    }
}