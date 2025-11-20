package com.smart.htu.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalTime


@Composable
fun SingleTaskCard(
    taskName: String,
    taskDescription: String,
    taskColor: Color,
    startTime: LocalTime,
    endTime: LocalTime,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isInProgress = LocalTime.now().isAfter(startTime) && LocalTime.now().isBefore(endTime)
    val isPassed = LocalTime.now().isAfter(endTime)
    Surface(
        modifier = modifier,
        onClick = {
            onClick()
        },
        color = Color.Transparent
    ) {
        val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
        else MiuixTheme.colorScheme.primary
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(taskColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = taskName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (isPassed) MiuixTheme.colorScheme.onSurfaceVariantSummary else MiuixTheme.colorScheme.onSurface
                    ),
                    textDecoration = if (isPassed) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier
                )
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
                                            time = endTime,
                                            pattern = "HH:mm"
                                        )
                                    }"

                                    else -> "${
                                        convertLocalTimeToStringTime(
                                            time = startTime,
                                            pattern = "HH:mm"
                                        )
                                    }-${
                                        convertLocalTimeToStringTime(
                                            time = endTime,
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
                            text = taskDescription.ifEmpty { "暂无" },
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
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCourseCard(modifier: Modifier, course: CourseEntity) {
    val isBottomSheetShow = remember { mutableStateOf(false) }
    SingleTaskCard(
        taskName = course.courseName,
        taskDescription = course.classroomName ?: "暂无教室",
        taskColor = getColorByCourseName(course.courseName),
        startTime = course.startTime,
        endTime = course.endTime,
        modifier = modifier,
    ) {
        isBottomSheetShow.value = true
    }
    CourseDetailDialog(course, isBottomSheetShow)
}

@Composable
fun CourseDetailDialog(
    message: CourseEntity,
    isBottomSheetShow: MutableState<Boolean>,
    overlapCourseList: List<CourseEntity>? = null,
    onSelectOverlapCourse: (Int) -> Unit = {}
) {
    SuperBottomSheet(
        show = isBottomSheetShow,
        onDismissRequest = {
            isBottomSheetShow.value = false
        },
        title = message.courseName + if (message.classroomName.isNullOrEmpty()) {
            " - ${message.projectName}"
        } else {
            ""
        } + " ${message.startTime} - ${message.endTime}",
        insideMargin = DpSize(16.dp, 24.dp)
    ) {
        MessageCardDisplay(
            modifier = Modifier.fillMaxWidth(),
            message = listOf(
                SingleInfo(
                    label = "教师",
                    content = message.teacherName ?: "暂无",
                    leadingIcon = R.drawable.ic_outline_person
                ),
                SingleInfo(
                    label = "教室",
                    content = message.classroomName ?: "暂无",
                    leadingIcon = R.drawable.apartment_24px
                ),
                SingleInfo(
                    label = "课程类型",
                    content = "${message.assessmentMethod} / ${message.teachingEnvironment}",
                    leadingIcon = R.drawable.category_24px
                ),
                SingleInfo(
                    label = "节次",
                    content = message.classTimeCodeDetailed,
                    leadingIcon = R.drawable.schedule_24px
                ),
                SingleInfo(
                    label = "人数",
                    content = message.totalStudents.toString(),
                    leadingIcon = R.drawable.groups_24px
                ),
                SingleInfo(
                    label = "上课班级",
                    content = message.className,
                    leadingIcon = R.drawable.co_present_24px
                )
            )
        )
        overlapCourseList?.size?.let {
            if (it > 2) {
                overlapCourseList.forEach {
                    Text(it.courseName)
                }
            }
        }
    }
}