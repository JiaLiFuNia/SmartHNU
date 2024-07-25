package com.xhand.hnu.components

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun UpdateDialog(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    if (viewModel.isShowUpdateDialog)
        AlertDialog(
            title = {
                if (viewModel.updateMessage.type == "update")
                    Text("发现新版本 v${viewModel.updateMessage.version}")
                else
                    Text("公告")
            },
            onDismissRequest = { viewModel.isShowUpdateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.isShowUpdateDialog = false
                        Intent(Intent.ACTION_VIEW).also {
                            it.data = Uri.parse(viewModel.updateMessage.confirm)
                            if (it.resolveActivity(context.packageManager) != null) {
                                context.startActivity(it)
                            }
                        }
                    }
                ) {
                    Text(text = "更新")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.isShowUpdateDialog = false }
                ) {
                    Text(text = "暂不更新")
                }
            },
            text = {
                Text(text = viewModel.updateMessage.message)
            }
        )
}