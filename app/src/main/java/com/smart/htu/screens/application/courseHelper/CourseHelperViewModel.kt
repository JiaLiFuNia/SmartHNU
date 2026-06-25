package com.smart.htu.screens.application.courseHelper

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseInfoEntity
import com.smart.htu.api.module.CourseItemEntity
import com.smart.htu.api.module.SelectableCourseTypeEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.JWCNetworkRepo
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

data class CourseHelperUiState(
    val cookie: String = "",
    val allCourseType: List<SelectableCourseTypeEntity>,
    val courseRepo: List<CourseItemEntity>? = null,
    val searchCourseRepo: List<CourseItemEntity> = emptyList(),
    val targetCourseList: List<CourseItemEntity> = emptyList(),
    val courseInfo: List<CourseInfoEntity>? = null,
    val isSelecting: Boolean = false,
    val isInfoDialogShow: MutableState<Boolean> = mutableStateOf(false),
    val termCode: String? = null,
    val selectionPhase: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val description: String = "",
    val isCancelable: Boolean = false
)

@HiltViewModel
class CourseHelperViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CourseHelperUiState(
            allCourseType = listOf(
                SelectableCourseTypeEntity(
                    courseTypeId = "01",
                    courseTypeName = "公共任选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "02",
                    courseTypeName = "体育专选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "03",
                    courseTypeName = "外语专选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "06",
                    courseTypeName = "专业选修",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "07",
                    courseTypeName = "文学专选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "08",
                    courseTypeName = "体育专选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "10",
                    courseTypeName = "音乐专选",
                    description = "仅示例，不代表可选，请等待加载最新可选列表或选课通知"
                )
            )
        )
    )
    val uiState: StateFlow<CourseHelperUiState> = _uiState.asStateFlow()

    private val targetCourseListStateFlow = dataStoreRepo.observeTargetCourseList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeTargetCourseList().first()
            }
        )

    init {
        viewModelScope.launch {
            targetCourseListStateFlow.collect { value ->
                _uiState.update {
                    it.copy(targetCourseList = value)
                }
            }
        }
        viewModelScope.launch {
            getCourseType()
        }
        viewModelScope.launch {
            delay(500)
            changeInfoDialogShow(true)
        }
    }

    fun searchCourse(text: String) {
        _uiState.update {
            it.copy(
                searchCourseRepo = _uiState.value.courseRepo?.filter {
                    it.courseName.contains(text, true) ||
                            it.category.contains(text, true) ||
                            (it.teacherName?.contains(text, true) ?: false) ||
                            it.courseCategoryName.contains(text, true)
                } ?: emptyList()
            )
        }
    }

    fun selectTargetCourse(
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSelecting = true) }
            _uiState.value.targetCourseList.forEach { course ->
                selectCourse(
                    courseTypeId = course.courseTypeId,
                    courseTaskCode = course.courseTaskCode,
                    courseName = course.courseName
                ) { result ->
                    onResult("${course.courseName} $result")
                }
            }
            _uiState.update { it.copy(isSelecting = false) }
        }
    }

    suspend fun selectCourse(
        courseTypeId: String,
        courseTaskCode: String,
        courseName: String,
        onResult: (String) -> Unit = {}
    ) {
        jwcNetworkRepo.selectCourseService(
            courseTypeId = courseTypeId,
            courseTaskCode = courseTaskCode,
            courseName = courseName
        )
            .onSuccess { res ->
                onResult(res)
            }
            .onFailure { res ->
                onResult(res.message.toString())
            }
    }

    suspend fun getCourseRepo(courseTypeId: String) {
        _uiState.update { it.copy(courseRepo = null) }
        jwcNetworkRepo.getCourseRepo(courseTypeId)
            .onSuccess { res ->
                _uiState.update {
                    it.copy(
                        courseRepo = res
                            .sortedByDescending {
                                it.totalCapacity - (it.enrolledCount.toIntOrNull() ?: 0)
                            }
                            .sortedByDescending { it.courseName }
                            .sortedByDescending { it.category }
                    )
                }
            }
            .onFailure {
                _uiState.update { it.copy(courseRepo = emptyList()) }
            }
    }

    suspend fun getCourseInfo(courseCode: String, termCode: String) {
        _uiState.update { it.copy(courseInfo = null) }
        jwcNetworkRepo.getCourseInfo(termCode, courseCode)
            .onSuccess { res ->
                _uiState.update { it.copy(courseInfo = res) }
            }
            .onFailure { _ ->
                _uiState.update { it.copy(courseInfo = emptyList()) }
            }
    }

    suspend fun getCourseTypeInfo(
        courseTypeId: String
    ) {
        jwcNetworkRepo.getCourseTypeInfo(courseTypeId)
            .onSuccess { res ->
                Log.i("TAG666 getCourseType", res.toString())
                _uiState.update {
                    it.copy(
                        termCode = res.termCode,
                        selectionPhase = res.selectionPhase,
                        startTime = res.startTime,
                        endTime = res.endTime,
                        description = res.description,
                        isCancelable = res.isCancelable
                    )
                }
            }
            .onFailure {
                Log.i("TAG666 getCourseType", it.message.toString())
            }
    }

    suspend fun getCourseType() {
        getWebCookie()
        jwcNetworkRepo.getSelectableCourseType()
            .onSuccess { res ->
                if (res.isNotEmpty()) _uiState.update { it.copy(allCourseType = res) }
            }
            .onFailure {
                _uiState.update { it.copy(allCourseType = emptyList()) }
            }
    }

    suspend fun addTargetCourse(course: CourseItemEntity) {
        val currentList = _uiState.value.targetCourseList.toMutableList()
        if (!currentList.contains(course) && currentList.size < 3) {
            currentList.add(course)
            dataStoreRepo.saveTargetCourseList(currentList)
        }
    }

    suspend fun removeTargetCourse(course: CourseItemEntity) {
        val currentList = _uiState.value.targetCourseList.toMutableList()
        if (currentList.contains(course)) {
            currentList.remove(course)
            dataStoreRepo.saveTargetCourseList(currentList)
        }
    }

    suspend fun getWebCookie() {
        jwcNetworkRepo.getWebCookie().onSuccess { res ->
            _uiState.update { it.copy(cookie = res) }
        }
    }


    fun changeCourseRepo(courseRepo: List<CourseItemEntity>?) {
        _uiState.update { it.copy(courseRepo = courseRepo) }
    }

    fun changeInfoDialogShow(isShow: Boolean) {
        _uiState.update {
            it.copy(isInfoDialogShow = mutableStateOf(isShow))
        }
    }

}