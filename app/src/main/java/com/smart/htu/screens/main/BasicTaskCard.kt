package com.smart.htu.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CoPresent
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.utils.CourseColorUtil.getColorByCourseName
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalTime


@Composable
fun BasicTaskCard(
    title: String,
    description: String? = null,
    descriptionContent: @Composable (() -> Unit)? = null,
    taskColor: Color,
    modifier: Modifier = Modifier,
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
            title = title,
            summary = description,
            startAction = {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(taskColor)
                )
            },
            bottomAction = descriptionContent

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCourseCard(
    modifier: Modifier,
    course: CourseEntity,
    onSearchCourse: (String) -> Unit = {}
) {
    val isBottomSheetShow = remember { mutableStateOf(false) }
    val isInProgress =
        LocalTime.now().isAfter(course.startTime) && LocalTime.now().isBefore(course.endTime)
    val isPassed = LocalTime.now().isAfter(course.endTime)
    val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
    else MiuixTheme.colorScheme.primary
    BasicTaskCard(
        title = course.courseName,
        descriptionContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.schedule_24px),
                        contentDescription = "time",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 4.dp),
                        tint = iconColor
                    )
                    Text(
                        text =
                            when {
                                isInProgress -> "进行中-${
                                    convertLocalTimeToStringTime(
                                        time = course.endTime,
                                        pattern = "HH:mm"
                                    )
                                }"

                                else -> "${
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
                            },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = when {
                                isInProgress -> MiuixTheme.colorScheme.onSurface
                                isPassed -> MiuixTheme.colorScheme.onSurfaceVariantSummary
                                else -> MiuixTheme.colorScheme.onSurface
                            }
                        ),
                        textDecoration = if (isPassed) TextDecoration.LineThrough else TextDecoration.None,
                        textAlign = TextAlign.Start
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
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
                        text = course.classroomName ?: "暂无教室",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = when {
                                isInProgress -> MiuixTheme.colorScheme.onSurface
                                isPassed -> MiuixTheme.colorScheme.onSurfaceVariantSummary
                                else -> MiuixTheme.colorScheme.onSurface
                            }
                        ),
                        textDecoration = if (isPassed) TextDecoration.LineThrough else TextDecoration.None,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        taskColor = getColorByCourseName(course.courseName),
        modifier = modifier,
    ) {
        isBottomSheetShow.value = true
    }
    CourseDetailBottomSheet(
        course = course,
        isBottomSheetShow = isBottomSheetShow,
        onSearchCourse = {
            isBottomSheetShow.value = false
            onSearchCourse(it)
        }
    )
}

@Composable
fun CourseDetailBottomSheet(
    course: CourseEntity,
    isBottomSheetShow: MutableState<Boolean>,
    onSearchCourse: (String) -> Unit = { },
    overlapCourseList: List<CourseEntity>? = null,
    onSelectOverlapCourse: (Int) -> Unit = {}
) {
    SuperBottomSheet(
        show = isBottomSheetShow,
        onDismissRequest = {
            isBottomSheetShow.value = false
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
                    leadingIcon = Icons.Outlined.Person,
                    rowIndex = 1
                ),
                SingleInfo(
                    label = "教室",
                    content = course.classroomName ?: "暂无",
                    leadingIcon = Icons.Outlined.Apartment,
                    rowIndex = 1
                ),
                SingleInfo(
                    label = "课程类型",
                    content = "${course.assessmentMethod} / ${course.teachingEnvironment}",
                    leadingIcon = Icons.Outlined.Category,
                    rowIndex = 2
                ),
                SingleInfo(
                    label = "节次",
                    content = course.classTimeCodeDetailed ?: "暂无",
                    leadingIcon = Icons.Outlined.Schedule,
                    rowIndex = 2
                ),
                SingleInfo(
                    label = "人数",
                    content = course.totalStudents.toString(),
                    leadingIcon = Icons.Outlined.Groups,
                    rowIndex = 3
                ),
                SingleInfo(
                    label = "上课班级",
                    content = course.className,
                    leadingIcon = Icons.Outlined.CoPresent,
                    rowIndex = 3
                )
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card {
            SuperArrow(
                title = "搜索同课程",
                onClick = {
                    onSearchCourse(course.courseName)
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