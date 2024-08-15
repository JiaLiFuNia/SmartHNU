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
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.CourseSearchKBList
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("MutableCollectionMutableState")
class CourseSearchViewModel() : ViewModel() {

    var userInfo: UserInfoEntity? = null
    private val searchService = GradeService.instance()

    var showDatePicker by mutableStateOf(false)
    var showRoomSheet by mutableStateOf(false)
    val searchContentKeys = listOf(
        CourseSearchContentKeys("pageNumber", "页码", icon = null, false),
        CourseSearchContentKeys("pageSize", "每页大小", icon = null, false),
        CourseSearchContentKeys("xnxqdm", "学期", courseIndex = "xnxqList", readOnly = true),
        CourseSearchContentKeys("xqdm", "校区", courseIndex = "xqList", readOnly = true),
        CourseSearchContentKeys("zydm", "专业", courseIndex = "zyList", readOnly = true),
        CourseSearchContentKeys("kcmc", "课程名称", null),
        CourseSearchContentKeys("kcywmc", ""),
        CourseSearchContentKeys("teaxm", "教师", null),
        CourseSearchContentKeys("jxbmc", "教学班", null, show = false),
        CourseSearchContentKeys("jzwdm", "教学楼", courseIndex = "jxlList", readOnly = true),
        CourseSearchContentKeys("gnqdm", "功能区", courseIndex = "gnqList", readOnly = true, show = false),
        CourseSearchContentKeys("rq", "日期", icon = Icons.Filled.DateRange, readOnly = true),
        CourseSearchContentKeys("zc", "周数", null),
        CourseSearchContentKeys("xq", "星期", null),
        CourseSearchContentKeys("jcdm", "节次(如:0304)", null),
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
            "202302",
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
    var searchResult by mutableStateOf(mutableListOf<CourseSearchKBList>())
    var isGettingCourse by mutableStateOf(false)
    var searchCourseIndex by mutableStateOf(
        CourseSearchIndexEntity(
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
        )
    )
    suspend fun getCourse(searchContent: CourseSearchPost) {
        try {
            isGettingCourse = true
            Log.i("TAG2310", "getCourse(): $searchContent")
            val res = searchService.courseSearch(searchContent, userInfo?.token ?: "")
            searchResult = res.kbList.toMutableList()
            isGettingCourse = false
            Log.i("TAG2310", "getCourse(): $res")
        } catch (e: Exception) {
            Log.i("TAG2310", "getCourse: $e")
        }
    }

    fun courseSearch(searchContent: CourseSearchPost) = viewModelScope.launch {
        getCourse(searchContent)
    }

    suspend fun getCourseIndex() {
        try {
            searchCourseIndex = searchService.courseSearchIndex(userInfo?.token ?: "")
            Log.i("TAG2310", "searchCourseList: $searchCourseIndex")
        } catch (e: Exception) {
            Log.i("TAG2310", "searchCourseList: $e")
        }
    }
    private fun getCurrentDates(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
}