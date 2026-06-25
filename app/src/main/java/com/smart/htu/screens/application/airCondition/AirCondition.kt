package com.smart.htu.screens.application.airCondition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.chart.BasicColumnChart
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlin.math.ceil

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

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.refreshConfig()
            isRefreshing = false
        }
    }

    val loadingState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (uiState.dormRoomId.isNotEmpty() && uiState.billRecords == null) {
            loadingState.value = true
            viewModel.getCurrentBillData()
            viewModel.getBillRecords()
            viewModel.getBuyRecords()
            loadingState.value = false
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = "空调电费",
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
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
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Settings,
                                contentDescription = "setting"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .let {
                    if (backdrop != null) it.layerBackdrop(backdrop) else it
                },
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .overScrollVertical()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .scrollEndHaptic(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isShowSuggestChip.value) {
                        item {
                            SuggestChip(
                                onClick = {
                                    navigator.push(Route.AirConditionSetting)
                                },
                                text = "请设置你的宿舍楼，房间号和 Cookie",
                                type = SuggestChipType.ERROR,
                                icon = Icons.Outlined.Info
                            )
                        }
                    }
                    item {
                        Card {
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
                                        painter = painterResource(R.drawable.water_ec_24px_filled),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp),
                                        tint = Color(ApplicationEntity.ApplicationColor.GREEN.color)
                                    )
                                }
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f)
                            ) {
                                BasicComponent(
                                    title = "${uiState.billRecords?.rows?.firstOrNull()?.used ?: "--"} 度",
                                    summary = "昨日用电"
                                )
                            }
                            Card(
                                modifier = Modifier.weight(1f)
                            ) {
                                BasicComponent(
                                    title = "${
                                        uiState.billRecords?.rows?.map { it.used.toDouble() }
                                            ?.take(7)
                                            ?.average()?.let { "%.2f".format(it) } ?: "--"
                                    } 度",
                                    summary = "近7天平均用电"
                                )
                            }
                        }
                    }
                    item {
                        Card {
                            Box {
                                val markerState = remember { mutableStateOf(false) }
                                if (!markerState.value) {
                                    BasicComponent(
                                        title = "近30天用电情况",
                                        endActions = {
                                            Row(
                                                modifier = Modifier
                                                    .clickable {
                                                        navigator.push(Route.AirConditionHistory)
                                                    }
                                                    .align(Alignment.CenterVertically),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "更多",
                                                    color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                                                    fontSize = MiuixTheme.textStyles.body2.fontSize
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(
                                                    imageVector = MiuixIcons.Basic.ArrowRight,
                                                    contentDescription = null,
                                                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions,
                                                )
                                            }
                                        },
                                        modifier = Modifier.zIndex(1f)
                                    )
                                }
                                val xData = uiState.billRecords?.rows?.map {
                                    it.easyDateTime
                                }
                                val yData = uiState.billRecords?.rows?.map {
                                    it.used.toDouble()
                                }
                                if (!xData.isNullOrEmpty() && !yData.isNullOrEmpty()) {
                                    val step =
                                        ceil((yData.maxOrNull() ?: 5.0) / 5.0).coerceAtLeast(1.0)
                                    val maxY = step * 5
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp)
                                            .padding(bottom = 12.dp)
                                            .padding(top = 28.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BasicColumnChart(
                                            xData = remember(xData) { mutableStateOf(xData) },
                                            yData = remember(yData) { mutableStateOf(yData) },
                                            verticalAxisItemPlacerStep = step,
                                            columnCollectionSpacing = 28.dp,
                                            maxY = maxY,
                                            isShowMaker = true,
                                            markerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(
                                                suffix = " 度"
                                            ),
                                            onMakerShow = {
                                                markerState.value = it
                                            }
                                        )
                                    }
                                } else {
                                    EmptyContent(
                                        text = "数据为空",
                                        modifier = Modifier
                                            .padding(top = 60.dp)
                                            .height(120.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Card {
                            BasicComponent(title = "缴费情况", summary = "新乡市电价为：0.56元/度")
                            val xData = uiState.buyRecords?.rows?.map {
                                it.dateTime
                            }
                            val yData = uiState.buyRecords?.rows?.map {
                                "${it.money.toDoubleOrNull()?.toInt() ?: 0} 元"
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            if (!xData.isNullOrEmpty() && !yData.isNullOrEmpty()) {
                                MessageCardDisplay(
                                    modifier = Modifier.fillMaxWidth(),
                                    message = xData.mapIndexed { index, it ->
                                        SingleInfo(
                                            label = it.toString(),
                                            content = yData[index],
                                            rowIndex = index / 2
                                        )
                                    },
                                    labelOnTop = false
                                )
                            } else {
                                EmptyContent(
                                    text = "数据为空",
                                    modifier = Modifier.height(120.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        BottomCircularProgressIndicator(loadingState.value)
    }
}