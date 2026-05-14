package com.smart.htu.screens

import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.smart.htu.App
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.Main
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.Navigator
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.screens.setting.SettingScreen
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.utils.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold

val LocalMainPagerState = staticCompositionLocalOf<PagerState> { error("No pager state") }
val LocalHandlePageChange =
    staticCompositionLocalOf<(Int) -> Unit> { error("No handle page change") }

@Composable
fun MainFrame(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    settingViewModel: SettingViewModel,
    messageViewModel: MessageViewModel,
    airConditionViewModel: AirConditionViewModel
) {
    val mainUiState by mainViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    LaunchedEffect(Unit) {
        if (!Permission.hasPermissions(App.context, Permission.NOTIFICATION_PERMISSION)) {
            notificationPermissionLauncher.launch(Permission.NOTIFICATION_PERMISSION)
        }
    }

    val navigationItems = listOf(
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

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { navigationItems.size })
    val handlePageChange: (Int) -> Unit = remember(pagerState, scope) {
        { page ->
            scope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }

    MainScreenBackHandler(pagerState, LocalNavigator.current, scope)

    CompositionLocalProvider(
        LocalMainPagerState provides pagerState,
        LocalHandlePageChange provides handlePageChange,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = pagerState.currentPage == index,
                            onClick = { handlePageChange(index) },
                            icon = item.icon,
                            label = item.label
                        )
                    }
                }
            }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier,
                userScrollEnabled = false,
                verticalAlignment = Alignment.Top,
                beyondViewportPageCount = 1,
                overscrollEffect = null,
                pageContent = { page ->
                    when (page) {
                        0 -> Main(
                            mainViewModel = mainViewModel,
                            loginViewModel = loginViewModel,
                            airConditionViewModel = airConditionViewModel,
                            messageViewModel = messageViewModel,
                            contentPadding = it
                        )

                        1 -> Application(
                            loginViewModel = loginViewModel,
                            contentPadding = it
                        )

                        2 -> NewsScreen(
                            contentPadding = it
                        )

                        3 -> SettingScreen(
                            viewModel = settingViewModel,
                            loginViewModel = loginViewModel,
                            contentPadding = it
                        )
                    }
                }
            )
            val showUpdateDialog = rememberSaveable { mutableStateOf(false) }
            LaunchedEffect(mainUiState.update.isNeedUpdate, mainUiState.isShowUpdateDialog) {
                showUpdateDialog.value =
                    mainUiState.update.isNeedUpdate && mainUiState.isShowUpdateDialog.value
            }
            UpdateDialog(
                showDialog = showUpdateDialog.value,
                updateInfo = mainUiState.update,
                targetDirectory = Environment.DIRECTORY_DOWNLOADS,
                onDismissRequest = {
                    showUpdateDialog.value = false
                }
            )
        }
    }

}

@Composable
private fun MainScreenBackHandler(
    mainState: PagerState,
    navController: Navigator,
    scope: CoroutineScope
) {
    val isPagerBackHandlerEnabled by remember {
        derivedStateOf {
            navController.backStackSize() == 1 && mainState.currentPage != 0
        }
    }

    val navEventState = rememberNavigationEventState(NavigationEventInfo.None)

    NavigationBackHandler(
        state = navEventState,
        isBackEnabled = isPagerBackHandlerEnabled,
        onBackCompleted = {
            scope.launch {
                mainState.animateScrollToPage(0)
            }
        }
    )
}