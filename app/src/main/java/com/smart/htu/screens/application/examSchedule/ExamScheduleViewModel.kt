package com.smart.htu.screens.application.examSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
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
import java.time.LocalTime
import javax.inject.Inject

data class ExamScheduleUiState(
    val examScheduleList: List<ExamEntity> = emptyList(),
    val globalTermCode: String,
    val blurEffect: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class ExamScheduleViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ExamScheduleUiState(
            globalTermCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<ExamScheduleUiState> = _uiState.asStateFlow()

    private val blurStateFlow = dataStoreRepo.observerBlurState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observerBlurState().first()
            }
        )

    private val globalTermCodeStateFlow = dataStoreRepo.observeGlobalTermCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeGlobalTermCode().first()
            }
        )

    private val examScheduleStateFlow = dataStoreRepo.observeExamScheduleList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeExamScheduleList().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEffect = value) }
            }
        }
        viewModelScope.launch {
            globalTermCodeStateFlow.collect { value ->
                _uiState.update { it.copy(globalTermCode = value) }
            }
        }
        viewModelScope.launch {
            examScheduleStateFlow.collect { value ->
                _uiState.update { it.copy(examScheduleList = value) }
            }
        }
        // initExamSchedule()
    }

    fun initExamSchedule() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    examScheduleList = emptyList()/*listOf(
                        ExamEntity(
                            "202501",
                            LocalDate.now(),
                            LocalTime.now(),
                            LocalTime(13, 30),
                            120.0f,
                            "高等数学A(1)",
                            "综合楼A-101",
                            "12",
                            ExamType.FINAL
                        ),
                        ExamEntity(
                            "202501",
                            LocalDate(2024, 6, 20),
                            LocalTime(10, 30),
                            LocalTime(14, 30),
                            120.0f,
                            "大学英语(1)",
                            "综合楼B-202",
                            "34",
                            ExamType.MIDTERM
                        ),
                        ExamEntity(
                            "202501",
                            LocalDate(2024, 6, 21),
                            LocalTime(13, 30),
                            LocalTime(14, 30),
                            120.0f,
                            "计算机导论",
                            "综合楼C-303",
                            "2",
                            ExamType.CERTIFICATE
                        ),
                        ExamEntity(
                            "202501",
                            LocalDate.now(),
                            LocalTime(13, 30),
                            LocalTime(14, 30),
                            120.0f,
                            "大学物理A(1)",
                            "综合楼D-404",
                            "2",
                            ExamType.FINAL
                        ),
                        ExamEntity(
                            "202501",
                            LocalDate.now(),
                            LocalTime(10, 30),
                            LocalTime(14, 30),
                            120.0f,
                            "思想道德修养与法律基础",
                            "综合楼E-505",
                            "2",
                            ExamType.OTHER
                        )
                    )*/
                )
            }
        }
    }

    fun addExamSchedule(examEntity: ExamEntity) {
        viewModelScope.launch {
            val newExamList = uiState.value.examScheduleList.toMutableList().apply {
                this.add(examEntity.apply {
                    this.duration =
                        (LocalTime.of(endTime.hour, endTime.minute).toSecondOfDay() - LocalTime.of(
                            startTime.hour,
                            startTime.minute
                        ).toSecondOfDay()) / 60f
                    this.id = System.currentTimeMillis().toString()
                })
            }
            dataStoreRepo.saveExamScheduleList(newExamList)
        }
    }

    fun deleteExamSchedule(examEntity: ExamEntity) {
        viewModelScope.launch {
            val newExamList = uiState.value.examScheduleList.toMutableList().apply {
                this.remove(examEntity)
            }
            dataStoreRepo.saveExamScheduleList(newExamList)
        }
    }

    fun modifyExamSchedule(oldExamEntityId: String, newExamEntity: ExamEntity) {
        viewModelScope.launch {
            val newExamList = uiState.value.examScheduleList.toMutableList().apply {
                val index = this.indexOfFirst { it.id == oldExamEntityId }
                if (index != -1) {
                    this[index] = newExamEntity.apply {
                        this.duration =
                            (LocalTime.of(endTime.hour, endTime.minute)
                                .toSecondOfDay() - LocalTime.of(
                                startTime.hour,
                                startTime.minute
                            ).toSecondOfDay()) / 60f
                    }
                }
            }
            dataStoreRepo.saveExamScheduleList(newExamList)
        }
    }

}