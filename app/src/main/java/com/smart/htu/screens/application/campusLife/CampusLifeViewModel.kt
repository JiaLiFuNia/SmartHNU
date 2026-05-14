package com.smart.htu.screens.application.campusLife

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.NowWeatherData
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CampusLifeUiState(
    val userUrl: String = "",
    val isUrlValidity: Boolean = true,
    val weatherData: NowWeatherData? = null,
    val isWeatherLoading: Boolean = false,
    val weatherError: String? = null
)

@HiltViewModel
class CampusLifeViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampusLifeUiState())
    val uiState: StateFlow<CampusLifeUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isWeatherLoading = true, weatherError = null) }
            try {
                val weather = networkRepo.getWeatherService()
                _uiState.update {
                    it.copy(
                        weatherData = weather,
                        isWeatherLoading = false,
                        weatherError = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        weatherError = "获取天气失败",
                        isWeatherLoading = false
                    )
                }
            }
        }
    }
}