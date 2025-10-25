package com.smart.htu.screens.news

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.AIRole
import com.smart.htu.api.module.ChatRequest
import com.smart.htu.api.module.Message
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.repo.AIChatNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.screens.setting.AI_MODEL_LIST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class NewsUiState(
    val blurEffect: Boolean = true,
    val newsOptionItems: List<NewsCategoryEntity> = emptyList(),
    val bannerPicList: List<NewsItemEntity> = emptyList(),
    val newsList: List<MutableList<NewsItemEntity>?> = List(newsOptionItems.size) { null },
    val searchList: List<NewsItemEntity>? = null,
    val newsPages: List<Int> = List(newsOptionItems.size) { 1 },
    val aiModelKey: String = "",
    val selectedAIModelIndex: Int = 2,
    val aiSummaryContent: String? = null,
    val aiSummaryReasoningContent: String? = null,
    val isAISummaryReasoning: Boolean = false,
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
    private val aiChatNetworkRepo: AIChatNetworkRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val newsOptionItems = listOf(
        NewsCategoryEntity(NewsType.RESEARCH, "河南师范大学主页", "", "xsygcs"),
        NewsCategoryEntity(NewsType.NOTICE, "河南师范大学主页", "", "8955"),
        NewsCategoryEntity(NewsType.FAST_NEWS, "河南师范大学主页", "", "8957"),
        NewsCategoryEntity(NewsType.HEADLINES, "河南师范大学主页", "", "8954"),
        // NewsCategoryEntity(NewsType.MEDIA, "河南师范大学主页", "", "9008"),
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

    private val aiModelKeyStateFlow = dataStoreRepo.observeAIModelKey()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeAIModelKey().first()
            }
        )

    private val selectedAIModelStateFlow = dataStoreRepo.observeSelectedAIModel()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeSelectedAIModel().first()
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
            selectedAIModelStateFlow.collect { value ->
                _uiState.update { it.copy(selectedAIModelIndex = value) }
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

    fun aiNewsSummaryService(articleContent: String, publishDate: String, articleTitle: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            aiSummaryContent = "",
                            aiSummaryReasoningContent = ""
                        )
                    }
                }
                aiChatNetworkRepo.streamChat(
                    key = _uiState.value.aiModelKey,
                    data = ChatRequest(
                        messages = listOf(
                            Message(
                                content = "你是一个 “河南师范大学新闻总结助手”。" +
                                        "你的任务是：对用户提供的 HTML 格式新闻网页内容 进行结构化总结与提炼，生成规范化的新闻摘要。" +
                                        "请严格遵循以下要求生成输出内容：\n" +
                                        "输出要求\n" +
                                        "1. 输出格式：使用 Markdown 格式 输出；最大标题层级为 ####；不要包含任何额外说明、分析或与新闻无关的内容。" +
                                        "2. 输出模板：\n" +
                                        "请严格按照以下格式输出结果，并保持字段顺序与层级一致：\n" +
                                        "#### **{{新闻标题}}** （概括总结出新闻标题，如原文标题可直接提取则直接使用）。" +
                                        "#### **发布时间**：从原文中提取新闻发布时间。" +
                                        "#### **发布单位**：从原文中提取新闻的发布单位或来源。" +
                                        "#### **主要内容**：以简洁、逻辑清晰的语言概括新闻主要内容。" +
                                        "- 长度适中（不宜过短或过长）；" +
                                        "- 对关键事件、数字、政策、结论等 **重点内容加粗显示**；" +
                                        "- 保持客观、忠实于原文，不随意添加主观评价或推测。" +
                                        "若新闻中包含附件，请按以下格式列出：（若无附件，请省略此部分）" +
                                        "### **附件**：" +
                                        "附件1. [附件名称](http://www.htu.edu.cn/附件链接)  " +
                                        "附件2. [附件名称](http://www.htu.edu.cn/附件链接)  " +
                                        "若附件名称中有类似‘附件1’等重复序号的词语，则删除。" +
                                        "交互说明\n" +
                                        "用户可能会继续就该篇新闻的内容提出问题。你应根据原文信息如实回答。如原文信息不足，可在必要时搜索网络以补充上下文，但必须确保内容真实、准确、符合事实。不得生成与新闻无关的解释、评论或主观性语句。",
                                role = AIRole.SYSTEM.value
                            ),
                            Message(
                                content = "{\"articleTitle\":\"${articleTitle}\",\"publishDate\":\"${publishDate}\", \"articleContent\":\"${articleContent}\"}",
                                role = AIRole.USER.value
                            )
                        ),
                        model = AI_MODEL_LIST[_uiState.value.selectedAIModelIndex].model,
                        stream = true
                    )
                ).collect { chunk ->
                    val content = chunk.choices.first().delta
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                isAISummaryReasoning = true,
                                aiSummaryReasoningContent = _uiState.value.aiSummaryReasoningContent + content?.reasoningContent.orEmpty(),
                                aiSummaryContent = _uiState.value.aiSummaryContent + content?.content.orEmpty()
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TAG666 aiNewsSummaryService", "$e")
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