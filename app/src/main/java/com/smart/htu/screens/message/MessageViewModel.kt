package com.smart.htu.screens.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.Notice
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.repo.SharedDataRepoImpl
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
    val noticeList: List<Notice> = emptyList(),
    val hadReadIdList: List<Int> = emptyList(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observerBlurState().first() }
        )

    private val _hadReadIdListStateFlow = dataStoreRepo.observeNoticeReadIdList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observeNoticeReadIdList().first() }
        )

    init {
        viewModelScope.launch {
            sharedDataRepository.giteeConfig
                .collect { config ->
                    _uiState.update {
                        it.copy(noticeList = config?.notice ?: emptyList())
                    }
                }
        }
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            _hadReadIdListStateFlow.collect { value ->
                _uiState.update { it.copy(hadReadIdList = value) }
            }
        }
    }

    fun addHadReadList(id: Int) = viewModelScope.launch {
        _uiState.update { it.copy(hadReadIdList = it.hadReadIdList + id) }
        dataStoreRepo.saveNoticeReadId(_uiState.value.hadReadIdList)
    }

    fun refreshGiteeConfig() {
        viewModelScope.launch {
            sharedDataRepository.getGiteeConfig()
        }
    }
}