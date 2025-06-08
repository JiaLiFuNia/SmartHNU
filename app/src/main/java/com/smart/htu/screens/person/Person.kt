package com.smart.htu.screens.person

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.login.LogoutDialog
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    contentPadding: PaddingValues,
    isShowPrivateMessage: MutableState<Boolean>
) {
    val uiState by viewModel.uiState.collectAsState()

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.getPersonalMessage()
            }
        }
    }

    var showLogoutDialog = remember { mutableStateOf(false) }
    top.yukonga.miuix.kmp.basic.PullToRefresh(
        pullToRefreshState = pullToRefreshState,
        refreshTexts = PULL_TO_REFRESH_TEXT,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
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
                        trailingText = uiState.personalMessage.data?.username
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.birthday),
                        trailingText = uiState.personalMessage.data?.birthday,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.student_id),
                        trailingText = uiState.personalMessage.data?.studentId,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.class_name),
                        trailingText = uiState.personalMessage.data?.className,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.academic),
                        trailingText = uiState.personalMessage.data?.academic,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(R.string.campus_name),
                        trailingText = uiState.personalMessage.data?.campusName,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.political_outlook),
                        trailingText = uiState.personalMessage.data?.politicalProfile,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.phone),
                        trailingText = uiState.personalMessage.data?.phoneNumber,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.email),
                        trailingText = uiState.personalMessage.data?.emailNumber,
                        isShowPrivateMessage = isShowPrivateMessage.value
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                LargeCardDisplay(
                    modifier = Modifier,
                    title = "账号管理",
                    leadingIconPainting = R.drawable.circle_admin
                ) {
                    PersonalMessage(
                        label = "统一身份认证系统",
                        trailingText = stringResource(id = loginStateString(uiState.loginState)),
                        onClick = {
                            if (uiState.loginState != 1) navController.navigate(Destinations.Login.route)
                        }
                    )
                    PersonalMessage(
                        label = "河南师大智慧教务",
                        trailingText = stringResource(id = loginStateString(uiState.loginJWCState)),
                        onClick = {
                            if (uiState.loginJWCState != 1) navController.navigate(Destinations.Login.route)
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
}

fun loginStateString(state: Int): Int {
    return when (state) {
        0 -> R.string.no_login
        1 -> R.string.logged
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
                        Modifier.blur(radius = blurSize)
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