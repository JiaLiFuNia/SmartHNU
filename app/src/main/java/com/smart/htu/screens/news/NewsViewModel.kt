package com.smart.htu.screens.news

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.AIMessageEntity
import com.smart.htu.api.module.AIModelConfigEntity
import com.smart.htu.api.module.AIModulePostEntity
import com.smart.htu.api.module.AIRole
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.screens.news.entity.NewsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class NewsUiState(
    val blurEffect: Boolean = true,
    val newsOptionItems: List<NewsCategoryEntity> = emptyList(),
    val bannerPicList: List<NewsItemEntity> = emptyList(),
    val newsList: List<MutableList<NewsItemEntity>?> = List(newsOptionItems.size) { null },
    val searchList: List<NewsItemEntity>? = null,
    val newsPages: List<Int> = List(newsOptionItems.size) { 1 },
    val aiModelKey: String = "",
    val newsArticle: NewsArticleEntity? = null,
    val bionicReadingEnabled: Boolean = true,
    val loadImgEnabled: Boolean = true,
    val newsHistoryList: List<NewsMarkEntity> = emptyList(),
    val newsFavoriteList: List<NewsMarkEntity> = emptyList(),
    val newsFontSize: Int = 17,
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val newsOptionItems = listOf(
        NewsCategoryEntity(NewsType.RESEARCH, "河南师范大学主页", "", "xsygcs"),
        NewsCategoryEntity(NewsType.NOTICE, "河南师范大学主页", "", "8955"),
        NewsCategoryEntity(NewsType.FAST_NEWS, "河南师范大学主页", "", "8957"),
        NewsCategoryEntity(NewsType.HEADLINES, "河南师范大学主页", "", "8954"),
        NewsCategoryEntity(NewsType.MEDIA, "河南师范大学主页", "", "9008"),
        NewsCategoryEntity(NewsType.MATH_LECTURES, "数学与信息科学学院", "math", "xsyg"),
        NewsCategoryEntity(NewsType.MATH_NEWS, "数学与信息科学学院", "math", "xinwen"),
        NewsCategoryEntity(NewsType.MATH_NOTICE, "数学与信息科学学院", "math", "1143"),
        NewsCategoryEntity(NewsType.TEACHING_NEWS, "河南师范大学教务处", "teaching", "3257"),
        NewsCategoryEntity(NewsType.TEACHING_NOTICE, "河南师范大学教务处", "teaching", "3251"),
        NewsCategoryEntity(
            NewsType.TEACHING_ANNOUNCEMENT,
            "河南师范大学教务处",
            "teaching",
            "3258"
        ),
        NewsCategoryEntity(
            NewsType.EXAMINATION_NOTICE,
            "河南师范大学教务处",
            "teaching",
            "kwgl"
        )
    )

    private val _uiState = MutableStateFlow(
        NewsUiState(
            newsOptionItems = newsOptionItems
        )
    )
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val aiModelKeyStateFlow = dataStoreRepo.observeAIModelConfig()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAIModelConfig().first()
            }
        )

    private val bionicReadingEnabledStateFlow = dataStoreRepo.observeBionicReadingEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeBionicReadingEnabled().first()
            }
        )


    private val loadImgEnabledStateFlow = dataStoreRepo.observeLoadImgEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoadImgEnabled().first()
            }
        )

    private val newsHistoryListStateFlow = dataStoreRepo.observeNewsHistoryList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeNewsHistoryList().first()
            }
        )

    private val newsFavoriteListStateFlow = dataStoreRepo.observeNewsFavoriteList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeNewsFavoriteList().first()
            }
        )

    private val newsFontSizeStateFlow = dataStoreRepo.observeNewsFontSize()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeNewsFontSize().first()
            }
        )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            aiModelKeyStateFlow.collect { value ->
                _uiState.update { it.copy(aiModelKey = value) }
            }
        }
        viewModelScope.launch {
            bionicReadingEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(bionicReadingEnabled = value) }
            }
        }
        viewModelScope.launch {
            loadImgEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(loadImgEnabled = value) }
            }
        }
        viewModelScope.launch {
            newsHistoryListStateFlow.collect { value ->
                _uiState.update { it.copy(newsHistoryList = value) }
            }
        }
        viewModelScope.launch {
            newsFavoriteListStateFlow.collect { value ->
                _uiState.update { it.copy(newsFavoriteList = value) }
            }
        }
        viewModelScope.launch {
            newsFontSizeStateFlow.collect { value ->
                _uiState.update { it.copy(newsFontSize = value) }
            }
        }
        viewModelScope.launch {
            if (_uiState.value.loadImgEnabled) getBannerImgList()
        }
    }

    suspend fun searchNews(keyword: String, page: Int = 1) {
        try {
            _uiState.update { it.copy(searchList = null) }
            val searchInfo =
                """[{"field":"pageIndex","value":${page}},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$keyword"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
            val searchInfoEncode = Base64.encodeToString(searchInfo.toByteArray(), 0)
            val res = networkRepo.searchNewsService(searchInfoEncode)
            Log.i("TAG666", "searchNews: $res")
            _uiState.update { it.copy(searchList = res) }
        } catch (e: Exception) {
            Log.i("TAG666", "searchNews: $e")
        }
    }

    suspend fun getNewsList(typeIndex: Int, pageIndex: Int = 1) {
        try {
            val res = networkRepo.getNewsService(
                newsOptionItems = _uiState.value.newsOptionItems[typeIndex],
                page = pageIndex
            ).sortedByDescending { it.time }
            val currentList = _uiState.value.newsList.toMutableList()
            if (currentList[typeIndex] == null) {
                currentList[typeIndex] = res.toMutableList()
            } else {
                currentList[typeIndex] = (currentList[typeIndex]?.plus(res))?.toMutableList()
            }
            _uiState.update { it.copy(newsList = currentList) }
            Log.i("TAG666 getNewsList", "${currentList[typeIndex]} $res")
        } catch (e: Exception) {
            Log.e("TAG666 getNewsList ", "error: ${e.message}")
        }
    }

    suspend fun getBannerImgList() {
        try {
            val res = networkRepo.getNewsService(
                newsOptionItems = NewsCategoryEntity(
                    label = NewsType.BANNER,
                    source = "河南师范大学主页",
                    academic = "",
                    type = "21040"
                )
            )
            _uiState.update { it.copy(bannerPicList = res) }
            Log.i("TAG666", "getNewsList: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList: $e")
        }
    }

    suspend fun getNewsDetail(url: String) {
        val res = networkRepo.getNewsDetailService(url)
        _uiState.update { it.copy(newsArticle = res) }
    }

    fun aiNewsSummaryService(onResponse: (String) -> Unit, message: String) =
        viewModelScope.launch {
            val res = networkRepo.chatService(
                url = AIModelConfigEntity().url,
                key = _uiState.value.aiModelKey,
                data = AIModulePostEntity(
                    messages = listOf(
                        AIMessageEntity(
                            content = "我是一个新闻摘要助手",
                            role = AIRole.SYSTEM.value
                        ),
                        AIMessageEntity(
                            content = message,
                            role = AIRole.USER.value
                        )
                    ),
                    model = AIModelConfigEntity().module
                )
            )
            res.onSuccess {
                if (it.choices.first().message.result.contains("测试成功")) {
                    onResponse("测试成功")
                } else {
                    onResponse("测试失败: ${it.choices.first().message.result}")
                }
            }.onFailure {
                onResponse("测试失败: " + (it.message ?: "请检查API配置"))
            }
        }

    fun addNewsHistory(newsItem: NewsMarkEntity) {
        viewModelScope.launch {
            dataStoreRepo.changeNewsHistoryList(newsItem)
        }
    }

    fun addNewsFavorite(newsItem: NewsMarkEntity) {
        viewModelScope.launch {
            dataStoreRepo.addNewsFavoriteList(newsItem)
        }
    }

}