package com.xhand.htu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.htu.model.entity.ArticleEntity
import com.xhand.htu.model.entity.ArticleService
import java.sql.Types.NULL

//新闻列表
class ArticleViewModel: ViewModel() {

    private var articleService = ArticleService.instance()
    var list by mutableStateOf(
        listOf(
            ArticleEntity(
                title = "关于召开2023年学生军训汇报暨总结表彰大会的通知",
                time = "2023-09-26"
            ),
            ArticleEntity(
                title = "关于高性能计算平台关机更改供电模式的通知",
                time = "2023-10-02"
            ),
            ArticleEntity(
                title = "关于召开主题教育总结大会的通知",
                time = "2023-09-27",
            ),
            ArticleEntity(
                title = "关于申报2024年度河南省高等学校重点科研项目计划基础研究专项的通知",
                time = "2023-09-27"
            ),
            ArticleEntity(
                title = "关于举办河南师范大学“中华民族一家亲 同心共筑中国梦”主题演讲比赛的通知",
                time = "2023-09-26"
            ),
            ArticleEntity(
                title = "关于2023年中秋节国庆节放假安排的通知",
                time = "2023-09-26"
            ),
            ArticleEntity(
                title = "关于召开2023年学生军训汇报暨总结表彰大会的通知",
                time = "2023-09-26"
            ),
            ArticleEntity(
                title = "关于高性能计算平台关机更改供电模式的通知",
                time = "2023-10-02"
            ),
            ArticleEntity(
                title = "关于召开主题教育总结大会的通知",
                time = " 2023-09-27"
            ),
            ArticleEntity(
                title = "关于申报2024年度河南省高等学校重点科研项目计划基础研究专项的通知",
                time = "2023-09-27"
            ),
            ArticleEntity(
                title = "关于举办河南师范大学“中华民族一家亲 同心共筑中国梦”主题演讲比赛的通知",
                time = "2023-09-26"
            ),
            ArticleEntity(
                title = "关于2023年中秋节国庆节放假安排的通知",
                time = "2023-09-26"
            )
        )
    )
        private set

    suspend fun fetchArticleList() {
        val res = articleService.list()
        if (res.code == 200) {
            list = res.data
        }
    }
}