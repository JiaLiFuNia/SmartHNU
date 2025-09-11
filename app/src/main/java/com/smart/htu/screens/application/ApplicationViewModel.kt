package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
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
    val appList: List<ApplicationEntity>,
    val commonAppList: List<ApplicationEntity>,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ApplicationUiState(
            appList = ALL_APP_LIST.sortedBy { it.category },
            commonAppList = INIT_COMMON_APP_LIST
        )
    )
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    private val commonAppListStateFlow = dataStoreRepo.observeCommonAppList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeCommonAppList().first()
            }
        )

    init {
        viewModelScope.launch {
            commonAppListStateFlow.collect { value ->
                _uiState.update { it.copy(commonAppList = value) }
            }
        }
    }

    fun changeCommonAppListState(app: ApplicationEntity, add: Boolean = true) {
        viewModelScope.launch {
            val currentList = _uiState.value.commonAppList.toMutableList()
            if (add)
                currentList.apply { add(app) }
            else
                currentList.apply { remove(app) }
            dataStoreRepo.setCommonApp(currentList)
            _uiState.update { it.copy(commonAppList = currentList) }
        }
    }
}