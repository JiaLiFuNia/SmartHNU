package com.smart.htu.screens.application.courseTable

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.Course
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_TOKEN_VALIDITY
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.utils.Term.getCurrentTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CourseTableUiState(
    val courseTable: ResultWithStatus<List<List<Course>>> = ResultWithStatus(),
    val startDateCurrentWeek: LocalDate? = null,
    val week: String? = "-",
    val todayWeekday: Int? = 1,
    val termCode: String,
    val termList: List<SingleTerm> = emptyList(),
    val termRange: Pair<Int, Int> = Pair(0, 20),
    val isTokenValid: Boolean = DEFAULT_TOKEN_VALIDITY
)

@HiltViewModel
class CourseTableViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CourseTableUiState(
            termCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<CourseTableUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCourseSchedule()
            calculateStartDatePerWeek()
        }
    }

    fun calculateStartDatePerWeek() {
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek.value
        _uiState.update {
            it.copy(
                todayWeekday = currentDayOfWeek,
                startDateCurrentWeek = today.minusDays((currentDayOfWeek - 1).toLong())
            )
        }
    }

    suspend fun getCourseSchedule() {
        try {
            val res = jwcNetworkRepo.getCourseScheduleService()
            // 转换为 List<List<Course>>
            val processedCourses = res?.courseTable?.map { weekMap ->
                weekMap.values.flatten()
            } ?: emptyList()
            _uiState.update {
                it.copy(
                    courseTable = ResultWithStatus(processedCourses),
                    termCode = res?.termCode ?: getCurrentTerm(),
                    termRange = Pair(res?.minWeek?.toInt() ?: 0, res?.maxWeek?.toInt() ?: 0),
                    week = res?.week
                )
            }
            Log.i("TAG666", "getCourseSchedule: $res")
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseSchedule: $e")
        }
    }
}