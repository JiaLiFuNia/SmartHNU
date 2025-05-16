package com.smart.htu.screens.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.InfoBadge
import com.smart.htu.screens.UpdateDialog
import com.smart.htu.screens.main.entity.DarkMode
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.APPVersion.getVersionName
import com.smart.htu.utils.Constants.Companion.GITHUB_PERSON_URL
import com.smart.htu.utils.Constants.Companion.GITHUB_PROJECT_URL
import com.smart.htu.utils.Constants.Companion.SMH_URL
import com.smart.htu.utils.Term
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val showUpdateDialog = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
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
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
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
                .hazeSource(state = hazeState)
                .fillMaxSize()
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
        ) {
            item {
                SettingItemCard(
                    label = "开发",
                    modifier = Modifier
                ) {
                    SuperArrow(
                        leftAction = {
                            Box(
                                contentAlignment = Alignment.TopStart,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.developer_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                            }
                        },
                        title = stringResource(id = R.string.developer_name),
                        summary = stringResource(id = R.string.developer_description),
                        onClick = {
                            startWebUrl(GITHUB_PERSON_URL)
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.participate),
                        onClick = {
                            startWebUrl(GITHUB_PROJECT_URL)
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = "教务",
                    modifier = Modifier
                ) {
                    val termString = Term.termConverter(uiState.termCode).split("-")
                    SuperArrow(
                        title = "学期",
                        summary = "当前学期 ${termString[0]}-${termString[1]} 学年第 ${termString[2]} 学期",
                        onClick = {
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.display_color),
                    modifier = Modifier
                ) {
                    val themeModes = mapOf(
                        0 to "动态",
                        1 to "师大"
                    )
                    SuperDropdown(
                        title = stringResource(id = R.string.theme_color),
                        summary = stringResource(id = R.string.theme_color_description),
                        items = themeModes.values.toList(),
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
            /*item {
                SettingItemCard(
                    label = stringResource(id = R.string.screen_style),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = stringResource(id = R.string.main_screen),
                        onClick = {
                            navController.navigate(Destinations.MainSetting.route)
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.application_screen),
                        onClick = {
                            navController.navigate(Destinations.AppSetting.route)
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.news_screen),
                        onClick = {
                            navController.navigate(Destinations.NewsSetting.route)
                        }
                    )
                }
            }*/
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.application),
                    modifier = Modifier
                ) {
                    SuperArrow(
                        title = stringResource(id = R.string.about_app),
                        summary = stringResource(id = R.string.about_app_description),
                        onClick = {
                            navController.navigateToWebView(
                                url = SMH_URL,
                                label = "关于师韵"
                            )
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
                    SuperArrow(
                        title = stringResource(id = R.string.open_source_license),
                        onClick = {
                            navController.navigate(Destinations.License.route)
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
        onConfirmClick = {},
        onDismissRequest = {},
        updateData = uiState.updateInfo
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
            shape = SmoothRoundedCornerShape(top.yukonga.miuix.kmp.basic.ButtonDefaults.CornerRadius),
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
                    shape = SmoothRoundedCornerShape(8.dp)
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