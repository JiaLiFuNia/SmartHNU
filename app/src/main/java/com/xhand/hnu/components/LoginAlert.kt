package com.xhand.hnu.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun ShowAlert(
    viewModel: SettingsViewModel,
    text: String
) {
    if (viewModel.isShowAlert)
        AlertDialog(
            title = { Text(text = "提示") },
            text = { Text(text = "确定要退出登录吗？") },
            onDismissRequest = { viewModel.isShowDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.isShowAlert = false
                        viewModel.userInfo = null
                        viewModel.cookie = ""
                        viewModel.stateCode = 0
                        viewModel.loginCode = 0
                        viewModel.clearUserInfo()
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.isShowAlert = false
                    }
                ) {
                    Text("取消")
                }
            }
        )
}