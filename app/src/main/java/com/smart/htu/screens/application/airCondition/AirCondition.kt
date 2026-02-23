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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.chart.ColumnChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TabRowDefaults
import top.yukonga.miuix.kmp.basic.TabRowWithContour
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AirCondition(
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val isShowSuggestChip by remember {
        derivedStateOf { mutableStateOf(uiState.dormRoomId.isEmpty() || !uiState.isCookieValid) }
    }
    val snackBarHostState = viewModel.snackBarHostState
    val hazeState = rememberHazeState()

    val tabItem = listOf("用电情况", "缴费情况")
    val pagerState = rememberPagerState { tabItem.size }
    val selectTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.refreshConfig()
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = "空调电费",
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navigator.push(Route.AirConditionSetting)
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Settings,
                            contentDescription = "setting"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = true
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding() + 16.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
                    .hazeSource(hazeState)
            ) {
                if (isShowSuggestChip.value) {
                    SuggestChip(
                        onClick = {
                            navigator.push(Route.AirConditionSetting)
                        },
                        text = "请设置你的宿舍楼，房间号和 Cookie",
                        type = SuggestChipType.ERROR,
                        icon = Icons.Outlined.Info,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    BasicComponent(
                        title = (uiState.billData?.data?.displayRoomName
                            ?: "加载中...").replace("河南师范大学", ""),
                        summary = uiState.billData?.data?.surplusList?.first()?.roomStatus
                            ?: "未知状态",
                        endActions = {
                            Text(
                                text = "${uiState.billData?.data?.soc ?: 0.0} 度",
                                color = MiuixTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        startAction = {
                            Icon(
                                imageVector = Icons.Outlined.Bolt,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 12.dp),
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                TabRowWithContour(
                    tabs = tabItem,
                    selectedTabIndex = selectTabIndex,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    colors = TabRowDefaults.tabRowColors(backgroundColor = MiuixTheme.colorScheme.secondary),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                                it.easyDateTime
                            }

                            else -> uiState.buyRecords?.rows?.map {
                                it.easyDateTime
                            }
                        },
                        yData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                it.used.toDouble()
                            }

                            else -> uiState.buyRecords?.rows?.map {
                                it.money.toDouble()
                            }
                        },
                        labelData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                it.dateTime
                            }

                            else -> uiState.buyRecords?.rows?.map {
                                it.dateTime
                            }
                        },
                        contentData = when (page) {
                            0 -> uiState.billRecords?.rows?.map {
                                "${it.used} 度"
                            }

                            else -> uiState.buyRecords?.rows?.map {
                                "${it.money} 元"
                            }
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
    xData: List<String>?,
    yData: List<Double>?,
    labelData: List<LocalDate>?,
    contentData: List<String>?
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .overScrollVertical(),
        overscrollEffect = null
    ) {
        if (xData == null || yData == null || labelData == null || contentData == null) {
            item {
                EmptyContent(text = "暂无数据", image = emptyData())
            }
        } else {
            item {
                AirConditionChart(
                    page = page,
                    xData = xData,
                    yData = yData
                )
            }
            item {
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    message = labelData.mapIndexed { index, it ->
                        SingleInfo(
                            label = it.toString(),
                            content = contentData[index],
                            rowIndex = index / 2
                        )
                    },
                    labelOnTop = false
                )
            }
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
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surfaceContainer
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
    Card {
        BasicComponent(
            title = label,
            summary = content
        )
    }
    /*Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surfaceContainer
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
    }*/
}