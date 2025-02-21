package com.smart.htu.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.LoginDialog
import com.smart.htu.component.animation.SlideTransition
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.Main
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.navigation.BottomNavigationItem
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.person.PersonScreen
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.utils.DoubleBackToExitApp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainFrame(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    settingViewModel: SettingViewModel,
    newsViewModel: NewsViewModel,
    applicationViewModel: ApplicationViewModel
) {
    val context = LocalContext.current
    val savableStateHolder = rememberSaveableStateHolder()
    val (selectedItemIndex, onSelectedItemIndex) = rememberSaveable { mutableIntStateOf(0) }
    val loginUiState = loginViewModel.uiState.collectAsState().value
    val mainUiState = mainViewModel.uiState.collectAsState().value
    val settingUiState = settingViewModel.uiState.collectAsState().value
    val navigationItem = listOf(
        BottomNavigationItem(
            title = R.string.main,
            selectedIcon = R.drawable.baseline_home_24,
            unselectedIcon = R.drawable.outline_home_24,
            badge = (mainUiState.config?.notice?.filter {
                !mainUiState.hadReadIdList.contains(it.id)
            }?.size ?: 0) + if (settingUiState.updateState) 1 else 0
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
            enabled = loginUiState.isLogSuccess,
            title = R.string.my,
            selectedIcon = R.drawable.ic_filled_person,
            unselectedIcon = R.drawable.ic_outline_person,
            badge = 0
        )
    )

    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationSuiteScaffold(
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                AnimatedContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    label = "page",
                    targetState = selectedItemIndex,
                    transitionSpec = {
                        SlideTransition.slideLeft.enterTransition()
                            .togetherWith(SlideTransition.slideLeft.exitTransition())
                    },
                ) { page ->
                    savableStateHolder.SaveableStateProvider(
                        key = page,
                        content = {
                            when (page) {
                                0 -> Main(
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    navigateToApplication = {
                                        onSelectedItemIndex(1)
                                    },
                                    loginViewModel = loginViewModel,
                                    applicationViewModel = applicationViewModel
                                )

                                1 -> Application(
                                    navController = navController,
                                    viewModel = applicationViewModel,
                                    loginViewModel = loginViewModel
                                )

                                2 -> NewsScreen(
                                    navController = navController,
                                    viewModel = newsViewModel
                                )

                                3 -> PersonScreen(
                                    navController = navController,
                                    viewModel = loginViewModel
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    DoubleBackToExitApp(
        onExit = {
            (context as? Activity)?.finish()
        }
    )

    var showLoginDialog by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = loginUiState.isLogSuccess, key2 = loginUiState.isGuest) {
        showLoginDialog = !(loginUiState.isLogSuccess || loginUiState.isGuest)
    }
    LoginDialog(
        showDialog = showLoginDialog,
        onDismissRequests = {
            loginViewModel.guest()
            showLoginDialog = true
        },
        onConfirmClick = {
            showLoginDialog = false
            navController.navigate(Destinations.Login.route)
        }
    )
}