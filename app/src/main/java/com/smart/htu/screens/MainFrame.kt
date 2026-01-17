package com.smart.htu.screens

import android.app.Activity
import android.os.Environment
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.animation.SlideTransition
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.Main
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.BottomNavigationItem
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.screens.setting.SettingScreen
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.utils.DoubleBackToExitApp
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFrame(
    navController: NavController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    settingViewModel: SettingViewModel,
    messageViewModel: MessageViewModel,
    airConditionViewModel: AirConditionViewModel
) {
    val context = LocalContext.current
    val mainUiState by mainViewModel.uiState.collectAsState()
    val settingUiState by settingViewModel.uiState.collectAsState()
    val messageUiState by messageViewModel.uiState.collectAsState()
    val savableStateHolder = rememberSaveableStateHolder()
    val (selectedItemIndex, onSelectedItemIndex) = rememberSaveable { mutableIntStateOf(0) }
    val messageCount = remember {
        derivedStateOf { messageUiState.notReadNoticeIdCount }
    }
    val navigationItem = listOf(
        BottomNavigationItem(
            title = R.string.main,
            selectedIcon = R.drawable.baseline_home_24,
            unselectedIcon = R.drawable.outline_home_24,
            badge = messageCount.value + if (mainUiState.update.isNeedUpdate) 1 else 0
        ),
        BottomNavigationItem(
            title = R.string.application,
            selectedIcon = R.drawable.widgets_24px_filled,
            unselectedIcon = R.drawable.widgets_24px_outline,
            badge = 0
        ),
        BottomNavigationItem(
            title = R.string.news,
            selectedIcon = R.drawable.ic_filled_article,
            unselectedIcon = R.drawable.ic_outline_article,
            badge = 0
        ),
        BottomNavigationItem(
            title = R.string.setting,
            selectedIcon = R.drawable.baseline_settings_24,
            unselectedIcon = R.drawable.outline_settings_24,
            badge = if (settingUiState.isUpdate || settingUiState.isCaptchaUpdate) 1 else 0
        ),
        /*
        BottomNavigationItem(
            enabled = loginUiState.jwcLoginState == 1 || loginUiState.jwcLoginState == -2,
            title = R.string.my,
            selectedIcon = R.drawable.ic_filled_person,
            unselectedIcon = R.drawable.ic_outline_person,
            badge = 0
        )*/
    )

    val items = listOf(
        NavigationItem(
            label = "主页",
            icon = Icons.Outlined.Home
        ),
        NavigationItem(
            label = "应用",
            icon = Icons.Outlined.Widgets
        ),
        NavigationItem(
            label = "新闻",
            icon = Icons.AutoMirrored.Outlined.Article
        ),
        NavigationItem(
            label = "设置",
            icon = Icons.Outlined.Settings
        )
    )

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    top.yukonga.miuix.kmp.basic.Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
                ShortNavigationBar(
                    containerColor = MiuixTheme.colorScheme.surfaceContainer
                ) {
                    navigationItem.filter { it.enabled }.forEachIndexed { index, item ->
                        ShortNavigationBarItem(
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badge > 0) {
                                            Badge {
                                                Text(text = item.badge.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            }
                                        ),
                                        contentDescription = "icon"
                                    )
                                }
                            },
                            label = {
                                Text(text = stringResource(id = item.title))
                            },
                            selected = selectedItemIndex == index,
                            onClick = {
                                onSelectedItemIndex(index)
                            },
                            iconPosition = NavigationItemIconPosition.Start,
                            modifier = Modifier
                        )
                    }
                }
            else
                top.yukonga.miuix.kmp.basic.NavigationBar(
                    items = items,
                    selected = selectedItemIndex,
                    onClick = { onSelectedItemIndex(it) }
                )
        }
    ) {
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize(),
            label = "page",
            targetState = selectedItemIndex,
            transitionSpec = {
                SlideTransition.slideLeft.enterTransition()
                    .togetherWith(SlideTransition.slideLeft.exitTransition())
            }
        ) { page ->
            savableStateHolder.SaveableStateProvider(
                key = page,
                content = {
                    when (page) {
                        0 -> Main(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            loginViewModel = loginViewModel,
                            airConditionViewModel = airConditionViewModel,
                            messageViewModel = messageViewModel,
                            contentPadding = it
                        )

                        1 -> Application(
                            navController = navController,
                            loginViewModel = loginViewModel,
                            contentPadding = it
                        )

                        2 -> NewsScreen(
                            navController = navController,
                            contentPadding = it
                        )

                        3 -> SettingScreen(
                            navController = navController,
                            viewModel = settingViewModel,
                            loginViewModel = loginViewModel,
                            contentPadding = it
                        )
                    }
                }
            )
        }

        // val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val showUpdateDialog = rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(mainUiState.update.isNeedUpdate, mainUiState.isShowUpdateDialog) {
            showUpdateDialog.value =
                mainUiState.update.isNeedUpdate && mainUiState.isShowUpdateDialog.value
        }
        UpdateDialog(
            showDialog = showUpdateDialog,
            updateInfo = mainUiState.update,
            targetDirectory = Environment.DIRECTORY_DOWNLOADS
        )
    }

    DoubleBackToExitApp(
        context = context,
        onExit = {
            (context as? Activity)?.finish()
        }
    )
}


/*NavigationSuiteScaffold(
    navigationSuiteItems = {
        navigationItem.filter { it.enabled }.forEachIndexed { index, bottomNavigationItem ->
            item(
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (index == selectedItemIndex) {
                                bottomNavigationItem.selectedIcon
                            } else
                                bottomNavigationItem.unselectedIcon
                        ),
                        contentDescription = "icon"
                    )
                },
                label = {
                    Text(text = stringResource(id = bottomNavigationItem.title))
                },
                selected = selectedItemIndex == index,
                onClick = {
                    onSelectedItemIndex(index)
                },
                badge = {
                    if (bottomNavigationItem.badge > 0) {
                        Badge { Text(text = bottomNavigationItem.badge.toString()) }
                    }
                },
                modifier = Modifier
            )
        }
    },
    layoutType = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            currentWindowAdaptiveInfo()
        )
    }
) {}*/