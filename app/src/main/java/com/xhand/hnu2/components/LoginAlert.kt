package com.xhand.hnu2.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.xhand.hnu2.viewmodel.SettingsViewModel

@Composable
fun showAlert(
    settingsViewModel: SettingsViewModel,
    text: String
) {
    AlertDialog(
        title = { Text(text = "提示") },
        text = { Text(text = text) },
        onDismissRequest = { settingsViewModel.isShowDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    settingsViewModel.isShowAlert = false
                }
            ) {
                Text("取消")
            }
            TextButton(
                onClick = {
                    settingsViewModel.isShowAlert = false
                    settingsViewModel.userInfo = null
                }
            ) {
                Text("确认")
            }
        }
    )

}