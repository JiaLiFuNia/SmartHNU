package com.xhand.hnu2.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.xhand.hnu2.model.entity.KccjList

@Composable
fun GradeListItem(
    grade: KccjList,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = grade.kcmc,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Text(
                text = "成绩  ${grade.zcjfs}"
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

@Preview
@Composable
fun pp() {
    GradeListItem(
        grade = KccjList(
            xsxm = "许博涵",
            zcjfs = 100.0,
            xnxqmc = "2023-2024-1",
            kcdlmc = "专业教育",
            cjjd = 5.0,
            kcrwdm = "10000000",
            kcmc = "数据结构",
            cjdm = "1000000",
            xf = 3,
            xnxqdm = "202301",
            xdfsmc = "类型",
            cjfsmc = "cj"
        ), modifier = Modifier.clickable { }
    )
}