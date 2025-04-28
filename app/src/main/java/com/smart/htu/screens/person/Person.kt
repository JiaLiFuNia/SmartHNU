package com.smart.htu.screens.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import top.yukonga.miuix.kmp.basic.LazyColumn
import top.yukonga.miuix.kmp.basic.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    contentPadding: PaddingValues
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
    var showEditMessageDialog = remember { mutableStateOf(false) }
    top.yukonga.miuix.kmp.basic.PullToRefresh(
        pullToRefreshState = pullToRefreshState,
        refreshTexts = PULL_TO_REFRESH_TEXT,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                /*PreferencesCard(
                    headlineText = "河南师范大学",
                    supportingText = "省属重点大学、省特色骨干大学建设高校",
                    leadingIcon = R.drawable.hnu,
                    onClick = {
                        navController.navigateToWebView(
                            HENAN_NORMAL_UNIVERSITY,
                            "河南师范大学"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))*/
            }
            item {
                LargeCardDisplay(
                    modifier = Modifier,
                    title = "我的信息",
                    leadingIconPainting = R.drawable.person_search_24px
                ) {
                    PersonalMessage(
                        label = stringResource(id = R.string.avatar),
                        content = {
                            if (uiState.qqNumber == "")
                                Image(
                                    painter = painterResource(id = R.drawable.avator_1),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            else
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://q1.qlogo.cn/g?b=qq&nk=${uiState.qqNumber}&s=100")
                                        .crossfade(true)
                                        .addHeader("User-Agent", "Mozilla/5.0")
                                        .error(R.drawable.avator_1)
                                        .build(),
                                    contentDescription = "picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    placeholder = painterResource(id = R.drawable.book_failure)
                                )
                        },
                        onClick = {
                            showEditMessageDialog.value = true
                        }
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.username),
                        trailingText = uiState.personalMessage.data?.username
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.birthday),
                        trailingText = uiState.personalMessage.data?.birthday
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.student_id),
                        trailingText = uiState.personalMessage.data?.studentId
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.class_name),
                        trailingText = uiState.personalMessage.data?.className
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.academic),
                        trailingText = uiState.personalMessage.data?.academic
                    )
                    PersonalMessage(
                        label = stringResource(R.string.campus_name),
                        trailingText = uiState.personalMessage.data?.campusName
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.political_outlook),
                        trailingText = uiState.personalMessage.data?.politicalProfile
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.phone),
                        trailingText = uiState.personalMessage.data?.phoneNumber
                    )
                    PersonalMessage(
                        label = stringResource(id = R.string.email),
                        trailingText = uiState.personalMessage.data?.emailNumber
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

    EditQQNumberDialog(
        showDialog = showEditMessageDialog,
        onConfirmRequests = {
            viewModel.editQQNumber(it)
        }
    )

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
    onClick: (() -> Unit)? = null
) {
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
                )
            }
        },
        modifier = Modifier.clickable {
            if (onClick != null) {
                onClick()
            } else {
                if (trailingText != null) {
                    scope.launch {
                        copyContent(trailingText)
                        snackBarHostState.showSnackbar("已复制到剪贴板")
                    }
                }
            }
        }
    )
}