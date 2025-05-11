package com.smart.htu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.htu.api.module.UpdateData
import com.smart.htu.component.textButtonPrimaryColors
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun UpdateDialog(
    showDialog: MutableState<Boolean>,
    isForceUpdate: Boolean,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    updateData: UpdateData
) {
    SuperDialog(
        title = "发现新版本",
        show = showDialog,
        summary = "版本：${updateData.versionName}(${updateData.versionCode})",
        onDismissRequest = {
            if (!isForceUpdate) {
                onDismissRequest()
                showDialog.value = false
            }
        }
    ) {
        Column {
            Text(text = updateData.update?.content ?: "更新内容", color = MiuixTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!isForceUpdate) {
                    top.yukonga.miuix.kmp.basic.TextButton(
                        text = "关闭",
                        onClick = {
                            onDismissRequest()
                            showDialog.value = false
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(20.dp))
                }
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = "下载并更新",
                    onClick = {
                        onConfirmClick()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonPrimaryColors()
                )
            }
        }
    }
}