package com.smart.htu.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.HolidayEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.api.module.WeatherCurrentData
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.utils.Constants.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.utils.getCurrentDates
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
    val todayCourseList: List<CourseEntity>? = null,
    val currentWeather: ResultWithStatus<WeatherCurrentData> = ResultWithStatus(),
    val newsList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus(),
    val courseSchedule: CourseScheduleEntity? = null,
    val holidayEntity: HolidayEntity? = null,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val username: String = DEFAULT_USERNAME,
    val readNoticeIdList: List<Int> = emptyList(),
    val noticeIdList: List<Int> = emptyList(),
    val updateEntity: UpdateEntity = UpdateEntity(),
    val isShowUpdateDialog: MutableState<Boolean> = mutableStateOf(false),
    val commonAppList: List<ApplicationEntity> = INIT_COMMON_APP_LIST,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo,
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val appNetworkRepo: AppNetworkRepo,
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

    private val readNoticeIdListStateFlow = dataStoreRepo.observeReadNoticeIdList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observeReadNoticeIdList().first() }
        )

    private val commonAppListStateFlow = dataStoreRepo.observeCommonAppList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observeCommonAppList().first() }
        )

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    init {
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
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
            commonAppListStateFlow.collect { value ->
                _uiState.update { it.copy(commonAppList = value) }
            }
        }
        viewModelScope.launch {
            readNoticeIdListStateFlow.collect { value ->
                _uiState.update { it.copy(readNoticeIdList = value) }
            }
        }
        viewModelScope.launch {
            sharedDataRepository.getUpdate()
            sharedDataRepository.update
                .collect { config ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            updateEntity = config ?: UpdateEntity(),
                            isShowUpdateDialog = mutableStateOf(config?.isNeedUpdate == true)
                        )
                    }
                }
        }
        viewModelScope.launch {
            sharedDataRepository.getNotice()
            sharedDataRepository.notice
                .collect { config ->
                    _uiState.update {
                        it.copy(
                            noticeIdList = config?.data?.map { it.id } ?: emptyList()
                        )
                    }
                }
        }
        viewModelScope.launch {
            sharedDataRepository.getTermIndex()
        }
        viewModelScope.launch { getNewsList() }
        viewModelScope.launch { getCurrentWeather() }
        viewModelScope.launch { getHoliday() }
        viewModelScope.launch { getTodayCourse() }
        viewModelScope.launch { getCurrentWeek() }
    }

    suspend fun refreshNoticeAndUpdate() {
        sharedDataRepository.getNotice()
        sharedDataRepository.getUpdate()
    }

    fun getCurrentWeek() = viewModelScope.launch {
        try {
            jwcNetworkRepo.getCourseScheduleService()
                .onSuccess { res ->
                    Log.i("TAG666 main", "getCurrentWeek: $res")
                    _uiState.update { it.copy(courseSchedule = res) }
                }
        } catch (e: Exception) {
            Log.i("TAG666 main", "getCurrentWeek: $e")
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
        } catch (e: Exception) {
            Log.i("TAG666", "getNewsList: $e")
        }
    }

    fun getCurrentWeather() = viewModelScope.launch {
        try {
            val res = networkRepo.getWeatherService()
            _uiState.update {
                it.copy(currentWeather = ResultWithStatus(res))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getNowWeather: $e")
        }
    }

    fun getTodayCourse() = viewModelScope.launch {
        try {
            jwcNetworkRepo.getTodayCourseService()
                .onSuccess { res ->
                    _uiState.update {
                        it.copy(todayCourseList = res.courseList.sortedBy { it.sortNumber })
                    }
                }
        } catch (e: Exception) {
            Log.i("TAG666", "getTodayCourse: $e")
        }
    }

    fun getHoliday() = viewModelScope.launch {
        try {
            val today = getCurrentDates()
            val res = appNetworkRepo.holidayService(today)
            res.onSuccess {
                _uiState.update { uiState ->
                    uiState.copy(holidayEntity = it)
                }
            }
            Log.i("TAG666", "getHoliday: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getHoliday: $e")
        }
    }

    fun changeUpdateDialogState(state: Boolean) {
        _uiState.update { it.copy(isShowUpdateDialog = mutableStateOf(state)) }
    }

}