package com.smart.htu.screens.application.courseHelper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseItemEntity
import com.smart.htu.api.module.CourseTimeEntity
import com.smart.htu.api.module.SelectableCourseTypeEntity
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.JWCNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class CourseHelperUiState(
    val cookie: String = "",
    val allCourseType: List<SelectableCourseTypeEntity>,
    val courseRepo: List<CourseItemEntity>? = null,
    val searchCourseRepo: List<CourseItemEntity> = emptyList(),
    val targetCourseList: List<CourseItemEntity> = emptyList(),
    val courseInfo: List<CourseTimeEntity>? = null
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
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "02",
                    courseTypeName = "体育专选",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "03",
                    courseTypeName = "外语专选",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "06",
                    courseTypeName = "专业选修",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "07",
                    courseTypeName = "文学专选",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "08",
                    courseTypeName = "体育专选",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                ),
                SelectableCourseTypeEntity(
                    courseTypeId = "10",
                    courseTypeName = "音乐专选",
                    courseTermString = "默认学期",
                    description = "仅示例，不代表可选，请等待加载最新可选列表",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now()
                )
            )
        )
    )
    val uiState: StateFlow<CourseHelperUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCourseType()
        }
    }

    fun searchCourse(text: String) {
        _uiState.update {
            it.copy(
                searchCourseRepo = _uiState.value.courseRepo?.filter {
                    it.courseName.contains(text, true) ||
                            it.category.contains(text, true) ||
                            (it.teacherName?.contains(text, true) ?: false)
                } ?: emptyList()
            )
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

    suspend fun getCourseInfo(
        termCode: String = "202502",
        courseCode: String
    ) {
        _uiState.update { it.copy(courseInfo = null) }
        jwcNetworkRepo.getCourseInfo(termCode, courseCode)
            .onSuccess { res ->
                _uiState.update { it.copy(courseInfo = res) }
            }
            .onFailure {  res ->
                _uiState.update { it.copy(courseInfo = emptyList()) }
            }
    }

    suspend fun getCourseType() {
        getWebCookie()
        jwcNetworkRepo.getSelectableCourseType()
            .onSuccess { res ->
                _uiState.update { it.copy(allCourseType = res) }
            }
            .onFailure {
                _uiState.update { it.copy(allCourseType = emptyList()) }
            }
    }

    fun addTargetCourse(course: CourseItemEntity) {
        val currentList = _uiState.value.targetCourseList.toMutableList()
        if (!currentList.contains(course) && currentList.size < 3) {
            currentList.add(course)
            _uiState.update { it.copy(targetCourseList = currentList) }
            // dataStoreRepo.saveTargetCourseList(currentList)
        }
    }

    fun removeTargetCourse(course: CourseItemEntity) {
        val currentList = _uiState.value.targetCourseList.toMutableList()
        if (currentList.contains(course)) {
            currentList.remove(course)
            _uiState.update { it.copy(targetCourseList = currentList) }
            // dataStoreRepo.saveTargetCourseList(currentList)
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

}