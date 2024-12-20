package com.smart.htu.screens.person


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.EditMessageDialog
import com.smart.htu.component.LogoutDialog
import com.smart.htu.component.PreferenceItem
import com.smart.htu.component.PreferencesHintCard
import com.smart.htu.component.SettingItemCard
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.utils.openInBrowser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    logoutClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.getStudentInfo()
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }
    var showEditMessageDialog by remember {
        mutableStateOf(false)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = stringResource(id = R.string.my)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onRefresh() }) {
                        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "refresh")
                    }
                }
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            modifier = Modifier
                .padding(it)
        ) {
            LazyColumn(
                Modifier
                    .padding(horizontal = 15.dp)
            ) {
                item {
                    PreferencesHintCard(
                        title = "河南师范大学",
                        description = "省属重点大学、省特色骨干大学建设高校",
                        iconRes = R.drawable.hnu,
                        onClick = {
                            openInBrowser("https://www.htu.edu.cn/")
                        }
                    )
                }
                item {
                    SettingItemCard(
                        label = "我的信息",
                        modifier = Modifier
                    ) {
                        PreferenceItem(
                            title = stringResource(id = R.string.avatar),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.avator_1),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                            },
                            onClick = {
                                showEditMessageDialog = true
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.nickname),
                            trailingIcon = {
                                Text(
                                    text = uiState.editableMessage.customUsername,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            },
                            onClick = {
                                showEditMessageDialog = true
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.username),
                            trailingIcon = {
                                Text(
                                    text = uiState.uneditableMessage.username ?: "",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.student_id),
                            trailingIcon = {
                                Text(
                                    text = uiState.uneditableMessage.studentId ?: "",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.academic),
                            trailingIcon = {
                                Text(
                                    text = uiState.uneditableMessage.academic ?: "",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.phone),
                            trailingIcon = {
                                Text(
                                    text = uiState.uneditableMessage.phoneNumber.toString(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.email),
                            trailingIcon = {
                                Text(
                                    text = uiState.uneditableMessage.emailNumber ?: "",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                        )
                    }
                }
                item {
                    SettingItemCard(
                        label = "账号管理",
                        modifier = Modifier
                    ) {
                        PreferenceItem(
                            title = "统一认证登录",
                            trailingIcon = {
                                Text(
                                    text = stringResource(id = loginStateString(1)),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            },
                            onClick = {
                                // navController.navigate(Destinations.Login.route)
                            }
                        )
                        PreferenceItem(
                            title = "河南师大智慧教务",
                            trailingIcon = {
                                Text(
                                    text = stringResource(id = loginStateString(2)),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            },
                            onClick = {
                                // navController.navigate(Destinations.Login.route)
                            }
                        )
                        PreferenceItem(
                            title = "第二课堂管理系统",
                            trailingIcon = {
                                Text(
                                    text = stringResource(id = loginStateString(0)),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            },
                            onClick = {
                                // navController.navigate(Destinations.Login.route)
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SettingItemCard(
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
                /*item {
                    Spacer(modifier = Modifier.height(60.dp))
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Column {
                                Spacer(modifier = Modifier.height(50.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .padding(top = 70.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Column {
                                            PersonalSingleMessage(
                                                "个人信息",
                                                Modifier.padding(horizontal = 16.dp),
                                                R.drawable.ic_outline_person
                                            )
                                            HorizontalDivider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 30.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                PersonalMessage(
                                                    label = "用户名",
                                                    content = if (uiState.editableMessage.customUsername == "") "-"
                                                    else uiState.editableMessage.customUsername
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalMessage(
                                                    "姓名",
                                                    uiState.uneditableMessage.username
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalMessage(
                                                    "学号",
                                                    uiState.uneditableMessage.studentId
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalMessage(
                                                    "学院",
                                                    uiState.uneditableMessage.academic
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalMessage(
                                                    "邮箱",
                                                    uiState.uneditableMessage.emailNumber
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(20.dp))
                                            PersonalSingleMessage(
                                                "登录状态",
                                                Modifier.padding(horizontal = 16.dp),
                                                R.drawable.admin_panel_settings_24px
                                            )
                                            HorizontalDivider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 30.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                PersonalStateMessage("统一认证登录", 1)
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalStateMessage("河南师大智慧教务", 2)
                                                Spacer(modifier = Modifier.height(10.dp))
                                                PersonalStateMessage("第二课堂登录系统", 0)
                                            }
                                            Spacer(modifier = Modifier.height(20.dp))
                                        }

                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        TextButton(onClick = { /*TODO*/ }) {
                                            Text(text = "信息有误？")
                                        }
                                        TextButton(onClick = { /*TODO*/ }) {
                                            Text(text = "修改密码")
                                        }
                                    }
                                    TextButton(onClick = logoutClick) {
                                        Text(text = "退出登录")
                                    }
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.avator_1),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                                    .align(Alignment.TopCenter),
                            )
                        }

                    }
                }*/
            }
        }
    }

    EditMessageDialog(
        showDialog = showEditMessageDialog,
        onDismissRequests = { showEditMessageDialog = false },
        viewModel = viewModel
    )

    LogoutDialog(
        showDialog = showLogoutDialog,
        onDismissRequests = { showLogoutDialog = false },
        onConfirmClick = {
            logoutClick()
            showLogoutDialog = false
        }
    )
}

fun loginStateString(state: Int): Int {
    return when (state) {
        0 -> R.string.no_login
        1 -> R.string.logged
        2 -> R.string.login_expired
        else -> R.string.unknown_status
    }
}


@Composable
fun PersonalMessage(label: String, content: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Gray,
            modifier = Modifier
                .weight(0.65f)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = content,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.55f)
        )
    }
}

@Composable
fun PersonalSingleMessage(title: String, modifier: Modifier, icon: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = title,
            fontWeight = FontWeight.W700,
            color = MaterialTheme.colorScheme.primary
        )
    }
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