package com.smart.htu.screens.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.NoticeEntity
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
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
    val noticeList: List<NoticeEntity> = emptyList(),
    val readNoticeIdList: MutableList<String> = mutableListOf(),
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val appNetworkRepo: AppNetworkRepo
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
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            hadReadIdListStateFlow.collect { value ->
                _uiState.update { it.copy(readNoticeIdList = value.toMutableList()) }
            }
        }
        viewModelScope.launch {
            getNotice()
        }
    }

    suspend fun readAllNotice() {
        _uiState.value.noticeList.forEach {
            addReadNoticeId(it.id)
        }
    }

    suspend fun addReadNoticeId(id: String) {
        if (!_uiState.value.readNoticeIdList.contains(id)) {
            val currentReadIdList = _uiState.value.readNoticeIdList
            currentReadIdList.add(id)
            dataStoreRepo.addReadNoticeId(currentReadIdList)
        }
    }

    suspend fun getNotice() {
        try {
            appNetworkRepo.getNotice().onSuccess { res ->
                _uiState.update {
                    it.copy(noticeList = res.data)
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getNotice: $e")
        }
    }

    fun calculateNotReadIdListSize(): Int {
        val noticeList = _uiState.value.noticeList
        val readIdList = _uiState.value.readNoticeIdList
        var count = 0
        noticeList.forEach {
            if (!readIdList.contains(it.id)) {
                count++
            }
        }
        return count
    }
}