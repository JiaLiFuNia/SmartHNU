package com.smart.htu.screens.application.airCondition

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.BasicBottomSheet
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.PreferenceSubtitle
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.chart.ColumnChart
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.MiuixPopupUtils.Companion.dismissDialog
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirCondition(
    themeMode: Int,
    navController: NavController,
    viewModel: AirConditionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val openBottomSheet = remember { mutableStateOf(false) }
    val isShowSuggestChip = remember {
        mutableStateOf((uiState.roomCode.isEmpty() || uiState.buildingCode.isEmpty() || !uiState.isCookieValid))
    }

    val tabItem = listOf("用电情况", "缴费情况")
    val pagerState = rememberPagerState { tabItem.size }
    val (selectTabIndex, onSelectTabIndex) = remember { mutableIntStateOf(0) }

    LaunchedEffect(pagerState.currentPage) {
        onSelectTabIndex(pagerState.currentPage)
    }

    LaunchedEffect(selectTabIndex) {
        pagerState.animateScrollToPage(selectTabIndex)
    }
    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.getAirConditionConfig()
                viewModel.getBillDetailService()
                viewModel.getBillRecords()
                viewModel.getBuyRecords()
            }
        }
    }

    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surface
                    },
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
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
                            scope.launch {
                                openBottomSheet.value = true
                            }
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
            top.yukonga.miuix.kmp.basic.LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Log.e("TAG666 airLog", isShowSuggestChip.value.toString())
                    SuggestChip(
                        onClick = {
                            openBottomSheet.value = true
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
                                        ?: if (uiState.isLoadingBillRecords) "加载中..." else "加载失败").replace(
                                        "河南师范大学",
                                        ""
                                    )
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = uiState.billData?.data?.surplusList?.first()?.roomStatus
                                        ?: "正常用电"
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
                            onSelectTabIndex(it)
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
                                        AirConditionChart(
                                            xData = uiState.billRecords?.rows?.map {
                                                it.dateTimeDouble
                                            } ?: listOf("0.0"),
                                            yData = uiState.billRecords?.rows?.map {
                                                it.used.toDouble()
                                            } ?: listOf(0.0)
                                        )
                                        // Spacer(modifier = Modifier.height(4.dp))
                                        uiState.billRecords?.rows?.forEach {
                                            SingleMessage(it.datetime, "${it.used} 度")
                                            // Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }

                                    1 -> {
                                        AirConditionChart(
                                            xData = uiState.buyRecords?.rows?.map {
                                                it.dateTimeDouble
                                            } ?: listOf("0.0"),
                                            yData = uiState.buyRecords?.rows?.map {
                                                it.money.toDouble()
                                            } ?: listOf(0.0)
                                        )
                                        uiState.buyRecords?.rows?.forEach {
                                            SingleMessage(it.dateTime, "${it.money} 元")
                                            // Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        SetCookieBottomSheet(
            isBottomSheetShow = openBottomSheet,
            uiState = uiState,
            viewModel = viewModel,
            onConfirmClick = {
                scope.launch {
                    viewModel.saveUserCookie()
                    onRefresh()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetCookieBottomSheet(
    isBottomSheetShow: MutableState<Boolean>,
    uiState: AirConditionUiState,
    viewModel: AirConditionViewModel,
    onConfirmClick: () -> Unit
) {
    var buildingId by remember { mutableStateOf(uiState.buildingCode) }
    var roomId by remember { mutableStateOf(uiState.roomCode) }
    BasicBottomSheet(
        title = "设置",
        showDialog = isBottomSheetShow,
        onConfirmClick = {
            dismissDialog(isBottomSheetShow)
            viewModel.saveBuildingAndRoomId(buildingId, roomId)
            onConfirmClick()
        }
    ) {
        SmallTitle(
            text = "宿舍楼和房间",
            insideMargin = PaddingValues(start = 12.dp, top = 12.dp, bottom = 8.dp)
        )
        val buildingIdPattern = Regex("^[西|东]\\d{2}$")
        val roomIdPattern = Regex("^\\d{4}$")
        val (buildingIdError, onBuildingError) = remember { mutableStateOf(false) }
        val (roomIdError, onRoomError) = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = buildingId,
                label = "宿舍楼",
                onValueChange = {
                    buildingId = it
                    onBuildingError(!buildingIdPattern.matches(it))
                },
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (buildingIdError)
                        Icon(
                            painter = painterResource(id = R.drawable.warning_24px),
                            contentDescription = "warning",
                            tint = MaterialTheme.colorScheme.error
                        )
                }
            )
            TextField(
                value = roomId,
                label = "房间",
                onValueChange = {
                    roomId = it
                    onRoomError(!roomIdPattern.matches(it) || roomId.length != 4)
                },
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (roomIdError)
                        Icon(
                            painter = painterResource(id = R.drawable.warning_24px),
                            contentDescription = "warning",
                            tint = MaterialTheme.colorScheme.error
                        )
                }
            )
        }
        SmallTitle(
            text = "Cookie",
            insideMargin = PaddingValues(start = 12.dp, top = 16.dp, bottom = 8.dp)
        )
        val dropdownOptions = listOf("云端", "自定义")
        Card(
            color = MiuixTheme.colorScheme.secondaryContainer,
        ) {
            SuperDropdown(
                title = "Cookie 来源",
                summary = "云端 Cookie 统一由开发者提供，自定义 Cookie 由用户自行抓包获取",
                items = dropdownOptions,
                selectedIndex = uiState.setCookieType,
                onSelectedIndexChange = { newOption ->
                    viewModel.changeCookieType(
                        newOption
                    )
                }
            )
        }

        AnimatedVisibility(visible = uiState.setCookieType == 1) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    label = "shiroJID",
                    value = uiState.userLoginCookie?.shiroJID ?: "",
                    onValueChange = { viewModel.changeUserShiroJid(it) }
                )
                TextField(
                    label = "ymId",
                    value = uiState.userLoginCookie?.ymId ?: "",
                    onValueChange = { viewModel.changeUserYmld(it) }
                )
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
                xData = xData,
                yData = yData
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