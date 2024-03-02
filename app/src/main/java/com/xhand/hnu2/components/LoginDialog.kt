package com.xhand.hnu2.components

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun showLoginDialog(): Boolean {
    var displayPassword by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val context = LocalContext.current
    var showdialog by remember {
        mutableStateOf(true)
    }
    var stuid by remember {
        mutableStateOf("")
    }
    var pwd by remember {
        mutableStateOf("")
    }
    displayPassword = isPressed

    AlertDialog(
        title = {
            Text(text = "河南师大智慧教务")
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                TextField(
                    value = stuid,
                    onValueChange = { stuid = it },
                    label = { Text("学号") },
                    isError = false,
                    supportingText = { },
                    trailingIcon = { },
                )
                TextField(
                    value = pwd,
                    onValueChange = { pwd = it },
                    label = { Text("密码") },
                    isError = false,
                    supportingText = { },
                    trailingIcon = { },
                    visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation()
                )
            }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    Toast.makeText(context, "登录功能正在开发中...", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("登录")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showdialog = false
                }
            ) {
                Text("取消")
            }
        }
    )
    return showdialog
}

@Preview
@Composable
fun ShowLoginDiaPreview() {
    showLoginDialog()
}