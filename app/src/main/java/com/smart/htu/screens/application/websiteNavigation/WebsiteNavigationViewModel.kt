package com.smart.htu.screens.application.websiteNavigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.WebsiteNavigationEntity
import com.smart.htu.repo.AppNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WebsiteNavigationUiState(
    val websiteList: List<WebsiteNavigationEntity> = emptyList()
)

@HiltViewModel
class WebsiteNavigationViewModel @Inject constructor(
    private val appNetworkRepo: AppNetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(WebsiteNavigationUiState())
    val uiState: StateFlow<WebsiteNavigationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getWebsiteNavigation()
        }
    }

    suspend fun getWebsiteNavigation() {
        appNetworkRepo.configService()
            .onSuccess { configEntity ->
                _uiState.update {
                    it.copy(websiteList = configEntity.websiteNavigation)
                }
            }.onFailure {
                Log.i("TAG666 website", it.toString());
            }
    }

}