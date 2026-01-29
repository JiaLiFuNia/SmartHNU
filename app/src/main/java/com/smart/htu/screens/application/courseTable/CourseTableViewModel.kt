package com.smart.htu.screens.application.courseTable

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.repo.AppNetworkRepo
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_LOGIN_STATE
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_USERNAME
import com.smart.htu.repo.JWCNetworkRepo
import com.smart.htu.repo.SharedDataRepository
import com.smart.htu.utils.FileUtil.saveTextToFile
import com.smart.htu.utils.TermUtil.getCurrentTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CourseTableUiState(
    val weekCourseSchedule: List<List<CourseEntity>>? = null,
    val allCourseSchedule: List<List<List<CourseEntity>>>? = null,
    val localCourseSchedule: Map<String, List<List<List<CourseEntity>>>> = emptyMap(),
    val isSharing: MutableState<Boolean> = mutableStateOf(false),
    val isImporting: Boolean = false,
    val shareCode: String = "",
    val selectedCourseLabel: String = "我的课表",

    val weekIndex: Int = 1,
    val totalWeekCount: Int = 0,
    val termCode: String,
    val termList: List<SingleTerm> = emptyList(),

    val selectedDataSource: Int = 0,
    val backgroundBlurRadius: Dp = 20.dp,
    val isWeekendCourseShow: Boolean = false,
    val isWriteCalendarEnable: Boolean = false,
    val username: String = DEFAULT_USERNAME,
    val loginJWCState: Int = DEFAULT_LOGIN_STATE,
    val selectedTermCode: String = getCurrentTerm()
)

