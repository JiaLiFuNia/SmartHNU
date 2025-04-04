package com.smart.htu.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.utils.MiuixPopupUtils.Companion.dismissDialog

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LogoutDialog(
    showDialog: MutableState<Boolean>,
    onConfirmClick: () -> Unit
) {
    var countdown by remember { mutableIntStateOf(3) }
    var isConfirmEnabled by remember { mutableStateOf(false) }

    SuperDialog(
        title = "提示",
        summary = stringResource(id = R.string.confirm_logout),
        show = showDialog,
        onDismissRequest = {
            dismissDialog(showDialog)
        }
    ) {
        LaunchedEffect(showDialog) {
            if (showDialog.value) {
                countdown = 3
                isConfirmEnabled = false
                while (countdown > 0) {
                    delay(1000L)
                    countdown--
                }
                isConfirmEnabled = true
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            top.yukonga.miuix.kmp.basic.TextButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    dismissDialog(showDialog)
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            top.yukonga.miuix.kmp.basic.TextButton(
                enabled = countdown == 0,
                text = stringResource(id = R.string.confirm) + if (isConfirmEnabled) "" else " ($countdown)",
                onClick = {
                    onConfirmClick()
                    dismissDialog(showDialog)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
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