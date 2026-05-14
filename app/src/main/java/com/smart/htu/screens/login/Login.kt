package com.smart.htu.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smart.htu.App.Companion.context
import com.smart.htu.component.TextButtonWithProgressIndicator
import com.smart.htu.component.imageVectors.appLogo
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.Constants.Companion.HENAN_NORMAL_UNIVERSITY
import com.smart.htu.utils.ToastUtil.showSnackbar
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.startLaunchAPK
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults.buttonColorsPrimary
import top.yukonga.miuix.kmp.basic.ButtonDefaults.textButtonColorsPrimary
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Remove
import top.yukonga.miuix.kmp.icon.extended.Rename
import top.yukonga.miuix.kmp.icon.extended.Tune
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val autofillManager = LocalAutofillManager.current
    val scope = rememberCoroutineScope()

    val showLoginInfoDialog = remember { mutableStateOf(false) }
    var isCodeLogDialogShow by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.jwcLoginState) {
        if (uiState.jwcLoginState == 1) {
            navigator.pop()
        }
    }

    val scrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "登录",
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.pop()
                        },
                        
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Remove,
                            contentDescription = "退出"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isCodeLogDialogShow = true
                        },
                        holdDownState = isCodeLogDialogShow
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Tune,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .overScrollVertical()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(it),
            overscrollEffect = null,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = appLogo(),
                        contentDescription = "app_logo",
                        modifier = Modifier
                            .size(120.dp)
                    )
                }
            }
            item {
                var passwordVisible by remember { mutableStateOf(false) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    TextField(
                        value = uiState.studentID,
                        onValueChange = {
                            viewModel.changeStudentID(it)
                        },
                        label = "学号",
                        useLabelAsPlaceholder = true,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentType = ContentType.Username },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = uiState.jwcPassword,
                        onValueChange = {
                            viewModel.changeJWCPassword(it)
                            viewModel.changePassword(it)
                        },
                        label = "智慧教务密码",
                        useLabelAsPlaceholder = true,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.padding(end = 12.dp)
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
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                TextButtonWithProgressIndicator(
                    text = if (uiState.isLoading) "正在登录..." else "登录",
                    onClick = {
                        scope.launch {
                            focusManager.clearFocus()
                            if (uiState.studentID == "admin") {
                                viewModel.testLogin()
                                // navController.navigate(Destinations.AccountManage.route)
                            } else {
                                viewModel.jwcLogin(
                                    onSuccess = {
                                        autofillManager?.commit()
                                        showToast(context, "登录成功！")
                                    },
                                    onFailure = {
                                        scope.launch {
                                            showToast(context, it)
                                        }
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isLoading = uiState.isLoading,
                    colors = buttonColorsPrimary(),
                    textColors = textButtonColorsPrimary()
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = "忘记密码?",
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                showLoginInfoDialog.value = true
                            }
                    )
                    Text(
                        text = "游客访问",
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                viewModel.guestLogin()
                                navigator.pop()
                            }
                    )
                }
            }
            item {
                Text(
                    AnnotatedString(
                        text = "河南师范大学  |  i 师大",
                        annotations = listOf(
                            AnnotatedString.Range(
                                item = LinkAnnotation.Clickable(
                                    tag = "web",
                                    linkInteractionListener = LinkInteractionListener {
                                        navigator.pushWebView(
                                            url = HENAN_NORMAL_UNIVERSITY,
                                            title = "河南师范大学"
                                        )
                                    }
                                ),
                                start = 0,
                                end = 6
                            ),
                            AnnotatedString.Range(
                                item = LinkAnnotation.Clickable(
                                    tag = "hnu",
                                    linkInteractionListener = LinkInteractionListener {
                                        startLaunchAPK("i师大", "com.autewifi.sd.enroll")
                                    }
                                ),
                                start = 11,
                                end = 15
                            )
                        )
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                )
            }
        }

        LoginInfoDialog(
            showDialog = showLoginInfoDialog.value,
            onDismissRequests = {
                scope.launch {
                    showLoginInfoDialog.value = false
                    showSnackbar(
                        viewModel.snackBarHostState,
                        "请前往河南师大智慧教务微信公众号进行密码重置"
                    )
                }
            }
        )
        CodeLogDialog(
            showDialog = isCodeLogDialogShow,
            onLoginByCode = {
                scope.launch {
                    viewModel.wechatLogin(
                        code = it,
                        onSuccess = {
                            showToast(context, "登录成功！")
                            isCodeLogDialogShow = false
                        },
                        onFailure = {
                            showToast(context, it)
                        }
                    )
                }
            },
            onDismissRequest = {
                isCodeLogDialogShow = false
            }
        )
    }
}