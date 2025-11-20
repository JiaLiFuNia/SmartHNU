package com.smart.htu.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.setting.SettingItemCard
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManage(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val (editable, onEditable) = remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = { Text(text = "登录信息管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SuggestChip(
                    onClick = { },
                    onActionClick = { onEditable(true) },
                    text = "请不要将此页面信息泄露给他人",
                    type = SuggestChipType.ERROR,
                    icon = Icons.Outlined.Info
                )
            }
            item {
                SettingItemCard(label = "河南师大智慧教务", modifier = Modifier) {
                    TextField(
                        backgroundColor = MiuixTheme.colorScheme.surface,
                        value = uiState.token,
                        onValueChange = {
                            viewModel.setJWCLogToken(it)
                        },
                        label = "token",
                        enabled = editable,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            item {
                uiState.cookies.groupBy { it.domain }.forEach {
                    SettingItemCard(label = "Cookie-${it.key}", modifier = Modifier) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            it.value.forEach {
                                TextField(
                                    backgroundColor = MiuixTheme.colorScheme.surface,
                                    value = it.value,
                                    onValueChange = {},
                                    label = it.name,
                                    enabled = false,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}