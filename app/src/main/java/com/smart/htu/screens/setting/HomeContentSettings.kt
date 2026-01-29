package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun HomeContentSettings(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val hazeState = rememberHazeState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val context = LocalContext.current

    Scaffold(
        containerColor = MiuixTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = "主页内容",
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
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
                        title = "聚焦信息",
                        summary = "展示周次、电费、学时、借书数目等信息",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                        }
                    )
                }
            }
            item {
                Card {
                    SuperSwitch(
                        title = "今日课程",
                        summary = "当天的所有课程和考试项目",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                        }
                    )
                }
            }
            item {
                Card {
                    SuperSwitch(
                        title = "今日任务",
                        summary = "通过手动创建的任务",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                        }
                    )
                }
            }
            item {
                Card {
                    SuperSwitch(
                        title = "空闲教室",
                        summary = "快速查看偏好教学楼的占用情况",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                        }
                    )
                }
            }
            item {
                Card {
                    SuperSwitch(
                        title = "今日新闻",
                        summary = "集合了校园近两日的部分新闻",
                        checked = uiState.aiFunctionEnabled,
                        onCheckedChange = {
                        }
                    )
                }
            }
        }
    }
}