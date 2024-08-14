package com.xhand.hnu.components

import android.widget.Toast
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.xhand.hnu.R
import com.xhand.hnu.network.createImageLoader
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun ShowSecondClassLoginDialog(
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
            Text(text = "第二课堂管理系统")
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                if (viewModel.scLoginCircle)
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth()
                    )
                TextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("学号") },
                    isError = viewModel.stateCode == -1,
                    supportingText = {
                        if (viewModel.stateCode == -1) {
                            Text(text = "学号或密码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        if (viewModel.stateCode == -1) {
                            Icon(
                                painterResource(id = R.drawable.ic_baseline_warning_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                TextField(
                    value = viewModel.scPassword,
                    onValueChange = { viewModel.scPassword = it },
                    label = { Text("密码") },
                    isError = viewModel.stateCode == -1,
                    supportingText = {
                        if (viewModel.stateCode == -1) {
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
                TextField(
                    value = viewModel.verifyCode,
                    onValueChange = { viewModel.verifyCode = it },
                    label = { Text("验证码") },
                    isError = viewModel.stateCode == -2 || viewModel.stateCode == -3,
                    supportingText = {
                        if (viewModel.stateCode == -2 || viewModel.stateCode == -3) {
                            Text(text = "验证码错误！", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .sizeIn(maxWidth = 120.dp, maxHeight = 40.dp)
                                .padding(end = 10.dp)
                        ) {
                            if (viewModel.scService)
                                CircularProgressIndicator()
                            else
                                AsyncImage(
                                    model = viewModel.verifyImg,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .aspectRatio(3 / 1f)
                                        .clickable {
                                            coroutineScope.launch {
                                                viewModel.secondClassService()
                                            }
                                        },
                                    contentScale = ContentScale.Crop,
                                    imageLoader = createImageLoader(context, viewModel.cookie)
                                )
                        }
                    }
                )
                Text(
                    text = AnnotatedString(
                        text = "如果你频繁遇到验证码错误，请点击此处刷新",
                        spanStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ),
                    modifier = Modifier
                        .basicMarquee()
                        .clickable {
                            coroutineScope.launch {
                                viewModel.secondClassService()
                            }
                        },
                )
                Text(
                    text = AnnotatedString(
                        text = "忘记密码？",
                        spanStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ),
                    modifier = Modifier.clickable {
                        Toast.makeText(
                            context,
                            "请联系学校管理员解决",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                )
            }
        },
        onDismissRequest = { viewModel.isShowScDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.secondClassLogin()
                    }
                },
                enabled = !viewModel.scLoginCircle
            ) {
                Text("登录")
                if (viewModel.stateCode == 1) {
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                    viewModel.isShowScDialog = false
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.isShowScDialog = false
                }
            ) {
                Text("取消")
            }
        }
    )
}

@Preview
@Composable
fun PreviewShowSecondClassLoginDialog() {
    ShowSecondClassLoginDialog(SettingsViewModel(context = LocalContext.current))
}