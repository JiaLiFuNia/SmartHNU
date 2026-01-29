package com.smart.htu.screens.application

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.smart.htu.MainActivity
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.screens.main.TaskType
import com.smart.htu.utils.Permission
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.icon.extended.Remove
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AddTaskBottomSheet(
    show: MutableState<Boolean>,
    initTaskInfo: TaskEntity? = null,
    onTask: (TaskEntity) -> Unit
) {
    val task = mutableStateOf(initTaskInfo ?: TaskEntity.emptyTask())
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    SuperBottomSheet(
        show = show,
        title = "创建任务",
        startAction = {
            IconButton(onClick = { show.value = false }) {
                Icon(MiuixIcons.Remove, contentDescription = null)
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
                Card {
                    SuperArrow(
                        title = "开始时间",
                        endActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                task.value.startDateTime.toString(),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SuperArrow(
                        title = "结束时间",
                        endActions = {
                            top.yukonga.miuix.kmp.basic.Text(
                                task.value.endDateTime.toString(),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            focusManager.clearFocus()
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
                            if (Permission.hasCalendarPermissions(context)) {
                                task.value = task.value.copy(isAddToCalendar = it)
                            } else {
                                if (context is MainActivity) {
                                    context.requestCalendarPermissions()
                                }
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
}