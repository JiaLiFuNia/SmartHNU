package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun ShowAlert(
    grade: KccjList,
    viewModel: SettingsViewModel,
    cjdm: String
) {
    LaunchedEffect(Unit) {
        viewModel.gradeDetailService(cjdm)
    }
    val gradeDetail = viewModel.gradeDetail
    val gradeDetail2 = viewModel.gradeDetails // 有平时成绩
    AlertDialog(
        title = { Text(text = "${grade.kcmc}  [${grade.xdfsmc}]") },
        text = {
            if (viewModel.isGettingDetailGrade) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row {
                        Column(
                            modifier = Modifier.weight(0.48f)
                        ) {
                            if (grade.cjfsmc == "五级制")
                                Text(text = "成绩 ${grade.zcjfs} (${grade.zcj})")
                            else
                                Text(text = "成绩 ${grade.zcjfs}")

                            Text(text = "绩点 ${grade.cjjd}")
                            Text(text = "排名 ${gradeDetail.pm} / ${gradeDetail.rs}")
                        }
                        Column(
                            modifier = Modifier.weight(0.52f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (gradeDetail2.cj4.toString().isNotEmpty()) {
                                Text(text = "期末 ${gradeDetail2.cj4}（${gradeDetail2.bl4.toString().split(".")[0]}%）")
                                Text(text = "平时 ${gradeDetail2.cj1}（${gradeDetail2.bl1.toString().split(".")[0]}%）")
                            }
                        }
                    }
                    if (grade.kcdlmc != "博约通识")
                        Text(text = "类型 ${grade.kcdlmc}")
                    else
                        Text(text = "类型 ${grade.kcflmc.replace("（", "-").replace("）", "")}")
                }
        },
        onDismissRequest = {
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.showPersonAlert = false
                }
            ) {
                Text("关闭")
            }
        },
        confirmButton = {

        }
    )
}
