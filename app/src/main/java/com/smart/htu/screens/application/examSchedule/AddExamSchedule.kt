package com.smart.htu.screens.application.examSchedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.icon.icons.useful.Confirm
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AddExamSchedule(
    exam: ExamEntity? = null,
    navController: NavController,
    viewModel: ExamScheduleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val hazeState = rememberHazeState()

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
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = if (exam == null) "添加考试项目" else "修改考试项目",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Back,
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
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Confirm,
                            contentDescription = "check"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = uiState.blurEnabled
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
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .hazeSource(hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical().imePadding(),
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
                    label = "考试项目名称",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Card {
                    SuperDropdown(
                        title = "考试类型",
                        items = ExamType.entries.map { it.type },
                        selectedIndex = ExamType.entries.indexOf(examEntity.value.examType),
                        onSelectedIndexChange = {
                            examEntity.value =
                                examEntity.value.copy(examType = ExamType.entries[it])
                        },
                        onClick = {
                            focusManager.clearFocus()
                        }
                    )
                }
            }
            item {
                Card {
                    SuperArrow(
                        title = "日期",
                        rightActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                convertLocalDateToStringDate(
                                    date = examEntity.value.date,
                                    pattern = "yyyy年MM月dd日"
                                ),
                                Modifier.padding(end = 8.dp),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
                            showDatePicker.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "开始时间",
                        rightActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                convertLocalTimeToStringTime(
                                    time = examEntity.value.startTime,
                                    pattern = "HH:mm"
                                ),
                                Modifier.padding(end = 8.dp),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
                            showStartTimePicker.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "结束时间",
                        rightActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                convertLocalTimeToStringTime(
                                    time = examEntity.value.endTime,
                                    pattern = "HH:mm"
                                ),
                                Modifier.padding(end = 8.dp),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
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
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
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
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
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
                        },
                        onClick = {
                            focusManager.clearFocus()
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