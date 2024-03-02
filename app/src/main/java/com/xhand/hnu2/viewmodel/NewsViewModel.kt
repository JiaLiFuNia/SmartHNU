package com.xhand.hnu2.viewmodel

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.hnu2.model.entity.ArticleListEntity
import com.xhand.hnu2.network.NewsListService
import com.xhand.hnu2.network.OtherNewsListService
import com.xhand.hnu2.network.SearchService
import com.xhand.hnu2.network.getNewsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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

    var list by mutableStateOf(listOf(ArticleListEntity("正在加载中...", "正在加载中...", 1, "", "通知公告")))
    private val newsListService = NewsListService.instance()
    // private val otherNewsListService = OtherNewsListService.instance()
    private val newsListType = mapOf(
        "8955" to "通知公告",
        "8954" to "师大要闻",
        "8957" to "院部动态"
    )
    private val otherNewsList = mapOf(
        "3251" to "教务通知",
        "3258" to "公示公告",
        "kwgl" to "考务管理"
    )
    suspend fun newsList() {
        // var list1 by mutableStateOf(listOf(ArticleListEntity("正在加载中...", "正在加载中...", 1, "", "通知公告")))
        // var list2 by mutableStateOf(listOf(ArticleListEntity("正在加载中...", "正在加载中...", 1, "", "通知公告")))
        for (type in newsListType) {
            for (i in 1..10) {
                val htmlRes = newsListService.getNewsList(i.toString(), type.key)
                list = getNewsList(htmlRes, type.value)
            }
        }
        Log.i("TAG666", "newsList: $list")
        // for (type in otherNewsList) {
        //     for (i in 1..10) {
        //         val htmlRes = otherNewsListService.getNewsList(i.toString(), type.key)
        //         list2 = getNewsList(htmlRes, type.value)
        //     }
        // }
        // list = list1 + list2
    }


    private val searchList = mutableListOf<ArticleListEntity>()
    private val searchService = SearchService.instance()

    suspend fun searchRes(searchKey: String): MutableList<ArticleListEntity> {
        val searchKeys =
            """[{"field":"pageIndex","value":2},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$searchKey"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
        val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
        Log.d("TAG666", "searchRes: $searchKeys")
        try {
            val searchRes = searchService.pushPost(searchKeyEncode)
            val document: Document = Jsoup.parse(searchRes.body()?.data ?: String())
            val titleAndUrl = document.select("div.result_item h3.item_title a")
            val time = document.select("div.result_item span.item_metas:nth-of-type(2)")
            var num = 0
            for (index in 0 until titleAndUrl.size) {
                var url = titleAndUrl[index].attr("href")
                if (url[0] != 'h') {
                    url = "https://www.htu.edu.cn${titleAndUrl[index].attr("href")}"
                }
                num += 1
                searchList.add(
                    ArticleListEntity(
                        title = titleAndUrl[index].text(),
                        time = time[index].text().substring(5, 15),
                        id = num,
                        url = url,
                        type = "搜索"
                    )
                )
            }
            Log.i("5545", "SearchRes: $searchList")
        } catch (e: Exception) {
            Log.i("5545", "SearchRes: $e")
        }
        return searchList
    }
}