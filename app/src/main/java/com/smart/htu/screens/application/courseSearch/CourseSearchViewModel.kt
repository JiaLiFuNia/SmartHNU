package com.smart.htu.screens.application.courseSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseInfoEntity
import com.smart.htu.api.module.CourseSearchIndex.OptionItem
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.screens.main.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

data class CourseSearchUiState(
    // val searchInfo: CourseSearchPostEntity,
    val searchRes: List<CourseInfoEntity>? = null,
    val selectableTermList: List<String> = listOf(),
    val termList: List<SingleTerm> = emptyList(),
    val departmentList: List<OptionItem> = emptyList(),
    val campusList: List<OptionItem> = emptyList(),
    val buildingList: List<OptionItem> = emptyList(),
    val studentGradeList: List<OptionItem> = emptyList(),
    val studentDepartmentList: List<OptionItem> = emptyList(),
    val majorList: List<OptionItem> = emptyList(),
    val isLoadingIndex: Boolean = false,
    val currentTermCode: String = "",
    val taskList: List<TaskEntity> = emptyList(),
)

@HiltViewModel
class CourseSearchViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CourseSearchUiState()
    )
    val uiState: StateFlow<CourseSearchUiState> = _uiState.asStateFlow()

    private val taskListStateFlow = dataStoreRepo.observeTaskList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTaskList().first()
            }
        )

    init {
        viewModelScope.launch {
            taskListStateFlow.collect { value ->
                _uiState.update { it.copy(taskList = value) }
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingIndex = true) }
            delay(1000)
            courseIndex()
        }
        viewModelScope.launch {
        }
    }

    suspend fun courseIndex() {
        jwcNetworkRepo.searchCourseIndexService()
            .onSuccess { res ->
                _uiState.update {
                    it.copy(
                        currentTermCode = res.termCode,
                        termList = res.termList,
                        departmentList = listOf(OptionItem("不限", "")) + res.departmentList,
                        campusList = listOf(OptionItem("不限", "")) + res.campusList,
                        buildingList = listOf(OptionItem("不限", "")) + res.buildingList,
                        studentGradeList = listOf(
                            OptionItem(
                                "不限",
                                ""
                            )
                        ) + res.studentGradeList,
                        studentDepartmentList = listOf(
                            OptionItem(
                                "不限",
                                ""
                            )
                        ) + res.studentDepartmentList,
                        majorList = listOf(OptionItem("不限", "")) + res.majorList
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        termList = emptyList(),
                        departmentList = emptyList(),
                        campusList = emptyList(),
                        buildingList = emptyList(),
                        studentGradeList = emptyList(),
                        studentDepartmentList = emptyList(),
                        majorList = emptyList()
                    )
                }
            }
        _uiState.update { it.copy(isLoadingIndex = false) }
    }

    suspend fun courseSearch(searchInfo: CourseSearchPostEntity) {
        jwcNetworkRepo.searchCourseService(searchInfo)
            .onSuccess { res ->
                _uiState.update { it.copy(searchRes = res) }
            }
            .onFailure {
                _uiState.update { it.copy(searchRes = emptyList()) }
            }
    }

    suspend fun removeTaskList(task: TaskEntity) {
        val currentTaskList = _uiState.value.taskList
        val newTaskList = currentTaskList.toMutableList().apply {
            remove(task)
        }
        dataStoreRepo.saveTaskList(newTaskList)
    }

    suspend fun addTaskList(task: TaskEntity) {
        val currentTaskList = _uiState.value.taskList
        val newTaskList = currentTaskList.toMutableList().apply {
            add(task)
        }
        dataStoreRepo.saveTaskList(newTaskList)
    }

}