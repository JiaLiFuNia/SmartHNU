package com.smart.htu.screens.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.smart.htu.R
import com.smart.htu.component.DropdownListItem
import com.smart.htu.component.SelectionItem
import com.smart.htu.component.SettingItemCard
import com.smart.htu.screens.main.entity.DarkMode
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.APPVersion
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer
                ),
                title = { Text(text = stringResource(id = R.string.setting)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
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
        LazyColumn(
            modifier = Modifier
                .hazeSource(state = hazeState)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 15.dp,
                end = 15.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 15.dp
            )
        ) {
            item {
                SettingItemCard(
                    label = "开发与贡献",
                    modifier = Modifier
                ) {
                    Column {
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.developer_name)) },
                            subtitle = {
                                Text(text = stringResource(id = R.string.developer_description))
                            },
                            icon = {
                                Image(
                                    painter = painterResource(id = R.drawable.avator_1),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                startWebUrl("https://github.com/JiaLiFuNia")
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.participate)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_auto_awesome_24),
                                    contentDescription = null
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {},
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
                /*PreferencesHintCard(
                    title = if (loginUiState.isLogSuccess)
                        loginUiState.username
                        else stringResource(id = R.string.login_now),
                    description = stringResource(id = R.string.person_description),
                    icon = if (loginUiState.isLogSuccess) painterResource(id = R.drawable.avator_1) else R.drawable.outline_account_circle_24,
                    onClick = {
                        navController.navigate(Destinations.Login.route)
                    }
                )*/
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.display_color),
                    modifier = Modifier
                ) {
                    Column {
                        SettingsSwitch(
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_color_lens_24),
                                    contentDescription = null
                                )
                            },
                            title = { Text(text = stringResource(id = R.string.theme_color)) },
                            subtitle = { Text(text = stringResource(id = R.string.theme_color_description)) },
                            state = uiState.dynamicColor,
                            onCheckedChange = {
                                viewModel.changeDynamicTheme(enabled = !uiState.dynamicColor)
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsSwitch(
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.blur_on_24px),
                                    contentDescription = null
                                )
                            },
                            title = { Text(text = "实时模糊") },
                            subtitle = { Text(text = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异") },
                            state = uiState.blurEffect,
                            onCheckedChange = {
                                viewModel.changeBlurState()
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        DropdownListItem(
                            leadingImageVector = R.drawable.outline_nightlight_24,
                            headlineText = stringResource(id = R.string.dark_theme),
                            value = DarkMode.entries[uiState.isDarkTheme],
                            selections = DarkMode.entries
                                .map { SelectionItem(it.toStringResourceId(), it) },
                            onValueChanged = { index, _ ->
                                viewModel.changDarkMode(index)
                            },
                            trailingImageVector = R.drawable.outline_unfold_more_24
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
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.screen_style),
                    modifier = Modifier
                ) {
                    Column {
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.main_screen)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_home_24),
                                    contentDescription = null
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                navController.navigate(Destinations.MainSetting.route)
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.application_screen)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.widgets_24px_outline),
                                    contentDescription = null
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                navController.navigate(Destinations.AppSetting.route)
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.news_screen)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_outline_article),
                                    contentDescription = null
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                navController.navigate(Destinations.NewsSetting.route)
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.about),
                    modifier = Modifier
                ) {
                    Column {
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.about_app)) },
                            subtitle = { Text(text = stringResource(id = R.string.about_app_description)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_outline_article),
                                    contentDescription = null
                                )
                            },
                            action = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = { },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.check_update)) },
                            subtitle = { Text(text = stringResource(id = R.string.check_update_description)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Refresh,
                                    contentDescription = null
                                )
                            },
                            onClick = { },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = "版本信息") },
                            subtitle = { Text(text = "当前为最新版本 ${APPVersion.getVersionName()} | 第 ${APPVersion.getVersionCode()} 次更新") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null
                                )
                            },
                            onClick = { },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        SettingsMenuLink(
                            title = { Text(text = stringResource(id = R.string.appreciate)) },
                            subtitle = { Text(text = stringResource(id = R.string.appreciate_description)) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_auto_awesome_24),
                                    contentDescription = null
                                )
                            },
                            onClick = { navController.navigate(Destinations.Appreciate.route) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
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
