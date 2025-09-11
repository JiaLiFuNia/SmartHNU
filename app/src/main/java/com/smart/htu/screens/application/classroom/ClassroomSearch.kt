package com.smart.htu.screens.application.classroom

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.R
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DatePickerDialog
import com.smart.htu.component.TabRow
import com.smart.htu.utils.Constants.Companion.COURSE_PERIOD
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import network.chaintech.kmp_date_time_picker.utils.now
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.extra.SuperSpinner
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import kotlin.math.ceil

@SuppressLint("UnrememberedMutableState")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class
)
@Composable
fun ClassroomSearchScreen(
    navController: NavController,
    viewModel: ClassroomSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = rememberHazeState()
    val coroutineScope = rememberCoroutineScope()

    val (selectedRoomIndex, onSelectedRoomIndex) = rememberSaveable { mutableIntStateOf(0) }
    val (selectedTimeIndex, onSelectedTimeIndex) = rememberSaveable {
        mutableIntStateOf(checkTimeInterval())
    }

    val showTooltip = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(kotlinx.datetime.LocalDate.now()) }
    val showDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(selectedRoomIndex, selectedDate) {
        viewModel.getClassroomOccupation(selectedDate.value.toString(), selectedRoomIndex)
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.classroom_search)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
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
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                bottom = 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SmallTitle(
                    text = "选择教学楼和时间",
                    insideMargin = PaddingValues(start = 12.dp, top = 4.dp, bottom = 8.dp)
                )
                Card {
                    SuperArrow(
                        title = "选择日期",
                        rightText = selectedDate.value.toString(),
                        onClick = {
                            showDatePicker.value = true
                        },
                        enabled = false
                    )
                    SuperSpinner(
                        items = COURSE_PERIOD.keys.toList()
                            .map { SpinnerEntry(title = stringResource(it)) },
                        selectedIndex = selectedTimeIndex,
                        title = "选择时间段",
                        onSelectedIndexChange = {
                            onSelectedTimeIndex(it)
                        },
                        dialogButtonString = "取消"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card {
                    SuperSpinner(
                        items = uiState.buildingsList.map { SpinnerEntry(title = it.buildingName) },
                        selectedIndex = selectedRoomIndex,
                        title = "选择教学楼",
                        onSelectedIndexChange = {
                            onSelectedRoomIndex(it)
                        },
                        dialogButtonString = "取消"
                    )
                }
                /*LazyVerticalGridCustom(
                    modifier = Modifier.fillMaxSize(),
                    list = uiState.buildingsList,
                    columnSize = if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) 4 else 3
                ) { index, building ->
                    FilterChip(
                        selected = index == selectedRoomIndex,
                        onClick = { onSelectedRoomIndex(index) },
                        label = { Text(text = building.buildingName) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }*/
            }
            item {
                /*SmallTitle(
                        text = stringResource(id = R.string.time),
                insideMargin = PaddingValues(start = 12.dp, top = 12.dp, bottom = 8.dp)
                )*/
                /*LazyVerticalGridCustom(
                    modifier = Modifier.fillMaxSize(),
                    list = COURSE_PERIOD.keys.toList(),
                    columnSize = if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) 5 else 3
                ) { currentIndex, timeLabel ->
                    FilterChip(
                        selected = currentIndex == selectedTimeIndex,
                        onClick = { onSelectedTimeIndex(currentIndex) },
                        label = { Text(text = stringResource(id = timeLabel)) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }*/
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SmallTitle(
                        text = stringResource(id = R.string.occupy),
                        insideMargin = PaddingValues(start = 12.dp)
                    )
                    IconButton(
                        onClick = { showTooltip.value = true },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.help_24px),
                            contentDescription = "help",
                            tint = MiuixTheme.colorScheme.onBackground
                        )
                    }
                }
                if (uiState.isLoading || uiState.buildingsOccupation[selectedRoomIndex] == null) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val singleBuildingRoomOccupation =
                            uiState.buildingsOccupation[selectedRoomIndex]
                        val allRoomListGroupByFloor =
                            singleBuildingRoomOccupation?.allRoomList?.groupBy {
                                it.floorNumber
                            }?.values?.toList() ?: emptyList()
                        val busyRoomListFilterByPeriod =
                            singleBuildingRoomOccupation?.busyRoomList?.filter {
                                COURSE_PERIOD.values.toList()[selectedTimeIndex] in it.busyPeriodCode || it.busyPeriodCode in COURSE_PERIOD.values.toList()[selectedTimeIndex]
                            }
                        val floorPagerState = rememberPagerState(
                            pageCount = { allRoomListGroupByFloor.size }
                        )
                        val selectFloorIndex =
                            remember { derivedStateOf { floorPagerState.currentPage } }
                        val tabRowItem = allRoomListGroupByFloor.map {
                            stringResource(
                                id = when (it.first().floorNumber) {
                                    1 -> R.string.first_floor
                                    2 -> R.string.second_floor
                                    3 -> R.string.third_floor
                                    4 -> R.string.fourth_floor
                                    5 -> R.string.fifth_floor
                                    else -> R.string.other
                                }
                            )
                        }
                        TabRow(
                            tabs = tabRowItem,
                            selectedTabIndex = selectFloorIndex.value,
                            onTabSelected = {
                                coroutineScope.launch {
                                    floorPagerState.animateScrollToPage(it)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val columns = if (windowSizeClass.isWidthAtLeastBreakpoint(
                                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
                            )
                        ) 4 else 3
                        HorizontalPager(
                            verticalAlignment = Alignment.Top,
                            state = floorPagerState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val currentRoomList = allRoomListGroupByFloor[it]
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(columns),
                                modifier = Modifier.height((ceil(currentRoomList.size / columns.toFloat()) * 58).dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(currentRoomList) {
                                    SingleRoom(
                                        label = it.roomName,
                                        state = !busyRoomListFilterByPeriod?.map { it.roomCode }
                                            ?.contains(it.roomCode)!!,
                                        onClick = {},
                                        modifier = Modifier
                                    )
                                }
                            }
                            /*LazyVerticalGridCustom(
                                modifier = Modifier.fillMaxSize(),
                                list = currentRoomList,
                                columnSize =
                                    if (windowSizeClass.isWidthAtLeastBreakpoint(
                                            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
                                        )
                                    ) 4 else 3,
                                ifEqualWeight = true
                            ) { _, room ->
                                SingleRoom(
                                    label = room.roomName,
                                    state = !busyRoomListFilterByPeriod?.map { it.roomName }
                                        ?.contains(room.roomName)!!,
                                    onClick = {},
                                    modifier = Modifier
                                )
                            }*/
                        }
                    }
                }

            }
        }

        DatePickerDialog(
            showDatePicker = showDatePicker,
            onConfirmClick = { selectedDate.value = it }
        )
        TipDialog(showTooltip)
    }
}

