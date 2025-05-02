package com.smart.htu.screens.application.courseTable

import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.Course
import com.smart.htu.component.CourseDetailDialog
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import com.smart.htu.utils.CourseTimeRange.summerOrWinterTimeInterval
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.lang.Integer.max
import java.time.format.DateTimeFormatter

/*
*Author: jayfunc
*Project: GongYun-for-Android
*GitHub: https://github.com/jayfunc/GongYun-for-Android/blob/daaa651b821558319a46d672a90b3325fe9a2520/app/src/main/java/com/dart/campushelper/ui/schedule/ScheduleTable.kt
*/

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseTable(
    navController: NavController,
    courseTableViewModel: CourseTableViewModel = hiltViewModel()
) {
    val uiState by courseTableViewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val nodeColumnWeight = 0.65F
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val minHeight = max((screenHeight - 180) / 12, 70)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
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
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 4.dp)
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
                        Box(
                            modifier = Modifier
                                .weight(1F)
                        ) {
                            val color =
                                if (uiState.todayWeekday == index + 1) MiuixTheme.colorScheme.onBackground
                                else Color.Gray
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
                                    text = uiState.startDateCurrentWeek?.plusDays(index.toLong())
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
                    .verticalScroll(rememberScrollState())
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
                        if (uiState.courseTable.data != null) {
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
                                        uiState.courseTable.data?.getOrNull(dayIndex) ?: emptyList()
                                    // 用于记录每个时间段是否有课程
                                    val timeSlots = Array(10) { slot ->
                                        dayClasses.find { course ->
                                            course.sectionList.firstOrNull() == (slot + 1)
                                        }
                                    }
                                    // 标记哪些时间段已经被占用
                                    val occupied = BooleanArray(10) { false }
                                    // 遍历所有时间段
                                    for (slot in 0 until 10) {
                                        if (occupied[slot]) continue // 如果该时间段已被占用，跳过
                                        val course = timeSlots[slot]
                                        if (course != null) {
                                            // 计算这节课占用的时间段数量
                                            val slotsOccupied = course.sectionList.size
                                            // 标记已占用
                                            for (i in 0 until slotsOccupied) {
                                                if (slot + i < 10) {
                                                    occupied[slot + i] = true
                                                }
                                            }
                                            CourseTableSingleCourseCard(
                                                course = course,
                                                minHeight = minHeight,
                                                slotsOccupied = slotsOccupied
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
                                Text(
                                    text = "暂无课表数据",
                                    style = MaterialTheme.typography.bodyLarge
                                )
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
    course: Course,
    minHeight: Int,
    slotsOccupied: Int = 1
) {
    val isBottomSheetShow = remember { mutableStateOf(false) }
    Surface(
        onClick = {
            isBottomSheetShow.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height((minHeight * slotsOccupied).dp)
            .padding(vertical = 2.dp),
        shape = MaterialTheme.shapes.small,
        color = MiuixTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 2.dp, horizontal = 3.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = course.courseName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MiuixTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "@${if (course.classroomName.isNullOrBlank()) course.projectName else course.classroomName}",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                color = MiuixTheme.colorScheme.onSurface.copy(0.8f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = course.teacherName,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                textAlign = TextAlign.Start,
                color = MiuixTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    CourseDetailDialog(course, isBottomSheetShow)
}