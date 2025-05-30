package com.smart.htu.screens.application.physicalTest

import androidx.lifecycle.ViewModel
import com.smart.htu.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PhysicalTestUiState(
    val userUrl: String = "",
    val isUrlValidity: Boolean = true
)

@HiltViewModel
class PhysicalTestViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhysicalTestUiState())

    val uiState: StateFlow<PhysicalTestUiState> = _uiState.asStateFlow()


}