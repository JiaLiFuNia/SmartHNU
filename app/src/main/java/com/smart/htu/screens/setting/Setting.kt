package com.smart.htu.screens.setting

import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.component.InfoBadge
import com.smart.htu.screens.CaptchaUpdateDialog
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.setting.entity.DarkMode
import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.APPVersion.getVersionName
import com.smart.htu.utils.FileUtil.moveFile
import com.smart.htu.utils.ToastUtil.showSnackbar
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val showUpdateDialog = remember { mutableStateOf(false) }
    val showCaptchaUpdateDialog = remember { mutableStateOf(false) }

    val captchaModelDownloadDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
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
            viewModel.setLocalCaptchaVersion(
                uiState.updateInfo?.captchaModelVersion ?: CaptchaVersionEntity()
            )
            showToast(context, "验证码识别模型已更新")
        }
    }

    Column {
        MediumTopAppBar(
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (uiState.blurEnabled) Color.Transparent else MiuixTheme.colorScheme.background,
                scrolledContainerColor = if (uiState.blurEnabled) Color.Transparent else MiuixTheme.colorScheme.background
            ),
            title = { Text(text = stringResource(id = R.string.setting)) },
            modifier = Modifier.hazeEffect(
                state = hazeState,
                style = HazeMaterials.thick()
            ) {
                blurRadius = 30.dp
                blurEnabled = uiState.blurEnabled
            }
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .hazeSource(state = hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .overScrollVertical()
                .padding(bottom = contentPadding.calculateBottomPadding()),
            overscrollEffect = null,
            contentPadding = PaddingValues(16.dp, 12.dp),
        ) {
            item {
                SettingItemCard(
                    label = "账号",
                    titlePaddingValues = PaddingValues(start = 12.dp, bottom = 8.dp, top = 8.dp),
                ) {
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
                    SuperSwitch(
                        checked = true,
                        title = "预测式返回",
                        summary = "通过预测用户操作，提升页面响应速度（可能增加流量消耗）",
                        onCheckedChange = {

                        },
                        enabled = false
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.display),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = "聚焦",
                        summary = "设置首页聚焦内容",
                        onClick = {
                            showToast(context, "开发中...")
                        },
                        enabled = false
                    )
                    SuperArrow(
                        title = "新闻正文样式",
                        summary = "调整新闻正文字体样式及大小",
                        onClick = {
                            navController.navigate(Destinations.ArticleStyle.route)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.theme),
                    modifier = Modifier
                ) {
                    val themeModes = listOf("动态", "师大青")
                    SuperDropdown(
                        title = stringResource(id = R.string.theme_color),
                        summary = stringResource(id = R.string.theme_color_description),
                        items = themeModes,
                        selectedIndex = uiState.themeMode,
                        onSelectedIndexChange = { mode ->
                            viewModel.changeDynamicTheme(mode)
                        },
                    )
                    /*SuperSwitch(
                        title = "实时模糊",
                        checked = uiState.blurEffect,
                        summary = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异",
                        onCheckedChange = { value ->
                            viewModel.changeBlurState(value)
                        },
                        switchColors = SwitchDefaults.switchColors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )*/
                    SuperDropdown(
                        title = stringResource(id = R.string.dark_theme),
                        summary = "切换应用深色模式",
                        items = DarkMode.entries.map { item -> item.toStringResourceId() },
                        selectedIndex = uiState.isDarkTheme,
                        onSelectedIndexChange = { index ->
                            viewModel.changDarkMode(index)
                        }
                    )
                    /*DropdownListItem(
                        leadingImageVector = R.drawable.outline_language_24,
                        headlineText = stringResource(id = R.string.language),
                        value = uiState.languageList[uiState.selectedLanguageIndex].value,
                        selections = uiState.languageList,
                        onValueChanged = { index, _ ->
                            viewModel.changeLanguage(index, context)
                        },
                        trailingImageVector = R.drawable.outline_unfold_more_24
                    )*/
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
                        rightActions = {
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
                                                showSnackbar(snackBarHostState, "当前已是最新版本")
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
                        rightText = uiState.cacheSize
                    )
                    BasicComponent(
                        title = "验证码识别模型",
                        summary = "当前版本 ${uiState.captchaLocalInfo.versionName}(${uiState.captchaLocalInfo.versionCode})",
                        rightActions = {
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
                                                    snackBarHostState.showSnackbar("当前已是最新版本")
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


fun DarkMode.toStringResourceId(): String {
    return when (this) {
        DarkMode.SYSTEM -> "跟随系统"
        DarkMode.ON -> "开启"
        DarkMode.OFF -> "关闭"
    }
}