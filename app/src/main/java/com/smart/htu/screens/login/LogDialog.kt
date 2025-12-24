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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Rename
import top.yukonga.miuix.kmp.theme.MiuixTheme

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
            showDialog.value = false
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
            TextButton(
                enabled = countdown == 0,
                text = stringResource(id = R.string.confirm) + if (isConfirmEnabled) "" else " (${countdown}s)",
                onClick = {
                    onConfirmClick()
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun LoginDialog(
    showDialog: MutableState<Boolean>,
    title: String = stringResource(R.string.login),
    summary: String? = null,
    initStudentID: String = "",
    isNeedVerifyCode: Boolean = false,
    verifyCodeModel: ImageRequest? = null,
    onLogin: (String, String, String) -> Unit,
    onClickVerifyCode: () -> Unit = {},
    logState: Int
) {
    val account = remember { mutableStateOf(initStudentID) }
    val password = remember { mutableStateOf("") }
    val verifyCode = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    SuperDialog(
        title = title,
        summary = summary,
        show = showDialog,
        onDismissRequest = {
            showDialog.value = false
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
                            imageVector = MiuixIcons.Useful.Rename,
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
                                    onClickVerifyCode()
                                },
                            placeholder = painterResource(id = R.drawable.ic_loading_placeholder_horizontal),
                            error = painterResource(id = R.drawable.ic_loading_placeholder_horizontal)
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
                    TextButtonWithProgressIndicator(
                        text = stringResource(id = R.string.login),
                        onClick = {
                            onLogin(account.value, password.value, verifyCode.value)
                        },
                        isLoading = logState == 2,
                        enabled = password.value.isNotEmpty() && account.value.isNotEmpty(),
                        colors = ButtonDefaults.buttonColorsPrimary(),
                        textColors = ButtonDefaults.textButtonColorsPrimary(),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxWidth(),
                    )
                    Spacer(Modifier.width(12.dp))
                    TextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            showDialog.value = false
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
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
                            onLogin(account.value, password.value, verifyCode.value)
                        },
                        isLoading = logState == 2,
                        enabled = password.value.isNotEmpty() && account.value.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColorsPrimary(),
                        textColors = ButtonDefaults.textButtonColorsPrimary(),
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            showDialog.value = false
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
    showDialog: MutableState<Boolean>,
    onDismissRequests: () -> Unit
) {
    SuperDialog(
        title = "提示",
        show = showDialog,
        summary = "智慧教务密码与教务系统(https://jwc.htu.edu.cn)密码一致",
        onDismissRequest = {
            showDialog.value = false
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = "真忘了",
                onClick = {
                    onDismissRequests()
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "我知道了",
                onClick = {
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}