package com.smart.htu.screens.person

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.login.LogoutDialog
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.copyContent
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    contentPadding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showLogoutDialog = remember { mutableStateOf(false) }
    val showLoginDialog = remember { mutableStateOf(false) }
    val isShowPrivateMessage = remember { mutableStateOf(true) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            viewModel.getPersonalMessage()
            isRefreshing = false
        }
    }

    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
            title = { Text(text = stringResource(R.string.my)) },
            actions = {
                IconButton(
                    onClick = {
                        isShowPrivateMessage.value = !isShowPrivateMessage.value
                    }
                ) {
                    Icon(
                        painter = painterResource(id = if (isShowPrivateMessage.value) R.drawable.visibility_24px else R.drawable.visibility_off_24px),
                        contentDescription = "eye"
                    )
                }
            }
        )
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding())
        ) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
                Row {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxSize()
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
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                                    label = "统一身份认证系统",
                                    trailingText = stringResource(id = loginStateString(uiState.loginState)),
                                    onClick = {
                                        showLoginDialog.value = true
                                    }
                                )
                                PersonalMessage(
                                    label = "河南师大智慧教务",
                                    trailingText = stringResource(id = loginStateString(uiState.loginJWCState)),
                                    onClick = {
                                        if (uiState.loginJWCState != 1) navController.navigate(
                                            Destinations.Login.route
                                        )
                                    }
                                )
                                PersonalMessage(
                                    label = "第二课堂管理系统",
                                    trailingText = stringResource(id = loginStateString(0)),
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier
                        .fillMaxSize()
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
                        Spacer(modifier = Modifier.height(20.dp))
                    }
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
                                label = "统一身份认证系统",
                                trailingText = stringResource(id = loginStateString(uiState.loginState)),
                                onClick = {
                                    if (uiState.loginState != 1) showLoginDialog.value = true
                                }
                            )
                            PersonalMessage(
                                label = "河南师大智慧教务",
                                trailingText = stringResource(id = loginStateString(uiState.loginJWCState)),
                                onClick = {
                                    if (uiState.loginJWCState != 1) navController.navigate(
                                        Destinations.Login.route
                                    )
                                }
                            )
                            PersonalMessage(
                                label = "第二课堂管理系统",
                                trailingText = stringResource(id = loginStateString(0)),
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

        LogoutDialog(
            showDialog = showLogoutDialog,
            onConfirmClick = {
                viewModel.logout()
                navController.navigate(Destinations.App.route)
                showLogoutDialog.value = false
            }
        )

        LoginDialog(
            showDialog = showLoginDialog,
            summary = "统一身份认证系统",
            onLogin = { studentID, password, _ ->
                scope.launch {
                    viewModel.authLogin(studentID, password)
                }
            },
            logState = uiState.loginState
        )
    }
}

fun loginStateString(state: Int): Int {
    return when (state) {
        0 -> R.string.no_login
        1 -> R.string.logged
        2 -> R.string.logging_in
        3 -> R.string.login_expired
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
    val blurSize by animateDpAsState(
        targetValue = if (!isShowPrivateMessage) 10.dp else 0.dp,
        label = ""
    )
    val scope = rememberCoroutineScope()
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = if (!isShowPrivateMessage) {
                        Modifier.hazeEffect()
                    } else {
                        Modifier
                    }
                )
            }
        },
        modifier = Modifier.clickable {
            if (onClick != null) {
                onClick()
            } else {
                if (trailingText != null) {
                    scope.launch {
                        if (isShowPrivateMessage) {
                            copyContent(trailingText)
                            snackBarHostState.showSnackbar("已复制到剪贴板")
                        } else {
                            snackBarHostState.showSnackbar("已开启隐私保护模式，禁止复制信息")
                        }
                    }
                }
            }
        }
    )
}