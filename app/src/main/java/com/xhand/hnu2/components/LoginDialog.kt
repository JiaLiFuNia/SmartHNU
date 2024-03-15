package com.xhand.hnu2.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.xhand.hnu2.R
import com.xhand.hnu2.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun showLoginDialog(
    settingsViewModel: SettingsViewModel
) {
    var displayPassword by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showPassword by remember {
        mutableStateOf(false)
    }
    displayPassword = isPressed
    AlertDialog(
        title = {
            Text(text = "河南师大智慧教务")
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                TextField(
                    value = settingsViewModel.username,
                    onValueChange = { settingsViewModel.username = it },
                    label = { Text("学号") },
                    isError = !settingsViewModel.isLoginSuccess and settingsViewModel.isLogging,
                    supportingText = {
                        if (!settingsViewModel.isLoginSuccess and settingsViewModel.isLogging) {
                            Text(text = "学号或密码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = { },
                )
                TextField(
                    value = settingsViewModel.password,
                    onValueChange = { settingsViewModel.password = it },
                    label = { Text("密码") },
                    isError = !settingsViewModel.isLoginSuccess and settingsViewModel.isLogging,
                    supportingText = {
                        if (!settingsViewModel.isLoginSuccess and settingsViewModel.isLogging) {
                            Text(text = "学号或密码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                painterResource(
                                    id = if (showPassword) R.drawable.ic_outline_visibility_24 else R.drawable.ic_outline_visibility_off_24
                                ),
                                contentDescription = "展示密码",
                                tint = Color.Black
                            )
                        }

                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                )
            }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        settingsViewModel.login()
                    }

                }
            ) {
                Text("登录")
                if (settingsViewModel.loginCircle) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp
                    )
                }
                if (settingsViewModel.isLoginSuccess) {
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                    settingsViewModel.isShowDialog = false
                    settingsViewModel.isLogging = false
                }
            }
            TextButton(
                onClick = {
                    settingsViewModel.username = "2201214001"
                    settingsViewModel.password = "xubohan2004819."
                }
            ) {
                Text("临时")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    settingsViewModel.isShowDialog = false
                    if (settingsViewModel.isLoginSuccess) {
                        settingsViewModel.isLogging = false
                    }
                }
            ) {
                Text("取消")
            }
        }
    )
}