package com.xhand.hnu.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.xhand.hnu.R
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowLoginDialog(
    viewModel: SettingsViewModel
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
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("学号") },
                    isError = viewModel.loginCode != 200 && viewModel.loginCode != 0,
                    supportingText = {
                        if (viewModel.loginCode != 200 && viewModel.loginCode != 0) {
                            Text(text = "学号或密码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        if (viewModel.loginCode != 200 && viewModel.loginCode != 0) {
                            Icon(
                                painterResource(id = R.drawable.ic_baseline_warning_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("密码") },
                    isError = viewModel.loginCode != 200 && viewModel.loginCode != 0,
                    supportingText = {
                        if (viewModel.loginCode != 200 && viewModel.loginCode != 0) {
                            Text(text = "学号或密码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                painterResource(
                                    id = if (showPassword) R.drawable.ic_outline_visibility_24 else R.drawable.ic_outline_visibility_off_24
                                ), contentDescription = "展示密码", tint = Color.Black
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                )
                ClickableText(
                    modifier = Modifier
                        .basicMarquee(),
                    text = AnnotatedString(
                        text = "*此登录接口采用的是河南师大智慧教务公众号",
                        spanStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                ) {

                }
                ClickableText(
                    text = AnnotatedString(
                        text = "忘记密码？",
                        spanStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                ) {
                    Toast.makeText(context, "请前往河南师大智慧教务修改密码", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.login()
                    }
                }
            ) {
                Text("登录")
                if (viewModel.loginCircle) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp), strokeWidth = 2.dp
                    )
                }
                if (viewModel.isLoginSuccess) {
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                    viewModel.isShowDialog = false
                    // viewModel.isLogging = false
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.isShowDialog = false
            }
            ) {
                Text("取消")
            }
        }
    )
}