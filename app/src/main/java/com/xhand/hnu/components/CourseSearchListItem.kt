package com.xhand.hnu.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import com.xhand.hnu.model.entity.CourseSearchKBList


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseSearchListItem(
    course: CourseSearchKBList
) {
    val cbManager = LocalClipboardManager.current
    ListItem(
        headlineContent = {
            Text(
                text = course.kcmc
            )
        },
        supportingContent = {
            Text(
                text = course.teaxms
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = course.jxcdmc
                )
                Text(
                    text = "第${course.jcdm.substring(0, 2).toInt()}-${
                        course.jcdm.takeLast(2).toInt()
                    }节"
                )
            }
        },
        colors = ListItemDefaults.colors(Color.Transparent),
        modifier = Modifier.combinedClickable(
            onLongClick = {
                copyText(cbManager, course.kcmc)
            }
        ) { }
    )
}