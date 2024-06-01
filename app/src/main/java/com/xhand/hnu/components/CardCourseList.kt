package com.xhand.hnu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xhand.hnu.model.entity.KbList
import com.xhand.hnu.screens.isCurrentTimeBetween

@Composable
fun CardCourseList(schedule: KbList) {
    Card(
        onClick = { },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .width(6.dp)
                    .height(35.dp),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.primary
                )
            ) { Text(text = "") }
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                val isAfterCurrentTime = isCurrentTimeBetween(
                    schedule.jssj.substring(
                        startIndex = 0,
                        endIndex = 5
                    )
                )// 课程结束时间大于当前时间为true
                Text(
                    text = "${schedule.khfsmc}-${schedule.kcmc}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isAfterCurrentTime <= 0) Color.Gray else Color.Unspecified
                )
                Row {
                    Text(
                        text = "${
                            schedule.qssj.substring(
                                startIndex = 0,
                                endIndex = 5
                            )
                        } - ${schedule.jssj.substring(0, 5)}",
                        modifier = Modifier.weight(0.4f),
                        textAlign = TextAlign.Left,
                        color = if (isAfterCurrentTime <= 0) Color.Gray else Color.Unspecified
                    )
                    Text(
                        text = schedule.teaxms,
                        modifier = Modifier.weight(0.25f),
                        textAlign = TextAlign.Left,
                        color = if (isAfterCurrentTime <= 0) Color.Gray else Color.Unspecified
                    )
                    Text(
                        text = schedule.jxcdmc,
                        modifier = Modifier.weight(0.35f),
                        textAlign = TextAlign.Left,
                        color = if (isAfterCurrentTime <= 0) Color.Gray else Color.Unspecified
                    )
                }
            }
        }
    }
}