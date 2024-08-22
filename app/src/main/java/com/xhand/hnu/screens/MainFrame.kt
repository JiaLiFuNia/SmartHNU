package com.xhand.hnu.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.xhand.hnu.BottomNavigationItem
import com.xhand.hnu.R
import com.xhand.hnu.components.SlideTransition
import com.xhand.hnu.components.UpdateDialog
import com.xhand.hnu.model.NetworkConnectionState
import com.xhand.hnu.model.rememberConnectivityState
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun MainFrame(
    viewModel: SettingsViewModel,
    navController: NavHostController,
    context: Context
) {
    val connectionState by rememberConnectivityState()

    val isConnected by remember(connectionState) {
        derivedStateOf { connectionState == NetworkConnectionState.Available }
    }
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = "我的",
            selectedIcon = R.drawable.ic_filled_person,
            unselectedIcon = R.drawable.ic_outline_person,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "新闻",
            selectedIcon = R.drawable.ic_filled_article,
            unselectedIcon = R.drawable.ic_outline_article,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "设置",
            selectedIcon = R.drawable.ic_filled_settings,
            unselectedIcon = R.drawable.ic_outline_settings,
            hasNews = viewModel.ifNeedUpdate
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    val savableStateHolder = rememberSaveableStateHolder()
    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                        item(
                            icon = {
                                Icon(
                                    painterResource(
                                        id = if (index == selectedItemIndex) {
                                            bottomNavigationItem.selectedIcon
                                        } else
                                            bottomNavigationItem.unselectedIcon
                                    ),
                                    contentDescription = bottomNavigationItem.title
                                )
                            },
                            label = {
                                Text(text = bottomNavigationItem.title)
                            },
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            badge = {
                                if (bottomNavigationItem.hasNews) {
                                    Badge()
                                }
                            }
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
                AnimatedContent(
                    modifier = Modifier.fillMaxSize(),
                    label = "home-content",
                    targetState = selectedItemIndex,
                    transitionSpec = {
                        SlideTransition.slideLeft.enterTransition()
                            .togetherWith(SlideTransition.slideLeft.exitTransition())
                    },
                ) { page ->
                    savableStateHolder.SaveableStateProvider(
                        key = page,
                        content = {
                            Box(modifier = Modifier) {
                                when (page) {
                                    0 -> PersonScreen(
                                        navController = navController,
                                        viewModel = viewModel,
                                        context = context
                                    )

                                    1 -> NewsScreen(
                                        navController = navController,
                                        context = context
                                    )

                                    2 -> SettingScreen(
                                        viewModel = viewModel,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    )
                }
            }
            UpdateDialog(viewModel = viewModel)
        }
    if (!isConnected) {
        Toast.makeText(context, "当前网络不佳，请重试", Toast.LENGTH_SHORT).show()
    }
}