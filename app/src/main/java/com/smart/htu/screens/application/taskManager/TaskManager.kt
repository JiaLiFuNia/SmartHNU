package com.smart.htu.screens.application.taskManager

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.application.AddTaskBottomSheet
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.FloatingActionButton
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Delete
import top.yukonga.miuix.kmp.icon.extended.SelectAll
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TaskManager(
    navController: NavController,
    viewModel: TaskManagerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val hazeState = rememberHazeState()
    val scope = rememberCoroutineScope()

    val isAddTaskBottomSheetShow = remember { mutableStateOf(false) }
    val initTaskInfo = remember { mutableStateOf<TaskEntity?>(null) }

    val isSelectMode = remember { mutableStateOf(false) }
    val selectedTaskIdList = remember { mutableStateOf(listOf<String>()) }

    BackHandler(enabled = true) {
        if (isSelectMode.value) {
            isSelectMode.value = false
            selectedTaskIdList.value = emptyList()
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = if (isSelectMode.value) "已选择${selectedTaskIdList.value.size}项"
                else stringResource(R.string.task_manager),
                navigationIcon = {
                    if (isSelectMode.value) {
                        IconButton(
                            onClick = {
                                isSelectMode.value = false
                                selectedTaskIdList.value = emptyList()
                            },
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Close,
                                contentDescription = "cancel select"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    }
                },
                actions = {
                    if (isSelectMode.value) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.deleteSelectedTasks(selectedTaskIdList.value)
                                    isSelectMode.value = false
                                }
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Delete,
                                contentDescription = "delete"
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            if (selectedTaskIdList.value.isEmpty()) {
                                selectedTaskIdList.value = uiState.taskList.map { it.id }
                            } else {
                                selectedTaskIdList.value = emptyList()
                            }
                            isSelectMode.value = true
                        },
                        modifier = Modifier.padding(end = 16.dp),
                        enabled = uiState.taskList.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.SelectAll,
                            contentDescription = "select all"
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    initTaskInfo.value = null
                    isAddTaskBottomSheetShow.value = true
                }
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "add",
                    tint = MiuixTheme.colorScheme.onPrimary
                )
            }
        }
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
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(top = 16.dp)
                .hazeSource(hazeState)
                .overScrollVertical(),
            overscrollEffect = null
        ) {
            if (uiState.taskList.isEmpty()) {
                item {
                    EmptyContent(text = "暂无任务", image = emptyData())
                }
            }
            val currentDateTime = LocalDateTime.now()
            val tasksGroupByDate =
                uiState.taskList.groupBy { it.startDateTime.toLocalDate() }.toSortedMap()
            tasksGroupByDate.forEach { (date, tasks) ->
                item {
                    val dateString = convertLocalDateToStringDate(date, "yyyy年MM月dd日 E")
                    SmallTitle(
                        text = if (date.isEqual(LocalDate.now())
                        ) "今天 ($dateString)" else dateString,
                        insideMargin = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                items(items = tasks) { task ->
                    ExpandTaskCard(
                        task = task,
                        isInProgress = currentDateTime.isAfter(task.startDateTime) &&
                                currentDateTime.isBefore(task.endDateTime),
                        isPassed = currentDateTime.isAfter(task.endDateTime),
                        onClick = {
                            initTaskInfo.value = it
                            isAddTaskBottomSheetShow.value = true
                        },
                        selectable = isSelectMode,
                        checked = selectedTaskIdList.value.contains(task.id),
                        onLongPress = {
                            isSelectMode.value = true
                        },
                        onCheckedChange = {
                            selectedTaskIdList.value = if (it) {
                                selectedTaskIdList.value + task.id
                            } else {
                                selectedTaskIdList.value - task.id
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        AddTaskBottomSheet(
            show = isAddTaskBottomSheetShow,
            initTaskInfo = initTaskInfo.value,
            onTask = {
                if (initTaskInfo.value != null) {
                    viewModel.modifyTask(initTaskInfo.value!!.id, it)
                } else {
                    viewModel.addTask(it)
                }
            }
        )
    }
}

@Composable
fun ExpandTaskCard(
    task: TaskEntity,
    isInProgress: Boolean,
    isPassed: Boolean,
    selectable: MutableState<Boolean>,
    checked: Boolean = false,
    onClick: (TaskEntity) -> Unit = {},
    onLongPress: () -> Unit = { },
    onCheckedChange: (Boolean) -> Unit = { }
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.surfaceContainer),
        pressFeedbackType = PressFeedbackType.Sink,
        onClick = { if (selectable.value) onCheckedChange(!checked) else onClick(task) },
        onLongPress = {
            onCheckedChange(true)
            onLongPress()
        }
    ) {
        val textColor = if (isPassed) MiuixTheme.colorScheme.disabledOnSecondaryVariant
        else MiuixTheme.colorScheme.onSurface
        val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
        else MiuixTheme.colorScheme.primary
        val (statusText, statusColor) = when {
            isPassed -> "已结束" to MiuixTheme.colorScheme.outline
            isInProgress -> "进行中" to MiuixTheme.colorScheme.secondary
            else -> "待开始" to MiuixTheme.colorScheme.primary
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp)
                ) {
                    Text(
                        text = task.title,
                        style = MiuixTheme.textStyles.title3.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    InfoBadge(statusText, statusColor)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.schedule_24px),
                        contentDescription = "time",
                        modifier = Modifier
                            .size(23.dp)
                            .padding(end = 4.dp),
                        tint = iconColor
                    )
                    Text(
                        text = "${
                            convertLocalTimeToStringTime(
                                time = task.startDateTime.toLocalTime(),
                                pattern = "HH:mm"
                            )
                        } - ${
                            convertLocalTimeToStringTime(
                                time = task.endDateTime.toLocalTime(),
                                pattern = "HH:mm"
                            )
                        }",
                        modifier = Modifier.weight(1f),
                        style = MiuixTheme.textStyles.title4.copy(color = textColor),
                        textAlign = TextAlign.Start
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location_on_24px),
                            contentDescription = "building",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = iconColor
                        )
                        Text(
                            text = task.location,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            style = MiuixTheme.textStyles.body1.copy(color = textColor),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chair_24px),
                            contentDescription = "time",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = iconColor
                        )
                        Text(
                            text = "座号：${task.id}",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            style = MiuixTheme.textStyles.body1.copy(color = textColor),
                        )
                    }
                }
            }
            if (selectable.value) Spacer(modifier = Modifier.width(12.dp))
            AnimatedVisibility(
                visible = selectable.value
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckedChange(it) },
                )
            }
        }
    }
}