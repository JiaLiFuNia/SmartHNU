package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.DropdownListItem
import com.smart.htu.component.PreferenceItem
import com.smart.htu.component.PreferenceSwitchWithDivider
import com.smart.htu.component.PreferencesHintCard
import com.smart.htu.component.SelectionItem
import com.smart.htu.component.SettingItemCard
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.entity.DarkMode
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.openInBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel,
    loginViewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = stringResource(id = R.string.setting)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            item {
                PreferencesHintCard(
                    title = if (loginUiState.isLogSuccess) loginUiState.editableMessage.customUsername
                        else stringResource(id = R.string.login_now),
                    description = stringResource(id = R.string.person_description),
                    icon = if (loginUiState.isLogSuccess) painterResource(id = R.drawable.avator_1) else R.drawable.outline_account_circle_24,
                    onClick = {
                        navController.navigate(Destinations.Login.route)
                    }
                )
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.display_color),
                    modifier = Modifier
                ) {
                    Column {
                        PreferenceSwitchWithDivider(
                            icon = R.drawable.outline_color_lens_24,
                            title = stringResource(id = R.string.theme_color),
                            description = stringResource(id = R.string.theme_color_description),
                            isChecked = uiState.dynamicColor,
                            onClick = {
                                navController.navigate(Destinations.DynamicColorSetting.route)
                            },
                            onChecked = {
                                viewModel.changeDynamicTheme(enabled = !uiState.dynamicColor)
                            }
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
                        PreferenceItem(
                            title = stringResource(id = R.string.main_screen),
                            icon = painterResource(id = R.drawable.outline_home_24),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                navController.navigate(Destinations.MainSetting.route)
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.application_screen),
                            icon = painterResource(id = R.drawable.widgets_24px_outline),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                navController.navigate(Destinations.AppSetting.route)
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.news_screen),
                            icon = painterResource(id = R.drawable.ic_outline_article),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
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
                        PreferenceItem(
                            title = stringResource(id = R.string.about_app),
                            description = stringResource(id = R.string.about_app_description),
                            icon = painterResource(id = R.drawable.ic_outline_article),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.check_update),
                            description = stringResource(id = R.string.check_update_description),
                            icon = Icons.Outlined.Refresh
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.current_version),
                            description = stringResource(id = R.string.version),
                            icon = Icons.Outlined.Info
                        )
                    }
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.other),
                    modifier = Modifier
                ) {
                    Column {
                        PreferenceItem(
                            title = stringResource(id = R.string.privacy),
                            icon = painterResource(id = R.drawable.book_ribbon_24px),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.user_agreement),
                            icon = painterResource(id = R.drawable.outline_gavel_24),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.appreciate),
                            description = stringResource(id = R.string.appreciate_description),
                            icon = painterResource(id = R.drawable.outline_auto_awesome_24),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            }
                        )
                    }
                }
            }
            item {
                SettingItemCard(
                    label = stringResource(id = R.string.developer),
                    modifier = Modifier
                ) {
                    Column {
                        PreferenceItem(
                            title = stringResource(id = R.string.developer_name),
                            description = stringResource(id = R.string.developer_description),
                            icon = R.drawable.developer_icon,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            },
                            onClick = {
                                openInBrowser("https://github.com/JiaLiFuNia")
                            }
                        )
                        PreferenceItem(
                            title = stringResource(id = R.string.participate),
                            description = "前往 GitHub 参与贡献",
                            icon = painterResource(id = R.drawable.outline_auto_awesome_24),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                )
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
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
