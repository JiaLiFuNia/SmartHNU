package com.smart.htu.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.smart.htu.R
import kotlinx.coroutines.delay

@Composable
fun LogTipDialog(
    showDialog: Boolean,
    onDismissRequests: () -> Unit,
    onConfirmRequests: () -> Unit
) {
    var countdown by remember { mutableIntStateOf(3) }
    var isConfirmEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            countdown = 3
            isConfirmEnabled = false
            while (countdown > 0) {
                delay(1000L)
                countdown--
            }
            isConfirmEnabled = true
        }
    }

    if (showDialog)
        AlertDialog(
            icon = {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "ins")
            },
            title = {
                Text(text = stringResource(id = R.string.tip))
            },
            text = {
                Text(text = "你需要在此页面进行登录，登录成功后将自动跳转到个人信息页面；若没有发生跳转，请点击右上方登录按钮。")
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isConfirmEnabled) onConfirmRequests()
                    },
                    enabled = isConfirmEnabled
                ) {
                    Text(text = if (isConfirmEnabled) "确认" else "确认 ($countdown)")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onConfirmRequests()
                        onDismissRequests()
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
}