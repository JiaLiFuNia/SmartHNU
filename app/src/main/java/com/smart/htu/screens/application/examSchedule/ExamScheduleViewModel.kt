package com.smart.htu.screens.application.examSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.repo.DataStoreRepo
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
    val blurEnabled: Boolean = true// DEFAULT_BLUR_EFFECT
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
                // _uiState.update { it.copy(blurEnabled = value) }
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

    fun modifyExamSchedule(oldExamId: String, newExam: ExamEntity) {
        viewModelScope.launch {
            val newExamList = uiState.value.examScheduleList.toMutableList().apply {
                val index = this.indexOfFirst { it.id == oldExamId }
                if (index != -1) {
                    this[index] = newExam.apply {
                        this.duration =
                            (LocalTime.of(endTime.hour, endTime.minute)
                                .toSecondOfDay() - LocalTime.of(
                                startTime.hour,
                                startTime.minute
                            ).toSecondOfDay()) / 60f
                        this.id = oldExamId
                    }
                }
            }
            dataStoreRepo.saveExamScheduleList(newExamList)
        }
    }

}