@HiltViewModel
class CourseTableViewModel @Inject constructor(
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val appNetworkRepo: AppNetworkRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val sharedDataRepo: SharedDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CourseTableUiState(
            termCode = getCurrentTerm()
        )
    )
    val uiState: StateFlow<CourseTableUiState> = _uiState.asStateFlow()

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

    private val weekendCourseShowState = dataStoreRepo.observeWeekendCourseShowState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeWeekendCourseShowState().first()
            }
        )

    private val backgroundBlurRadiusState = dataStoreRepo.observeCourseTableBackgroundBlurRadius()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeCourseTableBackgroundBlurRadius().first()
            }
        )

    private val localCourseScheduleStateFlow = dataStoreRepo.observeCourseTableData()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observeCourseTableData().first()
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
                _uiState.update { it.copy(isWriteCalendarEnable = value) }
            }
        }
        viewModelScope.launch {
            weekendCourseShowState.collect { isShow ->
                _uiState.update { it.copy(isWeekendCourseShow = isShow) }
            }
        }
        viewModelScope.launch {
            backgroundBlurRadiusState.collect { radius ->
                _uiState.update { it.copy(backgroundBlurRadius = radius.dp) }
            }
        }
        viewModelScope.launch {
            combine(
                sharedDataRepo.termList,
                sharedDataRepo.totalWeekCount,
                sharedDataRepo.weekIndex
            ) { termList, totalTermCount, weekIndex ->
                Triple(termList, totalTermCount, weekIndex)
            }.collect { (termList, totalTermCount, weekIndex) ->
                _uiState.update {
                    it.copy(
                        termList = termList,
                        totalWeekCount = totalTermCount,
                        weekIndex = weekIndex
                    )
                }
            }
        }
        viewModelScope.launch {
            localCourseScheduleStateFlow.collect { value ->
                _uiState.update {
                    it.copy(
                        localCourseSchedule = value,
                        allCourseSchedule = value[it.selectedCourseLabel],
                        weekCourseSchedule = value[it.selectedCourseLabel]?.getOrNull(it.weekIndex - 1)
                    )
                }
            }
        }
        viewModelScope.launch {
            refreshCourseSchedule()
        }
    }

    suspend fun refreshCourseSchedule() {
        if (_uiState.value.selectedDataSource == 0) getCourseSchedule(0)
        else getCourseScheduleJWC(0)
    }

    suspend fun getCourseSchedule(week: Int) {
        try {
            jwcNetworkRepo.getCourseScheduleService(
                week = when (week) {
                    in 1.._uiState.value.totalWeekCount -> week.toString()
                    else -> "all"
                }
            ).onSuccess { res ->
                _uiState.update {
                    it.copy(
                        allCourseSchedule = res,
                        weekCourseSchedule = res.getOrNull(_uiState.value.weekIndex - 1), // 本周课表
                        termCode = sharedDataRepo.currentTermCode.first()
                    )
                }
                saveCourseScheduleToLocal("我的课表", res)
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseSchedule: $e")
        }
    }

    suspend fun getCourseScheduleJWC(week: Int) {
        try {
            jwcNetworkRepo.getCourseScheduleJWCService(
                week = when (week) {
                    in 1.._uiState.value.totalWeekCount -> week.toString()
                    else -> ""
                },
                termCode = _uiState.value.selectedTermCode,
                totalWeeks = _uiState.value.totalWeekCount
            ).onSuccess { res ->
                _uiState.update {
                    it.copy(
                        allCourseSchedule = res,
                        weekCourseSchedule = res.getOrNull(_uiState.value.weekIndex - 1),
                        termCode = _uiState.value.selectedTermCode
                    )
                }
            }
        } catch (e: Exception) {
            Log.i("TAG666", "getCourseScheduleJWC: $e")
        }
    }

    suspend fun shareCourseSchedule(
        onShareSuccess: (String) -> Unit,
        onShareFailure: (String) -> Unit
    ) {
        try {
            _uiState.update { it.copy(isSharing = mutableStateOf(true)) }
            val courseData = _uiState.value.allCourseSchedule
            if (courseData.isNullOrEmpty()) {
                onShareFailure("没有课表数据可供分享")
                return
            }
            val courseDataJson = Json.encodeToString<List<List<List<CourseEntity>>>>(courseData)
            appNetworkRepo.shareCourseService(courseDataJson)
                .onSuccess { res ->
                    _uiState.update { it.copy(isSharing = mutableStateOf(false), shareCode = res) }
                    onShareSuccess(res)
                }
                .onFailure { e ->
                    onShareFailure(e.message ?: "分享课表失败，请稍后重试")
                }
            _uiState.update { it.copy(isSharing = mutableStateOf(false)) }
        } catch (e: Exception) {
            Log.e("TAG666", "shareCourseSchedule: 分享课表失败 ${e.message}")
        }
    }

    suspend fun importSharedCourseSchedule(
        shareCode: String,
        onImportSuccess: () -> Unit,
        onImportFailure: (String) -> Unit
    ) {
        try {
            _uiState.update { it.copy(isImporting = true) }
            appNetworkRepo.importSharedCourseService(shareCode)
                .onSuccess { res ->
                    saveCourseScheduleToLocal(res.first, res.second)
                    onImportSuccess()
                }
                .onFailure { e ->
                    onImportFailure(e.message.toString())
                }
            _uiState.update { it.copy(isImporting = false) }
        } catch (e: Exception) {
            Log.e("TAG666", "importSharedCourseSchedule: 导入课表失败 ${e.message}")
        }
    }

    fun setIsWriteCalendarPermissionGranted(isGranted: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setIsWriteCalendarPermissionGranted(isGranted)
        }
    }

    fun exportToICS(
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val courseData = _uiState.value.allCourseSchedule
                if (courseData.isNullOrEmpty()) {
                    onSuccess("没有课表数据导出")
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
                    content = icsContent,
                    targetDirectory = Environment.DIRECTORY_DOWNLOADS
                )
                onSuccess("已成功导出到下载目录")
            } catch (e: Exception) {
                Log.e("TAG666", "导出ICS文件失败: ${e.message}")
                onFailure("导出ICS文件失败: ${e.message}")
            }
        }
    }

    private fun buildICSFile(
        courses: List<List<List<CourseEntity>>>,
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
            sb.append("UID:${course.courseName}-${course.weekIndex}-${course.dayOfWeek}-${course.classTimeCode}@htu\n")
            sb.append(
                "DTSTAMP:${
                    ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).format(formatter)
                }\n"
            )
            sb.append("DTSTART:${startDateTime.format(formatter)}\n")
            sb.append("DTEND:${endDateTime.format(formatter)}\n")
            sb.append("SUMMARY:${course.courseName}\n")
            sb.append("LOCATION:${course.classroomName ?: course.projectName} ${course.teacherName}\n")
            sb.append("DESCRIPTION:第${course.sessionList.first()} - ${course.sessionList.last()}节\\n${course.classroomName ?: course.projectName ?: ""}\\n${course.teacherName}\n")

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

    fun saveCourseScheduleToLocal(
        label: String,
        data: List<List<List<CourseEntity>>>
    ) {
        viewModelScope.launch {
            val localData = _uiState.value.localCourseSchedule.toMutableMap()
            localData[label] = data
            dataStoreRepo.saveCourseTableData(localData)
        }
    }

    fun deleteLocalCourseSchedule(label: String) {
        viewModelScope.launch {
            val localData = _uiState.value.localCourseSchedule.toMutableMap()
            localData.remove(label)
            dataStoreRepo.saveCourseTableData(localData)
        }
    }

    fun changeSelectedCourseLabel(label: String) {
        _uiState.update {
            it.copy(
                selectedCourseLabel = label,
                allCourseSchedule = it.localCourseSchedule.get(label),
                weekCourseSchedule = it.localCourseSchedule.get(label)
                    ?.getOrNull(_uiState.value.weekIndex - 1),
            )
        }
    }

    fun changeSharingState(isSharing: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSharing = mutableStateOf(isSharing)) }
        }
    }

    fun getDateOfWeekMonday(weekIndex: Int): LocalDate {
        return sharedDataRepo.getDateOfWeekMonday(weekIndex - 1)
    }

    fun changeDateSource(dateSourceIndex: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedDataSource = dateSourceIndex) }
        }
    }

    fun changeBackgroundBlurRadius(blurRadius: Dp) {
        viewModelScope.launch {
            dataStoreRepo.changeCourseTableBackgroundBlurRadius(blurRadius.value.toInt())
        }
    }

    fun changeIsShowWeekendCourse(isShow: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.changeWeekendCourseShowState(isShow)
        }
    }

    fun changeSelectedTermCode(termCode: String) {
        _uiState.update { it.copy(selectedTermCode = termCode) }
    }

}