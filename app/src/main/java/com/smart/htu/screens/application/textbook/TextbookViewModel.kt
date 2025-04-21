package com.smart.htu.screens.application.textbook

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.OverallTerm
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.api.module.Textbook
import com.smart.htu.api.module.TextbookEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_IS_TOKEN_VALID
import com.smart.htu.repo.JWCNetworkRepo
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

data class TextbookUiState(
    val termCode: String,
    val termIndex: List<SingleTerm> = emptyList(),
    val courseTaskCode: String = "",
    val courseList: ResultWithStatus<TextbookEntity> = ResultWithStatus(),
    val selectableList: ResultWithStatus<List<Textbook>> = ResultWithStatus(),
    val selectedList: ResultWithStatus<List<Textbook>> = ResultWithStatus(),
    val isTokenValid: Boolean = DEFAULT_IS_TOKEN_VALID,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class TextbookViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TextbookUiState(
            termCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<TextbookUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val tokenValidStateFlow = dataStoreRepo.observeTokenValid()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTokenValid().first()
            }
        )
    private val termCodeStateFlow = dataStoreRepo.observeOverallTermCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeOverallTermCode().first()
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
            termCodeStateFlow.collect { value ->
                _uiState.update { it.copy(termCode = value) }
            }
        }
        viewModelScope.launch {
            getTermIndex()
            getTextbook(_uiState.value.termCode)
        }
    }

    suspend fun getTextbook(termCode: String) {
        try {
            val res = jwcNetworkRepo.getTextbookService(OverallTerm(termCode))
            _uiState.update { uiState ->
                uiState.copy(courseList = ResultWithStatus(res))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseGrade: $e")
        }
    }

    suspend fun getSelectableTextbookService(
        courseTaskCode: String,
        termCode: String
    ) {
        try {
            val res = jwcNetworkRepo.getSelectableTextbookService(
                termCode, courseTaskCode
            )
            _uiState.update {
                it.copy(selectableList = ResultWithStatus(res?.selectableList))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getSelectableTextbookService: $e")
        }
    }

    suspend fun getSelectedTextbookService(
        courseTaskCode: String,
        termCode: String
    ) {
        try {
            val res = jwcNetworkRepo.getSelectedTextbookService(
                termCode,
                courseTaskCode
            )
            _uiState.update {
                it.copy(selectedList = ResultWithStatus(res?.selectedList))
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getSelectedTextbookService: $e")
        }
    }

    private suspend fun getTermIndex() {
        val res = jwcNetworkRepo.getTermIndexService(OverallTerm())
        Log.i("TAG666", "getTermIndex: $res")
        res.onSuccess {
            _uiState.update { uiState ->
                uiState.copy(termIndex = it.termList)
            }
        }
    }

    fun changeTermCode(termCode: String) {
        _uiState.update { it.copy(termCode = termCode) }
    }

    fun showSnackBar(message: String, actionLabel: String? = null) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message, actionLabel)
        }
    }

}