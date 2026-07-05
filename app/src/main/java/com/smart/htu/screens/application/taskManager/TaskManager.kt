package com.smart.htu.screens.application.taskManager

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.utils.Calendar.addEvent
import com.smart.htu.utils.DateUtil.toStringDate
import com.smart.htu.utils.TimeUtil.toStringTime
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
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
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Delete
import top.yukonga.miuix.kmp.icon.extended.SelectAll
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun TaskManager(
    viewModel: TaskManagerViewModel = hiltViewModel(),
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val lazyListState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
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
            navigator.pop()
        }
    }

    /*LaunchedEffect(Unit) {

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val random = System.currentTimeMillis().toInt()

        val tickerText = "还有 ${uiState.taskList.size} 个任务未完成"
        val baseInfoTitleText = uiState.taskList.lastOrNull()?.title
        val baseInfoContentText = uiState.taskList.lastOrNull()?.location
        val colorString = uiState.taskList.lastOrNull()?.type?.lightColor?.primaryColor?.toHexString()

        val picRes = Icon.createWithResource(context, R.mipmap.ic_launcher)

        val extras = FocusNotification.buildV3 {
            enableFloat = true
            ticker = tickerText
            showSmallIcon = true

            picInfo {
                pic = createPicture("pic_info", picRes)
                picDark = createPicture("pic_info_dark", picRes)
            }

            baseInfo {
                type = 2
                title = baseInfoTitleText
                content = baseInfoContentText
            }

            hintInfo {
                type = 2
                content = "时间"
                colorContent = colorString
                title = uiState.taskList.firstOrNull()?.startDateTime?.toLocalTime()
                    ?.let {
                        convertLocalTimeToStringTime(it, "HH:mm")
                    }
                actionInfo {
                    type = 2
                    actionTitle = "开启静音"

                }
            }
        }

        notificationManager.notify(
            random,
            Notification.Builder(context, FOCUS_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(tickerText)
                .setContentTitle(baseInfoTitleText)
                .setContentText(baseInfoContentText)
                .setContentIntent(pendingIntent)
                .addExtras(extras)
                .build()
        )
    }*/

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = if (isSelectMode.value) "已选择${selectedTaskIdList.value.size}项"
                    else stringResource(R.string.task_manager),
                    navigationIcon = {
                        if (isSelectMode.value) {
                            IconButton(
                                onClick = {
                                    isSelectMode.value = false
                                    selectedTaskIdList.value = emptyList()
                                },

                                ) {
                                Icon(
                                    imageVector = MiuixIcons.Regular.Close,
                                    contentDescription = "cancel select"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { navigator.pop() },

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
                            enabled = uiState.taskList.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.SelectAll,
                                contentDescription = "select all"
                            )
                        }
                    }
                )
            }
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
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .scrollEndHaptic(),
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
                        val dateString = date.toStringDate("yyyy年MM月dd日 E")
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
        }

        AddTaskBottomSheet(
            show = isAddTaskBottomSheetShow.value,
            initTaskInfo = initTaskInfo.value,
            onTask = {
                scope.launch {
                    if (initTaskInfo.value != null) {
                        viewModel.modifyTask(initTaskInfo.value!!.id, it)
                    } else {
                        viewModel.addTask(it)
                    }
                    if (it.isAddToCalendar) {
                        addEvent(
                            context = context,
                            title = it.title,
                            desc = it.remarkableInfo ?: "",
                            start = it.startDateTime.atZone(ZoneId.systemDefault())
                                .toInstant()
                                .epochSecond,
                            end = it.endDateTime.atZone(ZoneId.systemDefault())
                                .toInstant()
                                .epochSecond
                        )
                    }
                }
            },
            onDismissRequest = { isAddTaskBottomSheetShow.value = false }
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
        BasicComponent(
            title = task.title,
            summary = "${
                task.startDateTime.toLocalTime().toStringTime("HH:mm")                
            }-${
                task.endDateTime.toLocalTime().toStringTime("HH:mm")
            } | ${task.location}",
            bottomAction = if (!task.remarkableInfo.isNullOrEmpty()) {
                {
                    Text(
                        text = task.remarkableInfo,
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        maxLines = 4
                    )
                }
            } else {
                null
            },
            endActions = {
                when {
                    selectable.value -> {
                        Checkbox(
                            state = ToggleableState(checked),
                            onClick = { onCheckedChange(!checked) }
                        )
                    }

                    isInProgress -> Text(
                        text = "进行中",
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MiuixTheme.colorScheme.primary)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1
                    )

                    isPassed -> Text(
                        text = "已结束",
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MiuixTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1
                    )
                }
            },
            startAction = {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(task.type.lightColor.primaryColor)
                )
            }
        )
    }
}