package com.smart.htu.screens.application.taskManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_BLUR_EFFECT
import com.smart.htu.screens.main.TaskEntity
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

data class TaskManagerUiState(
    val taskList: List<TaskEntity> = emptyList(),
    val globalTermCode: String,
    val blurEnabled: Boolean = DEFAULT_BLUR_EFFECT
)

@HiltViewModel
class TaskManagerViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TaskManagerUiState(
            globalTermCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<TaskManagerUiState> = _uiState.asStateFlow()

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

    private val examScheduleStateFlow = dataStoreRepo.observeTaskList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTaskList().first()
            }
        )

    init {
        viewModelScope.launch {
            blurStateFlow.collect { value ->
                _uiState.update { it.copy(blurEnabled = value) }
            }
        }
        viewModelScope.launch {
            globalTermCodeStateFlow.collect { value ->
                _uiState.update { it.copy(globalTermCode = value) }
            }
        }
        viewModelScope.launch {
            examScheduleStateFlow.collect { value ->
                _uiState.update { it.copy(taskList = value) }
            }
        }
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            val newTaskList = _uiState.value.taskList.toMutableList()
            newTaskList.add(task.copy(id = System.currentTimeMillis().toString()))
            dataStoreRepo.saveTaskList(newTaskList)
        }
    }

    suspend fun deleteTask(taskId: String) {
        val newTaskList = _uiState.value.taskList.toMutableList().apply {
            this.removeAll { it.id == taskId }
        }
        dataStoreRepo.saveTaskList(newTaskList)
    }

    suspend fun deleteSelectedTasks(taskIds: List<String>) {
        taskIds.forEach {
            deleteTask(it)
        }
    }

    fun modifyTask(oldTaskId: String, newTask: TaskEntity) {
        viewModelScope.launch {
            val newTaskList = _uiState.value.taskList.toMutableList().apply {
                val index = this.indexOfFirst { it.id == oldTaskId }
                if (index != -1) {
                    this[index] = newTask
                }
            }
            dataStoreRepo.saveTaskList(newTaskList)
        }
    }

}