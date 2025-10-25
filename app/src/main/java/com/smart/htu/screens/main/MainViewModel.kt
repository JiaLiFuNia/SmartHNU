package com.smart.htu.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.api.module.HolidayData
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.NowWeatherData
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.api.module.WarningWeatherData
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.utils.Constants.Companion.INIT_COMMON_APP_LIST
import com.smart.htu.utils.DateUtil.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    val currentWeather: ResultWithStatus<NowWeatherData> = ResultWithStatus(),
    val warningWeatherData: List<WarningWeatherData> = emptyList(),
    val newsList: ResultWithStatus<List<NewsItemEntity>> = ResultWithStatus(),
    val courseSchedule: CourseScheduleEntity? = null,
    val examScheduleList: List<ExamEntity> = emptyList(),
    val holiday: HolidayData? = null,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val username: String = DEFAULT_USERNAME,
    val readNoticeIdList: List<String> = emptyList(),
    val noticeIdList: List<String> = emptyList(),
    val update: UpdateEntity = UpdateEntity(),
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

    private val examScheduleStateFlow = dataStoreRepo.observeExamScheduleList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeExamScheduleList().first()
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
            examScheduleStateFlow.collect { value ->
                _uiState.update { it.copy(examScheduleList = value) }
            }
        }
        viewModelScope.launch {
            sharedDataRepository.getUpdate()
            sharedDataRepository.update
                .collect { config ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            update = config ?: UpdateEntity(),
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
            val currentWeatherDeferred = async { getCurrentWeather() }
            val holidayDeferred = async { getHoliday() }
            val getWarningWeatherDeferred = async { getWarningWeather() }

            checkJWCToken()
            val termIndex = async { sharedDataRepository.getTermIndex() }
            val todayCourseDeferred = async { getTodayCourse() }
            val currentWeekDeferred = async { getCurrentWeek() }

            currentWeatherDeferred.await()
            holidayDeferred.await()
            getWarningWeatherDeferred.await()
            termIndex.await()
            todayCourseDeferred.await()
            currentWeekDeferred.await()
        }
    }

    suspend fun refreshNoticeAndUpdateMessage() {
        sharedDataRepository.getNotice()
        sharedDataRepository.getUpdate()
    }

    private suspend fun checkJWCToken() {
        jwcNetworkRepo.checkJWCTokenService()
            .onSuccess { changeLoginJWCState(1) }
            .onFailure { changeLoginJWCState(-2) }
    }

    suspend fun getCurrentWeek() {
        try {
            jwcNetworkRepo.getCourseScheduleService()
                .onSuccess { res ->
                    _uiState.update { it.copy(courseSchedule = res) }
                }
        } catch (e: Exception) {
            Log.i("TAG666 main", "getCurrentWeek: $e")
        }
    }

    suspend fun getCurrentWeather() {
        try {
            val res = networkRepo.getWeatherService()
            _uiState.update {
                it.copy(currentWeather = ResultWithStatus(res))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getNowWeather: $e")
        }
    }

    fun getWarningWeather() = viewModelScope.launch {
        networkRepo.getWarningWeatherService()
            .onSuccess { res ->
                _uiState.update { it.copy(warningWeatherData = res) }
            }
    }

    suspend fun getTodayCourse() {
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

    suspend fun getHoliday() {
        try {
            val res = appNetworkRepo.holidayService(getCurrentDate())
            res.onSuccess {
                _uiState.update { uiState ->
                    uiState.copy(holiday = it.holiday)
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

    private fun changeLoginJWCState(state: Int) {
        viewModelScope.launch {
            dataStoreRepo.changeLoginJWCState(state = state)
        }
    }

}