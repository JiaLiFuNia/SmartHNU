package com.smart.htu.screens.application

import androidx.lifecycle.ViewModel
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.utils.Constants.Companion.ALL_APP_LIST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ApplicationUiState(
    val appList: List<ApplicationEntity>,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
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

    init {

    }

}