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

class NewsViewModel : ViewModel() {

    // 是否正在加载
    var newsIsLoading by mutableStateOf(true)

    // 新闻列表
    var list = mutableListOf<ArticleListEntity>()

    // 搜索列表
    var searchList = mutableListOf<ArticleListEntity>()

    // 临时列表
    private var listTemp = mutableListOf<ArticleListEntity>()

    // 主页新闻类型
    private val newsListType = mapOf(
        "8955" to "通知公告",
        "8954" to "师大要闻",
        "8957" to "院部动态"
    )

    // 教务新闻类型
    private val teacherNewsListType = mapOf(
        "3251" to "教务通知",
        "3258" to "公示公告",
        "kwgl" to "考务管理"
    )

    // 网络请求
    private val newsListService = NewsListService.instance()
    private val searchService = SearchService.instance()

    // 是否正在刷新
    var isRefreshing by mutableStateOf(false)
        private set

    // 搜索内容
    var content by mutableStateOf("")

    // 是否正在搜索
    val isSearching: Boolean
        get() {
            return content.isNotEmpty()
        }

    // 是否点击搜索
    val isSearched: Boolean
        get() {
            return searchList.isEmpty()
        }

    // 新闻列表请求
    suspend fun newsList() {
        isRefreshing = true
        try {
            for (type in newsListType) {
                for (i in 1..10) {
                    val htmlRes = newsListService.getNewsList(i.toString(), type.key)
                    listTemp = (listTemp + getNewsList(
                        htmlRes.body()?.string(),
                        type.value,
                        1
                    )).toMutableList()
                }
            }
            for (type in teacherNewsListType) {
                for (i in 1..10) {
                    val htmlRes = newsListService.getTeacherNewsList(i.toString(), type.key)
                    listTemp = (listTemp + getNewsList(
                        htmlRes.body()?.string(),
                        type.value,
                        2
                    )).toMutableList()
                }
            }
            list = listTemp
            listTemp = mutableListOf() // 避免刷新时重复增加
            newsIsLoading = false
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
        isRefreshing = false
    }

    // 搜索列表请求
    suspend fun searchRes() {
        val searchKeys =
            """[{"field":"pageIndex","value":2},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$content"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
        val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
        try {
            val searchRes = searchService.pushPost(searchKeyEncode)
            searchList = getNewsList(searchRes.body()?.data, "搜索", 3)
            Log.i("TAG666", "$searchList")
            newsIsLoading = false
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }
}