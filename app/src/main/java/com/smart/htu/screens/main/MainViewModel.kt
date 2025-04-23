package com.smart.htu.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.Course
import com.smart.htu.api.module.GiteeEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.WeatherNowData
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.utils.Constants.Companion.INIT_COMMON_APP_LIST
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

data class AppUiState(
    val todayCourseList: ResultWithStatus<List<Course>> = ResultWithStatus(),
    val currentWeather: ResultWithStatus<WeatherNowData> = ResultWithStatus(),
    val newsList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val username: String = DEFAULT_USERNAME,
    val isLogSuccess: Boolean = false,
    val hadReadIdList: List<Int> = emptyList(),
    val giteeConfig: GiteeEntity? = null,
    val appListIsCommonList: List<SmallCardContent> = INIT_COMMON_APP_LIST
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val usernameStateFlow = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeUsername().first()
            }
        )

    private val hadReadIdListStateFlow = dataStoreRepo.observeNoticeReadIdList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observeNoticeReadIdList().first() }
        )

    private val loginState = dataStoreRepo.observeLoginState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        runBlocking { dataStoreRepo.observeLoginState().first() }
    )

    private val appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        runBlocking { dataStoreRepo.observeSmallCard().first() }
    )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            usernameStateFlow.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            loginState.collect { value ->
                _uiState.update {
                    it.copy(isLogSuccess = value == 1)
                }
            }
        }
        viewModelScope.launch {
            appListIsCommonListStateFlow.collect { value ->
                _uiState.update { it.copy(appListIsCommonList = value) }
            }
        }
        viewModelScope.launch {
            hadReadIdListStateFlow.collect { value ->
                _uiState.update { it.copy(hadReadIdList = value) }
            }
        }
        viewModelScope.launch {
            sharedDataRepository.getGiteeConfig()
            sharedDataRepository.getTermIndex()
        }
        viewModelScope.launch {
            sharedDataRepository.giteeConfig
                .collect { config ->
                    _uiState.update {
                        it.copy(giteeConfig = config)
                    }
                }
        }
        getNewsList()
        getCurrentWeather()
        getTodayCourse()
    }

    fun refreshGiteeConfig() {
        viewModelScope.launch {
            sharedDataRepository.getGiteeConfig()
        }
    }

    fun getNewsList() = viewModelScope.launch {
        try {
            val res = networkRepo.getNewsService(
                newsOptionItems = NewsCategoryEntity(
                    label = NewsType.RESEARCH,
                    source = "河南师范大学主页",
                    academic = "",
                    type = "xsygcs"
                ),
                page = 1
            )
            _uiState.update { it.copy(newsList = ResultWithStatus(res)) }
            Log.i("TAG666", "getNewsList: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList: $e")
        }
    }

    fun getCurrentWeather() = viewModelScope.launch {
        try {
            val res = networkRepo.getWeatherService()
            Log.i("TAG666", "getNowWeather: $res")
            _uiState.update {
                it.copy(currentWeather = ResultWithStatus(res))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getNowWeather: $e")
        }
    }

    fun getTodayCourse() = viewModelScope.launch {
        try {
            val res = jwcNetworkRepo.getTodayCourseService()
            Log.i("TAG666", "getTodayCourse: $res")
            _uiState.update { uiState ->
                uiState.copy(todayCourseList = ResultWithStatus(res?.courseList?.sortedBy { it.sortNumber }))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getTodayCourse: $e")
        }
    }

}