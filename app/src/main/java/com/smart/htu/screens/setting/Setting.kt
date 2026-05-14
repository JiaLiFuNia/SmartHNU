package com.smart.htu.screens.setting

import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
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
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.CaptchaUpdateDialog
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.ui.theme.KeyColors
import com.smart.htu.utils.FileUtil.moveFile
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.isRenderEffectSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    loginViewModel: LoginViewModel,
    contentPadding: PaddingValues
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val hazeState = rememberHazeState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val showUpdateDialog = remember { mutableStateOf(false) }
    val showCaptchaUpdateDialog = remember { mutableStateOf(false) }

    val captchaModelDownloadDir = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
        "captcha.traineddata"
    )
    val captchaModelFileDir = File(
        context.filesDir,
        "captcha.traineddata"
    )

    val onCaptchaUpdateClick: () -> Unit = {
        scope.launch {
            moveFile(
                sourceFile = captchaModelDownloadDir,
                targetDirectory = context.filesDir,
                targetFileName = "captcha.traineddata"
            )
                .onSuccess {
                    if (it) {
                        viewModel.setLocalCaptchaVersion(
                            uiState.updateInfo?.captchaModelVersion ?: CaptchaVersionEntity()
                        )
                        showToast(context, "验证码识别模型已更新")
                    } else {
                        showToast(context, "文件不存在")
                    }
                }
                .onFailure {
                    showToast(context, it.message.toString())
                }
        }
    }

    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    titlePadding = 16.dp,
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
                        SwitchPreference(
                            checked = uiState.blurEnabled,
                            title = "实时模糊",
                            summary = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异",
                            onCheckedChange = {
                                viewModel.changeBlurEnabled(it)
                            },
                            enabled = isRenderEffectSupported()
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
                                    InfoBadge(
                                        text = "新版本",
                                        color = MaterialTheme.colorScheme.error
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
                        BasicComponent(
                            title = "验证码识别模型",
                            summary = "当前版本 ${uiState.captchaLocalInfo.versionName}(${uiState.captchaLocalInfo.versionCode})",
                            endActions = {
                                if (uiState.isCaptchaUpdate && !captchaModelDownloadDir.exists() && !captchaModelFileDir.exists()) {
                                    InfoBadge(
                                        text = "新版本",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    if (captchaModelDownloadDir.exists() && !captchaModelFileDir.exists()) {
                                        TextButton(
                                            onClick = { onCaptchaUpdateClick() }
                                        ) {
                                            Text(
                                                text = "安装模型",
                                                color = MiuixTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = {
                                scope.launch {
                                    if (!captchaModelDownloadDir.exists()) {
                                        viewModel.getUpdate(
                                            onCaptchaModelUpdate = {
                                                scope.launch {
                                                    if (it) {
                                                        showCaptchaUpdateDialog.value = true
                                                    } else {
                                                        showToast(context, "当前已是最新版本")
                                                    }
                                                }
                                            }
                                        )
                                    } else {
                                        if (captchaModelFileDir.exists()) {
                                            showToast(context, "验证码识别模型已是最新版本")
                                        } else {
                                            onCaptchaUpdateClick()
                                        }
                                    }
                                }
                            },
                            enabled = false
                        )
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
    CaptchaUpdateDialog(
        showDialog = showCaptchaUpdateDialog.value,
        updateInfo = uiState.updateInfo?.captchaModelVersion ?: return,
        onDismissRequest = {
            showCaptchaUpdateDialog.value = false
        },
    )
}