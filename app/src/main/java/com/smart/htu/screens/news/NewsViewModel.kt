package com.smart.htu.screens.news

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class NewsUiState(
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val newsOptionItems: List<NewsCategoryEntity> = emptyList(),
    val bannerPicList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus()
)


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {


    private val newsOptionItems = listOf(
        NewsCategoryEntity(NewsType.BANNER, "河南师范大学主页", "", ""),
        NewsCategoryEntity(NewsType.NOTICE, "河南师范大学主页", "", "8955"),
        NewsCategoryEntity(NewsType.FAST_NEWS, "河南师范大学主页", "", "8954"),
        NewsCategoryEntity(NewsType.HEADLINES, "河南师范大学主页", "", "8957"),
        NewsCategoryEntity(NewsType.MEDIA, "河南师范大学主页", "", "9008"),
        NewsCategoryEntity(NewsType.MATH_NEWS, "数学与信息科学学院", "math", "1074"),
        NewsCategoryEntity(NewsType.MATH_NOTICE, "数学与信息科学学院", "math", "1143"),
        NewsCategoryEntity(NewsType.TEACHING_NEWS, "河南师范大学教务处", "teaching", "3257"),
        NewsCategoryEntity(
            NewsType.TEACHING_NOTICE,
            "河南师范大学教务处",
            "teaching",
            "3251"
        ),
        NewsCategoryEntity(
            NewsType.TEACHING_ANNOUNCEMENT,
            "河南师范大学教务处",
            "teaching",
            "3258",
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
            DEFAULT_BLUR_EFFECT
        )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            getNewsList()
        }
    }


    suspend fun getNewsList() {
        try {
            val res = networkRepo.getBannerPicService(_uiState.value.newsOptionItems[0])
            _uiState.update { it.copy(bannerPicList = ResultWithStatus(res)) }
            Log.i("TAG666", "getNewsList: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList: $e")
        }
    }

}