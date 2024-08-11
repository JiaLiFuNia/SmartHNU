package com.xhand.hnu.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.xhand.hnu.model.viewWebsite
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun MessageDialog(
    title: String,
    viewModel: SettingsViewModel,
    onClick: () -> Unit,
    confirmText: String,
    dismissText: String
) {
    AlertDialog(
        title = {
            Text(title)
        },
        onDismissRequest = { viewModel.isShowUpdateDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.isShowUpdateDialog = false
                    onClick()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.isShowUpdateDialog = false }
            ) {
                Text(text = dismissText)
            }
        },
        text = {
            Text(text = viewModel.updateMessage.message)
        }
    )
}


@Composable
fun UpdateDialog(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    if (viewModel.isShowUpdateDialog)
        when (viewModel.updateMessage.type) {
            "update" -> MessageDialog(
                title = "发现新版本 v${viewModel.updateMessage.version}",
                viewModel = viewModel,
                onClick = {
                    viewWebsite(viewModel.updateMessage.confirm, context)
                },
                confirmText = "更新",
                dismissText = "暂不更新"
            )

            "notice" -> MessageDialog(
                title = "公告 ${viewModel.updateMessage.version}",
                viewModel = viewModel,
                onClick = {},
                confirmText = "晓得了",
                dismissText = "关闭"
            )
        }
}