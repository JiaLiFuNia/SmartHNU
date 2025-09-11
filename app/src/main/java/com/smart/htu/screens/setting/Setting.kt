package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.InfoBadge
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.setting.entity.DarkMode
import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.APPVersion.getVersionName
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.G2RoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = rememberHazeState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val showUpdateDialog = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEnabled) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEnabled) Color.Transparent else MiuixTheme.colorScheme.background
                ),
                title = { Text(text = stringResource(id = R.string.setting)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thick()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEnabled
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
                .hazeSource(state = hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                end = 12.dp,
                bottom = 16.dp
            )
        ) {
            item {
                SettingItemCard(
                    label = "通用",
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = "AI 功能",
                        summary = "使用大模型为应用注入新活力",
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
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(R.string.display),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = "自定义聚焦",
                        summary = "设置首页聚焦板块的内容",
                        onClick = {
                            showToast(context, "开发中...")
                        }
                    )
                    SuperArrow(
                        title = "文章样式",
                        summary = "调整文章字体以及字体大小",
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
                        mode = DropDownMode.AlwaysOnRight,
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
                        mode = DropDownMode.AlwaysOnRight,
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
                        summary = "当前版本 ${getVersionName()}(${getVersionCode()})",
                        rightActions = {
                            if (uiState.isUpdate) {
                                InfoBadge(text = "新版本", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        onClick = {
                            // showUpdateDialog.value = true
                            scope.launch {
                                val res = viewModel.getUpdate()
                                if (res) {
                                    showUpdateDialog.value = true
                                } else {
                                    snackBarHostState.showSnackbar("当前已是最新版本")
                                }
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
                }
            }
        }
    }
    UpdateDialog(
        showDialog = showUpdateDialog,
        isForceUpdate = uiState.updateInfo.isForceUpdate,
        onDismissRequest = {},
        updateEntity = uiState.updateInfo
    )
}


fun DarkMode.toStringResourceId(): String {
    return when (this) {
        DarkMode.SYSTEM -> "跟随系统"
        DarkMode.ON -> "开启"
        DarkMode.OFF -> "关闭"
    }
}


@Composable
fun UpdateCard(
    onClick: () -> Unit,
    uiState: SettingUiState
) {
    if (uiState.isUpdate || uiState.updateInfo.versionCode > 0) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = G2RoundedCornerShape(top.yukonga.miuix.kmp.basic.CardDefaults.CornerRadius),
            color = MiuixTheme.colorScheme.surface,
            onClick = {

            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicComponent(
                    title = "版本更新",
                    summary = "版本号：${getVersionCode()} -> ${uiState.updateInfo.versionCode}",
                    rightActions = {
                        if (uiState.isUpdate) {
                            InfoBadge(text = "新版本", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
                Text(
                    text = uiState.updateInfo.update?.content ?: "暂无更新内容",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                TextButton(
                    onClick = {
                        onClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = G2RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "立即更新",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}