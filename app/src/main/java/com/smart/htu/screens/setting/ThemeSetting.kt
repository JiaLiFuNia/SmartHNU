package com.smart.htu.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.ui.theme.KeyColors
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.shader.isRenderEffectSupported
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun ThemeSetting(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()

    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    title = "主题设置",
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(
            WindowInsetsSides.Horizontal
        )
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .scrollEndHaptic(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        val themeModes = listOf(
                            stringResource(id = R.string.settings_theme_mode_system),
                            stringResource(id = R.string.settings_theme_mode_light),
                            stringResource(id = R.string.settings_theme_mode_dark),
                            stringResource(id = R.string.settings_theme_mode_monet_system),
                            stringResource(id = R.string.settings_theme_mode_monet_light),
                            stringResource(id = R.string.settings_theme_mode_monet_dark),
                        )
                        OverlayDropdownPreference(
                            title = "应用主题",
                            summary = "选择应用的主题模式",
                            items = themeModes,
                            selectedIndex = uiState.themeMode,
                            onSelectedIndexChange = { mode ->
                                viewModel.changeDynamicTheme(mode)
                            }
                        )
                        val keyColorOptions =
                            remember { listOf("Default") + KeyColors.map { it.first } }
                        AnimatedVisibility(visible = uiState.themeMode in listOf(3, 4, 5)) {
                            OverlayDropdownPreference(
                                title = "强调色",
                                summary = "在使用 Monet 主题时选择强调色",
                                items = keyColorOptions,
                                selectedIndex = uiState.keyColorSeedIndex,
                                onSelectedIndexChange = { viewModel.changKeyColorSeedIndex(it) }
                            )
                        }
                    }
                }
                item {
                    Card {
                        SwitchPreference(
                            checked = uiState.blurEnabled,
                            title = "模糊",
                            summary = "启用顶栏和底栏的模糊效果。具体效果因机型、系统而异",
                            onCheckedChange = {
                                viewModel.changeBlurEnabled(it)
                            },
                            enabled = isRenderEffectSupported()
                        )
                        SwitchPreference(
                            title = "悬浮底栏",
                            summary = "使用 Apple 风格的悬浮底栏",
                            checked = uiState.enableFloatingBottomBar,
                            onCheckedChange = {
                                viewModel.changeEnableFloatingBottomBar(it)
                            }
                        )
                        AnimatedVisibility(visible = uiState.enableFloatingBottomBar) {
                            SwitchPreference(
                                title = "液态玻璃",
                                summary = "启用悬浮底栏的液态玻璃效果",
                                checked = uiState.enableFloatingBottomBarBlur,
                                onCheckedChange = {
                                    viewModel.changeEnableFloatingBottomBarBlur(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}