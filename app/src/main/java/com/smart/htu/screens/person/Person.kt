package com.smart.htu.screens.person

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.di.NetworkModule.ApiConstants.AUTH_BASE_URL
import com.smart.htu.di.NetworkModule.ApiConstants.EHALL_BASE_URL
import com.smart.htu.di.NetworkModule.ApiConstants.SECOND_CLASS_BASE_URL
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.login.LogoutDialog
import com.smart.htu.screens.navigation.Route
import com.smart.htu.screens.setting.SettingItemCard
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun PersonScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showLogoutDialog = remember { mutableStateOf(false) }
    val showLoginDialog = remember { mutableStateOf(false) }
    val isShowPrivateMessage = remember { mutableStateOf(true) }
    val isShowMessageDialog = remember { mutableStateOf(false) }
    val showDialogTarget = remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.getPersonalMessage()
            isRefreshing = false
        }
    }

    val scrollBehavior = MiuixScrollBehavior()
    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = "账号与信息",
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isShowPrivateMessage.value = !isShowPrivateMessage.value
                            }
                        ) {
                            Icon(
                                imageVector = if (isShowPrivateMessage.value)
                                    Icons.Outlined.Visibility
                                else
                                    Icons.Outlined.VisibilityOff,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .overScrollVertical()
                        .scrollEndHaptic()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    )
                ) {
                    item {
                        SettingItemCard(
                            modifier = Modifier,
                            label = "个人信息"
                        ) {
                            PersonalMessage(
                                label = stringResource(id = R.string.username),
                                stateText = uiState.personalMessage?.username ?: "--"
                            )
                            Column(
                                modifier = Modifier
                                    .animateContentSize()
                            ) {
                                PersonalMessage(
                                    label = stringResource(id = R.string.birthday),
                                    stateText = uiState.personalMessage?.birthday ?: "2000-01-01",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.student_id),
                                    stateText = uiState.personalMessage?.studentId ?: "--",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.class_name),
                                    stateText = uiState.personalMessage?.className ?: "--班",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.academic),
                                    stateText = uiState.personalMessage?.academic ?: "--学院",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(R.string.campus_name),
                                    stateText = uiState.personalMessage?.campusName ?: "--",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.political_outlook),
                                    stateText = uiState.personalMessage?.politicalProfile ?: "--",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.phone),
                                    stateText = uiState.personalMessage?.phoneNumber ?: "--",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.email),
                                    stateText = uiState.personalMessage?.emailNumber
                                        ?: "--@stu.htu.edu.cn",
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                            }
                        }
                    }
                    item {
                        SettingItemCard(modifier = Modifier, label = "登录状态") {
                            PersonalMessage(
                                label = "河南师大智慧教务",
                                stateText = stringResource(id = loginStateString(uiState.jwcLoginState)),
                                onClick = {
                                    // isShowMessageDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "统一身份认证系统",
                                stateText = stringResource(loginStateString(uiState.authLoginState)),
                                onClick = {
                                    showDialogTarget.value = "统一身份认证系统"
                                    isShowMessageDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "第二课堂管理系统",
                                stateText = stringResource(loginStateString(uiState.scLoginState)),
                                onClick = {
                                    showDialogTarget.value = "第二课堂管理系统"
                                    isShowMessageDialog.value = true
                                }
                            )
                        }
                    }
                    item {
                        Card {
                            ArrowPreference(
                                title = "登录信息管理",
                                onClick = {
                                    navigator.push(Route.AccountManage)
                                }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(height = 16.dp))
                        TextButton(
                            text = stringResource(id = R.string.log_out),
                            onClick = {
                                showLogoutDialog.value = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.textButtonColors(
                                color = MiuixTheme.colorScheme.secondary,
                                textColor = MiuixTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }

        LogoutDialog(
            showDialog = showLogoutDialog.value,
            onConfirmClick = {
                scope.launch {
                    viewModel.logout()
                    showLogoutDialog.value = false
                    navigator.pop()
                }
            },
            onDismissRequest = {
                showLogoutDialog.value = false
            }
        )

        LoginDialog(
            showDialog = showLoginDialog.value,
            title = "统一身份认证登录",
            summary = "该应用需要进行统一身份认证，请认证后使用。密码与寝室校园网密码一致。",
            onLogin = { studentID, password, _ ->
                scope.launch {
                    viewModel.authLogin(
                        studentID = studentID,
                        password = password,
                        onSuccess = {
                            showLoginDialog.value = false
                            showToast(context, "登录成功!")
                        },
                        onFailure = {
                            showToast(context, "登录失败！请检查账号密码是否正确")
                        }
                    )
                }
            },
            loginState = uiState.authLoginState,
            onDismissRequest = {
                showLoginDialog.value = false
            }
        )

        DeleteMessageDialog(
            showDialog = isShowMessageDialog.value,
            target = showDialogTarget,
            onDismissRequest = {
                isShowMessageDialog.value = false
            },
            onConfirmClick = {
                scope.launch {
                    when (showDialogTarget.value) {
                        "统一身份认证系统" -> {
                            viewModel.changeLoginAuthState(0)
                            viewModel.clearCookieForUrl(EHALL_BASE_URL)
                            viewModel.clearCookieForUrl(AUTH_BASE_URL)
                        }

                        "第二课堂管理系统" -> {
                            viewModel.changeLoginSCState(0)
                            viewModel.clearCookieForUrl(SECOND_CLASS_BASE_URL)
                        }
                    }
                    isShowMessageDialog.value = false
                }
            }
        )
    }
}

@Composable
fun DeleteMessageDialog(
    showDialog: Boolean,
    target: MutableState<String>,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    OverlayDialog(
        title = "提示",
        summary = "是否清除 ${target.value} 的登录信息？",
        onDismissRequest = onDismissRequest,
        show = showDialog
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
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
                    text = "确认",
                    onClick = {
                        onConfirmClick()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

fun loginStateString(state: Int): Int {
    return when (state) {
        0 -> R.string.no_login
        1 -> R.string.logged
        2 -> R.string.logging_in
        -2 -> R.string.login_expired
        -1 -> R.string.login_failed
        else -> R.string.unknown_status
    }
}

@Composable
fun PersonalMessage(
    label: String,
    stateText: String,
    isShowPrivateMessage: Boolean = true,
    onClick: (() -> Unit)? = null,
    holdDownState: Boolean = false
) {
    val scope = rememberCoroutineScope()
    if (isShowPrivateMessage) {
        BasicComponent(
            title = label,
            endActions = {
                Text(
                    text = stateText,
                    fontSize = MiuixTheme.textStyles.body1.fontSize,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
            },
            onClick = {
                if (onClick != null) {
                    onClick()
                } else {
                    scope.launch {
                        if (isShowPrivateMessage) {
                            copyContent(stateText)
                            showToast(context, "已复制到剪贴板")
                        } else {
                            showToast(context, "请先显示隐私信息")
                        }
                    }
                }
            },
            holdDownState = holdDownState
        )
    }
}