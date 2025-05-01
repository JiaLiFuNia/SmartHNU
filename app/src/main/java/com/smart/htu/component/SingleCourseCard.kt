package com.smart.htu.component

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.api.module.Course
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCourseCard(modifier: Modifier, onClick: () -> Unit, message: Course) {
    val isBottomSheetShow = remember { mutableStateOf(false) }
    top.yukonga.miuix.kmp.basic.Surface(
        modifier = modifier,
        onClick = {
            onClick()
            isBottomSheetShow.value = true
        },
        color = Color.Transparent,
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius)
    ) {
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
                    .background(MiuixTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message.courseName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    softWrap = false,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MiuixTheme.colorScheme.onBackground
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.schedule_24px),
                            contentDescription = "time",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = MiuixTheme.colorScheme.primary
                        )
                        Text(
                            text = "${message.startTime}-${message.endTime}".ifEmpty { "暂无" },
                            modifier = Modifier.weight(4 / 10f),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.7f
                                )
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location_on_24px),
                            contentDescription = "time",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = MiuixTheme.colorScheme.primary
                        )
                        Text(
                            text = message.classroomName.ifEmpty { "暂无" },
                            modifier = Modifier.weight(4 / 10f),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.7f
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    BasicBottomSheet(
        showDialog = isBottomSheetShow,
        title = "${message.courseName} ${message.projectName}",
        summary = "上课时间：${message.startTime} - ${message.endTime}",
        insideMargin = DpSize(16.dp, 24.dp)
    ) {
        MessageCardDisplay(
            modifier = Modifier.fillMaxWidth(),
            message = listOf(
                SingleInfo(
                    label = "教师",
                    content = message.teacherNames.ifEmpty { "暂无" },
                    leadingIcon = R.drawable.ic_outline_person
                ),
                SingleInfo(
                    label = "教室",
                    content = message.classroomName.ifEmpty { "暂无" },
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
                )
            )
        )
    }
}