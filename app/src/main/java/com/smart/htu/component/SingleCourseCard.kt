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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smart.htu.api.module.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCourseCard(modifier: Modifier, onClick: () -> Unit, message: Course) {
    val (isBottomSheetShow, onBottomSheetStateChange) = remember { mutableStateOf(false) }
    top.yukonga.miuix.kmp.basic.Surface(
        modifier = modifier,
        onClick = {
            onClick()
            onBottomSheetStateChange(true)
        },
        color = Color.Transparent
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
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${message.teachingEnvironment}-${message.courseName}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    softWrap = false,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = message.teacherNames.ifEmpty { "暂无" },
                        modifier = Modifier.weight(3 / 10f),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.7f
                            )
                        )
                    )
                    Text(
                        text = "${message.startTime}-${message.endTime}".ifEmpty { "暂无" },
                        modifier = Modifier.weight(3 / 10f),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.7f
                            )
                        )
                    )
                    Text(
                        text = message.classroomName.ifEmpty { "暂无" },
                        modifier = Modifier.weight(3 / 10f),
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

    BasicBottomSheet(
        isBottomSheetShow = isBottomSheetShow,
        title = "详情",
        onDismissRequest = { onBottomSheetStateChange(false) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${message.courseName} ${message.projectName}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SingleInfo("教师", message.teacherNames)
                        SingleInfo("教室", message.classroomName)
                        SingleInfo("时间", "${message.startTime} - ${message.endTime}")
                        SingleInfo("班级", message.className)
                        SingleInfo("考试方式", message.assessmentMethod)
                        SingleInfo("教学环境", message.teachingEnvironment)
                    }
                }
            }
        }
    }
}

@Composable
fun SingleInfo(label: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = content,
            modifier = Modifier.weight(0.6f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}