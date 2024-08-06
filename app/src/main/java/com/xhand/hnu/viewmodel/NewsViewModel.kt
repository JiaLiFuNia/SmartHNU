package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.DataManager
import com.xhand.hnu.model.entity.ArticleListEntity
import com.xhand.hnu.network.NewsDetailService
import com.xhand.hnu.network.NewsListService
import com.xhand.hnu.network.PictureListItem
import com.xhand.hnu.network.SearchService
import com.xhand.hnu.network.getNewsList
import com.xhand.hnu.network.getPicList
import com.xhand.hnu.network.preProcessNewsDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
class NewsViewModel(context: Context) : ViewModel() {
    private val dataManager = DataManager(context)

    init {
        viewModelScope.launch {
            val historyList = dataManager.historyList.firstOrNull()
            searchHistory = historyList?.toMutableList() ?: mutableListOf()
        }
    }

    fun clearHistoryList() {
        viewModelScope.launch {
            dataManager.clearHistoryList()
            searchHistory.clear()
        }
    }

    var hadGetNew by mutableStateOf(false)

    var searchText by mutableStateOf("")

    var searchBarExpand by mutableStateOf(false)

    // 新闻列表
    var list by mutableStateOf(
        mutableListOf<ArticleListEntity>()
    )

    // 搜索列表
    var searchList by mutableStateOf(
        mutableListOf<ArticleListEntity>()
    )

    var searchHistory by mutableStateOf(
        mutableListOf<String>()
    )

    // 主页新闻类型
    private val newsListType = mapOf(
        "8955" to "通知公告",
        "8954" to "师大要闻",
        "8957" to "新闻速递"
    )

    // 教务新闻类型
    private val teacherNewsListType = mapOf(
        "3251" to "教务通知",
        "3258" to "公示公告",
        "kwgl" to "考务管理"
    )

    var pictures by mutableStateOf(
        mutableStateListOf(
            PictureListItem(
                "https://www.htu.edu.cn/upload/2021/10/202110201013271.jpg",
                "加载中...",
                "https://www.htu.edu.cn/"
            )
        )
    )

    // 网络请求
    private val newsListService = NewsListService.instance()
    private val searchService = SearchService.instance()
    private val detailService = NewsDetailService.instance()

    // 是否正在刷新
    var isRefreshing by mutableStateOf(true)
    var isSearching by mutableStateOf(true)

    // 网页源码请求
    var htmlParsing by mutableStateOf("")
    var isDetailLoading by mutableStateOf(true)

    // 新闻链接
    var article by mutableStateOf(ArticleListEntity("", "", "", "", false))

    // 新闻页面详情请求
    suspend fun detailService() {
        try {
            val res = detailService.getNewsDetail(article.url)
            delay(500)
            htmlParsing = preProcessNewsDetail(res.body()?.string() ?: "")
            Log.i("TAG666", htmlParsing)
            isDetailLoading = false
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    private var sliderPosition by mutableFloatStateOf(4f)
    // 新闻列表请求
    suspend fun newsList() {
        list.clear()
        try {
            for (type in newsListType) {
                for (i in 1..sliderPosition.toInt()) {
                    val htmlRes = newsListService.getNewsList(i.toString(), type.key)
                    list.addAll(
                        getNewsList(
                            str = htmlRes.body()?.string(),
                            type = type.value,
                            rule = if (type.key == "8955") 4 else 1
                        )
                    )
                }
            }
            for (type in teacherNewsListType) {
                for (i in 1..sliderPosition.toInt()) {
                    val htmlRes = newsListService.getTeacherNewsList(i.toString(), type.key)
                    list.addAll(
                        getNewsList(
                            htmlRes.body()?.string(),
                            type.value,
                            3
                        )
                    )
                }
            }
            hadGetNew = true
        } catch (e: Exception) {
            Log.i("TAG666", "$e")
        }
    }

    // 搜索列表请求
    suspend fun searchRes(content: String) {
        searchList.clear()
        for (i in 1..sliderPosition.toInt()) {
            val searchKeys =
                """[{"field":"pageIndex","value":1},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$content"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
            val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
            try {
                val searchRes = searchService.pushPost(searchKeyEncode)
                searchList.addAll(getNewsList(searchRes.body()?.data, "搜索", 2))
                Log.i("TAG666", "$searchList")

            } catch (e: Exception) {
                Log.i("TAG666", "$e")
            }
        }
        if (searchList.isNotEmpty()) {
            if (content in searchHistory)
                null
            else {
                searchHistory.add(content)
                viewModelScope.launch {
                    dataManager.saveHistoryList(searchHistory)
                }
            }
        }
    }

    // 主页图片加载
    suspend fun imageLoad() {
        val htmlRes = newsListService.getPicList(url = "https://www.htu.edu.cn/")
        // Log.i("TAG6656","${htmlRes.body()?.string()}")
        pictures = getPicList(str = htmlRes.body()?.string()).toMutableStateList()
        Log.i("TAG6656", "$pictures")
    }
}