package com.smart.htu.screens.application.courseTable

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuperSlider
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.main.CourseDetailBottomSheet
import com.smart.htu.ui.theme.isInDarkTheme
import com.smart.htu.utils.CourseColorUtil.getColorByCourseName
import com.smart.htu.utils.CourseTableBackgroundUtil
import com.smart.htu.utils.CourseTableBackgroundUtil.getBackground
import com.smart.htu.utils.CourseTableBackgroundUtil.saveBackground
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import com.smart.htu.utils.CourseTimeRange.summerOrWinterTimeInterval
import com.smart.htu.utils.Permission
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Delete
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import top.yukonga.miuix.kmp.preference.CheckboxPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.lang.Integer.max
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Copyright (C) 2025
 *
 * @link https://github.com/jayfunc/GongYun-for-Android/blob/daaa651b821558319a46d672a90b3325fe9a2520/app/src/main/java/com/dart/campushelper/ui/schedule/ScheduleTable.kt
 * @author jayfunc
 * @modifier JiaLiFuNia
 */

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CourseTable(
    viewModel: CourseTableViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackBarHostState = remember { SnackbarHostState() }

    val pagerState = key(uiState.weekIndex) {
        rememberPagerState(
            pageCount = { uiState.totalWeekCount },
            initialPage = uiState.weekIndex - 1
        )
    }

    val showDropDownMenu = remember { mutableStateOf(false) }
    val isMoreSettingsBottomSheetShow = remember { mutableStateOf(false) }
    val isSharedInfoDialogShow = remember { mutableStateOf(false) }
    val isImportCourseScheduleDialogShow = remember { mutableStateOf(false) }
    val backgroundUri = remember {
        mutableStateOf(getBackground(context))
    }

    LaunchedEffect(uiState.selectedDataSource, uiState.selectedTermCode) {
        viewModel.refreshCourseSchedule()
    }

    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.values.any { granted -> !granted }) {
            scope.launch {
                snackBarHostState.showSnackbar("需要日历权限才能使用此功能")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = if (backgroundUri.value != null) Color.Transparent else MiuixTheme.colorScheme.surface,
                largeTitle = "${uiState.termCode}学期 第 ${pagerState.currentPage + 1} 周",
                title = "第 ${pagerState.currentPage + 1} 周",
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() }
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isImportCourseScheduleDialogShow.value = true
                        },
                        holdDownState = isImportCourseScheduleDialogShow.value
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = "import"
                        )
                    }
                    IconButton(
                        onClick = {
                            showDropDownMenu.value = true
                        },
                        holdDownState = showDropDownMenu.value
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.More,
                            contentDescription = "more"
                        )
                    }
                    OverlayListPopup(
                        show = showDropDownMenu.value,
                        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                        alignment = PopupPositionProvider.Align.TopEnd,
                        onDismissRequest = {
                            showDropDownMenu.value = false
                        }
                    ) {
                        val optionSize = 4
                        ListPopupColumn {
                            DropdownImpl(
                                text = "共享课表",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    viewModel.changeSharingState(true)
                                    scope.launch {
                                        viewModel.shareCourseSchedule(
                                            onShareSuccess = {
                                                isSharedInfoDialogShow.value = true
                                            },
                                            onShareFailure = {
                                                showToast(context, "创建共享课表失败：$it")
                                            }
                                        )
                                    }
                                },
                                index = 0
                            )
                            DropdownImpl(
                                text = "同步到日历",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    if (Permission.hasPermissions(
                                            context,
                                            Permission.CALENDAR_PERMISSIONS
                                        )
                                    ) {
                                        scope.launch { showToast(context, "开发中...") }
                                    } else {
                                        calendarPermissionLauncher.launch(Permission.CALENDAR_PERMISSIONS)
                                    }
                                },
                                index = 1
                            )
                            DropdownImpl(
                                text = "导出为ICS日历文件",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    viewModel.exportToICS(
                                        onSuccess = {
                                            showToast(context, "已导出到下载文件夹")
                                        },
                                        onFailure = {
                                            showToast(context, it)
                                        }
                                    )
                                },
                                index = 2
                            )
                            DropdownImpl(
                                text = "更多设置",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    isMoreSettingsBottomSheetShow.value = true
                                },
                                index = 3
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (backgroundUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(backgroundUri.value),
                    contentDescription = "背景",
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (uiState.courseTableSettings.backgroundBlur > 0f) {
                                val blurRadius: Dp = uiState.courseTableSettings.backgroundBlur.dp
                                Modifier.blur(blurRadius)
                            } else {
                                Modifier
                            }
                        ),
                    contentScale = ContentScale.Crop,
                    alpha = uiState.courseTableSettings.backgroundAlpha
                )
            }
            HorizontalPager(
                state = pagerState,
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .scrollEndHaptic()
                    .overScrollVertical()
            ) {
                WeekCourseTable(
                    isWeekendCourseShow = uiState.courseTableSettings.showWeekendCourse,
                    startDatePerWeek = viewModel.getDateOfWeekMonday(it + 1),
                    weekCourseSchedule = uiState.allCourseSchedule?.getOrNull(it),
                    courseBlockAlpha = uiState.courseTableSettings.courseBlockAlpha,
                    selectedDataSource = uiState.selectedDataSource
                )
            }
        }

        SharedInfoDialog(
            showDialog = isSharedInfoDialogShow.value,
            shareCode = uiState.shareCode,
            onCopyShareCode = { shareCode ->
                copyContent(shareCode)
                showToast(context, "分享码已复制到剪切板")
            },
            onShareToOtherApp = { shareCode ->
                val shareIntent = android.content.Intent().apply {
                    action = android.content.Intent.ACTION_SEND
                    putExtra(
                        android.content.Intent.EXTRA_TEXT,
                        "我的课表分享码：$shareCode ，30分钟内有效。"
                    )
                    type = "text/plain"
                }
                context.startActivity(
                    android.content.Intent.createChooser(shareIntent, "课表分享码")
                )
            },
            onDismissRequest = {
                isSharedInfoDialogShow.value = false
            }
        )

        SharedCourseScheduleDialog(
            showDialog = isImportCourseScheduleDialogShow.value,
            sharedIdList = uiState.localCourseSchedule.keys.toList(),
            onChooseTable = {
                viewModel.changeSelectedCourseLabel(it)
                showToast(context, "已切换到 $it")
            },
            onDeleteTable = {
                viewModel.deleteLocalCourseSchedule(it)
            },
            selectTableId = uiState.selectedCourseLabel,
            isImporting = uiState.isImporting,
            onImportCourseSchedule = { shareCode ->
                scope.launch {
                    viewModel.importSharedCourseSchedule(
                        shareCode = shareCode,
                        onImportSuccess = {
                            showToast(context, "导入成功")
                        },
                        onImportFailure = {
                            showToast(context, "导入失败，$it")
                        }
                    )
                }
            },
            onDismissRequest = {
                isImportCourseScheduleDialogShow.value = false
            },
            selectedTermCode = uiState.selectedTermCode,
            termList = uiState.termList,
            selectedDataSource = uiState.selectedDataSource,
            onSelectDataSource = {
                viewModel.changeDateSource(it)
            },
            onSelectTermCode = {
                viewModel.changeSelectedTermCode(it)
            },
        )

        BottomCircularProgressIndicator(loadingState = uiState.isSharing.value)

        CourseTableMoreSettingBottomSheet(
            showBottomSheet = isMoreSettingsBottomSheetShow.value,
            isShowWeekendCourse = uiState.courseTableSettings.showWeekendCourse,
            backgroundBlur = uiState.courseTableSettings.backgroundBlur,
            backgroundUri = backgroundUri.value,
            backgroundAlpha = uiState.courseTableSettings.backgroundAlpha,
            courseBlockAlpha = uiState.courseTableSettings.courseBlockAlpha,
            onToggleShowWeekendCourse = {
                viewModel.changeShowWeekendCourse(it)
            },
            onBackgroundUriChange = {
                // viewModel.setBackgroundUri(it.toString())
                backgroundUri.value = getBackground(context)
            },
            onBackgroundBluerChange = {
                viewModel.setBackgroundBlur(it)
            },
            onBackgroundAlphaChange = {
                viewModel.setBackgroundAlpha(it)
            },
            onCourseBlockAlphaChange = {
                viewModel.setCourseBlockAlpha(it)
            },
            onDismissRequest = {
                isMoreSettingsBottomSheetShow.value = false
            }
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun WeekCourseTable(
    currentDate: LocalDate = LocalDate.now(),
    isWeekendCourseShow: Boolean,
    startDatePerWeek: LocalDate?,
    selectedDataSource: Int = 0,
    weekCourseSchedule: List<List<CourseEntity>>?,
    courseBlockAlpha: Float = 1f
) {

    val timeColumnWeight = 0.1f
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val minHeight = max((screenHeight - 180) / 12, 70)

    val emojiList = listOf("📚", "🎓", "📝", "🏫", "📖", "👩‍🎓", "👨‍🎓", "📆", "🕰️", "🎒", "🏅", "🏆", "🎉")
    val randomEmoji = remember { mutableStateOf(emojiList.random()) }

    val pattern = DateTimeFormatter.ofPattern("M-d")

    val scrollState = rememberScrollState()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .weight(timeColumnWeight)
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Text(
                        text = randomEmoji.value,
                        textAlign = TextAlign.Center,
                        color = MiuixTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1 - timeColumnWeight)
                    .align(Alignment.CenterVertically)
            ) {
                listOf(
                    stringResource(R.string.monday),
                    stringResource(R.string.tuesday),
                    stringResource(R.string.wednesday),
                    stringResource(R.string.thursday),
                    stringResource(R.string.friday),
                    stringResource(R.string.saturday),
                    stringResource(R.string.sunday),
                ).take(if (isWeekendCourseShow) 7 else 5)
                    .forEachIndexed { index, week ->
                        val date = startDatePerWeek?.plusDays(index.toLong())
                        Box(modifier = Modifier.weight(1F)) {
                            val color = if (date?.isEqual(currentDate) == true)
                                MiuixTheme.colorScheme.onBackground else Color.Gray
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = week,
                                    color = color,
                                    fontSize = 14.sp,
                                )
                                if (selectedDataSource == 0) {
                                    Text(
                                        text = date?.format(pattern) ?: "",
                                        color = color,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
            }
        }
        // 课程
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .scrollEndHaptic()
                .overScrollVertical()
                .padding(top = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
            ) {
                // 第一列 时间和节次
                Column(
                    modifier = Modifier
                        .weight(timeColumnWeight),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val times = summerOrWinterTimeInterval()
                    (0..4).forEach { node ->
                        (node * 2..node * 2 + 1).forEach {
                            val color = if (checkTimeInterval(true) == it)
                                MiuixTheme.colorScheme.onBackground else Color.Gray
                            Column(
                                modifier = Modifier
                                    .requiredHeight(minHeight.dp)
                                    .padding(vertical = 2.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "${it + 1}",
                                    textAlign = TextAlign.Center,
                                    color = color,
                                    fontSize = 14.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "${times[it].first}\n${times[it].second}",
                                    color = color,
                                    lineHeight = 12.sp,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                // 每一天的课程
                Row(
                    modifier = Modifier.weight(1 - timeColumnWeight),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (weekCourseSchedule == null) {
                        Box(
                            modifier = Modifier
                                .weight(1 - timeColumnWeight)
                                .height((minHeight * 10).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (weekCourseSchedule.any { it.isNotEmpty() }) {
                            // 遍历星期一到星期日的数据
                            (0..if (isWeekendCourseShow) 6 else 4).forEach { dayIndex ->
                                Column(
                                    modifier = Modifier
                                        .weight(1F)
                                        .padding(horizontal = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    val dayClasses =
                                        weekCourseSchedule.getOrNull(dayIndex) ?: emptyList()
                                    // 用于记录每个时间段是否有课程
                                    val timeSlots = Array(10) { slot ->
                                        dayClasses.filter { course ->
                                            course.sessionList.firstOrNull() == (slot + 1)
                                        }
                                    }
                                    // 标记哪些时间段已经被占用
                                    val occupied = BooleanArray(10) { false }
                                    // 遍历所有时间段
                                    for (slot in 0 until 10) {
                                        if (occupied[slot]) continue // 如果该时间段已被占用，跳过
                                        val course = timeSlots[slot]
                                        if (course.isNotEmpty()) {
                                            val selectedOverlapCourse =
                                                remember { mutableStateOf(0) }
                                            val currentCourse =
                                                course[selectedOverlapCourse.value]
                                            // 计算这节课占用的时间段数量
                                            val slotsOccupied =
                                                currentCourse.sessionList.size
                                            // 标记已占用
                                            for (i in 0 until slotsOccupied) {
                                                if (slot + i < 10) {
                                                    occupied[slot + i] = true
                                                }
                                            }
                                            CourseTableSingleCourseCard(
                                                overlapCourseList = course,
                                                course = currentCourse,
                                                minHeight = minHeight,
                                                slotsOccupied = slotsOccupied,
                                                onSelectOverlapCourse = {
                                                    selectedOverlapCourse.value = it
                                                },
                                                isShowWeekendCourse = isWeekendCourseShow,
                                                alpha = courseBlockAlpha
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(minHeight.dp)
                                                    .padding(vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1 - timeColumnWeight)
                                    .height((minHeight * 10).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                EmptyContent(text = "暂无课表数据")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseTableSingleCourseCard(
    overlapCourseList: List<CourseEntity>? = null,
    isShowWeekendCourse: Boolean,
    onSelectOverlapCourse: (Int) -> Unit = {},
    course: CourseEntity,
    minHeight: Int,
    slotsOccupied: Int = 1,
    alpha: Float = 1f
) {
    val isBottomSheetShow = remember { mutableStateOf(false) }
    val textColor = when {
        isInDarkTheme() && alpha > 0.4f -> Color.Black
        else -> MiuixTheme.colorScheme.onSurface
    }

    Surface(
        onClick = {
            isBottomSheetShow.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height((minHeight * slotsOccupied).dp)
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(6.dp),
        color = getColorByCourseName(course.courseName).copy(alpha)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp, horizontal = 3.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            overlapCourseList?.size?.let {
                Text(
                    text = if (it > 2) "[冲突]" else "" + course.courseName,
                    fontSize = if (isShowWeekendCourse) 12.sp else 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = "@${if (course.classroomName.isNullOrBlank()) course.projectName else course.classroomName}",
                fontSize = if (isShowWeekendCourse) 11.sp else 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = course.teacherName ?: "",
                fontSize = if (isShowWeekendCourse) 11.sp else 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor.copy(alpha = 0.6f),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    CourseDetailBottomSheet(
        course = course,
        isBottomSheetShow = isBottomSheetShow.value,
        overlapCourseList = overlapCourseList,
        onSelectOverlapCourse = onSelectOverlapCourse,
        onDismissRequest = {
            isBottomSheetShow.value = false
        }
    )
}

@Composable
fun CourseTableMoreSettingBottomSheet(
    showBottomSheet: Boolean,
    isShowWeekendCourse: Boolean,
    onToggleShowWeekendCourse: (Boolean) -> Unit,
    backgroundUri: Uri?,
    onBackgroundUriChange: (Uri?) -> Unit,
    backgroundBlur: Float,
    onBackgroundBluerChange: (Float) -> Unit,
    backgroundAlpha: Float,
    onBackgroundAlphaChange: (Float) -> Unit,
    courseBlockAlpha: Float,
    onCourseBlockAlphaChange: (Float) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val backgroundBlur = remember { mutableFloatStateOf(backgroundBlur) }
    val backgroundAlpha = remember { mutableFloatStateOf(backgroundAlpha) }
    val courseBlockAlpha = remember { mutableFloatStateOf(courseBlockAlpha) }

    val pickBackgroundImgLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) {
            showToast(context, "未选择图片")
            return@rememberLauncherForActivityResult
        } else {
            val backgroundUri = saveBackground(context, uri)
            Log.i("TAG666", "Selected image URI: $backgroundUri")
            onBackgroundUriChange(backgroundUri)
        }
    }
    val isBackgroundEmpty = remember(backgroundUri) {
        mutableStateOf(backgroundUri.toString() == "null")
    }
    OverlayBottomSheet(
        title = "更多设置",
        show = showBottomSheet,
        onDismissRequest = {
            onDismissRequest()
        },
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicComponent(
                        title = "课表背景",
                        summary = "更换或清除课表背景",
                        onClick = {
                            scope.launch {
                                pickBackgroundImgLauncher.launch("image/*")
                                onBackgroundUriChange(backgroundUri)
                            }
                        },
                        endActions = {
                            Image(
                                painter = rememberAsyncImagePainter(backgroundUri),
                                contentDescription = "背景",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .size(40.dp),
                                contentScale = ContentScale.Crop
                            )
                            if (!isBackgroundEmpty.value)
                                IconButton(
                                    onClick = {
                                        CourseTableBackgroundUtil.clearBackground(context)
                                        onBackgroundUriChange(null)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                        }
                    )
                    if (!isBackgroundEmpty.value) {
                        SuperSlider(
                            title = "背景模糊度",
                            endText = "${((backgroundBlur.floatValue / 80f) * 100).toInt()} %",
                            value = backgroundBlur.floatValue,
                            onValueChange = {
                                backgroundBlur.floatValue = it
                            },
                            onValueChangeFinished = {
                                onBackgroundBluerChange(backgroundBlur.floatValue)
                            },
                            valueRange = 0f..80f,
                            isShowValueDialog = true,
                        )
                        SuperSlider(
                            title = "背景透明度",
                            endText = "${backgroundAlpha.floatValue}",
                            value = backgroundAlpha.floatValue,
                            onValueChange = {
                                backgroundAlpha.floatValue = it
                            },
                            onValueChangeFinished = {
                                onBackgroundAlphaChange(backgroundAlpha.floatValue)
                            },
                            valueRange = 0f..1f,
                            isShowValueDialog = true,
                        )
                        SuperSlider(
                            title = "课程块透明度",
                            endText = "${courseBlockAlpha.floatValue}",
                            value = courseBlockAlpha.floatValue,
                            onValueChange = {
                                courseBlockAlpha.floatValue = it
                            },
                            onValueChangeFinished = {
                                onCourseBlockAlphaChange(courseBlockAlpha.floatValue)
                            },
                            valueRange = 0f..1f,
                            isShowValueDialog = true,
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SwitchPreference(
                        title = "是否显示周末课程",
                        summary = "开启后，周六、周日的课程将会显示在课表中",
                        checked = isShowWeekendCourse,
                        onCheckedChange = {
                            onToggleShowWeekendCourse(it)
                        }
                    )
                }
                Spacer(
                    Modifier.padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                                + WindowInsets.captionBar.asPaddingValues().calculateBottomPadding()
                    )
                )
            }
        }
    }
}

@Composable
fun SharedInfoDialog(
    showDialog: Boolean,
    shareCode: String,
    onCopyShareCode: (String) -> Unit,
    onShareToOtherApp: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    OverlayDialog(
        show = showDialog,
        onDismissRequest = {
            onDismissRequest()
        },
        title = "课表分享码",
    ) {
        Column {
            Text(text = "已创建共享课表，30 分钟内有效，可通过课表页面右上角粘贴分享码导入已共享的课表，分享码为：$shareCode")
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                TextButton(
                    text = "分享",
                    onClick = {
                        onDismissRequest()
                        onShareToOtherApp(shareCode)
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "复制",
                    onClick = {
                        onDismissRequest()
                        onCopyShareCode(shareCode)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

@Composable
fun SharedCourseScheduleDialog(
    selectedTermCode: String,
    termList: List<SingleTerm>,
    selectedDataSource: Int,
    onSelectDataSource: (Int) -> Unit,
    onSelectTermCode: (String) -> Unit,
    showDialog: Boolean,
    onImportCourseSchedule: (String) -> Unit,
    isImporting: Boolean,
    sharedIdList: List<String>,
    onChooseTable: (String) -> Unit,
    selectTableId: String = "",
    onDeleteTable: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var shareCode by remember { mutableStateOf("") }
    OverlayDialog(
        show = showDialog,
        onDismissRequest = {
            onDismissRequest()
        },
        title = "管理多课表",
        insideMargin = DpSize(0.dp, 24.dp)
    ) {
        Column {
            OverlayDropdownPreference(
                title = "切换数据源",
                items = listOf("智慧教务", "教务系统", "共享课表"),
                selectedIndex = selectedDataSource,
                onSelectedIndexChange = {
                    onSelectDataSource(it)
                },
                insideMargin = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            )
            AnimatedVisibility(
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                visible = selectedDataSource == 1
            ) {
                OverlayDropdownPreference(
                    title = "选择学期",
                    items = termList.map { it.termString },
                    selectedIndex = termList.indexOfFirst { it.termCode == selectedTermCode }
                        .coerceAtLeast(0),
                    onSelectedIndexChange = {
                        onSelectTermCode(termList[it].termCode)
                    },
                    insideMargin = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                )
            }
            AnimatedVisibility(
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                visible = selectedDataSource == 2
            ) {
                Column {
                    sharedIdList.forEach { id ->
                        CheckboxPreference(
                            title = id,
                            checked = selectTableId == id,
                            onCheckedChange = {
                                onChooseTable(id)
                            },
                            endActions = {
                                if (id != PERSONAL_TABLE) {
                                    IconButton(
                                        onClick = {
                                            onDeleteTable(id)
                                            onChooseTable(PERSONAL_TABLE)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = MiuixIcons.Delete,
                                            contentDescription = "delete"
                                        )
                                    }
                                }
                            },
                            insideMargin = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        TextField(
                            label = "请输入分享码",
                            value = shareCode,
                            onValueChange = {
                                shareCode = it
                            },
                            singleLine = true,
                            useLabelAsPlaceholder = true,
                            modifier = Modifier.weight(0.7f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        TextButton(
                            text = if (isImporting) "导入中" else "导入",
                            onClick = {
                                if (shareCode.isBlank()) {
                                    showToast(context, "请输入分享码")
                                } else {
                                    onImportCourseSchedule(shareCode)
                                }
                            },
                            enabled = !isImporting,
                            colors = ButtonDefaults.textButtonColorsPrimary(),
                            modifier = Modifier.weight(0.3f)
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "确定",
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors()
                )
            }

        }
    }
}