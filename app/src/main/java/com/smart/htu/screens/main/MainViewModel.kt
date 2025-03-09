package com.smart.htu.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.Course
import com.smart.htu.api.module.GiteeEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.OverallTerm
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.WeatherNowData
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
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
    val toDayCourseList: ResultWithStatus<List<Course>> = ResultWithStatus(),
    val nowWeather: ResultWithStatus<WeatherNowData> = ResultWithStatus(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val username: String = DEFAULT_USERNAME,
    val isLogSuccess: Boolean = false,
    val config: GiteeEntity? = null,
    val hadReadIdList: List<Int> = emptyList(),
    val appListIsCommonList: List<SmallCardContent> = INIT_COMMON_APP_LIST,
    val newsList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val jwcNetworkRepo: JWCNetworkRepo
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
        0
    )

    private val appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        INIT_COMMON_APP_LIST
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
            getNowWeather()
            getGiteeConfigService()
            getTermIndex()
            getTodayCourse()
            getNewsList()
        }
    }

    suspend fun getNewsList() {
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

    suspend fun getNowWeather() {
        try {
            val res = networkRepo.getWeatherService()
            Log.i("TAG666", "getNowWeather: $res")
            _uiState.update {
                it.copy(nowWeather = ResultWithStatus(res))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getNowWeather: $e")
        }
    }

    suspend fun getTodayCourse() {
        try {
            val res = jwcNetworkRepo.getTodayCourseService()
            Log.i("TAG666", "getTodayCourse: $res")
            _uiState.update { uiState ->
                uiState.copy(toDayCourseList = ResultWithStatus(res?.courseList?.sortedBy { it.sortNumber }))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getTodayCourse: $e")
        }
    }

    private suspend fun getTermIndex() {
        val res = jwcNetworkRepo.getTermIndexService(OverallTerm())
        res.onSuccess {
            setOverallTerm(term = it.termCode)
        }
    }

    suspend fun getGiteeConfigService() {
        val giteeConfig = networkRepo.getGiteeConfig()
        giteeConfig.onSuccess { res ->
            _uiState.update { it.copy(config = res) }
            dataStoreRepo.setOverallTermCode(res.termCode)
        }
    }

    private suspend fun setOverallTerm(term: String) {
        viewModelScope.launch {
            dataStoreRepo.setOverallTermCode(term)
        }
    }

}