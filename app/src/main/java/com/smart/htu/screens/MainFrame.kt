package com.smart.htu.screens

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.animation.SlideTransition
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.Main
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.navigation.BottomNavigationItem
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.person.PersonScreen
import com.smart.htu.utils.DoubleBackToExitApp
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFrame(
    navController: NavController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    newsViewModel: NewsViewModel,
    airConditionViewModel: AirConditionViewModel
) {
    val context = LocalContext.current
    val loginUiState by loginViewModel.uiState.collectAsState()
    val mainUiState by mainViewModel.uiState.collectAsState()
    val savableStateHolder = rememberSaveableStateHolder()
    val scope = rememberCoroutineScope()
    val (selectedItemIndex, onSelectedItemIndex) = rememberSaveable { mutableIntStateOf(0) }
    val messageCount = remember {
        derivedStateOf { mainUiState.noticeIdList.size - mainUiState.readNoticeIdList.size }
    }
    val navigationItem = listOf(
        BottomNavigationItem(
            title = R.string.main,
            selectedIcon = R.drawable.baseline_home_24,
            unselectedIcon = R.drawable.outline_home_24,
            badge = messageCount.value + if (mainUiState.updateEntity.isNeedUpdate == true) 1 else 0
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
            enabled = loginUiState.isLogSuccess || loginUiState.loginJWCState == 1,
            title = R.string.my,
            selectedIcon = R.drawable.ic_filled_person,
            unselectedIcon = R.drawable.ic_outline_person,
            badge = 0
        )
    )

    val isShowPrivateMessage = remember { mutableStateOf(true) }

    top.yukonga.miuix.kmp.basic.Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            when (selectedItemIndex) {
                0 -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
                    title = { Text(text = "欢迎！${mainUiState.username}") },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    route = Destinations.Message.route
                                )
                            }
                        ) {
                            BadgedBox(
                                badge = {
                                    if (messageCount.value > 0)
                                        Badge(
                                            content = {
                                                Text(text = messageCount.value.toString())
                                            }
                                        )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Destinations.Setting.route)
                            }
                        ) {
                            BadgedBox(
                                badge = { if (mainUiState.updateEntity.isNeedUpdate == true) Badge() }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "setting"
                                )
                            }
                        }
                    }
                )

                1 -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
                    title = { Text(text = stringResource(R.string.application)) },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add"
                            )
                        }
                    }
                )

                2 -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
                    title = { Text(text = stringResource(R.string.news)) },
                    actions = {
                        IconButton(onClick = { navController.navigate(Destinations.NewsHistory.route) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_history_24),
                                contentDescription = "history"
                            )
                        }
                        IconButton(onClick = { navController.navigate(Destinations.NewsSearch.route) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search"
                            )
                        }
                    }
                )

                3 -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
                    title = { Text(text = stringResource(R.string.my)) },
                    actions = {
                        IconButton(
                            onClick = {
                                isShowPrivateMessage.value = !isShowPrivateMessage.value
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = if (isShowPrivateMessage.value) R.drawable.visibility_24px else R.drawable.visibility_off_24px),
                                contentDescription = "eye"
                            )
                        }
                        IconButton(onClick = { navController.navigate(Destinations.AccountManage.route) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.key_24px),
                                contentDescription = "key"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MiuixTheme.colorScheme.surfaceContainer
            ) {
                navigationItem.filter { it.enabled }.forEachIndexed { index, item ->
                    NavigationBarItem(
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
                        modifier = Modifier
                    )
                }
            }
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
                            contentPadding = it
                        )

                        1 -> Application(
                            navController = navController,
                            loginViewModel = loginViewModel,
                            contentPadding = it
                        )

                        2 -> NewsScreen(
                            navController = navController,
                            viewModel = newsViewModel,
                            contentPadding = it
                        )

                        3 -> PersonScreen(
                            navController = navController,
                            viewModel = loginViewModel,
                            contentPadding = it,
                            isShowPrivateMessage = isShowPrivateMessage
                        )
                    }
                }
            )
        }
    }

    // val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val showUpdateDialog = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(mainUiState.updateEntity.isNeedUpdate, mainUiState.isShowUpdateDialog) {
        if (mainUiState.updateEntity.isNeedUpdate == true && mainUiState.isShowUpdateDialog.value) {
            showUpdateDialog.value = true
        } else {
            showUpdateDialog.value = false
        }
    }
    UpdateDialog(
        showDialog = showUpdateDialog,
        onDismissRequest = {
            mainViewModel.changeUpdateDialogState(false)
        },
        isForceUpdate = mainUiState.updateEntity.isForceUpdate,
        updateEntity = mainUiState.updateEntity
    )

    DoubleBackToExitApp(
        onExit = {
            (context as? Activity)?.finish()
        }
    )

    val (showLoginDialog, onShowLoginDialog) = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = loginUiState.loginJWCState, key2 = loginUiState.isGuest) {
        onShowLoginDialog(!(loginUiState.loginJWCState == 1 || loginUiState.isGuest))
    }
    LoginDialog(
        showDialog = showLoginDialog,
        onDismissRequests = {
            loginViewModel.guest()
            onShowLoginDialog(true)
        },
        onConfirmClick = {
            onShowLoginDialog(false)
            navController.navigate(Destinations.Login.route)
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