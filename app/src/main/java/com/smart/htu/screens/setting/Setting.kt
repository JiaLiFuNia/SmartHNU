package com.smart.htu.screens.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.SettingItemCard
import com.smart.htu.screens.main.entity.DarkMode
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.APPVersion
import com.smart.htu.utils.Term
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import top.yukonga.miuix.kmp.basic.SwitchDefaults
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    themeMode: Int,
    navController: NavController,
    viewModel: SettingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surface
                    },
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
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
        }
    ) {
        top.yukonga.miuix.kmp.basic.LazyColumn(
            modifier = Modifier
                .hazeSource(state = hazeState)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 16.dp
            )
        ) {
            item {
                SettingItemCard(
                    label = "开发",
                    themeMode = themeMode,
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
                            startWebUrl("https://github.com/JiaLiFuNia")
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.participate),
                        onClick = {
                            startWebUrl("https://github.com/JiaLiFuNia")
                        }
                    )
                }
            }
            item {
                SettingItemCard(
                    label = "教务",
                    modifier = Modifier,
                    themeMode = themeMode
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
                    modifier = Modifier,
                    themeMode = themeMode
                ) {
                    val themeModes = mapOf(
                        0 to "简洁",
                        1 to "动态",
                        2 to "师大"
                    )
                    SuperDropdown(
                        title = stringResource(id = R.string.theme_color),
                        summary = stringResource(id = R.string.theme_color_description),
                        items = themeModes.values.toList(),
                        selectedIndex = uiState.themeMode,
                        mode = DropDownMode.AlwaysOnRight,
                        onSelectedIndexChange = { mode ->
                            viewModel.changeDynamicTheme(mode)
                            if (mode == 0) viewModel.changeBlurState(false)
                        },
                    )
                    SuperSwitch(
                        title = "实时模糊",
                        checked = uiState.blurEffect,
                        summary = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异",
                        onCheckedChange = { value ->
                            viewModel.changeBlurState(value)
                        },
                        enabled = uiState.themeMode != 0,
                        switchColors = SwitchDefaults.switchColors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
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
                    label = stringResource(id = R.string.screen_style),
                    modifier = Modifier,
                    themeMode = themeMode
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
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.about),
                    modifier = Modifier,
                    themeMode = themeMode
                ) {
                    SuperArrow(
                        title = stringResource(id = R.string.about_app),
                        summary = stringResource(id = R.string.about_app_description),
                        onClick = {
                            navController.navigate(Destinations.About.route)
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.check_update),
                        summary = "当前版本 ${APPVersion.getVersionName()}(${APPVersion.getVersionCode()})",
                        onClick = {
                        }
                    )
                    SuperArrow(
                        title = stringResource(id = R.string.appreciate),
                        summary = stringResource(id = R.string.appreciate_description),
                        onClick = {
                            navController.navigate(Destinations.Appreciate.route)
                        }
                    )
                }
            }
        }
    }
}


fun DarkMode.toStringResourceId(): String {
    return when (this) {
        DarkMode.SYSTEM -> "跟随系统"
        DarkMode.ON -> "开启"
        DarkMode.OFF -> "关闭"
    }
}
