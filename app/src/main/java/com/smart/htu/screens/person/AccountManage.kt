package com.smart.htu.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.setting.SettingItemCard
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManage(
    viewModel: LoginViewModel
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

    val (editable, onEditable) = remember { mutableStateOf(false) }
    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = "登录信息管理",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() }
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
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
                top = it.calculateTopPadding(),
                end = 16.dp,
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SuggestChip(
                    onClick = { onEditable(true) },
                    text = "请不要将此页面信息泄露给他人",
                    type = SuggestChipType.ERROR,
                    icon = Icons.Outlined.Info
                )
            }
            item {
                SettingItemCard(label = "河南师大智慧教务", modifier = Modifier) {
                    TextField(
                        backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
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
                uiState.cookies.groupBy { it.domain }.forEach { items ->
                    SettingItemCard(label = "Cookie-${items.key}", modifier = Modifier) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items.value.forEach { item ->
                                TextField(
                                    backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                                    value = item.value,
                                    onValueChange = {
                                        // viewModel.editCookie(item.domain, item.name, it)
                                    },
                                    label = item.name,
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