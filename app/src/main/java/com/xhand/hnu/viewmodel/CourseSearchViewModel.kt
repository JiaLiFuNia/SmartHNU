package com.xhand.hnu.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.UserInfoManager
import com.xhand.hnu.model.entity.CourseSearchKBList
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

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

    var searchContent = mutableStateOf(
        CourseSearchPost(
            1,
            50,
            "202302",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "2024-06-13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "启智楼204"
        )
    )
    var searchResult by mutableStateOf(mutableListOf<CourseSearchKBList>())
    fun courseSearch() = viewModelScope.launch {
        try {
            val res = searchService.courseSearch(searchContent.value, userInfo?.token ?: "")
            searchResult = res.kbList.toMutableList()
            Log.i("TAG2310", res.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}