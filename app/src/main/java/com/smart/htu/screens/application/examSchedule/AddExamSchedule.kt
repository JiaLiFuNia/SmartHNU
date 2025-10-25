package com.smart.htu.screens.application.examSchedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.MainActivity
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.api.module.ExamType
import com.smart.htu.component.DatePickerDialog
import com.smart.htu.component.TimePickerDialog
import com.smart.htu.utils.Calendar.addEvent
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.Permission
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExamSchedule(
    exam: ExamEntity? = null,
    navController: NavController,
    viewModel: ExamScheduleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val showDatePicker = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }

    val examEntity = remember {
        mutableStateOf(
            ExamEntity(
                examName = exam?.examName ?: "",
                examType = exam?.examType ?: ExamType.FINAL,
                date = exam?.date ?: LocalDate.now(),
                startTime = exam?.startTime ?: LocalTime.now(),
                endTime = exam?.endTime ?: LocalTime.now(),
                duration = exam?.duration ?: 0f,
                examRoom = exam?.examRoom ?: "",
                seatNumber = exam?.seatNumber ?: "",
                termCode = uiState.globalTermCode,
                isAddToCalendar = exam?.isAddToCalendar ?: false
            )
        )
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = { Text(text = if (exam == null) "添加考试项目" else "修改考试项目") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (examEntity.value.examName.isEmpty() || examEntity.value.examRoom.isEmpty()) {
                                    showToast(context, "请填写完整信息")
                                } else {
                                    if (exam == null) {
                                        viewModel.addExamSchedule(examEntity.value)
                                    } else {
                                        viewModel.modifyExamSchedule(
                                            exam.id ?: "",
                                            examEntity.value
                                        )
                                    }
                                    if (Permission.hasCalendarPermissions(context) && examEntity.value.isAddToCalendar) {
                                        addEvent(
                                            context = context,
                                            title = examEntity.value.examName,
                                            desc = "考场：${examEntity.value.examRoom}，座位号：${examEntity.value.seatNumber}",
                                            start = examEntity.value.date.atTime(examEntity.value.startTime)
                                                .atZone(java.time.ZoneId.systemDefault())
                                                .toInstant().toEpochMilli(),
                                            end = examEntity.value.date.atTime(examEntity.value.endTime)
                                                .atZone(java.time.ZoneId.systemDefault())
                                                .toInstant().toEpochMilli()
                                        )
                                    }
                                    navController.popBackStack()
                                    showToast(context, "日程已创建")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                bottom = 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TextField(
                    value = examEntity.value.examName,
                    onValueChange = {
                        examEntity.value = examEntity.value.copy(examName = it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    label = "请输入考试项目名称",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Card {
                    SuperDropdown(
                        title = "考试类型",
                        items = ExamType.entries.map { it.type },
                        mode = DropDownMode.AlwaysOnRight,
                        selectedIndex = ExamType.entries.indexOf(examEntity.value.examType),
                        onSelectedIndexChange = {
                            examEntity.value =
                                examEntity.value.copy(examType = ExamType.entries[it])
                        }
                    )
                }
            }
            item {
                Card {
                    SuperArrow(
                        title = "日期",
                        rightText = convertLocalDateToStringDate(
                            date = examEntity.value.date,
                            pattern = "yyyy年MM月dd日"
                        ),
                        onClick = {
                            showDatePicker.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "开始时间",
                        rightText = convertLocalTimeToStringTime(
                            time = examEntity.value.startTime,
                            pattern = "HH:mm"
                        ),
                        onClick = {
                            showStartTimePicker.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "结束时间",
                        rightText = convertLocalTimeToStringTime(
                            time = examEntity.value.endTime,
                            pattern = "HH:mm"
                        ),
                        onClick = {
                            showEndTimePicker.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                TextField(
                    value = examEntity.value.examRoom,
                    onValueChange = {
                        examEntity.value = examEntity.value.copy(examRoom = it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    label = "请输入考场地点",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = examEntity.value.seatNumber,
                    onValueChange = {
                        examEntity.value = examEntity.value.copy(seatNumber = it)
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    label = "请输入座位号",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Card {
                    SuperCheckbox(
                        title = "同时添加到系统日历",
                        checked = examEntity.value.isAddToCalendar,
                        onCheckedChange = {
                            if (Permission.hasCalendarPermissions(context)) {
                                examEntity.value = examEntity.value.copy(isAddToCalendar = it)
                            } else {
                                if (context is MainActivity) {
                                    context.requestCalendarPermissions()
                                }
                            }
                        }
                    )
                }
            }
            exam?.let {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        text = "删除",
                        onClick = {
                            scope.launch {
                                viewModel.deleteExamSchedule(it)
                                showToast(context, "已删除")
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(textColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        DatePickerDialog(
            date = examEntity.value.date,
            showDatePicker = showDatePicker,
            onConfirmClick = {
                examEntity.value = examEntity.value.copy(date = it)
            }
        )

        TimePickerDialog(
            time = examEntity.value.startTime,
            showDatePicker = showStartTimePicker,
            onConfirmClick = {
                examEntity.value = examEntity.value.copy(startTime = it)
            }
        )

        TimePickerDialog(
            time = examEntity.value.endTime,
            showDatePicker = showEndTimePicker,
            onConfirmClick = {
                examEntity.value = examEntity.value.copy(endTime = it)
            }
        )
    }
}