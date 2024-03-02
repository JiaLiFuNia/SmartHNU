package com.xhand.hnu2.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.xhand.hnu2.model.entity.ArticleListEntity
import com.xhand.hnu2.network.SearchService
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