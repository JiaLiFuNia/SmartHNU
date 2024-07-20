package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.xhand.hnu.components.ClassroomEmptyListItem
import com.xhand.hnu.components.ClassroomListItem
import com.xhand.hnu.components.RoomBottomSheet
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel,
    roomSearchViewModel: CourseSearchViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val haveClassroom = viewModel.haveClassRoom
    val allClassroom = viewModel.allClassRoom
    val buildingsSave = viewModel.buildingsSave
    var showMenu by remember { mutableStateOf(false) }
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }
    var currentDate by remember {
        mutableStateOf(getCurrentDates())
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh = {
        isRefreshing = true
        coroutineScope.launch {
            delay(timeMillis = 1000)
            if (viewModel.isLoginSuccess) {
                viewModel.classroomService(currentDate)
                isRefreshing = false
            }
        }
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var room by remember {
        mutableStateOf("")
    }
    LaunchedEffect(currentDate) {
        viewModel.isGettingRoom = true
        // viewModel.buildingService()
        viewModel.classroomService(currentDate)
    }
    LaunchedEffect(room, currentDate) {
        roomSearchViewModel.getCourse(
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
                currentDate,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                room
            )
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "教室查询")
                        TextButton(onClick = { showDatePicker = true }) {
                            Text(currentDate)
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "更多")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier
                            .toggleable(
                                value = checkedState,
                                onValueChange = { onStateChange(!checkedState) },
                                role = Role.Checkbox
                            )
                    ) {
                        DropdownMenuItem(
                            text = { Text("仅展示空闲教室") },
                            onClick = { onStateChange(!checkedState) },
                            trailingIcon = {
                                Checkbox(
                                    checked = checkedState,
                                    onCheckedChange = {
                                        onStateChange(it)
                                        Log.i("TAG6", "$checkedState")
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    ) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
        Log.i("TAG63", confirmEnabled.value.toString())
        if (showDatePicker)
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            currentDate = timeStamp2DateStr(datePickerState.selectedDateMillis ?: 0)
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("取消")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        if (viewModel.isGettingRoom)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        else {
            val isShowSubList = remember {
                mutableStateListOf(
                    false,
                    false,
                    false,
                    false
                )
            }
            val isShowSubSubList = remember {
                mutableStateListOf(
                    false,
                    false,
                    false,
                    false,
                    false
                )
            }
            val timeList = mapOf(
                "8:00-9:40(上午第1-2节)" to "0102",
                "10:10-11:50(上午第3-4节)" to "0304",
                "15:00-16:40(下午第1-2节)" to "0506",
                "17:10-18:50(下午第3-4节)" to "0708",
                "20:00-21:40(晚上第1-2节)" to "0910"
            )
            PullToRefreshBox(
                modifier = Modifier.padding(paddingValues = it),
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() },
                state = state,
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    // val buildings = buildingsList.filter { it.jzwmc in buildingsSave }
                    Log.i("TAG654", buildingsSave.toString())
                    Log.i("TAG654", haveClassroom.toString())
                    Log.i("TAG654", allClassroom.toString())
                    buildingsSave.forEachIndexed { index, building ->
                        val arrowRotateDegrees: Float by animateFloatAsState(
                            if (isShowSubList[index]) 0f else -90f,
                            label = ""
                        )
                        ClassroomListItem(
                            text = building.jzwmc,
                            modifier = Modifier.clickable {
                                for (i in isShowSubList.indices) {
                                    if (i == index)
                                        continue
                                    isShowSubList[i] = false
                                }
                                for (j in isShowSubSubList.indices) {
                                    isShowSubSubList[j] = false
                                }
                                isShowSubList[index] = !isShowSubList[index]
                            },
                            iconModifier = Modifier
                                .rotate(arrowRotateDegrees)
                        ) {
                            if (isShowSubList[index]) {
                                Column(
                                    modifier = Modifier.padding(start = 10.dp)
                                ) {
                                    timeList.keys.forEachIndexed { timeIndex, timeDivide ->
                                        val subArrowRotateDegrees: Float by animateFloatAsState(
                                            if (isShowSubSubList[timeIndex]) 0f else -90f,
                                            label = ""
                                        )
                                        ClassroomListItem(
                                            text = timeDivide,
                                            modifier = Modifier
                                                .clickable {
                                                    isShowSubSubList[timeIndex] =
                                                        !isShowSubSubList[timeIndex]

                                                },
                                            iconModifier = Modifier
                                                .rotate(subArrowRotateDegrees),
                                        ) {
                                            if (isShowSubSubList[timeIndex])
                                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                                    allClassroom.forEach { classroom ->
                                                        if (classroom.jzwdm == building.jzwdm)
                                                            if (classroom.jxcdmc.substring(
                                                                    0,
                                                                    4
                                                                ) != "启智楼5"
                                                            )
                                                                ClassroomEmptyListItem(
                                                                    classroom = classroom,
                                                                    modifier = Modifier.clickable {
                                                                        room = classroom.jxcdmc
                                                                        roomSearchViewModel.showRoomSheet =
                                                                            true
                                                                    },
                                                                    roomAndCourseList = haveClassroom,
                                                                    jcdm = timeList[timeDivide].toString(),
                                                                    isShowHadClassRoom = checkedState
                                                                )
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    if (roomSearchViewModel.showRoomSheet) {
        RoomBottomSheet(viewModel = roomSearchViewModel, room = room)
    }
}

fun timeStamp2DateStr(timeStamp: Long): String {
    if (timeStamp == 0L) {
        return getCurrentDates()
    }
    val instant = Instant.ofEpochMilli(timeStamp)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())
    val formattedDateTime = formatter.format(instant)
    return formattedDateTime
}

fun getCurrentDates(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = currentDate.format(formatter)
    return formattedDate
}
