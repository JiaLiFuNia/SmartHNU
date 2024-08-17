package com.xhand.hnu.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

data class CourseTaskUiState(
    val isGettingTask: Boolean = true,
    var taskList: List<Skrwlist> = emptyList(),
    var userInfo: UserInfoEntity? = null,
    val term: Term? = null
)

class CourseTaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseTaskUiState())
    val uiState: StateFlow<CourseTaskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userInfo = TokenRepository.getToken())
            }
            _uiState.update {
                it.copy(term = TokenRepository.getCurrentTerm())
            }
        }
    }

    var showBookSelect by mutableStateOf(false)

    private val taskService = GradeService.instance()

    var selectTerm by mutableIntStateOf(
        uiState.value.term!!.longGradeTerm.indexOf(
            uiState.value.term!!.nextLongTerm
        )
    )

    suspend fun getTask(xnxqdm: String) {
        try {
            Log.i("TAG666", "getTask: $selectTerm")
            _uiState.update {
                it.copy(isGettingTask = true)
            }
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