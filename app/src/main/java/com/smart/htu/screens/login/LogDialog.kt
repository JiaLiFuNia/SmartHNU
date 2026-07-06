package com.smart.htu.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.smart.htu.R
import com.smart.htu.component.TextButtonWithProgressIndicator
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Rename
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var countdown by remember { mutableIntStateOf(3) }
    var isConfirmEnabled by remember { mutableStateOf(false) }

    OverlayDialog(
        title = "提示",
        summary = stringResource(id = R.string.confirm_logout),
        show = showDialog,
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                enabled = countdown == 0,
                text = stringResource(id = R.string.confirm) + if (isConfirmEnabled) "" else " (${countdown}s)",
                onClick = {
                    onConfirmClick()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    onDismissRequest()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun LoginDialog(
    showDialog: Boolean,
    title: String = stringResource(R.string.login),
    summary: String? = null,
    initAccount: String = "",
    initPassword: String = "",
    isNeedVerifyCode: Boolean = false,
    verifyCodeModel: ImageRequest? = null,
    onLogin: (account: String, password: String, verifyCode: String) -> Unit,
    onRefreshVerifyCode: () -> Unit = {},
    loginState: Int,
    onDismissRequest: () -> Unit
) {
    val account = remember { mutableStateOf(initAccount) }
    val password = remember { mutableStateOf(initPassword) }
    val verifyCode = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    OverlayDialog(
        title = title,
        summary = summary,
        show = showDialog,
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = account.value,
                onValueChange = {
                    account.value = it
                },
                label = "学号",
                useLabelAsPlaceholder = true,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.Username },
            )
            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                label = "密码",
                useLabelAsPlaceholder = true,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (isNeedVerifyCode) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Rename,
                            tint = if (passwordVisible) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSecondaryContainer,
                            contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.Password },
            )
            if (isNeedVerifyCode) {
                TextField(
                    value = verifyCode.value,
                    onValueChange = {
                        verifyCode.value = it
                    },
                    label = "验证码",
                    useLabelAsPlaceholder = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
                    trailingIcon = {
                        AsyncImage(
                            model = verifyCodeModel,
                            contentDescription = "verifyCode",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .width(100.dp)
                                .aspectRatio(14 / 5f)
                                .fillMaxHeight()
                                .clickable {
                                    onRefreshVerifyCode()
                                },
                            placeholder = painterResource(id = R.drawable.loading_placeholder_horizontal),
                            error = painterResource(id = R.drawable.loading_placeholder_horizontal)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            if (isNeedVerifyCode) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            focusManager.clearFocus()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.width(12.dp))
                    TextButtonWithProgressIndicator(
                        text = stringResource(id = R.string.login),
                        onClick = {
                            focusManager.clearFocus()
                            onLogin(account.value, password.value, verifyCode.value)
                        },
                        isLoading = loginState == 2,
                        enabled = password.value.isNotEmpty() && account.value.isNotEmpty(),
                        colors = ButtonDefaults.buttonColorsPrimary(),
                        textColors = ButtonDefaults.textButtonColorsPrimary(),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxWidth(),
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButtonWithProgressIndicator(
                        text = stringResource(id = R.string.login),
                        onClick = {
                            focusManager.clearFocus()
                            onLogin(account.value, password.value, verifyCode.value)
                        },
                        isLoading = loginState == 2,
                        enabled = password.value.isNotEmpty() && account.value.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColorsPrimary(),
                        textColors = ButtonDefaults.textButtonColorsPrimary(),
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            focusManager.clearFocus()
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun LoginInfoDialog(
    showDialog: Boolean,
    onDismissRequests: () -> Unit
) {
    OverlayDialog(
        title = "提示",
        show = showDialog,
        summary = "智慧教务密码与教务系统(https://jwc.htu.edu.cn)密码一致",
        onDismissRequest = {
            onDismissRequests()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = "真忘了",
                onClick = {
                    onDismissRequests()
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "我知道了",
                onClick = {
                    onDismissRequests()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}


@Composable
fun CodeLogDialog(
    showDialog: Boolean,
    onLoginByCode: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val codeValue = remember { mutableStateOf("") }
    OverlayDialog(
        title = "便捷登录",
        show = showDialog,
        summary = "使用微信 Code 登录，无需输入学号和密码",
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        TextField(
            value = codeValue.value,
            onValueChange = {
                codeValue.value = it
            },
            label = "微信 Code",
            useLabelAsPlaceholder = true,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Password },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = "取消",
                onClick = {
                    onDismissRequest()
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "登录",
                onClick = {
                    onLoginByCode(codeValue.value)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}