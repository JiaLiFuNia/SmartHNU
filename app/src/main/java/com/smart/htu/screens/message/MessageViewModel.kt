package com.smart.htu.screens.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.NoticeEntity
import com.smart.htu.api.module.WarningWeatherData
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepository
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

data class MessageUiState(
    val warningWeatherData: List<WarningWeatherData> = emptyList(),
    val noticeList: List<NoticeEntity> = emptyList(),
    val readNoticeIdList: List<String> = emptyList(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepository: SharedDataRepository,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observerBlurState().first() }
        )

    private val hadReadIdListStateFlow = dataStoreRepo.observeReadNoticeIdList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observeReadNoticeIdList().first() }
        )

    init {
        viewModelScope.launch {
            sharedDataRepository.notice
                .collect { config ->
                    _uiState.update {
                        it.copy(
                            noticeList = config?.data ?: emptyList()
                        )
                    }
                }
        }
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            hadReadIdListStateFlow.collect { value ->
                _uiState.update { it.copy(readNoticeIdList = value) }
            }
        }
        getWarningWeather()
    }

    fun getWarningWeather() = viewModelScope.launch {
        networkRepo.getWarningWeatherService()
            .onSuccess { res ->
                _uiState.update { it.copy(warningWeatherData = res) }
            }
    }

    fun readAllNotice() = viewModelScope.launch {
        _uiState.value.noticeList.forEach {
            addReadNoticeId(it.id)
        }
    }

    fun addReadNoticeId(id: String) = viewModelScope.launch {
        if (!_uiState.value.readNoticeIdList.contains(id)) {
            _uiState.update { it.copy(readNoticeIdList = it.readNoticeIdList + id) }
            dataStoreRepo.addReadNoticeId(_uiState.value.readNoticeIdList)
        }
    }

    suspend fun refreshNoticeData() {
        sharedDataRepository.getNotice()
    }
}