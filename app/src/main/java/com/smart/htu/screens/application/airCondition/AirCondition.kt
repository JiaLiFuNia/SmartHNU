package com.smart.htu.screens.application.airCondition

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.component.BasicBottomSheet
import com.smart.htu.component.PreferenceSubtitle
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.chart.ColumnChart
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch

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
    val (openBottomSheet, onOpenBottomSheet) = remember { mutableStateOf(false) }
    val (selectTabIndex, onSelectTabIndex) = remember { mutableIntStateOf(0) }

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            viewModel.getBillDetailService()
            viewModel.getBillRecords()
            viewModel.getBuyRecords()
            isRefreshing = false
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
        containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
        scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer
        ),
                title = { Text(text = "寝室空调电费") },
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
                                onOpenBottomSheet(true)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
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
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = it.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = state
                )
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 8.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
            ) {
                item {
                    SuggestChip(
                        onClick = {
                            onOpenBottomSheet(true)
                        },
                        onActionClick = {
                        },
                        text = "请先设置你的宿舍楼和房间号",
                        type = SuggestChipType.ERROR,
                        visibility = remember {
                            derivedStateOf { uiState.roomCode == "" || uiState.buildingCode == "" }
                        }
                    )
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                                        ?: "").replace("河南师范大学", "")
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
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }
                item {
                    TabRow(
                        selectedTabIndex = selectTabIndex,
                        indicator = { tabPositions ->
                            TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectTabIndex]),
                                width = tabPositions[selectTabIndex].width / 2f,
                                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                            )
                        },
                        divider = {}
                    ) {
                        Tab(selected = selectTabIndex == 0, onClick = { onSelectTabIndex(0) }) {
                            Text(
                                text = "近 ${uiState.billRecords?.total ?: 0} 天用电情况",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Tab(selected = selectTabIndex == 1, onClick = { onSelectTabIndex(1) }) {
                            Text(
                                text = "缴费情况",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
                if (uiState.isLoadingBillRecords) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularWavyProgressIndicator(modifier = Modifier.size(36.dp))
                        }
                    }
                } else {
                    when (selectTabIndex) {
                        0 -> {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                    ) {
                                        ColumnChart(
                                            yData = uiState.billRecords?.rows?.map {
                                                it.used.toDouble()
                                            } ?: listOf(0.0)
                                        )
                                    }
                                }
                            }
                            items(uiState.billRecords?.rows ?: emptyList()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ListItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                        headlineContent = { Text(text = it.datetime) },
                                        trailingContent = {
                                            Text(
                                                text = "${it.used} 度",
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        1 -> {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                    ) {
                                        ColumnChart(
                                            yData = uiState.buyRecords?.rows?.map {
                                                it.money.toDouble()
                                            } ?: listOf(0.0)
                                        )
                                    }
                                }
                            }
                            items(uiState.buyRecords?.rows ?: emptyList()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ListItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                        headlineContent = { Text(text = it.dateTime) },
                                        trailingContent = {
                                            Text(
                                                text = "${it.money} 元",
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    )
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
        onDismissRequest = { onOpenBottomSheet(false) },
        onConfirmClick = {
            scope.launch {
                onOpenBottomSheet(false)
                viewModel.saveUserCookie()
                onRefresh()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetCookieBottomSheet(
    isBottomSheetShow: Boolean,
    uiState: AirConditionUiState,
    viewModel: AirConditionViewModel,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    var buildingId by remember { mutableStateOf(uiState.buildingCode) }
    var roomId by remember { mutableStateOf(uiState.roomCode) }
    LaunchedEffect(uiState.setCookieType) {
        viewModel.getAirConditionConfig()
    }
    BasicBottomSheet(
        title = "设置",
        isBottomSheetShow = isBottomSheetShow,
        onDismissRequest = onDismissRequest,
        onConfirmClick = {
            viewModel.saveBuildingAndRoomId(buildingId, roomId)
            onConfirmClick()
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                PreferenceSubtitle(text = "宿舍楼和房间")
                val buildingIdPattern = Regex("^[西|东]\\d{2}$")
                val roomIdPattern = Regex("^\\d{4}$")
                val (buildingIdError, onBuildingError) = remember { mutableStateOf(false) }
                val (roomIdError, onRoomError) = remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        label = { Text(text = "宿舍楼") },
                        placeholder = { Text(text = "如：西01 或 东02") },
                        maxLines = 1,
                        value = buildingId,
                        onValueChange = {
                            buildingId = it
                            onBuildingError(!buildingIdPattern.matches(it))
                        },
                        trailingIcon = {
                            if (buildingIdError)
                                Icon(
                                    painter = painterResource(id = R.drawable.warning_24px),
                                    contentDescription = "warning",
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        isError = buildingIdError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        label = { Text(text = "房间") },
                        placeholder = { Text(text = "如：0213(2层 13房间)") },
                        value = roomId,
                        onValueChange = {
                            roomId = it
                            onRoomError(!roomIdPattern.matches(it) || roomId.length != 4)
                        },
                        trailingIcon = {
                            if (roomIdError) Icon(
                                painter = painterResource(id = R.drawable.warning_24px),
                                contentDescription = "warning",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        isError = roomIdError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
            item {
                PreferenceSubtitle(text = "设置 Cookie")
                val radioOptions = listOf("开发者 Cookie", "自定义 Cookie")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(Modifier.selectableGroup()) {
                        radioOptions.forEachIndexed { index, text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .selectable(
                                        selected = (index == uiState.setCookieType),
                                        onClick = { viewModel.changeCookieType(index) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (index == uiState.setCookieType),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = uiState.setCookieType == 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            label = { Text(text = "shiroJID") },
                            maxLines = 1,
                            value = uiState.userLoginCookie?.shiroJID ?: "",
                            onValueChange = { viewModel.changeUserShiroJid(it) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            label = { Text(text = "ymID") },
                            value = uiState.userLoginCookie?.ymId ?: "",
                            onValueChange = { viewModel.changeUserYmld(it) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
            item {
                PreferenceSubtitle(text = "显示形式")
                val dataShowOptions = mapOf(
                    "显示数据和图表" to DataShowType.CHART_LIST,
                    "仅显示数据" to DataShowType.LIST,
                    "仅显示图表" to DataShowType.CHART
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(Modifier.selectableGroup()) {
                        dataShowOptions.keys.forEachIndexed { _, text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .selectable(
                                        selected = (dataShowOptions[text] == uiState.dataShowType),
                                        onClick = {
                                            viewModel.changeDataShowType(
                                                dataShowOptions[text] ?: DataShowType.LIST
                                            )
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (dataShowOptions[text] == uiState.dataShowType),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}