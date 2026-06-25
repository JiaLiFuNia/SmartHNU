package com.smart.htu.screens.setting

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.DeviceUtil.getSystem
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    loginViewModel: LoginViewModel,
    contentPadding: PaddingValues
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val showUpdateDialog = remember { mutableStateOf(false) }
    val showModifyStepDialog = remember { mutableStateOf(false) }

    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    title = stringResource(id = R.string.setting),
                    largeTitle = stringResource(id = R.string.setting),
                    scrollBehavior = scrollBehavior,
                    color = barColor
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
                    bottom = contentPadding.calculateBottomPadding() + 16.dp
                )
            ) {
                item {
                    Card {
                        ArrowPreference(
                            title = "账号与信息",
                            summary = if (loginUiState.jwcLoginState != 1) "暂未登录，点击登录" else "个人信息、登录状态、退出登录等",
                            onClick = {
                                navigator.pushWithLoginCheck(
                                    route = Route.Person,
                                    isGuest = false,
                                    loginState = loginUiState.jwcLoginState == 1
                                )
                            }
                        )
                    }
                }
                item {
                    SettingItemCard(
                        label = "通用",
                        modifier = Modifier
                    ) {
                        ArrowPreference(
                            title = "YunAI 配置",
                            summary = "使用 AI 模型为应用注入新活力",
                            onClick = {
                                navigator.push(Route.AIConfiguration)
                            }
                        )
                        SwitchPreference(
                            checked = !uiState.loadImgEnabled,
                            title = "无图模式",
                            summary = "关闭加载文章和列表图片，节省储存和流量",
                            onCheckedChange = {
                                viewModel.changeLoadImgEnabled(!it)
                            }
                        )
                        ArrowPreference(
                            title = "主页内容",
                            summary = "选择和关闭需要在主页显示的内容",
                            onClick = {
                                navigator.push(Route.HomeContentSettings)
                            }
                        )
                    }
                }
                item {
                    SettingItemCard(
                        label = stringResource(R.string.display),
                        modifier = Modifier
                    ) {
                        ArrowPreference(
                            title = "主题设置",
                            summary = "自定义更多主题选项",
                            onClick = {
                                navigator.push(Route.ThemeSetting)
                            }
                        )
                        ArrowPreference(
                            title = "字体设置",
                            summary = "调整新闻正文字体样式及大小",
                            onClick = {
                                navigator.push(Route.ArticleStyle)
                            }
                        )
                    }
                }
                item {
                    SettingItemCard(
                        label = stringResource(id = R.string.application),
                        modifier = Modifier
                    ) {
                        ArrowPreference(
                            title = stringResource(id = R.string.check_update),
                            endActions = {
                                if (uiState.isUpdate) {
                                    Text(
                                        text = "新版本",
                                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                                        color = MiuixTheme.colorScheme.error
                                    )
                                }
                            },
                            onClick = {
                                scope.launch {
                                    viewModel.getUpdate(
                                        onAppUpdate = {
                                            scope.launch {
                                                if (it) {
                                                    showUpdateDialog.value = true
                                                } else {
                                                    showToast(context, "当前已是最新版本")
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        )
                        ArrowPreference(
                            title = stringResource(id = R.string.about),
                            summary = stringResource(id = R.string.about_app_description),
                            onClick = {
                                navigator.push(Route.About)
                            }
                        )
                        ArrowPreference(
                            title = stringResource(R.string.feedback),
                            summary = "反馈问题或提出使用建议",
                            onClick = {
                                navigator.push(Route.Feedback)
                            }
                        )
                    }
                }
                item {
                    SettingItemCard(
                        label = stringResource(id = R.string.other),
                        modifier = Modifier
                    ) {
                        ArrowPreference(
                            title = stringResource(R.string.clear_cache),
                            onClick = {
                                viewModel.clearCache {
                                    showToast(context, it)
                                }
                            },
                            endActions = {
                                Text(
                                    text = uiState.cacheSize,
                                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                )
                            }
                        )
                        if (getSystem() == "Xiaomi") {
                            ArrowPreference(
                                title = stringResource(R.string.steps),
                                onClick = {
                                    showModifyStepDialog.value = true
                                },
                                endActions = {
                                    Text(
                                        text = uiState.steps.values.toList().sum().toString(),
                                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                                        color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    UpdateDialog(
        showDialog = showUpdateDialog.value,
        updateInfo = uiState.updateInfo?.data ?: return,
        targetDirectory = Environment.DIRECTORY_DOWNLOADS,
        onDismissRequest = {
            showUpdateDialog.value = false
        }
    )
    ModifyStepsDialog(
        show = showModifyStepDialog.value,
        onDismiss = {
            showModifyStepDialog.value = false
        },
        onConfirm = {
            scope.launch {
                viewModel.insertSteps(
                    newStep = it,
                    onResult = { showToast(context, it) }
                )
                showModifyStepDialog.value = false
            }
        }
    )
}

@Composable
fun ModifyStepsDialog(show: Boolean, onConfirm: (Int) -> Unit, onDismiss: () -> Unit) {
    OverlayDialog(
        show = show,
        title = "新增步数",
        summary = "支持小米/红米系列手机修改系统步数。需要 Root 权限或通过 Shizuku 授权。",
        onDismissRequest = { onDismiss() },
        onDismissFinished = { onDismiss() }
    ) {
        val newSteps = remember { mutableStateOf("") }
        TextField(
            value = newSteps.value,
            label = "增加的步数",
            onValueChange = {
                newSteps.value = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = "取消",
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "确定",
                onClick = {
                    onConfirm(newSteps.value.toIntOrNull() ?: 0)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}