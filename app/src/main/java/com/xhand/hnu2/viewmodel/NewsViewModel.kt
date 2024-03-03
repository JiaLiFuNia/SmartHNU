package com.xhand.hnu2.viewmodel

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.hnu2.model.entity.ArticleListEntity
import com.xhand.hnu2.network.NewsListService
import com.xhand.hnu2.network.SearchService
import com.xhand.hnu2.network.getNewsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class NewsUiState(
    val openFilterSheet: Boolean = false,
    var isSearchBarShow: Boolean = false
)

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        NewsUiState()
    )

    fun setOpenFilterSheet(value: Boolean) {
        _uiState.update {
            it.copy(openFilterSheet = value)
        }
    }

    var list by mutableStateOf(
        listOf(
            ArticleListEntity(
                "正在加载中...",
                "正在加载中...",
                1,
                "",
                "通知公告"
            )
        )
    )
    private var listTemp by mutableStateOf(
        listOf(
            ArticleListEntity(
                "",
                "",
                1,
                "",
                ""
            )
        )
    )

    private val newsListService = NewsListService.instance()
    private val newsListType = mapOf(
        "8955" to "通知公告",
        "8954" to "师大要闻",
        "8957" to "院部动态"
    )

    suspend fun newsList() {
        for (type in newsListType) {
            for (i in 1..10) {
                val htmlRes = newsListService.getNewsList(i.toString(), type.key)
                listTemp = listTemp + getNewsList(htmlRes.body()?.string(), type.value, 1)
                }
        }
        list = listTemp
    }

    private val searchService = SearchService.instance()
    suspend fun searchRes(searchKey: String): List<ArticleListEntity> {
        val searchKeys =
            """[{"field":"pageIndex","value":2},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$searchKey"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
        val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
        val searchRes = searchService.pushPost(searchKeyEncode)
        val searchList = getNewsList(searchRes.body()?.data, "搜索", 3)
        Log.i("TAG666", "viewmodel${searchList}")
        return searchList
    }
}