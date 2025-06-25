package com.smart.htu.screens.application.classroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN_VALIDITY
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.utils.getCurrentDates
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

data class ClassroomUiState(
    val buildingsList: List<BuildingEntity>,
    val buildingsOccupation: Map<Int, ClassroomOccupationEntity> = emptyMap(),
    val isLoading: Boolean = true,
    val token: String = DEFAULT_TOKEN,
    val isTokenValid: Boolean = DEFAULT_TOKEN_VALIDITY,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ClassroomSearchViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val buildingsList = listOf(
        BuildingEntity("104", "启智楼"),
        BuildingEntity("107", "新五五四楼"),
        BuildingEntity("102", "文渊楼"),
        BuildingEntity("310", "文昌楼（东综）")
    )

    private val _uiState = MutableStateFlow(
        ClassroomUiState(
            buildingsList = buildingsList,
        )
    )
    val uiState: StateFlow<ClassroomUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val tokenStateFlow = dataStoreRepo.observeJWCToken()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeJWCToken().first()
            }
        )

    private val tokenValidStateFlow = dataStoreRepo.observeTokenValidity()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTokenValidity().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            tokenStateFlow.collect { value ->
                _uiState.update { it.copy(token = value) }
            }
        }
        viewModelScope.launch {
            tokenValidStateFlow.collect { value ->
                _uiState.update { it.copy(isTokenValid = value) }
            }
        }
        viewModelScope.launch {
            getClassroomOccupation(getCurrentDates())
        }
    }

    suspend fun getClassroomOccupation(date: String, index: Int = 0) {
        try {
            changeLoadingState(true)
            buildingsList[index].let {
                jwcNetworkRepo.getClassroomOccupationService(
                    BuildingEntity(
                        buildingCode = it.buildingCode,
                        buildingName = it.buildingName,
                        date = date
                    )
                ).onSuccess {
                    _uiState.update { uiState ->
                        uiState.copy(buildingsOccupation = uiState.buildingsOccupation + (index to it))
                    }
                    setTokenValid(true)
                }.onFailure { }
            }
            changeLoadingState(false)
        } catch (e: Exception) {
            Log.i("TAG666", "getClassroomOccupation: $e")
        }
    }

    private fun setTokenValid(valid: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setTokenValidity(valid)
        }
    }

    fun changeLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }
}