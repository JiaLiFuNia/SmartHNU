package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu.model.entity.Skrwlist

@Composable
fun TaskCourseListItem(task: Skrwlist) {
    ListItem(
        headlineContent = {
            Text(text = task.kcmc, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = {
            Text(text = "${task.skjsxm} ")
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${task.xdfsmc}-${task.kcdlmc}")
                Text(text = "${task.zxs} | ${task.xf}学分")
            }
        }
    )
}