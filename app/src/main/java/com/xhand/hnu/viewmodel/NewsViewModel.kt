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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoadingNewsList: Boolean = true,
    val isSearching: Boolean = true,
    val isDetailLoading: Boolean = true,
    val hadGetNew: Boolean = false,
    val searchBarExpand: Boolean = false,
    val searchText: String = "",
    val list: MutableList<ArticleListEntity> = mutableListOf(),
    val searchList: MutableList<ArticleListEntity> = mutableListOf(),
    val searchHistory: MutableList<String> = mutableListOf(),
    val pictures: MutableList<PictureListItem> = mutableListOf(),
    val htmlParsing: String = "",
    val article: ArticleListEntity = ArticleListEntity("", "", "", "", false)
)

@SuppressLint("MutableCollectionMutableState")
class NewsViewModel(context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val dataManager = DataManager(context)

    init {
        viewModelScope.launch {
            val historyList = dataManager.historyList.firstOrNull()
            _uiState.update {
                it.copy(searchHistory = historyList?.toMutableList() ?: mutableListOf())
            }
        }
    }

    fun clearHistoryList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(searchHistory = mutableListOf())
            }
            dataManager.clearHistoryList()
        }
    }

    private fun addSearchHistory(searchHistory: String) {
        val searchHistoryTemp: MutableList<String> = _uiState.value.searchHistory
        searchHistoryTemp.add(searchHistory)
        viewModelScope.launch {
            dataManager.saveHistoryList(searchHistoryTemp)
            _uiState.update {
                it.copy(searchHistory = searchHistoryTemp)
            }
        }
    }

    fun changeSearchText(searchText: String) {
        _uiState.update {
            it.copy(searchText = searchText)
        }
    }

    var searchText by mutableStateOf("")

    var searchBarExpand by mutableStateOf(false)

    // 搜索列表
    var searchList by mutableStateOf(
        mutableListOf<ArticleListEntity>()
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
            Log.i("TAG666", "htmlParsing $htmlParsing")
            isDetailLoading = false
        } catch (e: Exception) {
            Log.i("TAG666", "detailService: $e")
        }
    }

    private var sliderPosition by mutableFloatStateOf(4f)
    // 新闻列表请求
    suspend fun newsList() {
        _uiState.update {
            it.copy(list = mutableListOf())
        }
        try {
            val newsList: MutableList<ArticleListEntity> = mutableListOf()
            for (type in newsListType) {
                for (i in 1..sliderPosition.toInt()) {
                    val htmlRes = newsListService.getNewsList(i.toString(), type.key)
                    newsList.addAll(
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
                    newsList.addAll(
                        getNewsList(
                            htmlRes.body()?.string(),
                            type.value,
                            3
                        )
                    )
                }
            }
            _uiState.update {
                it.copy(list = newsList)
            }
            _uiState.update {
                it.copy(hadGetNew = true)
            }
            _uiState.update {
                it.copy(isLoadingNewsList = false)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "newsList: $e")
        }
    }

    // 搜索列表请求
    suspend fun searchRes(content: String) {
        try {
            _uiState.update {
                it.copy(isSearching = true)
            }
            val searchListTemp = mutableListOf<ArticleListEntity>()
            for (i in 1..sliderPosition.toInt()) {
                val searchKeys =
                    """[{"field":"pageIndex","value":$i},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$content"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
                val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
                val searchRes = searchService.pushPost(searchKeyEncode)
                searchListTemp.addAll(getNewsList(searchRes.body()?.data, "搜索", 2))
                Log.i("TAG666", "searchList $searchListTemp")
                if (searchListTemp.isEmpty() || searchListTemp.size < 10) {
                    break
                }
            }
            if (content !in _uiState.value.searchHistory) {
                addSearchHistory(content)
                Log.i("TAG666", "searchHistory ${_uiState.value.searchHistory}")
            }
            searchList = searchListTemp
        } catch (e: Exception) {
            Log.i("TAG666", "searchRes: $e")
        }
        _uiState.update {
            it.copy(isSearching = false)
        }
    }

    // 主页图片加载
    suspend fun imageLoad() {
        val htmlRes = newsListService.getPicList(url = "https://www.htu.edu.cn/")
        // Log.i("TAG6656","${htmlRes.body()?.string()}")
        pictures = getPicList(str = htmlRes.body()?.string()).toMutableStateList()
        Log.i("TAG6656", "imageLoad: $pictures")
    }
}