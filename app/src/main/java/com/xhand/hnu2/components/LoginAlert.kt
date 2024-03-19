package com.xhand.hnu2.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.xhand.hnu2.viewmodel.SettingsViewModel

@Composable
fun showAlert(
    viewModel: SettingsViewModel,
    text: String
) {
    AlertDialog(
        title = { Text(text = "提示") },
        text = { Text(text = text) },
        onDismissRequest = { viewModel.isShowDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.isShowAlert = false
                }
            ) {
                Text("取消")
            }
            TextButton(
                onClick = {
                    viewModel.isShowAlert = false
                    viewModel.userInfo = null
                }
            ) {
                Text("确认")
            }
        }
    )
}