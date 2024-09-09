package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.components.CourseSearchContentKeys
import com.xhand.hnu.components.longToShort
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.CourseSearchKBList
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService
import com.xhand.hnu.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CourseSearchUiState(
    var userInfo: UserInfoEntity? = null,
    var searchCourseIndex: CourseSearchIndexEntity = CourseSearchIndexEntity(
        msg = "",
        zyList = mutableListOf(),
        code = 0,
        jhlxList = mutableListOf(),
        xsyxList = mutableListOf(),
        jxlList = mutableListOf(),
        xnxqmc = "",
        kkyxList = mutableListOf(),
        xnxqList = mutableListOf(),
        xnxqdm = "",
        xqList = mutableListOf(),
        gnqList = mutableListOf(),
        kkjysList = mutableListOf(),
        xsnjList = mutableListOf()
    ),
    var searchResult: MutableList<CourseSearchKBList>? = mutableListOf(),
    val isGettingCourse: Boolean = false
)

@SuppressLint("MutableCollectionMutableState")
class CourseSearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CourseSearchUiState())
    val uiState: StateFlow<CourseSearchUiState> = _uiState.asStateFlow()
    private val term = Repository.getCurrentTerm()
    private val currentTerm = term.currentLongTerm

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userInfo = Repository.getToken())
            }
        }
    }

    private val searchService = GradeService.instance()

    var showDatePicker by mutableStateOf(false)
    var showRoomSheet by mutableStateOf(false)

    val searchContentKeys = listOf(
        CourseSearchContentKeys("rq", "日期", icon = Icons.Filled.DateRange, readOnly = true),
        CourseSearchContentKeys("zc", "周数", null),
        CourseSearchContentKeys("xq", "星期", null),
        CourseSearchContentKeys("kcmc", "课程名称", null),
        CourseSearchContentKeys("zydm", "专业", courseIndex = "zyList", readOnly = true),
        CourseSearchContentKeys("jcdm", "节次(如:0304)", null),
        CourseSearchContentKeys("xnxqdm", "学期", courseIndex = "xnxqList", readOnly = true),
        CourseSearchContentKeys("jzwdm", "教学楼", courseIndex = "jxlList", readOnly = true),
        CourseSearchContentKeys("pageNumber", "页码", icon = null, false),
        CourseSearchContentKeys("pageSize", "每页大小", icon = null, false),
        CourseSearchContentKeys("xqdm", "校区", courseIndex = "xqList", readOnly = true),
        CourseSearchContentKeys("kcywmc", ""),
        CourseSearchContentKeys("teaxm", "教师", null),
        CourseSearchContentKeys("jxbmc", "教学班", null, show = false),
        CourseSearchContentKeys("gnqdm", "功能区", courseIndex = "gnqList", readOnly = true, show = false),
        CourseSearchContentKeys("kkyxdm", "开课单位", courseIndex = "kkyxList", readOnly = true),
        CourseSearchContentKeys("kkjysdm", "", readOnly = true),
        CourseSearchContentKeys("xsyxdm", "学生院系", courseIndex = "xsyxList", readOnly = true),
        CourseSearchContentKeys("xsnj", "学生年级", courseIndex = "xsnjList", readOnly = true),
        CourseSearchContentKeys("jhlxdm", "计划类型", courseIndex = "jhlxList", readOnly = true, show = false),
        CourseSearchContentKeys("jxcdmc", "教学场地", null, show = false)
    )
    var searchContent = mutableStateOf(
        CourseSearchPost(
            1,
            50000,
            longToShort(currentTerm),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            getCurrentDates(),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )

    suspend fun getCourse(searchContent: CourseSearchPost) {
        try {
            _uiState.update {
                it.copy(isGettingCourse = true)
            }
            Log.i("TAG2310", "getCourse(): $searchContent")
            val res =
                searchService.courseSearch(searchContent, _uiState.value.userInfo?.token ?: "")
            Log.i("TAG2310", "getCourse: $res")
            _uiState.update {
                it.copy(searchResult = res.kbList.toMutableList())
            }
        } catch (e: Exception) {
            Log.i("TAG2310", "getCourse: $e")
        }
        _uiState.update {
            it.copy(isGettingCourse = false)
        }
    }

    fun courseSearch(searchContent: CourseSearchPost) = viewModelScope.launch {
        getCourse(searchContent)
    }

    suspend fun getCourseIndex() {
        try {
            val searchCourseIndex =
                searchService.courseSearchIndex(_uiState.value.userInfo?.token ?: "")
            _uiState.update { uiState ->
                uiState.copy(searchCourseIndex = searchCourseIndex)
            }
            Log.i("TAG2310", "searchCourseList: $searchCourseIndex")
        } catch (e: Exception) {
            Log.i("TAG2310", "searchCourseList: $e")
        }
    }

    fun clearSearchResult() {
        _uiState.update {
            it.copy(searchResult = mutableListOf())
        }
    }

    private fun getCurrentDates(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
}