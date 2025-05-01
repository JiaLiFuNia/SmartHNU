package com.smart.htu.screens.application.grade

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.GradeData
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN_VALIDITY
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.Term.getCurrentTerm
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

data class GradeUiState(
    val courseGrade: ResultWithStatus<List<GradeData>> = ResultWithStatus(),
    val termCode: String,
    val globalTermCode: String,
    val termList: List<SingleTerm> = emptyList(),
    val isTokenValid: Boolean = DEFAULT_TOKEN_VALIDITY,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GradeUiState(
            termCode = getCurrentTerm(),
            globalTermCode = getCurrentTerm(),
        )
    )
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
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
            tokenValidStateFlow.collect { value ->
                _uiState.update { it.copy(isTokenValid = value) }
            }
        }
        viewModelScope.launch {
            sharedDataRepository.termIndex
                .collect { termIndex ->
                    _uiState.update {
                        it.copy(
                            termList = termIndex?.termList ?: emptyList(),
                            globalTermCode = termIndex?.termCode ?: getCurrentTerm(),
                            termCode = termIndex?.termCode ?: getCurrentTerm(),
                        )
                    }
                }
        }
        viewModelScope.launch {
            refreshTermList()
            getCourseGrade()
        }
    }

    fun refreshTermList() {
        viewModelScope.launch {
            sharedDataRepository.getTermIndex()
        }
    }

    fun getCourseGrade() = viewModelScope.launch {
        try {
            val res = jwcNetworkRepo.getCourseGradeService(
                GlobalTerm(_uiState.value.termCode)
            )
            _uiState.update { uiState ->
                uiState.copy(courseGrade = ResultWithStatus(res?.gradeData))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseGrade: $e")
        }
    }

    fun changeTermCode(termCode: String) {
        _uiState.update { it.copy(termCode = termCode) }
    }

}