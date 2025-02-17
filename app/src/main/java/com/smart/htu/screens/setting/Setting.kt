package com.smart.htu.screens.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.CommonListItem
import com.smart.htu.component.DropdownListItem
import com.smart.htu.component.SelectionItem
import com.smart.htu.component.SettingItemCard
import com.smart.htu.component.SwitchListItem
import com.smart.htu.screens.main.entity.DarkMode
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.APPVersion
import com.smart.htu.utils.Term
import com.smart.htu.utils.TermType
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
                colors = topAppBarColors(
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
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 16.dp
            )
        ) {
            item {
                SettingItemCard(
                    label = "开发",
                    modifier = Modifier
                ) {
                    Column {
                        CommonListItem(
                            headlineText = stringResource(id = R.string.developer_name),
                            supportingText = stringResource(id = R.string.developer_description),
                            leadingIcon = R.drawable.developer_icon,
                            onClick = {
                                startWebUrl("https://github.com/JiaLiFuNia")
                            }
                        )
                        CommonListItem(
                            headlineText = stringResource(id = R.string.participate),
                            leadingIcon = painterResource(id = R.drawable.github),
                            onClick = {

                            }
                        )
                    }
                }
            }
            item {
                SettingItemCard(
                    label = "教务",
                    modifier = Modifier
                ) {
                    val termString = Term.termConverter(uiState.termCode).split("-")
                    CommonListItem(
                        headlineText = "学期",
                        supportingText =
                        "当前学期 ${termString[0]}-${termString[1]} 学年第 ${termString[2]} 学期",
                        leadingIcon = painterResource(id = R.drawable.overview_24px),
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
                    Column {
                        SwitchListItem(
                            value = uiState.dynamicColor,
                            headlineText = stringResource(id = R.string.theme_color),
                            supportingText = stringResource(id = R.string.theme_color_description),
                            leadingIcon = painterResource(id = R.drawable.outline_color_lens_24),
                            onValueChanged = { value ->
                                viewModel.changeDynamicTheme(value)
                            }
                        )
                        SwitchListItem(
                            value = uiState.blurEffect,
                            headlineText = "实时模糊",
                            supportingText = "开启后部分页面将具有模糊效果，具体效果因机型、系统而异",
                            leadingIcon = painterResource(id = R.drawable.blur_on_24px),
                            onValueChanged = { value ->
                                viewModel.changeBlurState(value)
                            }
                        )
                        DropdownListItem(
                            value = DarkMode.entries[uiState.isDarkTheme],
                            headlineText = stringResource(id = R.string.dark_theme),
                            leadingIcon = painterResource(id = R.drawable.outline_nightlight_24),
                            selections = DarkMode.entries
                                .map { item -> SelectionItem(item.toStringResourceId(), item) },
                            onValueChanged = { index, _ ->
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
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.screen_style),
                    modifier = Modifier
                ) {
                    Column {
                        CommonListItem(
                            headlineText = stringResource(id = R.string.main_screen),
                            leadingIcon = painterResource(id = R.drawable.outline_home_24),
                            onClick = {
                                navController.navigate(Destinations.MainSetting.route)
                            }
                        )
                        CommonListItem(
                            headlineText = stringResource(id = R.string.application_screen),
                            leadingIcon = painterResource(id = R.drawable.widgets_24px_outline),
                            onClick = {
                                navController.navigate(Destinations.AppSetting.route)
                            }
                        )
                        CommonListItem(
                            headlineText = stringResource(id = R.string.news_screen),
                            leadingIcon = painterResource(id = R.drawable.ic_outline_article),
                            onClick = {
                                navController.navigate(Destinations.NewsSetting.route)
                            }
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
                        CommonListItem(
                            headlineText = stringResource(id = R.string.about_app),
                            supportingText = stringResource(id = R.string.about_app_description),
                            leadingIcon = painterResource(id = R.drawable.ic_outline_article),
                            onClick = {
                                navController.navigate(Destinations.About.route)
                            }
                        )
                        CommonListItem(
                            headlineText = stringResource(id = R.string.check_update),
                            supportingText = "当前版本 ${APPVersion.getVersionName()}(${APPVersion.getVersionCode()})",
                            leadingIcon = Icons.Outlined.Refresh,
                            onClick = {
                            }
                        )
                        CommonListItem(
                            headlineText = stringResource(id = R.string.appreciate),
                            supportingText = stringResource(id = R.string.appreciate_description),
                            leadingIcon = painterResource(id = R.drawable.outline_auto_awesome_24),
                            onClick = {
                                navController.navigate(Destinations.Appreciate.route)
                            }
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
