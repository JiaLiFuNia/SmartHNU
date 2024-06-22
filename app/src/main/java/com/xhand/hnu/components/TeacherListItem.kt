package com.xhand.hnu.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.xhand.hnu.R
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
            Icon(
                painterResource(id = if (teacherItem.wjkkp) R.drawable.baseline_check_circle_outline_24 else R.drawable.outline_circle_24),
                contentDescription = "完成"
            )
        },
        modifier = modifier
    )
}