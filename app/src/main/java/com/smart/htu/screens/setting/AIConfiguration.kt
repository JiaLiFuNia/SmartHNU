package com.smart.htu.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.component.TextButtonWithProgressIndicator
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AIConfigurationScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val hazeState = rememberHazeState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = MiuixTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = "YunAI 配置",
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        top.yukonga.miuix.kmp.basic.Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onBackground
                        )
                    }
                },
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = uiState.blurEnabled
                    }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(
            WindowInsetsSides.Horizontal
        )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .hazeSource(hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical()
                .scrollEndHaptic(),
            overscrollEffect = null,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card {
                    SuperSwitch(
                        title = "YunAI",
                        summary = "启用 YunAI, 为应用注入新活力",
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
                            BasicComponent(
                                title = "新闻总结",
                                summary = "使用 YunAI 对新闻进行总结，快速获取新闻要点"
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
                            text = "配置 API",
                            insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                        )
                        TextField(
                            label = "URL",
                            value = "https://api.siliconflow.cn/",
                            backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                            readOnly = true,
                            singleLine = true,
                            onValueChange = {
                                // viewModel.changeAIModel(url = it)
                            }
                        )
                        Card {
                            SuperDropdown(
                                title = "模型",
                                items = AI_MODEL_LIST.map { it.name },
                                selectedIndex = uiState.selectedAIModelIndex,
                                onSelectedIndexChange = {
                                    viewModel.selectAIModel(it)
                                }
                            )
                        }
                        TextField(
                            label = "Key",
                            value = uiState.aiModelKey,
                            backgroundColor = MiuixTheme.colorScheme.surfaceContainer,
                            singleLine = true,
                            onValueChange = {
                                viewModel.saveAIModelKey(key = it, test = false)
                            },
                            trailingIcon = {
                                top.yukonga.miuix.kmp.basic.IconButton(
                                    onClick = { },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(Icons.Outlined.Info, contentDescription = null)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextButtonWithProgressIndicator(
                            text = "测试",
                            onClick = {
                                scope.launch {
                                    focusManager.clearFocus()
                                    viewModel.testAIService(
                                        onResult = {
                                            scope.launch {
                                                snackBarHostState.showSnackbar(it)
                                            }
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            isLoading = uiState.isTestLoading
                        )
                    }
                }
            }
        }
    }
}