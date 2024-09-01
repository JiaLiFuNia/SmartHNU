package com.xhand.hnu.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xhand.hnu.model.entity.KccjList

@Composable
fun GradeListItem(
    grade: KccjList,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = grade.kcmc,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = "成绩  ${grade.zcjfs} | ${grade.cjjd}"
            )
        },
        trailingContent = {
            Text(
                text = grade.xnxqmc
            )
        },
        modifier = modifier
    )
}