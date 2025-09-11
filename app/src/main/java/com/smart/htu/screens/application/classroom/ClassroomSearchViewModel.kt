package com.smart.htu.screens.application.classroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.SharedDataRepository
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
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ClassroomSearchViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepo: SharedDataRepository
) : ViewModel() {

    private val buildingsList = listOf(
        BuildingEntity("104", "启智楼"),
        BuildingEntity("107", "新五五四楼"),
        BuildingEntity("102", "文渊楼"),
        BuildingEntity("310", "文昌楼（东综）"),
        BuildingEntity("302", "求是西楼"),
        BuildingEntity("307", "求是东楼"),
        BuildingEntity("301", "求是中楼"),
        BuildingEntity("119", "新联楼"),
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

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
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
                ).onSuccess { res ->
                    _uiState.update {
                        it.copy(buildingsOccupation = it.buildingsOccupation + (index to res))
                    }
                    Log.i("TAG666", "getClassroomOccupation: $date $res")
                }.onFailure {
                    sharedDataRepo.setJWCLoginState(-2)
                }
            }
            changeLoadingState(false)
        } catch (e: Exception) {
            Log.i("TAG666", "getClassroomOccupation: $e")
        }
    }

    fun changeLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }
}