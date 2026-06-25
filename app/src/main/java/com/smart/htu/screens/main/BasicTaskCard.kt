package com.smart.htu.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CoPresent
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.utils.CourseColorUtil.getColorByCourseName
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds

enum class TaskState(val state: Int) {
    NOT_START(0),
    IN_PROGRESS(1),
    FINISHED(2)
}

@Composable
fun BasicTaskCard(
    modifier: Modifier = Modifier,
    title: String,
    taskState: TaskState = TaskState.NOT_START,
    descriptionContent: @Composable (() -> Unit)? = null,
    summary: String? = null,
    taskColor: Color,
    holdDownState: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = {
            onClick()
        },
        color = MiuixTheme.colorScheme.surfaceContainer
    ) {
        BasicComponent(
            modifier = modifier,
            onClick = onClick,
            title = title,
            summary = summary,
            holdDownState = holdDownState,
            startAction = {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(taskColor)
                )
            },
            endActions = {
                when (taskState) {
                    TaskState.NOT_START -> {
                        descriptionContent?.invoke()
                    }

                    TaskState.IN_PROGRESS -> Text(
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

                    TaskState.FINISHED -> Text(
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
            }
        )
    }
}

@Composable
fun SingleCourseCard(
    modifier: Modifier,
    index: Int,
    course: CourseEntity,
    onSearchSameCourse: (String) -> Unit = {},
    onSearchSameRoom: (String) -> Unit
) {
    val holdDownIndex = remember { mutableIntStateOf(-1) }
    val isBottomSheetShow = remember { mutableStateOf(false) }
    val diffMinutesState = remember { mutableLongStateOf(0L) }

    // 计算每整分钟刷新一次的 diffMinutes
    LaunchedEffect(course) {
        while (true) {
            val now = LocalTime.now()
            val diff = ChronoUnit.MINUTES.between(now, course.startTime)
            diffMinutesState.longValue = diff

            // 计算距离下一个整分钟还需多少毫秒
            val delayMs = (60 - now.second) * 1000L - now.nano / 1000000L
            delay(delayMs.milliseconds)
        }
    }

    val isInProgress =
        LocalTime.now().isAfter(course.startTime) && LocalTime.now().isBefore(course.endTime)
    val isPassed = LocalTime.now().isAfter(course.endTime)
    // val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
    // else MiuixTheme.colorScheme.primary
    BasicTaskCard(
        title = course.courseName,
        taskState = when {
            isPassed -> TaskState.FINISHED
            isInProgress -> TaskState.IN_PROGRESS
            else -> TaskState.NOT_START
        },
        summary = buildString {
            append(
                "${
                    convertLocalTimeToStringTime(
                        time = course.startTime,
                        pattern = "HH:mm"
                    )
                }-${
                    convertLocalTimeToStringTime(
                        time = course.endTime,
                        pattern = "HH:mm"
                    )
                }"
            )
            append(" | ")
            append(course.classroomName?.ifEmpty { "无教室" } ?: "无教室")
        },
        taskColor = getColorByCourseName(course.courseName),
        descriptionContent = {
            val diffMinutes = diffMinutesState.longValue
            val diffHours = diffMinutes / 60
            when {
                diffHours >= 1 -> {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MiuixTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = MiuixTheme.textStyles.title2.fontSize
                                )
                            ) {
                                append(diffHours.toString())
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = MiuixTheme.colorScheme.onBackground,
                                    fontSize = 14.sp
                                )
                            ) {
                                append(" 小时")
                            }
                        }
                    )
                }

                diffMinutes >= 2 -> {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MiuixTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = MiuixTheme.textStyles.title2.fontSize
                                )
                            ) {
                                append(diffMinutes.toString())
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = MiuixTheme.colorScheme.onBackground,
                                    fontSize = 14.sp
                                )
                            ) {
                                append(" 分钟")
                            }
                        }
                    )
                }

                diffMinutes < 2 -> {
                    Text(
                        text = "即将开始",
                        color = MiuixTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium,
                        fontSize = MiuixTheme.textStyles.title2.fontSize
                    )
                }
            }
        },
        modifier = modifier,
        holdDownState = index == holdDownIndex.intValue && isBottomSheetShow.value,
    ) {
        holdDownIndex.intValue = index
        isBottomSheetShow.value = true
    }
    CourseDetailBottomSheet(
        course = course,
        isBottomSheetShow = isBottomSheetShow.value,
        onSearchSameCourse = {
            isBottomSheetShow.value = false
            onSearchSameCourse(it)
        },
        onSearchSameRoom = {
            isBottomSheetShow.value = false
            onSearchSameRoom(it)
        },
        onDismissRequest = {
            isBottomSheetShow.value = false
        }
    )
}

@Composable
fun CourseDetailBottomSheet(
    course: CourseEntity,
    isBottomSheetShow: Boolean,
    onSearchSameCourse: (String) -> Unit = { },
    onSearchSameRoom: (String) -> Unit = { },
    overlapCourseList: List<CourseEntity>? = null,
    onSelectOverlapCourse: (Int) -> Unit = {},
    onDismissRequest: () -> Unit
) {
    OverlayBottomSheet(
        show = isBottomSheetShow,
        onDismissRequest = {
            onDismissRequest()
        },
        title = course.courseName + if (course.classroomName.isNullOrEmpty()) {
            " - ${course.projectName}"
        } else {
            "\n"
        } + " ${course.startTime} - ${course.endTime}",
        insideMargin = DpSize(16.dp, 24.dp),
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        MessageCardDisplay(
            modifier = Modifier.fillMaxWidth(),
            message = listOf(
                SingleInfo(
                    label = "教师",
                    content = course.teacherName ?: "暂无",
                    startIcon = Icons.Outlined.Person,
                    rowIndex = 1
                ),
                SingleInfo(
                    label = "教室",
                    content = course.classroomName ?: "暂无",
                    startIcon = Icons.Outlined.Apartment,
                    rowIndex = 1
                ),
                SingleInfo(
                    label = "课程类型",
                    content = "${course.assessmentMethod} / ${course.teachingEnvironment}",
                    startIcon = Icons.Outlined.Category,
                    rowIndex = 2
                ),
                SingleInfo(
                    label = "节次",
                    content = course.classTimeCodeDetailed.ifEmpty { "暂无" },
                    startIcon = Icons.Outlined.Schedule,
                    rowIndex = 2
                ),
                SingleInfo(
                    label = "人数",
                    content = course.totalStudents.toString(),
                    startIcon = Icons.Outlined.Groups,
                    rowIndex = 3
                ),
                SingleInfo(
                    label = "上课班级",
                    content = course.className,
                    startIcon = Icons.Outlined.CoPresent,
                    rowIndex = 3
                )
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card {
            ArrowPreference(
                title = "搜索同课程",
                onClick = {
                    onSearchSameCourse(course.courseName)
                }
            )
            ArrowPreference(
                title = "搜索该教室",
                onClick = {
                    onSearchSameRoom(course.classroomName ?: "无")
                }
            )
        }
        /*overlapCourseList?.size?.let {
            if (it > 2) {
                overlapCourseList.forEach {
                    Text(it.courseName)
                }
            }
        }*/
    }
}