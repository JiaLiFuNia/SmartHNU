package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.UserInfoManager
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.CourseSearchKBList
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("MutableCollectionMutableState")
class CourseSearchViewModel(context: Context) : ViewModel() {

    private val userInfoManager = UserInfoManager(context)

    init {
        viewModelScope.launch {
            val userInfoStore = userInfoManager.userInfo.firstOrNull()
            userInfo = userInfoStore
        }
    }

    private var userInfo: UserInfoEntity? = null
    private val searchService = GradeService.instance()

    var showRoomSheet by mutableStateOf(false)

    var searchContent = mutableStateOf(
        CourseSearchPost(
            1,
            50000,
            "202302",
            "1",
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
            "",
            mutableListOf(),
            0,
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            "",
            mutableListOf(),
            mutableListOf(),
            "",
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
        )
    )
    suspend fun getCourse(searchContent: CourseSearchPost) {
        try {
            isGettingCourse = true
            Log.i("TAG2310", searchContent.toString())
            val res = searchService.courseSearch(searchContent, userInfo?.token ?: "")
            searchResult = res.kbList.toMutableList()
            isGettingCourse = false
            Log.i("TAG2310", res.toString())
        } catch (e: Exception) {
            Log.i("TAG2310", e.toString())
        }
    }
    fun courseSearch(searchContent: CourseSearchPost) = viewModelScope.launch {
        getCourse(searchContent)
    }

    suspend fun getCourseIndex() {
        try {
            searchCourseIndex = searchService.courseSearchIndex(userInfo?.token ?: "")
        } catch (e: Exception) {
            Log.i("TAG2310", e.toString())
        }
    }
    private fun getCurrentDates(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
}