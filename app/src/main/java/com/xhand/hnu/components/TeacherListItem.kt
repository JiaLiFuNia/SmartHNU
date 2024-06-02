package com.xhand.hnu.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xhand.hnu.model.entity.AllPjxxList
import com.xhand.hnu.model.entity.KccjList

@Composable
fun TeacherListItem(teacherItem: AllPjxxList, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = teacherItem.kcmc,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Text(
                text = "${teacherItem.jxhjmc} ${teacherItem.teaxm}"
            )
        },
        trailingContent = {
            Text(
                text = teacherItem.xnxqmc
            )
        },
        modifier = modifier
    )
}