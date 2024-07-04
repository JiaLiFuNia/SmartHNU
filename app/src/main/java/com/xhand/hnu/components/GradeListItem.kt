package com.xhand.hnu.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
            kcflmc = "博约经典",
            xnxqmc = "2023-2024-1",
            kcdlmc = "专业教育",
            cjjd = 5.0,
            kcrwdm = "10060608",
            kcmc = "数据结构",
            cjdm = "29664301",
            zcj = "优秀",
            xf = 3,
            xnxqdm = "202301",
            xdfsmc = "类型",
            cjfsmc = "百分制",
            order = 0
        ), modifier = Modifier.clickable { }
    )
}