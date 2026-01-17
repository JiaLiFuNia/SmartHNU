package com.smart.htu.screens.application

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.smart.htu.component.MiuixHintTextField
import com.smart.htu.screens.main.TaskEntity
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.icon.extended.Remove
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AddTaskBottomSheet(
    show: MutableState<Boolean>,
    initTaskInfo: TaskEntity? = null,
    onCreateTask: (TaskEntity) -> Unit
) {
    var task by remember { mutableStateOf(initTaskInfo) }
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
                    task?.let { onCreateTask(it) }
                    show.value = false
                }
            ) {
                Icon(MiuixIcons.Ok, contentDescription = null)
            }
        },
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        LazyColumn(

        ) {
            item {
                MiuixHintTextField(
                    value = task?.title ?: "",
                    label = "任务标题",
                    onValueChange = {
                        task = task?.copy(title = it)
                    }
                )
            }
            item {

            }
        }
    }
}