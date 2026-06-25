package com.smart.htu.screens

import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.smart.htu.App
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.FloatingBottomBar
import com.smart.htu.component.FloatingBottomBarItem
import com.smart.htu.component.rememberBlurBackdrop
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.math.abs

val LocalMainPagerState = staticCompositionLocalOf<MainPagerState> { error("No pager state") }

@Composable
fun MainFrame(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    settingViewModel: SettingViewModel,
    messageViewModel: MessageViewModel,
    airConditionViewModel: AirConditionViewModel
) {
    val mainUiState by mainViewModel.uiState.collectAsState()
    val settingUiState by settingViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val blurBackdrop = rememberBlurBackdrop(settingUiState.blurEnabled)
    val blurActive = blurBackdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    val backdrop = rememberLayerBackdrop {
        drawRect(barColor)
        drawContent()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    val permissions =
        Permission.NOTIFICATION_PERMISSION + Permission.ACTIVITY_RECOGNITION_PERMISSION
    LaunchedEffect(Unit) {
        if (!Permission.hasPermissions(App.context, permissions)) {
            permissionLauncher.launch(permissions)
        }
    }

    val navigationItems = listOf(
        NavigationItem(
            label = "主页",
            icon = Icons.Filled.Home
        ),
        NavigationItem(
            label = "应用",
            icon = Icons.Filled.Widgets
        ),
        NavigationItem(
            label = "新闻",
            icon = Icons.AutoMirrored.Filled.Article
        ),
        NavigationItem(
            label = "设置",
            icon = Icons.Filled.Settings
        )
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { navigationItems.size })
    val mainPagerState = rememberMainPagerState(pagerState)

    val currentPage = mainPagerState.pagerState.currentPage
    LaunchedEffect(currentPage) {
        mainPagerState.syncPage()
    }

    MainScreenBackHandler(mainPagerState, LocalNavigator.current)

    CompositionLocalProvider(
        LocalMainPagerState provides mainPagerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (!settingUiState.enableFloatingBottomBar) {
                        BlurredBar(
                            backdrop = blurBackdrop,
                            blurEnabled = settingUiState.blurEnabled
                        ) {
                            NavigationBar(
                                color = barColor,
                                content = {
                                    navigationItems.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = pagerState.currentPage == index,
                                            onClick = { mainPagerState.animateToPage(index) },
                                            icon = item.icon,
                                            label = item.label
                                        )
                                    }
                                }
                            )
                        }
                    } else {
                        FloatingBottomBar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {},
                                )
                                .padding(
                                    bottom = 12.dp + WindowInsets.navigationBars.asPaddingValues()
                                        .calculateBottomPadding()
                                ),
                            selectedIndex = { mainPagerState.selectedPage },
                            onSelected = { mainPagerState.animateToPage(it) },
                            backdrop = backdrop,
                            tabsCount = navigationItems.size,
                            isBlurEnabled = settingUiState.enableFloatingBottomBarBlur,
                        ) {
                            navigationItems.forEachIndexed { index, item ->
                                FloatingBottomBarItem(
                                    onClick = { mainPagerState.animateToPage(index) },
                                    modifier = Modifier.defaultMinSize(minWidth = 76.dp)
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = MiuixTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = item.label,
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp,
                                        color = MiuixTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Visible
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) {
            Box(modifier = if (blurBackdrop != null) Modifier.layerBackdrop(blurBackdrop) else Modifier) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .then(
                            if (settingUiState.enableFloatingBottomBar && settingUiState.enableFloatingBottomBarBlur)
                                Modifier.layerBackdrop(backdrop) else Modifier
                        ),
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
                                onClickToNewsScreen = {
                                    mainPagerState.animateToPage(2)
                                },
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
            }
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
    mainState: MainPagerState,
    navController: Navigator
) {
    val isPagerBackHandlerEnabled by remember {
        derivedStateOf {
            navController.backStackSize() == 1 && mainState.selectedPage != 0
        }
    }

    val navEventState = rememberNavigationEventState(NavigationEventInfo.None)

    NavigationBackHandler(
        state = navEventState,
        isBackEnabled = isPagerBackHandlerEnabled,
        onBackCompleted = {
            mainState.animateToPage(0)
        }
    )
}

class MainPagerState(
    val pagerState: PagerState,
    private val coroutineScope: CoroutineScope
) {
    var selectedPage by mutableIntStateOf(pagerState.currentPage)
        private set

    var isNavigating by mutableStateOf(false)
        private set

    private var navJob: Job? = null

    fun animateToPage(targetIndex: Int) {
        if (targetIndex == selectedPage) return

        navJob?.cancel()

        selectedPage = targetIndex
        isNavigating = true

        val distance = abs(targetIndex - pagerState.currentPage).coerceAtLeast(2)
        val duration = 100 * distance + 100
        val layoutInfo = pagerState.layoutInfo
        val pageSize = layoutInfo.pageSize + layoutInfo.pageSpacing
        val currentDistanceInPages =
            targetIndex - pagerState.currentPage - pagerState.currentPageOffsetFraction
        val scrollPixels = currentDistanceInPages * pageSize

        navJob = coroutineScope.launch {
            val myJob = coroutineContext.job
            try {
                pagerState.animateScrollBy(
                    value = scrollPixels,
                    animationSpec = tween(easing = EaseInOut, durationMillis = duration)
                )
            } finally {
                if (navJob == myJob) {
                    isNavigating = false
                    if (pagerState.currentPage != targetIndex) {
                        selectedPage = pagerState.currentPage
                    }
                }
            }
        }
    }

    fun syncPage() {
        if (!isNavigating && selectedPage != pagerState.currentPage) {
            selectedPage = pagerState.currentPage
        }
    }
}

@Composable
fun rememberMainPagerState(
    pagerState: PagerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainPagerState {
    return remember(pagerState, coroutineScope) {
        MainPagerState(pagerState, coroutineScope)
    }
}