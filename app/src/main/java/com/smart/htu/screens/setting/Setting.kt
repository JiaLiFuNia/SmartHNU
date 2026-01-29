package com.smart.htu.screens.setting

import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
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
import androidx.navigation.NavController
import com.smart.htu.BuildConfig
import com.smart.htu.R
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.component.InfoBadge
import com.smart.htu.screens.CaptchaUpdateDialog
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.ui.theme.KeyColors
import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.APPVersion.getVersionName
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
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel,
    loginViewModel: LoginViewModel,
    contentPadding: PaddingValues
) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                horizontalPadding = 16.dp,
                title = stringResource(id = R.string.setting),
                largeTitle = stringResource(id = R.string.setting),
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
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
        popupHost = {},
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
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 12.dp
            )
        ) {
            item {
                Card {
                    SuperArrow(
                        title = "账号与信息",
                        summary = if (loginUiState.jwcLoginState != 1) "暂未登录，点击登录" else "个人信息、登录状态、退出登录等",
                        onClick = {
                            if (loginUiState.jwcLoginState != 1) navController.navigate(
                                Destinations.Login.route
                            ) else {
                                navController.navigate(Destinations.Person.route)
                            }
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = "通用",
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = "YunAI 配置",
                        summary = "使用 AI 模型为应用注入新活力",
                        onClick = {
                            navController.navigate(Destinations.AIConfiguration.route)
                        }
                    )
                    SuperSwitch(
                        checked = !uiState.loadImgEnabled,
                        title = "无图模式",
                        summary = "关闭加载文章和列表图片，节省储存和流量",
                        onCheckedChange = {
                            viewModel.changeLoadImgEnabled(!it)
                        }
                    )
                    SuperArrow(
                        title = "主页内容",
                        summary = "选择和关闭需要在主页显示的内容",
                        onClick = {
                            navController.navigate(Destinations.HomeContentSettings.route)
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
                    SuperDropdown(
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
                        SuperDropdown(
                            title = "Key Color",
                            items = keyColorOptions,
                            selectedIndex = uiState.keyColorSeedIndex,
                            onSelectedIndexChange = { viewModel.changKeyColorSeedIndex(it) }
                        )
                    }
                    SuperSwitch(
                        checked = uiState.blurEnabled,
                        title = "实时模糊",
                        summary = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异",
                        onCheckedChange = {
                            viewModel.changeBlurEnabled(it)
                        }
                    )
                    SuperArrow(
                        title = "字体设置",
                        summary = "调整新闻正文字体样式及大小",
                        onClick = {
                            navController.navigate(Destinations.ArticleStyle.route)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.application),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = stringResource(id = R.string.about_app),
                        summary = stringResource(id = R.string.about_app_description),
                        onClick = {
                            navController.navigate(Destinations.About.route)
                        }
                    )
                    BasicComponent(
                        title = stringResource(id = R.string.check_update),
                        summary = "当前应用版本：${getVersionName()}(${getVersionCode()})\n编译时间：${BuildConfig.BUILD_TIME}",
                        endActions = {
                            if (uiState.isUpdate) {
                                InfoBadge(text = "新版本", color = MaterialTheme.colorScheme.error)
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
                    SuperArrow(
                        title = stringResource(R.string.feedback),
                        summary = "反馈问题或提出使用建议",
                        onClick = {
                            navController.navigate(Destinations.Feedback.route)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.other),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = stringResource(R.string.clear_cache),
                        onClick = {
                            viewModel.clearCache()
                        },
                        endActions = {
                            Text(
                                text = uiState.cacheSize,
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    )
                    BasicComponent(
                        title = "验证码识别模型",
                        summary = "当前版本 ${uiState.captchaLocalInfo.versionName}(${uiState.captchaLocalInfo.versionCode})",
                        endActions = {
                            if (uiState.isCaptchaUpdate && !captchaModelDownloadDir.exists() && !captchaModelFileDir.exists()) {
                                InfoBadge(text = "新版本", color = MaterialTheme.colorScheme.error)
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
    UpdateDialog(
        showDialog = showUpdateDialog,
        updateInfo = uiState.updateInfo?.data ?: return,
        targetDirectory = Environment.DIRECTORY_DOWNLOADS
    )
    CaptchaUpdateDialog(
        showDialog = showCaptchaUpdateDialog,
        updateInfo = uiState.updateInfo?.captchaModelVersion ?: return
    )
}