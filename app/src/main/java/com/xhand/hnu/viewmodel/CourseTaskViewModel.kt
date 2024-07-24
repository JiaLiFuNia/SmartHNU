package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xhand.hnu.model.UserInfoManager
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.Skrwlist
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CourseTaskViewModel(context: Context) : ViewModel() {

    private val userInfoManager = UserInfoManager(context)

    init {
        viewModelScope.launch {
            val userInfoStore = userInfoManager.userInfo.firstOrNull()
            userInfo = userInfoStore
        }
    }

    private val grade: String
        get() {
            return userInfo?.studentID?.substring(0, 2) ?: "22"
        }
    private val gradeInt: Int
        get() {
            return grade.toInt()
        }

    val longGradeTerm: MutableList<String>
        get() {
            return mutableListOf(
                "20${gradeInt}-20${gradeInt + 1}-1",
                "20${gradeInt}-20${gradeInt + 1}-2",
                "20${gradeInt + 1}-20${gradeInt + 2}-1",
                "20${gradeInt + 1}-20${gradeInt + 2}-2",
                "20${gradeInt + 2}-20${gradeInt + 3}-1",
                "20${gradeInt + 2}-20${gradeInt + 3}-2",
                "20${gradeInt + 3}-20${gradeInt + 4}-1",
                "20${gradeInt + 3}-20${gradeInt + 4}-2"
            )
        }

    // 学期
    val gradeTerm: MutableList<String>
        get() {
            return mutableListOf(
                "20${gradeInt}01",
                "20${gradeInt}02",
                "20${gradeInt + 1}01",
                "20${gradeInt + 1}02",
                "20${gradeInt + 2}01",
                "20${gradeInt + 2}02",
                "20${gradeInt + 3}01",
                "20${gradeInt + 3}02",
            )
        }

    var showBookSelect by mutableStateOf(false)

    private var userInfo: UserInfoEntity? = null
    private val taskService = GradeService.instance()

    @SuppressLint("MutableCollectionMutableState")
    var taskList = mutableListOf<Skrwlist>()
    var selectTerm by mutableIntStateOf(4)
    var isGettingTask by mutableStateOf(true)
    suspend fun getTask(xnxqdm: String) {
        try {
            val res =
                taskService.courseTask(GradePost(xnxqdm), token = userInfo?.token ?: "")
            if (res.code == 200) {
                taskList = res.skrwlist.toMutableList()
                Log.i("TAG666", "getTask: $taskList")
            }
            isGettingTask = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}