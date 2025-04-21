package com.smart.htu.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManage(
    themeMode: Int,
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val (editable, onEditable) = remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        scope.launch {
            delay(1500)
        }
    }

    ScaffoldWithHazeLazyColumn(
        themeMode = themeMode,
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
        refreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState(),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                val visibility = remember { mutableStateOf(true) }
                SuggestChip(
                    onClick = { /*TODO*/ },
                    onActionClick = { onEditable(false) },
                    text = "请不要将此页面信息泄露给他人",
                    type = SuggestChipType.ERROR,
                    icon = Icons.Outlined.Info,
                    visibility = visibility
                )
            }
            item {
                LargeCardDisplay(
                    themeMode = themeMode,
                    modifier = Modifier,
                    title = "统一认证登录",
                    leadingIconPainting = R.drawable.circle_admin
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.cookies.forEach {
                            OutlinedTextField(
                                shape = RoundedCornerShape(10.dp),
                                value = it.value,
                                label = {
                                    Text(
                                        text = it.name
                                    )
                                },
                                readOnly = editable,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            item {
                LargeCardDisplay(
                    themeMode = themeMode,
                    modifier = Modifier,
                    title = "河南师大智慧教务",
                    leadingIconPainting = R.drawable.circle_admin
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(10.dp),
                        value = uiState.token,
                        label = {
                            Text(
                                text = "token"
                            )
                        },
                        readOnly = editable,
                        onValueChange = { viewModel.setJWCLogToken(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}