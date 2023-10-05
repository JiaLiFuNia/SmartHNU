package com.xhand.htu.login

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ShowLoginDialog() {
    var displayPassword by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    displayPassword = isPressed

    AlertDialog(
        title = {
            Text(text = "智慧教务管理系统")
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                TextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("学号") },
                    isError = true,
                    supportingText = { },
                    trailingIcon = { },
                )
                TextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("密码") },
                    isError = true,
                    supportingText = { },
                    trailingIcon = { },
                    visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation()
                )
            }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = { }
            ) {
                Text("登录")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { }
            ) {
                Text("取消")
            }
        }
    )
}

@Preview
@Composable
fun ShowLoginDiaPreview() {
    ShowLoginDialog()
}