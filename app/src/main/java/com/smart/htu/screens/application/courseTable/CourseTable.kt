package com.smart.htu.screens.application.courseTable

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.smart.htu.MainActivity
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.screens.main.CourseDetailDialog
import com.smart.htu.utils.CourseColorUtil.getColorByCourseName
import com.smart.htu.utils.CourseTableBackgroundUtil
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import com.smart.htu.utils.CourseTimeRange.summerOrWinterTimeInterval
import com.smart.htu.utils.Permission
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ListPopup
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.DropdownImpl
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.lang.Integer.max
import java.time.format.DateTimeFormatter

/**
 * Copyright (C) 2025
 *
 * @link https://github.com/jayfunc/GongYun-for-Android/blob/daaa651b821558319a46d672a90b3325fe9a2520/app/src/main/java/com/dart/campushelper/ui/schedule/ScheduleTable.kt
 * @author jayfunc
 * @modifier JiaLiFuNia
 */

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseTable(
    navController: NavController,
    viewModel: CourseTableViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val nodeColumnWeight = 0.65F
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val minHeight = max((screenHeight - 180) / 12, 70)
    val showDropDownMenu = remember { mutableStateOf(false) }

    var backgroundUri by remember { mutableStateOf(CourseTableBackgroundUtil.getBackground(context)) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            backgroundUri = CourseTableBackgroundUtil.saveBackground(context, it)
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (backgroundUri != null) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (backgroundUri != null) Color.Transparent else MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(text = "${uiState.termCode} 学期 第 ${uiState.week} 周")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    ListPopup(
                        show = showDropDownMenu,
                        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                        alignment = PopupPositionProvider.Align.TopRight,
                        onDismissRequest = {
                            showDropDownMenu.value = false
                        }
                    ) {
                        val optionSize = if (backgroundUri != null) 6 else 5
                        ListPopupColumn {
                            DropdownImpl(
                                text = "切换到上一周",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    scope.launch {
                                        viewModel.getCurrentWeekCourseSchedule(uiState.week - 1)
                                    }
                                },
                                index = 0
                            )
                            DropdownImpl(
                                text = "切换到下一周",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    scope.launch {
                                        viewModel.getCurrentWeekCourseSchedule(uiState.week + 1)
                                    }
                                },
                                index = 1
                            )
                            DropdownImpl(
                                text = "同步到日历",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    if (Permission.hasCalendarPermissions(context)) {
                                        viewModel.showSnackBar("开发中...")
                                    } else {
                                        if (context is MainActivity) {
                                            context.requestCalendarPermissions()
                                        }
                                    }
                                },
                                index = 2
                            )
                            DropdownImpl(
                                text = "导出为ICS日历文件",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    viewModel.exportToICS()
                                },
                                index = 3
                            )
                            DropdownImpl(
                                text = "修改课表背景",
                                isSelected = false,
                                optionSize = optionSize,
                                onSelectedIndexChange = {
                                    showDropDownMenu.value = false
                                    launcher.launch("image/*")
                                },
                                index = 4
                            )
                            if (backgroundUri != null)
                                DropdownImpl(
                                    text = "清除背景",
                                    isSelected = false,
                                    optionSize = optionSize,
                                    onSelectedIndexChange = {
                                        showDropDownMenu.value = false
                                        CourseTableBackgroundUtil.clearBackground(context)
                                        backgroundUri = null
                                    },
                                    index = 5
                                )
                        }
                    }
                    IconButton(
                        onClick = {
                            showDropDownMenu.value = true
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "more")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val hazeState = rememberHazeState()
            if (backgroundUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(backgroundUri),
                    contentDescription = "背景",
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(state = hazeState)
                        .hazeEffect(HazeMaterials.ultraThin()) {
                            backgroundColor = Color.Transparent
                            this.blurEnabled = blurEnabled
                            this.drawContentBehind = drawContentBehind
                            this.blurRadius = 20.dp
                        },
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .padding(horizontal = 4.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                // 年份 星期 日期
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(
                        modifier = Modifier
                            .weight(nodeColumnWeight)
                    ) {
                        Text(
                            text = "25\n年",
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center,
                            color = MiuixTheme.colorScheme.onBackground,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(7F)
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
                        ).forEachIndexed { index, week ->
                            Box(modifier = Modifier.weight(1F)) {
                                val color = if (uiState.todayWeekday == index + 1)
                                    MiuixTheme.colorScheme.onBackground else Color.Gray
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Text(
                                        text = week,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = color
                                        )
                                    )
                                    Text(
                                        text = uiState.startDatePerWeek?.plusDays(index.toLong())
                                            ?.format(DateTimeFormatter.ofPattern("M-d")) ?: "",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = color
                                        )
                                    )
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
                        .padding(top = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        // 第一列 时间和节次
                        Column(
                            modifier = Modifier
                                .weight(nodeColumnWeight),
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
                                            .padding(vertical = 2.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${it + 1}",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = color
                                            )
                                        )
                                        Text(
                                            text = "${times[it].first}\n${times[it].second}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = color
                                            ),
                                            lineHeight = 12.sp,
                                        )
                                    }
                                }
                            }
                        }
                        // 每一天的课程
                        Row(
                            modifier = Modifier.weight(7F),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (uiState.currentWeekCourseTable == null) {
                                Box(
                                    modifier = Modifier
                                        .weight(7F)
                                        .height((minHeight * 10).dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                if (uiState.currentWeekCourseTable?.any { it.isNotEmpty() } == true) {
                                    // 遍历星期一到星期日的数据
                                    (0..6).forEach { dayIndex ->
                                        Column(
                                            modifier = Modifier
                                                .weight(1F)
                                                .padding(horizontal = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Top
                                        ) {
                                            val dayClasses =
                                                uiState.currentWeekCourseTable?.getOrNull(dayIndex)
                                                    ?: emptyList()
                                            // 用于记录每个时间段是否有课程
                                            val timeSlots = Array(10) { slot ->
                                                dayClasses.filter { course ->
                                                    course.sectionList.firstOrNull() == (slot + 1)
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
                                                        currentCourse.sectionList.size
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
                                                        modifier = Modifier
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
                                            .weight(7F)
                                            .height((minHeight * 10).dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        EmptyContent("暂无课表数据")
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

@Composable
fun CourseTableSingleCourseCard(
    overlapCourseList: List<CourseEntity>? = null,
    onSelectOverlapCourse: (Int) -> Unit = {},
    course: CourseEntity,
    minHeight: Int,
    slotsOccupied: Int = 1,
    modifier: Modifier = Modifier
) {
    val isBottomSheetShow = remember { mutableStateOf(false) }

    Surface(
        onClick = {
            isBottomSheetShow.value = true
        },
        modifier = modifier
            .fillMaxWidth()
            .height((minHeight * slotsOccupied).dp)
            .padding(vertical = 2.dp),
        shape = MaterialTheme.shapes.small,
        color = getColorByCourseName(course.courseName).copy(0.7f)
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
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = "@${if (course.classroomName.isNullOrBlank()) course.projectName else course.classroomName}",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = course.teacherName ?: "",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                color = Color.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    CourseDetailDialog(
        message = course,
        isBottomSheetShow = isBottomSheetShow,
        overlapCourseList = overlapCourseList,
        onSelectOverlapCourse = onSelectOverlapCourse
    )
}