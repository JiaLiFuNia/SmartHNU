package com.smart.htu.screens.application.airCondition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.TabRow
import com.smart.htu.component.card.BeautifulCardDisplay
import com.smart.htu.component.chart.ColumnChart
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.G2RoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirCondition(
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val isShowSuggestChip by remember {
        derivedStateOf { mutableStateOf(uiState.roomCode.isEmpty() || uiState.buildingCode.isEmpty() || !uiState.isCookieValid) }
    }
    val snackBarHostState = viewModel.snackBarHostState

    val tabItem = listOf("用电情况", "缴费情况")
    val pagerState = rememberPagerState { tabItem.size }
    val selectTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.refreshConfig()
            isRefreshing = false
        }
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
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
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding() + 8.dp)
                    .fillMaxSize()
            ) {
                if (isShowSuggestChip.value) {
                    SuggestChip(
                        onClick = {
                            navController.navigate(Destinations.AirConditionSetting.route)
                        },
                        onActionClick = {
                        },
                        text = "请设置你的宿舍楼，房间号和 Cookie",
                        type = SuggestChipType.ERROR,
                        icon = Icons.Outlined.Info,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                BeautifulCardDisplay(
                    icon = R.drawable.climate_mini_split_24px,
                    title = "寝室电费",
                    description = "河南师范大学寝室空调",
                    summary = "${uiState.billData?.data?.soc ?: 0.0} 度",
                    summaryDesc = "剩余电费",
                    rightBottomText = (uiState.billData?.data?.displayRoomName
                        ?: "加载中...").replace("河南师范大学", ""),
                    rightBottomEndText = uiState.billData?.data?.surplusList?.first()?.roomStatus
                        ?: "未知状态",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    tabs = tabItem,
                    selectedTabIndex = selectTabIndex,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
// 西09
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 12.dp,
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                ) { page ->
                    BillColumn(
                        page = page,
                        xData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                it.dateTimeDouble
                            } ?: listOf("0.0")

                            else -> uiState.buyRecords?.rows?.map {
                                it.dateTimeDouble
                            } ?: listOf("0.0")
                        },
                        yData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                it.used.toDouble()
                            } ?: listOf(0.0)

                            else -> uiState.buyRecords?.rows?.map {
                                it.money.toDouble()
                            } ?: listOf(0.0)
                        },
                        labelData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                it.datetime
                            } ?: listOf("无数据")

                            else -> uiState.buyRecords?.rows?.map {
                                it.dateTime
                            } ?: listOf("无数据")
                        },
                        contentData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                "${it.used} 度"
                            } ?: listOf("无数据")

                            else -> uiState.buyRecords?.rows?.map {
                                "${it.money} 元"
                            } ?: listOf("无数据")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PagerScope.BillColumn(
    page: Int,
    xData: List<String>,
    yData: List<Double>,
    labelData: List<String>,
    contentData: List<String>
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .overScrollVertical(),
        overscrollEffect = null
    ) {
        item {
            AirConditionChart(
                page = page,
                xData = xData,
                yData = yData
            )
        }
        itemsIndexed(labelData) { index, label ->
            SingleMessage(label, contentData[index])
        }
    }
}

@Composable
fun AirConditionChart(
    page: Int,
    xData: List<String>,
    yData: List<Double>,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            ColumnChart(
                xData = remember(xData) { mutableStateOf(xData) },
                yData = remember(yData) { mutableStateOf(yData) },
                verticalAxisItemPlacerStep = if (page == 0) 1.0 else 15.0,
                columnCollectionSpacing = 28.dp
            )
        }
    }
}

@Composable
fun SingleMessage(
    label: String,
    content: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(CardDefaults.CornerRadius),
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