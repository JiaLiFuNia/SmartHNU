package com.smart.htu.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.screens.login.TextWithProgressIndicatorButton
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AIConfigurationScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = "AI 功能") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(16.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card {
                    SuperSwitch(
                        title = "AI 功能",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                            viewModel.changeAiFunctionEnabled(it)
                        }
                    )
                }
            }
            item {
                AnimatedVisibility(
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    visible = uiState.aiFunctionEnabled
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SmallTitle(
                            text = "功能",
                            insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                        )
                        Card {
                            SuperArrow(
                                title = "新闻总结",
                                summary = "使用 AI 对新闻进行总结，快速获取新闻要点",
                            )
                        }
                    }
                }
            }
            item {
                AnimatedVisibility(
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    visible = uiState.aiFunctionEnabled
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SmallTitle(
                            text = "配置 API (*目前仅支持预置的 API)",
                            insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                        )
                        TextField(
                            label = "URL",
                            value = uiState.aiModuleConfig.url,
                            readOnly = true,
                            singleLine = true,
                            onValueChange = {
                                viewModel.setAIModuleConfig(url = it)
                            }
                        )
                        TextField(
                            label = "模型",
                            value = uiState.aiModuleConfig.module,
                            readOnly = true,
                            singleLine = true,
                            onValueChange = {
                                viewModel.setAIModuleConfig(module = it)
                            }
                        )
                        TextField(
                            label = "Key",
                            value = uiState.aiModuleConfig.key,
                            singleLine = true,
                            onValueChange = {
                                viewModel.setAIModuleConfig(key = it)
                            }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { }) {
                                Text(text = "如何申请 Key?")
                            }
                        }
                        TextWithProgressIndicatorButton(
                            text = "测试",
                            onClick = {
                                viewModel.testAIService(
                                    onResponse = {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(it)
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !uiState.isTestLoading
                        )
                    }
                }
            }
        }
    }
}