@Composable
fun <T> LazyVerticalGridCustom(
    modifier: Modifier,
    list: List<T>,
    columnSize: Int,
    ifEqualWeight: Boolean = false,
    content: @Composable (index: Int, timeLabel: T) -> Unit
) {
    val courseTimeList = list.chunked(columnSize)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        courseTimeList.forEachIndexed { rowIndex, singleRowButtons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                singleRowButtons.forEachIndexed { columnIndex, timeLabel ->
                    val currentIndex = rowIndex * columnSize + columnIndex
                    Box(
                        modifier = if (ifEqualWeight) Modifier
                            .weight(1f / columnSize) else Modifier
                    ) {
                        content(currentIndex, timeLabel)
                        if (columnIndex <= columnSize - 2) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
                if (singleRowButtons.size < columnSize) {
                    repeat(columnSize - singleRowButtons.size) {
                        Box(
                            modifier = if (ifEqualWeight) Modifier
                                .weight(1f / columnSize)
                                .padding(horizontal = 4.dp) else Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TipDialog(
    showDialog: MutableState<Boolean>
) {
    SuperDialog(
        show = showDialog,
        title = "说明",
        summary = "若当天教学楼为考场，请以实际为准",
        onDismissRequest = {
            showDialog.value = false
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(2) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.background)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                SingleRoom(
                                    label = "启智楼10${it + 1}",
                                    state = it == 0,
                                    onClick = {},
                                    modifier = Modifier.width(120.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (it == 0) "空闲教室" else "非空闲教室",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MiuixTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            top.yukonga.miuix.kmp.basic.TextButton(
                text = "我知道了",
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}