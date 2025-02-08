package com.smart.htu.screens.application.classroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.NetworkRepo
import com.smart.htu.utils.Constants.Companion.BUILDING_LIST
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
    val buildingsOccupation: Map<String, ClassroomOccupationEntity> = emptyMap(),
    val isLoading: Boolean = true,
    val token: String = DEFAULT_TOKEN,
    val isTokenValid: Boolean = true,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ClassroomSearchViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassroomUiState(
            buildingsList = BUILDING_LIST,
        )
    )
    val uiState: StateFlow<ClassroomUiState> = _uiState.asStateFlow()

    private val _blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )
    private val _tokenStateFlow = dataStoreRepo.observeJWCToken()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeJWCToken().first()
            }
        )

    init {
        viewModelScope.launch {
            _blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            _tokenStateFlow.collect { value ->
                _uiState.update { it.copy(token = value) }
            }
        }
        getClassroomOccupation(getCurrentDates())
    }

    fun getClassroomOccupation(date: String) = viewModelScope.launch {
        try {
            changeLoadingState(true)
            _uiState.value.buildingsList.forEach { it ->
                val res = networkRepo.getClassroomOccupationService(
                    building = BuildingEntity(
                        it.buildingCode,
                        it.buildingName,
                        date
                    ),
                    token = _uiState.value.token
                )
                Log.i("TAG666", "getClassroomOccupation: $res")
                res.onSuccess {
                    _uiState.update { uiState ->
                        uiState.copy(buildingsOccupation = uiState.buildingsOccupation + (it.buildingName to it))
                    }
                    changeLoadingState(false)
                }
                res.onFailure { failure ->
                    if (failure.message == "401")
                        _uiState.update { uiState ->
                            uiState.copy(isTokenValid = false)
                        }
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getClassroomOccupation: $e")
        }
    }

    fun changeLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }
}