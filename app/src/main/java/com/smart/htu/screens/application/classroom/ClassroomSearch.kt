package com.smart.htu.screens.application.classroom

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.R
import com.smart.htu.component.PreferenceSubtitle
import com.smart.htu.utils.Constants.Companion.COURSE_PERIOD
import com.smart.htu.utils.checkTimeInterval
import com.smart.htu.utils.getCurrentDates
import com.smart.htu.utils.timeStamp2DateStr
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnrememberedMutableState")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ClassroomSearchScreen(
    navController: NavController,
    viewModel: ClassroomSearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val hazeState = remember { HazeState() }

    val (selectedRoomIndex, onSelectedRoomIndex) = rememberSaveable { mutableIntStateOf(0) }
    val (selectedTimeIndex, onSelectedTimeIndex) = rememberSaveable {
        mutableIntStateOf(checkTimeInterval())
    }

    val (selectedDate, onSelectedDate) = remember { mutableStateOf(getCurrentDates()) }
    val (showDatePicker, onShowDatePicker) = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        yearRange = LocalDate.now().year - 1..LocalDate.now().year + 1,
    )
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.getClassroomOccupation(selectedDate, selectedRoomIndex)
            isRefreshing = false
        }
    }

    LaunchedEffect(selectedRoomIndex, selectedDate) {
        viewModel.getClassroomOccupation(selectedDate, selectedRoomIndex)
    }

    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
        containerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surface,
        scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surfaceContainer
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onShowDatePicker(true)
                            Log.i("TAG666", "ClassroomSearchScreen: $showDatePicker")
                        }
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = "date")
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
                    top = it.calculateTopPadding() + 15.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
            ) {
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.building))
                }
                item {
                    LazyVerticalGridCustom(
                        list = uiState.buildingsList,
                        columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 3
                    ) { index, building ->
                        FilterChip(
                            selected = index == selectedRoomIndex,
                            onClick = { onSelectedRoomIndex(index) },
                            label = { Text(text = building.buildingName) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.time))
                    LazyVerticalGridCustom(
                        list = COURSE_PERIOD.keys.toList(),
                        columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 5 else 3
                    ) { currentIndex, timeLabel ->
                        FilterChip(
                            selected = currentIndex == selectedTimeIndex,
                            onClick = { onSelectedTimeIndex(currentIndex) },
                            label = { Text(text = stringResource(id = timeLabel)) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.occupy))
                    Log.i("TAG666", "${uiState.isLoading} ${uiState.isTokenValid}")
                    if (uiState.isLoading || !uiState.isTokenValid || uiState.buildingsOccupation[selectedRoomIndex] == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularWavyProgressIndicator()
                        }
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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp)
                                    .padding(bottom = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TabRow(
                                    selectedTabIndex = floorPagerState.currentPage,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.PrimaryIndicator(
                                            modifier = Modifier
                                                .tabIndicatorOffset(tabPositions[floorPagerState.currentPage]),
                                            width = tabPositions[floorPagerState.currentPage].width / 1.5f,
                                            shape = RoundedCornerShape(
                                                topStart = 3.dp,
                                                topEnd = 3.dp
                                            ),
                                        )
                                    },
                                    divider = {}
                                ) {
                                    allRoomListGroupByFloor.forEachIndexed { index, floor ->
                                        Tab(
                                            text = {
                                                Text(
                                                    text = stringResource(
                                                        id = when (floor.first().floorNumber) {
                                                            1 -> R.string.first_floor
                                                            2 -> R.string.second_floor
                                                            3 -> R.string.third_floor
                                                            4 -> R.string.fourth_floor
                                                            5 -> R.string.fifth_floor
                                                            else -> R.string.other
                                                        }
                                                    )
                                                )
                                            },
                                            selected = selectFloorIndex.value == index,
                                            onClick = {
                                                coroutineScope.launch {
                                                    floorPagerState.animateScrollToPage(index)
                                                }
                                            },
                                            selectedContentColor = colorScheme.primary,
                                            unselectedContentColor = colorScheme.onSurface,
                                        )
                                    }
                                }
                            }
                            HorizontalPager(
                                verticalAlignment = Alignment.Top,
                                state = floorPagerState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val currentRoomList = allRoomListGroupByFloor[it]
                                LazyVerticalGridCustom(
                                    list = currentRoomList,
                                    columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 3,
                                    ifEqualWeight = true
                                ) { _, room ->
                                    SingleRoom(
                                        label = room.roomName,
                                        state = !busyRoomListFilterByPeriod?.map { it.roomName }
                                            ?.contains(room.roomName)!!,
                                        onClick = {},
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                onShowDatePicker(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelectedDate(
                            timeStamp2DateStr(datePickerState.selectedDateMillis ?: 0)
                        )
                        onShowDatePicker(false)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onShowDatePicker(false)
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun <T> LazyVerticalGridCustom(
    list: List<T>,
    columnSize: Int,
    ifEqualWeight: Boolean = false,
    content: @Composable (index: Int, timeLabel: T) -> Unit
) {
    val courseTimeList = list.chunked(columnSize)
    Column(
        modifier = Modifier.fillMaxWidth(),
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
                            .weight(1f / columnSize)
                            .padding(horizontal = 4.dp) else Modifier
                    ) {
                        content(currentIndex, timeLabel)
                    }
                }
                if (singleRowButtons.size < columnSize) {
                    for (i in singleRowButtons.size until columnSize) {
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