package com.xhand.hnu2.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun UpdateAlert() {
    AlertDialog(
        title = { Text(text = "提示") },
        text = {
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                }
            ) {
                Text("关闭")
            }
        }
    )
}