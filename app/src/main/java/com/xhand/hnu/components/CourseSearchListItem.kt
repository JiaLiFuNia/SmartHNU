package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.xhand.hnu.model.entity.CourseSearchKBList


@Composable
fun CourseSearchListItem(
    course: CourseSearchKBList
) {
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
                        course.jcdm.last()
                    }节"
                )
            }
        },
        colors = ListItemDefaults.colors(Color.Transparent)
    )
}