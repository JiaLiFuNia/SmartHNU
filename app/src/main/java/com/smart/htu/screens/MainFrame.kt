package com.smart.htu.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.R
import com.smart.htu.component.animation.SlideTransition
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.Main
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.navigation.BottomNavigationItem
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.utils.DoubleBackToExitApp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainFrame(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    applicationViewModel: ApplicationViewModel
) {
    val context = LocalContext.current
    val savableStateHolder = rememberSaveableStateHolder()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navigationItem = listOf(
        BottomNavigationItem(
            title = R.string.main,
            selectedIcon = R.drawable.baseline_home_24,
            unselectedIcon = R.drawable.outline_home_24
        ),
        BottomNavigationItem(
            title = R.string.application,
            selectedIcon = R.drawable.widgets_24px_filled,
            unselectedIcon = R.drawable.widgets_24px_outline
        ),
        BottomNavigationItem(
            title = R.string.news,
            selectedIcon = R.drawable.ic_filled_article,
            unselectedIcon = R.drawable.ic_outline_article,
            badge = true
        )
    )

    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                navigationItem.forEachIndexed { index, bottomNavigationItem ->
                    item(
                        icon = {
                            Icon(
                                painterResource(
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
                            selectedItemIndex = index
                        },
                        badge = {
                            if (bottomNavigationItem.badge == true) {
                                Badge()
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
                                    loginViewModel = loginViewModel,
                                    navigateToApplication = {
                                        selectedItemIndex = 1
                                    },
                                    applicationViewModel = applicationViewModel
                                )

                                1 -> Application(
                                    navController = navController,
                                    viewModel = applicationViewModel,
                                    mainViewModel = mainViewModel
                                )

                                2 -> NewsScreen(navController = navController)
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
}