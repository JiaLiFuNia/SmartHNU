package com.xhand.hnu2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.hnu2.model.entity.ArticleListEntity

class ArticleListViewModel : ViewModel() {
    var list by mutableStateOf(
        listOf(
            ArticleListEntity(
                title = "诚邀全球英才依托我校申报2024年海外优青",
                time = "2024-01-16",
                id = 10,
                url = "https://www.htu.edu.cn/rsc/2024/0116/c1595a296555/page.htm",
                type = "通知公告"
            )
        )
    )
        private set
}
