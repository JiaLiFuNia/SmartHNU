package com.smart.htu.screens.news

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
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
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val newsOptionItems: List<NewsCategoryEntity> = emptyList(),
    val bannerPicList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus(),
    val newsList: List<ResultWithStatus<List<NewsItemEntity>>> = List(newsOptionItems.size) { ResultWithStatus() },
    val searchList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus(),
    val searchPage: Int = 1,
    val hasMoreSearchResults: Boolean = true,
    val newsPages: List<Int> = List(newsOptionItems.size) { 1 },
    val hasMoreNews: List<Boolean> = List(newsOptionItems.size) { true }
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

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            getBannerImgList()
        }
    }

    suspend fun searchNews(keyword: String, loadMore: Boolean = false) {
        try {
            val currentPage = if (loadMore) _uiState.value.searchPage else 1

            _uiState.update {
                if (!loadMore)
                    it.copy(searchList = ResultWithStatus())
                else
                    it
            }

            val searchKeys =
                """[{"field":"pageIndex","value":${currentPage}},{"field":"group","value":0},{"field":"searchType","value":""},{"field":"keyword","value":"$keyword"},{"field":"recommend","value":"1"},{"field":4,"value":""},{"field":5,"value":""},{"field":6,"value":""},{"field":7,"value":""},{"field":8,"value":""},{"field":9,"value":""},{"field":10,"value":""}]"""
            val searchKeyEncode = Base64.encodeToString(searchKeys.toByteArray(), 0)
            val res = networkRepo.searchNewsService(searchKeyEncode)
            Log.i("TAG666", "searchNews: $res")
            val hasMore = res.isNotEmpty()
            val combinedResults = if (loadMore) {
                (_uiState.value.searchList.data ?: emptyList()) + res
            } else {
                res
            }
            _uiState.update {
                it.copy(
                    searchList = ResultWithStatus(combinedResults),
                    searchPage = currentPage + 1,
                    hasMoreSearchResults = hasMore
                )
            }
        } catch (e: Exception) {
            Log.i("TAG666", "searchNews: $e")
        }
    }

    /*
    * @Param index: tab index
    * @Param page: page index
    * */
    suspend fun getNewsList(index: Int, loadMore: Boolean = false) {
        try {
            if (!loadMore) {
                val tempList = _uiState.value.newsList.toMutableList()
                tempList[index] = ResultWithStatus()
                _uiState.update { it.copy(newsList = tempList) }
            }

            val currentPage = if (loadMore) _uiState.value.newsPages[index] else 1

            val res = networkRepo.getNewsService(_uiState.value.newsOptionItems[index], currentPage)
            val hasMore = res.isNotEmpty()

            val combinedResults = if (loadMore) {
                (_uiState.value.newsList[index].data ?: emptyList()) + res
            } else {
                res
            }

            val sortedResults = combinedResults.sortedByDescending { it.time }

            val tempList = _uiState.value.newsList.toMutableList()
            tempList[index] = ResultWithStatus(sortedResults)

            val tempPages = _uiState.value.newsPages.toMutableList()
            tempPages[index] = currentPage + 1

            val tempHasMore = _uiState.value.hasMoreNews.toMutableList()
            tempHasMore[index] = hasMore

            _uiState.update {
                it.copy(
                    newsList = tempList,
                    newsPages = tempPages,
                    hasMoreNews = tempHasMore
                )
            }

            Log.i("TAG666", "getNewsList $index $currentPage ${res.size}")
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList error: $e")
            val tempList = _uiState.value.newsList.toMutableList()
            tempList[index] = ResultWithStatus()
            _uiState.update { it.copy(newsList = tempList) }
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
            _uiState.update { it.copy(bannerPicList = ResultWithStatus(res)) }
            Log.i("TAG666", "getNewsList: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList: $e")
        }
    }

}