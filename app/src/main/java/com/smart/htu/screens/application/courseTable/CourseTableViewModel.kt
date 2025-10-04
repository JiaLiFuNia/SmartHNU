package com.smart.htu.screens.application.courseTable

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseScheduleEntity
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.utils.FileUtil.saveTextToFile
import com.smart.htu.utils.TermUtil.getCurrentTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CourseTableUiState(
    val currentWeekCourseTable: List<List<CourseEntity>>? = null,
    val allCourseTable: MutableList<List<MutableList<CourseEntity>>> =
        MutableList(25) { List(7) { mutableListOf() } },
    val startDatePerWeek: LocalDate? = null,
    val week: Int = 0,
    val todayWeekday: Int? = 1,
    val termCode: String,
    val termList: List<SingleTerm> = emptyList(),
    val termRange: Pair<Int, Int> = Pair(0, 25),
    val username: String = DEFAULT_USERNAME,
    val isWriteCalendarEnabled: Boolean = false,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE
)

@HiltViewModel
class CourseTableViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CourseTableUiState(
            termCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<CourseTableUiState> = _uiState.asStateFlow()

    val snackBarHostState = SnackbarHostState()

    private val usernameStateFlow = dataStoreRepo.observeUsername()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeUsername().first()
            }
        )

    private val writeCalendarPermissionStateFlow =
        dataStoreRepo.observeIsWriteCalendarPermissionGranted()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                runBlocking {
                    dataStoreRepo.observeIsWriteCalendarPermissionGranted().first()
                }
            )

    private val loginJWCStateStateFlow = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )

    init {
        viewModelScope.launch {
            usernameStateFlow.collect { value ->
                _uiState.update { it.copy(username = value) }
            }
        }
        viewModelScope.launch {
            loginJWCStateStateFlow.collect { value ->
                _uiState.update { it.copy(loginJWCState = value) }
            }
        }
        viewModelScope.launch {
            writeCalendarPermissionStateFlow.collect { value ->
                _uiState.update { it.copy(isWriteCalendarEnabled = value) }
            }
        }
        viewModelScope.launch {
            getCurrentWeekCourseSchedule(0)
        }
    }

    suspend fun getCourseSchedule(week: Int): CourseScheduleEntity? {
        try {
            jwcNetworkRepo.getCourseScheduleService(
                week = when (week) {
                    -1 -> "all"
                    in 1.._uiState.value.termRange.second -> week.toString()
                    else -> ""
                }
            ).onSuccess {
                Log.i("TAG666", "getCourseSchedule: $it")
                return it
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseSchedule: $e")
            return null
        }
        return null
    }

    suspend fun getCurrentWeekCourseSchedule(week: Int) {
        try {
            _uiState.update { it.copy(currentWeekCourseTable = null) }
            val res = getCourseSchedule(week)
            val processedCourses = res?.courseTable?.map { weekMap ->
                weekMap.values.flatten()
            } ?: emptyList()
            Log.i("TAG666", "getCurrentWeekCourseSchedule: $processedCourses")
            _uiState.update {
                it.copy(
                    currentWeekCourseTable = processedCourses,
                    termCode = res?.termCode ?: getCurrentTerm(),
                    termRange = Pair(res?.minWeek?.toInt() ?: 0, res?.maxWeek?.toInt() ?: 0),
                    week = res?.week ?: 0,
                    todayWeekday = res?.todayWeekday,
                    startDatePerWeek = res?.date?.minusDays(res.todayWeekday.toLong() - 1) // 往前推res?.todayWeekday - 1天
                )
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCurrentWeekCourseSchedule: $e")
        }
    }

    suspend fun getAllWeekCourseSchedule(): MutableList<List<MutableList<CourseEntity>>>? {
        try {
            val res = getCourseSchedule(-1)
            // 合并成按天的课表
            val processedCourses = res?.courseTable?.map { weekMap ->
                weekMap.values.flatten()
            } ?: emptyList()
            // 处理成按周的课表 第几周 星期几 当天的课
            val courseTable: MutableList<List<MutableList<CourseEntity>>> =
                MutableList(25) { List(7) { mutableListOf() } }
            processedCourses.forEach {
                it.forEach { course ->
                    val weekIndex = course.week
                    val weekday = course.weekday
                    courseTable[weekIndex - 1][weekday - 1].add(course)
                }
            }
            _uiState.update {
                it.copy(allCourseTable = courseTable)
            }
            return courseTable
        } catch (e: Exception) {
            return null
        }
    }

    fun setIsWriteCalendarPermissionGranted(isGranted: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setIsWriteCalendarPermissionGranted(isGranted)
        }
    }

    fun exportToICS() {
        viewModelScope.launch {
            try {
                val courseData = getAllWeekCourseSchedule()
                if (courseData.isNullOrEmpty()) {
                    showSnackBar("没有课表数据导出")
                    return@launch
                }
                val icsContent = buildICSFile(
                    courseData,
                    _uiState.value.termCode,
                    _uiState.value.username
                )
                val fileName =
                    "${_uiState.value.username}_${_uiState.value.termCode}学期课表_${System.currentTimeMillis()}.ics"
                saveTextToFile(
                    fileName = fileName,
                    content = icsContent
                )
                showSnackBar("$fileName 已成功导出到下载目录")
            } catch (e: Exception) {
                Log.e("TAG666", "导出ICS文件失败: ${e.message}")
                showSnackBar("导出失败: ${e.message}")
            }
        }
    }

    private fun buildICSFile(
        courses: MutableList<List<MutableList<CourseEntity>>>,
        termCode: String,
        username: String
    ): String {
        val sb = StringBuilder()
        // 文件头
        sb.append("BEGIN:VCALENDAR\n")
        sb.append("VERSION:2.0\n")
        sb.append("PRODID:-//SmartHNU Course Schedule//EN\n")
        sb.append("CALSCALE:GREGORIAN\n")
        sb.append("METHOD:PUBLISH\n")
        sb.append("X-WR-CALNAME:${username}_${termCode}学期课表\n")
        sb.append("X-WR-TIMEZONE:Asia/Shanghai\n")

        val flattenedCourses = courses.flatMapIndexed { weekIndex, weekList ->
            weekList.flatMapIndexed { dayIndex, dayCourses ->
                dayCourses
            }
        }

        flattenedCourses.forEach { course ->
            val startDateTime = ZonedDateTime.of(
                course.startDate,
                course.startTime,
                ZoneId.of("Asia/Shanghai")
            )
            val endDateTime = ZonedDateTime.of(
                course.endDate,
                course.endTime,
                ZoneId.of("Asia/Shanghai")
            )
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
            sb.append("BEGIN:VEVENT\n")
            sb.append("UID:${course.courseName}-${course.week}-${course.weekday}-${course.classTimeCode}@htu\n")
            sb.append(
                "DTSTAMP:${
                    ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).format(formatter)
                }\n"
            )
            sb.append("DTSTART:${startDateTime.format(formatter)}\n")
            sb.append("DTEND:${endDateTime.format(formatter)}\n")
            sb.append("SUMMARY:${course.courseName}\n")
            sb.append("LOCATION:${course.classroomName ?: course.projectName} ${course.teacherName}\n")
            sb.append("DESCRIPTION:第${course.sectionList.first()} - ${course.sectionList.last()}节\\n${course.classroomName ?: course.projectName ?: ""}\\n${course.teacherName}\n")

            sb.append("BEGIN:VALARM\n")
            sb.append("ACTION:DISPLAY\n")
            sb.append("TRIGGER;RELATED=START:-PT10M\n")
            sb.append("DESCRIPTION:${course.courseName}@${course.classroomName ?: course.projectName}\n")
            sb.append("END:VALARM\n")

            sb.append("END:VEVENT\n")
        }

        // 文件尾
        sb.append("END:VCALENDAR\n")
        return sb.toString()
    }

    fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message, actionLabel, withDismissAction, duration)
        }
    }

}