package com.smart.htu.screens.application.grade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseGradeDetailRes.CourseGradeDetailEntity
import com.smart.htu.api.module.CourseGradeRes.CourseGradeEntity
import com.smart.htu.api.module.CreditItemEntity
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.TermUtil.getCurrentTerm
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
    val termCode: String,
    val globalTermCode: String,
    val termList: List<SingleTerm> = emptyList(),
    val courseGPA: Map<String, Pair<List<String>, List<Double>>>,
    val allCredits: List<CreditItemEntity>? = null,
    val courseGrade: List<CourseGradeEntity>? = null,
    val courseGradeDetail: CourseGradeDetailEntity? = null,
    val isLoadingGPA: Boolean = false,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
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
            courseGPA = mapOf(
                "专业计划" to Pair(emptyList(), emptyList()),
                "全部" to Pair(emptyList(), emptyList())
            )
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
            getCourseGPA()
            getAllCredits()
        }
    }

    suspend fun refreshTermList() {
        sharedDataRepository.getTermIndex()
    }

    suspend fun getCourseGrade() {
        jwcNetworkRepo.getCourseGradeService(
            termCode = GlobalTerm(_uiState.value.termCode)
        )
            .onSuccess { res ->
                _uiState.update { it.copy(courseGrade = res.gradeData) }
            }
            .onFailure {
                _uiState.update { it.copy(courseGrade = null) }
            }
    }

    suspend fun getCourseGradeDetail(gradeCode: String) {
        _uiState.update { it.copy(courseGradeDetail = null) }
        jwcNetworkRepo.getCourseGradeDetailService(gradeCode)
            .onSuccess { res ->
                _uiState.update { it.copy(courseGradeDetail = res.gradeData) }
            }
            .onFailure {
                _uiState.update { it.copy(courseGradeDetail = null) }
            }
    }

    val type = mapOf("专业计划" to "01", "全部" to "")
    val statisticalMethod = mapOf("学期" to "1", "学年" to "2")
    suspend fun getCourseGPA(statisticalMethodIndex: Int = 1) {
        _uiState.update { it.copy(isLoadingGPA = true) }
        val currentMap = _uiState.value.courseGPA.toMutableMap()
        type.forEach { (key, value) ->
            jwcNetworkRepo.getCourseGPAService(
                statisticalMethod.values.toList()[statisticalMethodIndex], value
            ).onSuccess { res ->
                currentMap[key] = Pair(res.map { it.label }, res.map { it.gpa.toDouble() })
            }.onFailure { _uiState.update { it.copy(courseGPA = currentMap) } }
        }
        _uiState.update { it.copy(courseGPA = currentMap) }
        _uiState.update { it.copy(isLoadingGPA = false) }
    }

    suspend fun getAllCredits() {
        jwcNetworkRepo.getCourseCreditService()
            .onSuccess { res ->
                _uiState.update { it.copy(allCredits = res) }
            }
            .onFailure {
                _uiState.update { it.copy(allCredits = null) }
            }
    }

    fun changeTermCode(termCode: String) {
        _uiState.update { it.copy(termCode = termCode) }
    }

}