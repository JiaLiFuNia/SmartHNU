package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.utils.Constants.Companion.ALL_APP_LIST
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
    val blurEnabled: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ApplicationUiState(
            appList = ALL_APP_LIST.sortedBy { it.category }
        )
    )
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    private val blurEnabledStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    init {
        viewModelScope.launch {
            blurEnabledStateFlow.collect { value ->
                _uiState.update { it.copy(blurEnabled = value) }
            }
        }
    }

}