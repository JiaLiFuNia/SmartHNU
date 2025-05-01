package com.smart.htu.screens.application.airCondition

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.chart.ColumnChart
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirCondition(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val isShowSuggestChip by remember {
        derivedStateOf { mutableStateOf(uiState.roomCode.isEmpty() || uiState.buildingCode.isEmpty() || !uiState.isCookieValid) }
    }
    val snackBarHostState = viewModel.snackBarHostState

    val tabItem = listOf("用电情况", "缴费情况")
    val pagerState = rememberPagerState { tabItem.size }
    val selectTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.refreshConfig()
            }
        }
    }

    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
                ),
                title = { Text(text = "空调电费") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Destinations.AirConditionSetting.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "setting")
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        top.yukonga.miuix.kmp.basic.PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                item {
                    SuggestChip(
                        onClick = {
                            navController.navigate(Destinations.AirConditionSetting.route)
                        },
                        onActionClick = {
                        },
                        text = "请设置你的宿舍楼，房间号和 Cookie",
                        type = SuggestChipType.ERROR,
                        icon = Icons.Outlined.Info,
                        visibility = isShowSuggestChip,
                        modifier = Modifier
                    )
                    if (isShowSuggestChip.value)
                        Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    top.yukonga.miuix.kmp.basic.Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
                        color = MiuixTheme.colorScheme.primaryContainer,
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            leadingContent = {
                                Icon(
                                    painter = painterResource(id = R.drawable.bolt_24px),
                                    contentDescription = "bolt"
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = (uiState.billData?.data?.displayRoomName
                                        ?: if (uiState.isLoadingBillRecords) "加载中..." else "加载失败")
                                        .replace("河南师范大学", "")
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = uiState.billData?.data?.surplusList?.first()?.roomStatus
                                        ?: "未知状态"
                                )
                            },
                            trailingContent = {
                                Text(
                                    text = "${uiState.billData?.data?.soc ?: 0.0} 度",
                                    fontSize = 16.sp,
                                    color = MiuixTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    top.yukonga.miuix.kmp.basic.TabRow(
                        tabs = tabItem,
                        selectedTabIndex = selectTabIndex,
                        onTabSelected = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    if (uiState.isLoadingBillRecords) {
                        CircularProgressIndicator()
                    } else {
                        androidx.compose.foundation.pager.HorizontalPager(
                            modifier = Modifier
                                .windowInsetsPadding(
                                    WindowInsets.displayCutout.only(
                                        WindowInsetsSides.Horizontal
                                    )
                                )
                                .windowInsetsPadding(
                                    WindowInsets.navigationBars.only(
                                        WindowInsetsSides.Horizontal
                                    )
                                ),
                            pageSpacing = 12.dp,
                            state = pagerState,
                            beyondViewportPageCount = 1,
                            verticalAlignment = Alignment.Top,
                            userScrollEnabled = true,
                            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                        ) { page ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                when (page) {
                                    0 -> {
                                        val xData1 = uiState.billRecords?.rows?.map {
                                            it.dateTimeDouble
                                        } ?: listOf("0.0")
                                        val yData1 = uiState.billRecords?.rows?.map {
                                            it.used.toDouble()
                                        } ?: listOf(0.0)
                                        AirConditionChart(
                                            xData = xData1,
                                            yData = yData1
                                        )
                                        uiState.billRecords?.rows?.forEach {
                                            SingleMessage(it.datetime, "${it.used} 度")
                                        }
                                    }

                                    1 -> {
                                        val xData2 = uiState.buyRecords?.rows?.map {
                                            it.dateTimeDouble
                                        } ?: listOf("0.0")
                                        val yData2 = uiState.buyRecords?.rows?.map {
                                            it.money.toDouble()
                                        } ?: listOf(0.0)
                                        AirConditionChart(
                                            xData = xData2,
                                            yData = yData2
                                        )
                                        uiState.buyRecords?.rows?.forEach {
                                            SingleMessage(it.dateTime, "${it.money} 元")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AirConditionChart(
    xData: List<String>,
    yData: List<Double>,
) {
    top.yukonga.miuix.kmp.basic.Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ColumnChart(
                xData = remember(xData) { mutableStateOf(xData) },
                yData = remember(yData) { mutableStateOf(yData) }
            )
        }
    }
}

@Composable
fun SingleMessage(
    label: String,
    content: String
) {
    top.yukonga.miuix.kmp.basic.Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = { Text(text = label, style = MiuixTheme.textStyles.body1) },
            trailingContent = {
                Text(
                    text = content,
                    style = MiuixTheme.textStyles.subtitle
                )
            }
        )
    }
}