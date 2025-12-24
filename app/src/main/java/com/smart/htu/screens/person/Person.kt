package com.smart.htu.screens.person

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.login.LogoutDialog
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.setting.SettingItemCard
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showSnackbar
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = navController.context
    val showLogoutDialog = remember { mutableStateOf(false) }
    val showLoginDialog = remember { mutableStateOf(false) }
    val isShowPrivateMessage = remember { mutableStateOf(true) }
    val isShowMessageDialog = remember { mutableStateOf(false) }
    val showDialogTarget = remember { mutableStateOf("") }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.getPersonalMessage()
            isRefreshing = false
        }
    }

    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = "账号与信息",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Back,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isShowPrivateMessage.value = !isShowPrivateMessage.value
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = if (isShowPrivateMessage.value)
                                painterResource(id = R.drawable.visibility_24px)
                            else
                                painterResource(id = R.drawable.visibility_off_24px),
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
                Row {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .overScrollVertical(),
                        overscrollEffect = null
                    ) {
                        item {
                            LargeCardDisplay(
                                modifier = Modifier,
                                title = "我的信息",
                                leadingIconPainting = R.drawable.person_search_24px
                            ) {
                                PersonalMessage(
                                    label = stringResource(id = R.string.username),
                                    trailingText = uiState.personalMessage?.username
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.birthday),
                                    trailingText = uiState.personalMessage?.birthday,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.student_id),
                                    trailingText = uiState.personalMessage?.studentId,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.class_name),
                                    trailingText = uiState.personalMessage?.className,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.academic),
                                    trailingText = uiState.personalMessage?.academic,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(R.string.campus_name),
                                    trailingText = uiState.personalMessage?.campusName,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.political_outlook),
                                    trailingText = uiState.personalMessage?.politicalProfile,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.phone),
                                    trailingText = uiState.personalMessage?.phoneNumber,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.email),
                                    trailingText = uiState.personalMessage?.emailNumber,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                            }
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxSize()
                            .overScrollVertical(),
                        overscrollEffect = null
                    ) {
                        item {
                            LargeCardDisplay(
                                modifier = Modifier,
                                title = "账号管理",
                                leadingIconPainting = R.drawable.circle_admin,
                                actionText = "详情",
                                navigateTo = {
                                    navController.navigate(Destinations.AccountManage.route)
                                }
                            ) {
                                PersonalMessage(
                                    label = "河南师大智慧教务",
                                    trailingText = stringResource(
                                        id = loginStateString(
                                            uiState.jwcLoginState
                                        )
                                    ),
                                    onClick = {
                                        if (uiState.jwcLoginState != 1) navController.navigate(
                                            Destinations.Login.route
                                        )
                                    }
                                )
                                PersonalMessage(
                                    label = "统一身份认证系统",
                                    trailingText = stringResource(
                                        id = loginStateString(
                                            uiState.authLoginState
                                        )
                                    ),
                                    onClick = {
                                        showLoginDialog.value = true
                                    }
                                )
                                PersonalMessage(
                                    label = "第二课堂管理系统",
                                    trailingText = stringResource(
                                        id = loginStateString(
                                            uiState.scLoginState
                                        )
                                    ),
                                    onClick = {
                                    }
                                )
                                PersonalMessage(
                                    label = "我的图书馆",
                                    trailingText = stringResource(id = loginStateString(0)),
                                    onClick = {
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            TextButton(
                                text = stringResource(id = R.string.log_out),
                                onClick = {
                                    showLogoutDialog.value = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    textColor = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                    }
                }
            else
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .overScrollVertical(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
                ) {
                    item {
                        SettingItemCard(
                            modifier = Modifier,
                            label = "个人信息"
                        ) {
                            PersonalMessage(
                                label = stringResource(id = R.string.username),
                                trailingText = uiState.personalMessage?.username
                            )
                            Column(
                                modifier = Modifier
                                    .animateContentSize()
                                    .hazeEffect {
                                        blurEnabled = !isShowPrivateMessage.value
                                    }
                            ) {
                                PersonalMessage(
                                    label = stringResource(id = R.string.birthday),
                                    trailingText = uiState.personalMessage?.birthday,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.student_id),
                                    trailingText = uiState.personalMessage?.studentId,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.class_name),
                                    trailingText = uiState.personalMessage?.className,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.academic),
                                    trailingText = uiState.personalMessage?.academic,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(R.string.campus_name),
                                    trailingText = uiState.personalMessage?.campusName,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.political_outlook),
                                    trailingText = uiState.personalMessage?.politicalProfile,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.phone),
                                    trailingText = uiState.personalMessage?.phoneNumber,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                                PersonalMessage(
                                    label = stringResource(id = R.string.email),
                                    trailingText = uiState.personalMessage?.emailNumber,
                                    isShowPrivateMessage = isShowPrivateMessage.value
                                )
                            }
                        }
                    }
                    item {
                        SettingItemCard(modifier = Modifier, label = "登录状态") {
                            PersonalMessage(
                                label = "河南师大智慧教务",
                                trailingText = stringResource(id = loginStateString(uiState.jwcLoginState)),
                                onClick = {
                                    // isShowMessageDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "统一身份认证",
                                trailingText = stringResource(id = loginStateString(uiState.authLoginState)),
                                onClick = {
                                    showDialogTarget.value = "统一身份认证"
                                    isShowMessageDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "第二课堂管理系统",
                                trailingText = stringResource(id = loginStateString(uiState.scLoginState)),
                                onClick = {
                                    showDialogTarget.value = "第二课堂管理系统"
                                    isShowMessageDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "图书馆书目检索系统",
                                trailingText = stringResource(id = loginStateString(uiState.libraryLoginState)),
                                onClick = {
                                    showDialogTarget.value = "图书馆书目检索系统"
                                    isShowMessageDialog.value = true
                                }
                            )
                        }
                    }
                    item {
                        Card {
                            SuperArrow(
                                title = "登录信息管理",
                                onClick = {
                                    navController.navigate(Destinations.AccountManage.route)
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                color = MaterialTheme.colorScheme.errorContainer,
                                textColor = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
        }

        LogoutDialog(
            showDialog = showLogoutDialog,
            onConfirmClick = {
                viewModel.logout()
                showLogoutDialog.value = false
                navController.popBackStack()
            }
        )

        LoginDialog(
            showDialog = showLoginDialog,
            summary = "统一身份认证系统",
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
            logState = uiState.authLoginState
        )

        DeleteMessageDialog(isShowMessageDialog, showDialogTarget)
    }
}

@Composable
fun DeleteMessageDialog(
    showDialog: MutableState<Boolean>,
    target: MutableState<String>
) {
    SuperDialog(
        title = "提示",
        summary = "是否清除 ${target.value} 的登录信息？",
        onDismissRequest = {
            showDialog.value = false
        },
        show = showDialog
    ) {
        Column {
            Text("功能开发中...")
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "取消",
                    onClick = {
                        showDialog.value = false
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确认",
                    onClick = {
                        showDialog.value = false
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
    content: (@Composable () -> Unit)? = null,
    trailingText: String? = null,
    isShowPrivateMessage: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    if (isShowPrivateMessage) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            },
            trailingContent = {
                if (content != null) {
                    content()
                } else if (trailingText != null) {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            },
            modifier = Modifier.clickable(
                enabled = isShowPrivateMessage
            ) {
                if (onClick != null) {
                    onClick()
                } else {
                    if (trailingText != null) {
                        scope.launch {
                            if (isShowPrivateMessage) {
                                copyContent(trailingText)
                                showSnackbar(snackBarHostState, "已复制到剪贴板")
                            } else {
                                showSnackbar(
                                    snackBarHostState,
                                    "已开启隐私保护模式，禁止复制信息"
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}