package com.smart.htu.screens.application.campusLife

import androidx.lifecycle.ViewModel
import com.smart.htu.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CampusLifeUiState(
    val userUrl: String = "",
    val isUrlValidity: Boolean = true
)

@HiltViewModel
class CampusLifeViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(CampusLifeUiState())

    val uiState: StateFlow<CampusLifeUiState> = _uiState.asStateFlow()


}