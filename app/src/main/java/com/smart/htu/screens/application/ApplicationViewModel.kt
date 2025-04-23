package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.utils.Constants.Companion.ALL_APP_LIST
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

data class ApplicationUiState(
    val appList: List<SmallCardContent>,
    val appListIsCommonList: List<SmallCardContent>,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ApplicationUiState(
            appList = ALL_APP_LIST.sortedBy { it.category },
            appListIsCommonList = INIT_COMMON_APP_LIST
        )
    )
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    private val _appListIsCommonListStateFlow = dataStoreRepo.observeSmallCard()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeSmallCard().first()
            }
        )

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking { dataStoreRepo.observerBlurState().first() }
        )

    init {
        viewModelScope.launch {
            _appListIsCommonListStateFlow.collect { value ->
                _uiState.update { it.copy(appListIsCommonList = value) }
            }
        }
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
    }

    fun changeCommonAppListState(app: SmallCardContent, add: Boolean = true) {
        viewModelScope.launch {
            val currentListState = _uiState.value.appListIsCommonList.toMutableList()
            if (add)
                currentListState.apply { add(app) }
            else
                currentListState.apply { remove(app) }
            dataStoreRepo.saveSmallCard(currentListState)
            _uiState.update { it.copy(appListIsCommonList = currentListState) }
        }
    }
}