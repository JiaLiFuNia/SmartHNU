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
fun LogoutDialog(
    showDialog: Boolean,
    onDismissRequests: () -> Unit,
    onConfirmClick: () -> Unit
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
                Text(text = stringResource(id = R.string.confirm_logout))
            },
            onDismissRequest = { onDismissRequests() },
            confirmButton = {
                TextButton(onClick = { onDismissRequests() }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (isConfirmEnabled) onConfirmClick()
                    },
                    enabled = isConfirmEnabled
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm) + if (isConfirmEnabled) "" else " ($countdown)"
                    )
                }
            }
        )
}


@Composable
fun LoginDialog(
    showDialog: Boolean,
    onDismissRequests: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (showDialog)
        AlertDialog(
            icon = {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "ins")
            },
            title = {
                Text(text = stringResource(id = R.string.tip))
            },
            text = {
                Text(text = "暂未登录，立即登录体验更多功能！")
            },
            onDismissRequest = {},
            dismissButton = {
                TextButton(onClick = { onDismissRequests() }) {
                    Text(text = stringResource(R.string.guest))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.login_now))
                }
            }
        )
}