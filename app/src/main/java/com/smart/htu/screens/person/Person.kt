package com.smart.htu.screens.person


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.R
import com.smart.htu.component.EditMessageDialog
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.LogoutDialog
import com.smart.htu.component.PreferencesCard
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import com.smart.htu.component.SettingItemCard
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            viewModel.getStudentInfo()
            delay(1500)
            isRefreshing = false
        }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditMessageDialog by remember { mutableStateOf(false) }
    ScaffoldWithHazeLazyColumn(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        blurEnabledState = uiState.blurEffect,
        title = {
            Text(text = stringResource(id = R.string.my))
        },
        actions = {
            IconButton(onClick = { navController.navigate(Destinations.AccountManage.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.key_24px),
                    contentDescription = "key"
                )
            }
        },
        navigationIcon = {},
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        refreshState = state,
    ) {
        item {
            PreferencesCard(
                headlineText = "河南师范大学",
                supportingText = "省属重点大学、省特色骨干大学建设高校",
                leadingIcon = R.drawable.hnu,
                onClick = {
                    navController.navigateToWebView("https://www.htu.edu.cn/", "河南师范大学")
                }
            )
        }
        item {
            LargeCardDisplay(
                modifier = Modifier,
                title = "我的信息",
                leadingIconPainting = R.drawable.outline_account_box_24
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
                        showEditMessageDialog = true
                    }
                )
                PersonalMessage(
                    label = stringResource(id = R.string.username),
                    trailingText = uiState.uneditableMessage.username ?: ""
                )
                PersonalMessage(
                    label = stringResource(id = R.string.birthday),
                    trailingText = uiState.uneditableMessage.birthday
                )
                PersonalMessage(
                    label = stringResource(id = R.string.student_id),
                    trailingText = uiState.uneditableMessage.studentId ?: ""
                )
                PersonalMessage(
                    label = stringResource(id = R.string.class_name),
                    trailingText = uiState.uneditableMessage.className ?: ""
                )
                PersonalMessage(
                    label = stringResource(id = R.string.academic),
                    trailingText = uiState.uneditableMessage.academic ?: ""
                )
                PersonalMessage(
                    label = stringResource(id = R.string.political_outlook),
                    trailingText = uiState.uneditableMessage.politicalProfile ?: "",
                )
                PersonalMessage(
                    label = stringResource(id = R.string.phone),
                    trailingText = uiState.uneditableMessage.phoneNumber ?: ""
                )
                PersonalMessage(
                    label = stringResource(id = R.string.email),
                    trailingText = uiState.uneditableMessage.emailNumber,
                    onClick = {
                        startWebUrl("mailto:${uiState.uneditableMessage.emailNumber}")
                    }
                )
            }
        }
        item {
            LargeCardDisplay(
                modifier = Modifier,
                title = "账号管理",
                leadingIconPainting = R.drawable.admin_panel_settings_24px
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
        }
        item {
            SettingItemCard(
                themeMode = 1,
                modifier = Modifier
            ) {
                Card(
                    onClick = {
                        showLogoutDialog = true
                    },
                    modifier = Modifier.height(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.log_out),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    EditMessageDialog(
        showDialog = showEditMessageDialog,
        onDismissRequests = { showEditMessageDialog = false },
        onConfirmRequests = {
            viewModel.editQQNumber(it)
        }
    )

    LogoutDialog(
        showDialog = showLogoutDialog,
        onDismissRequests = { showLogoutDialog = false },
        onConfirmClick = {
            viewModel.logout()
            navController.navigate(Destinations.App.route)
            showLogoutDialog = false
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
            }
        }
    )
}

@Composable
fun PersonalStateMessage(label: String, state: Int, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Gray,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = when (state) {
                0 -> "未登录"
                1 -> "已登录"
                2 -> "登录过期"
                else -> "未知状态"
            },
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier.weight(0.20f),
            horizontalArrangement = Arrangement.Center
        ) {
            if (state != 1) {
                Row(
                    modifier = Modifier
                        .weight(0.15f)
                        .clickable { onClick() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "去登录",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (state) {
                                // 0 -> MaterialTheme.colorScheme.error
                                1 -> MaterialTheme.colorScheme.primary
                                // 2 -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.onPrimary
                            }
                        )
                )
            }
        }

    }
}