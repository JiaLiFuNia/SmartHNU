package com.xhand.hnu.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.Skrwlist
import com.xhand.hnu.model.entity.UserInfoEntity
import com.xhand.hnu.network.GradeService

class CourseTaskViewModel(
    settingsViewModel: SettingsViewModel
) : ViewModel() {
    val gradeTerm = settingsViewModel.gradeTerm
    val longGradeTerm = settingsViewModel.longGradeTerm

    var showBookSelect by mutableStateOf(false)

    var userInfo: UserInfoEntity? = null
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