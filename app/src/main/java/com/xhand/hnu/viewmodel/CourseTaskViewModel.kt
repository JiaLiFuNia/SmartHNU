package com.xhand.hnu.viewmodel

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
import com.xhand.hnu.repository.Term
import com.xhand.hnu.repository.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CourseTaskUiState(
    val isGettingTask: Boolean = true,
    var taskList: List<Skrwlist> = emptyList(),
    var userInfo: UserInfoEntity? = null
)

class CourseTaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseTaskUiState())
    val uiState: StateFlow<CourseTaskUiState> = _uiState.asStateFlow()

    val term: Term = TokenRepository.getCurrentTerm()
    val longGradeTerm = term.longGradeTerm
    val currentTerm = term.currentLongTerm

    var showBookSelect by mutableStateOf(false)

    private val taskService = GradeService.instance()

    var selectTerm by mutableIntStateOf(
        longGradeTerm.indexOf(
            term.nextLongTerm
        )
    )
    var isGettingTask by mutableStateOf(true)
    suspend fun getTask(xnxqdm: String) {
        try {
            Log.i("TAG666", "getTask: $selectTerm")
            val res =
                taskService.courseTask(GradePost(xnxqdm), token = _uiState.value.userInfo?.token ?: "")
            if (res.code == 200) {
                _uiState.update {
                    it.copy(taskList = res.skrwlist)
                }
                Log.i("TAG666", "getTask: ${_uiState.value.taskList}")
            }
            _uiState.update {
                it.copy(isGettingTask = false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}