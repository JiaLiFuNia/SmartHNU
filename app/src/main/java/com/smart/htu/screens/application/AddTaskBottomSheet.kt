package com.smart.htu.screens.application

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.smart.htu.component.DateTimePicker
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.screens.main.TaskType
import com.smart.htu.utils.Permission
import com.smart.htu.utils.ToastUtil.showToast
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.format.DateTimeFormatter

@Composable
fun AddTaskBottomSheet(
    show: MutableState<Boolean>,
    initTaskInfo: TaskEntity? = null,
    onTask: (TaskEntity) -> Unit
) {
    val task = remember { mutableStateOf(initTaskInfo ?: TaskEntity.emptyTask()) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val isStartDateTimePickerShow = remember { mutableStateOf(false) }
    val isEndDateTimePickerShow = remember { mutableStateOf(false) }

    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.values.any { granted -> !granted }) {
            showToast(context, "权限被拒绝，无法添加到系统日历")
        }
    }

    SuperBottomSheet(
        show = show,
        title = "创建任务",
        startAction = {
            IconButton(
                onClick = {
                    show.value = false
                }
            ) {
                Icon(MiuixIcons.Close, contentDescription = null)
            }
        },
        endAction = {
            IconButton(
                onClick = {
                    onTask(task.value)
                    show.value = false
                }
            ) {
                Icon(MiuixIcons.Ok, contentDescription = null)
            }
        },
        onDismissRequest = {
            focusManager.clearFocus()
            show.value = false
        },
        onDismissFinished = {
            focusManager.clearFocus()
            show.value = false
        },
        allowDismiss = false,
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                TextField(
                    value = task.value.title,
                    onValueChange = {
                        task.value = task.value.copy(title = it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    label = "请输入任务标题",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Card {
                    SuperDropdown(
                        title = "任务类型",
                        items = TaskType.entries.map { it.label },
                        selectedIndex = TaskType.entries.indexOf(task.value.type),
                        onSelectedIndexChange = {
                            task.value = task.value.copy(type = TaskType.entries[it])
                        }
                    )
                }
            }
            item {
                TextField(
                    value = task.value.location,
                    onValueChange = {
                        task.value = task.value.copy(location = it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    label = "请输入地点",
                    singleLine = true,
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                val formator = DateTimeFormatter.ofPattern("yyyy年M月d日E HH:mm")
                Card {
                    SuperArrow(
                        title = "开始时间",
                        endActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                task.value.startDateTime.format(formator),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
                            isStartDateTimePickerShow.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "结束时间",
                        endActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                task.value.endDateTime.format(formator),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
                            isEndDateTimePickerShow.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                TextField(
                    value = task.value.remarkableInfo ?: "",
                    onValueChange = {
                        task.value = task.value.copy(remarkableInfo = it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    label = "请输入备注信息",
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
                        checked = task.value.isAddToCalendar,
                        onCheckedChange = {
                            if (Permission.hasPermissions(
                                    context,
                                    Permission.CALENDAR_PERMISSIONS
                                )
                            ) {
                                task.value = task.value.copy(isAddToCalendar = it)
                            } else {
                                calendarPermissionLauncher.launch(Permission.CALENDAR_PERMISSIONS)
                            }
                        }
                    )
                }
            }
            item {
                Spacer(
                    Modifier.padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding() + WindowInsets.captionBar.asPaddingValues()
                            .calculateBottomPadding()
                    )
                )
            }
        }
    }

    DateTimePicker(
        title = "开始时间",
        show = isStartDateTimePickerShow,
        initialTime = task.value.startDateTime,
        onConfirm = {
            task.value = task.value.copy(startDateTime = it)
        }
    )

    DateTimePicker(
        title = "结束时间",
        show = isEndDateTimePickerShow,
        initialTime = task.value.endDateTime,
        onConfirm = {
            task.value = task.value.copy(endDateTime = it)
        }
    )
}