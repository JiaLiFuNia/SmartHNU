package com.smart.htu.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.screens.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.LazyColumn
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManage(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val (editable, onEditable) = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        scope.launch {
            delay(500)
        }
    }

    ScaffoldWithHazeLazyColumn(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        isMediumTopAppBar = true,
        blurEnabledState = uiState.blurEffect,
        title = { Text(text = stringResource(R.string.account_manage)) },
        actions = { },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
            }
        },
        refreshState = rememberPullToRefreshState(),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                SuggestChip(
                    onClick = { },
                    onActionClick = { onEditable(true) },
                    text = "请不要将此页面信息泄露给他人",
                    type = SuggestChipType.ERROR,
                    icon = Icons.Outlined.Info,
                    visibility = remember { mutableStateOf(true) }
                )
            }
            item {
                LargeCardDisplay(
                    modifier = Modifier,
                    title = "河南师大智慧教务",
                    containerColor = MiuixTheme.colorScheme.surface,
                    leadingIconPainting = R.drawable.circle_admin
                ) {
                    TextField(
                        value = uiState.token,
                        onValueChange = {},
                        label = "token",
                        enabled = editable,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            item {
                LargeCardDisplay(
                    modifier = Modifier,
                    title = "统一认证登录",
                    containerColor = MiuixTheme.colorScheme.surface,
                    leadingIconPainting = R.drawable.circle_admin
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.cookies.forEach {
                            TextField(
                                value = it.value,
                                onValueChange = {},
                                label = it.name,
                                enabled = editable,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}