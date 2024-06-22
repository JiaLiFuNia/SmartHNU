package com.xhand.hnu.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun ShowAlert(
    grade: KccjList,
    viewModel: SettingsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.gradeDetailService()
    }
    val gradeDetail = viewModel.gradeDetail
    val gradeDetail2 = viewModel.gradeDetails // 有平时成绩
    AlertDialog(
        title = { Text(text = "${grade.kcmc}  [${grade.xdfsmc}]") },
        text = {
            if (viewModel.isGettingGrade) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f)
                    ) {
                        if (grade.cjfsmc == "五级制") {
                            Text(text = "成绩 ${grade.zcjfs} (${grade.zcj})")
                        } else
                            Text(text = "成绩 ${grade.zcjfs}")
                        if (gradeDetail2 != null) {
                            Text(
                                text = "*${gradeDetail2.cj4} ✕ ${gradeDetail2.bl4}% + ${gradeDetail2.cj1} ✕ ${gradeDetail2.bl1}%",
                                fontSize = 8.sp
                            )
                        }
                        Text(text = "绩点 ${grade.cjjd}")
                        if (gradeDetail != null) {
                            Text(text = "排名 ${gradeDetail.pm} / ${gradeDetail.rs}")
                        }
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (grade.kcdlmc != "博约通识")
                            Text(
                                text = grade.kcdlmc
                            )
                        Text(
                            text = grade.kcflmc.replace("（", "\n").replace("）", "")
                        )
                    }
                }
        },
        onDismissRequest = {  },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.showPersonAlert = false
                }
            ) {
                Text("关闭")
            }
        }
    )
}

@Composable
@Preview
fun pppp() {
    ShowAlert(
        grade = KccjList(
            xsxm = "许博涵",
            zcjfs = 100.0,
            kcflmc = "博约经典（博约核心）",
            xnxqmc = "2023-2024-1",
            kcdlmc = "博约通识",
            cjjd = 5.0,
            kcrwdm = "10060608",
            kcmc = "数据结构",
            cjdm = "29664301",
            zcj = "优秀",
            xf = 3,
            xnxqdm = "202301",
            xdfsmc = "必修",
            cjfsmc = "百分制",
        ), viewModel = SettingsViewModel()
    )
}

@Composable
@Preview
fun ppppp() {
    ShowAlert(
        grade = KccjList(
            xsxm = "许博涵",
            zcjfs = 100.0,
            kcflmc = "",
            xnxqmc = "2023-2024-1",
            kcdlmc = "专业教育",
            cjjd = 5.0,
            kcrwdm = "10060608",
            kcmc = "数据结构",
            cjdm = "29664301",
            zcj = "优秀",
            xf = 3,
            xnxqdm = "202301",
            xdfsmc = "必修",
            cjfsmc = "百分制",
        ), viewModel = SettingsViewModel()
    )
}

@Composable
@Preview
fun pppppp() {
    ShowAlert(
        grade = KccjList(
            xsxm = "许博涵",
            zcjfs = 100.0,
            kcflmc = "博约经典（博约核心）",
            xnxqmc = "2023-2024-1",
            kcdlmc = "博约通识",
            cjjd = 5.0,
            kcrwdm = "10060608",
            kcmc = "数据结构",
            cjdm = "29664301",
            zcj = "优秀",
            xf = 3,
            xnxqdm = "202301",
            xdfsmc = "必修",
            cjfsmc = "五级制",
        ), viewModel = SettingsViewModel()
    )
}