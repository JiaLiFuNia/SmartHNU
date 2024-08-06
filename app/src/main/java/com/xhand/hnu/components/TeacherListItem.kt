package com.xhand.hnu.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.xhand.hnu.model.entity.AllPjxxList

@Composable
fun TeacherListItem(teacherItem: AllPjxxList, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = teacherItem.kcmc,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = "${teacherItem.jxhjmc} ${teacherItem.teaxm}"
            )
        },
        trailingContent = {
            Text(
                text = "已评教"
            )
        },
        modifier = modifier
    )
}