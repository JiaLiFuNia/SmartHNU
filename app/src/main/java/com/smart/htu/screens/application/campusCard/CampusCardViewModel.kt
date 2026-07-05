package com.smart.htu.screens.application.campusCard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.ConsumptionRecordData
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.CAMPUS_CARD_BASE_URL
import com.smart.htu.repo.CampusCardRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.DateUtil.toStringDate
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
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CampusCardUiState(
    val balance: Double? = null,
    val consumptionRecords: List<ConsumptionRecordData.ConsumptionRecordItemData>? = null,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT,
    val authLoginState: Int = DEFAULT_LOGIN_STATE, // 0 未认证 1 认证成功 -1 认证失败
    val loginState: Boolean = false,
    val breakfastFrequency: Double = 0.0,
    val averageExpenditure: Double = 0.0,
    val windowConsumptionData: Map<String, Int>? = null,
    val isLastPage: Boolean = false,
    val isRefreshing: Boolean = false
)

@HiltViewModel
class CampusCardViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val campusCardRepo: CampusCardRepo,
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {

    private val _uiState = MutableStateFlow(CampusCardUiState())
    val uiState: StateFlow<CampusCardUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val authLoginStateFlow = dataStoreRepo.observeLoginState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginState().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            authLoginStateFlow.collect { value ->
                _uiState.update { it.copy(authLoginState = value) }
            }
        }
        viewModelScope.launch {
            if (_uiState.value.authLoginState == 1) {
                _uiState.update { it.copy(loginState = true) }
                getCardBalance()
                getConsumptionRecord(
                    beginDate = LocalDate.now().minusDays(30).toStringDate(),
                    endDate = getCurrentDate(),
                    pageSize = 100
                )
            }
            if (!_uiState.value.loginState) {
                login()
            }
        }
    }

    suspend fun getCardBalance() {
        campusCardRepo.getCardBalance()
            .onSuccess { res ->
                _uiState.update { it.copy(balance = res.data.mainFare, loginState = true) }
            }.onFailure {
                _uiState.update { it.copy(balance = null, loginState = false) }
            }
    }

    suspend fun getConsumptionRecord(
        beginDate: String,
        endDate: String,
        beginIndex: Int = 0,
        pageSize: Int = 20,
        isAppend: Boolean = false
    ) {
        if (!isAppend) _uiState.update { it.copy(isRefreshing = true) }
        campusCardRepo.getConsumptionRecord(
            beginIndex = beginIndex,
            pageSize = pageSize,
            beginDate = beginDate,
            endDate = endDate
        )
            .onSuccess { res ->
                val newRecords = res.data.data
                val isLastPage = newRecords.size < pageSize

                _uiState.update { state ->
                    val updatedRecords = if (isAppend) {
                        (state.consumptionRecords ?: emptyList()) + newRecords
                    } else {
                        newRecords
                    }

                    // 仅在首页或全量获取时重新计算统计信息（如果 beginIndex 为 0）
                    val frequency =
                        if (beginIndex == 0 && pageSize >= 30) calculateBreakfastFrequency(
                            updatedRecords
                        ) else state.breakfastFrequency
                    val average =
                        if (beginIndex == 0 && pageSize >= 30) calculateAverageExpenditure(
                            updatedRecords
                        ) else state.averageExpenditure
                    val windowData =
                        if (beginIndex == 0) calculateWindowConsumption(updatedRecords) else state.windowConsumptionData

                    state.copy(
                        consumptionRecords = updatedRecords,
                        loginState = true,
                        breakfastFrequency = frequency,
                        averageExpenditure = average,
                        windowConsumptionData = windowData,
                        isLastPage = isLastPage,
                        isRefreshing = false
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        consumptionRecords = if (isAppend) it.consumptionRecords else null,
                        loginState = false,
                        isRefreshing = false
                    )
                }
            }
    }

    private fun calculateBreakfastFrequency(records: List<ConsumptionRecordData.ConsumptionRecordItemData>): Double {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val count = records
            .filter { record ->
                try {
                    val dateTime = LocalDateTime.parse(record.businessOpDate, formatter)
                    val time = dateTime.toLocalTime()
                    time.isAfter(LocalTime.of(5, 59)) && time.isBefore(LocalTime.of(10, 31))
                } catch (_: Exception) {
                    false
                }
            }
            .map { it.businessOpDate.substring(0, 10) } // 提取日期部分
            .distinct() // 每天仅计入一次
            .size
        return count / 30.0
    }

    private fun calculateAverageExpenditure(records: List<ConsumptionRecordData.ConsumptionRecordItemData>): Double {
        val totalExpense = records.filter { it.tradeFlag == "1" }.sumOf { it.amount }
        return totalExpense / 30.0
    }

    private fun calculateWindowConsumption(records: List<ConsumptionRecordData.ConsumptionRecordItemData>): Map<String, Int> {
        return records.filter { it.tradeFlag == "1" } // 仅统计支出
            .groupBy { it.location }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .take(5) // 仅取前五个窗口，其余的可选汇总为“其他”
            .toMap()
    }

    suspend fun login() {
        campusCardRepo.login()
            .onSuccess {
                _uiState.update { it.copy(loginState = true) }
                Log.d(
                    "TAG666 CampusCardViewModel",
                    "login: 登录成功，cookie: ${
                        networkCookieJar.loadCookiesForUrl(
                            CAMPUS_CARD_BASE_URL
                        )
                    }"
                )
                getCardBalance()
                getConsumptionRecord(
                    beginDate = LocalDate.now().minusDays(30).toStringDate(),
                    endDate = getCurrentDate(),
                    pageSize = 100
                )
            }.onFailure {
                _uiState.update { it.copy(loginState = false) }
            }
    }


}