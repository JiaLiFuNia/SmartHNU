package com.smart.htu.screens.application.classroom

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.R
import com.smart.htu.component.PreferenceSubtitle
import com.smart.htu.utils.checkTimeInterval
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomSearchScreen(
    navController: NavHostController,
    viewModel: ClassroomSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedDate by remember { mutableStateOf(getCurrentDates()) }
    var selectedRoomIndex by remember { mutableIntStateOf(0) }
    var selectedTimeIndex by remember { mutableIntStateOf(checkTimeInterval()) }

    var showDatePicker by remember { mutableStateOf(false) }
    val currentDate = LocalDate.now()
    val datePickerState = rememberDatePickerState(
        yearRange = currentDate.year..currentDate.year + 1,
    )
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(2000)
            isRefreshing = false
        }
    }

    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(id = R.string.classroom_search)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showDatePicker = true
                        }
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = "date")
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.building))
                    LazyVerticalGridCustom(
                        list = uiState.buildingsList,
                        columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 3
                    ) { currentIndex, building ->
                        OutlinedButton(
                            onClick = {
                                selectedRoomIndex = currentIndex
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedRoomIndex == currentIndex) colorScheme.primaryContainer else colorScheme.background,
                                contentColor = colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(text = building.buildingName)
                        }
                    }
                }
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.time))
                    LazyVerticalGridCustom(
                        list = viewModel.haveCourseTime,
                        columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 5 else 3
                    ) { currentIndex, timeLabel ->
                        OutlinedButton(
                            onClick = {
                                selectedTimeIndex = currentIndex
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTimeIndex == currentIndex) colorScheme.primaryContainer else colorScheme.background,
                                contentColor = colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = stringResource(id = timeLabel))
                        }
                    }
                }
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.occupy))
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val roomListSortByFloor = viewModel.roomList.groupBy { it.floorNumber }
                        val newsPagerState = rememberPagerState(
                            pageCount = { roomListSortByFloor.keys.size }
                        )
                        val selectFloorIndex =
                            remember { derivedStateOf { newsPagerState.currentPage } }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TabRow(
                                selectedTabIndex = newsPagerState.currentPage,
                                indicator = { tabPositions ->
                                    TabRowDefaults.PrimaryIndicator(
                                        modifier = Modifier
                                            .tabIndicatorOffset(tabPositions[newsPagerState.currentPage]),
                                        width = tabPositions[newsPagerState.currentPage].width,
                                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                                    )
                                },
                            ) {
                                roomListSortByFloor.keys.forEachIndexed { index, floorNumber ->
                                    Tab(
                                        text = {
                                            Text(
                                                text = stringResource(
                                                    id = when (floorNumber) {
                                                        1 -> R.string.first_floor
                                                        2 -> R.string.second_floor
                                                        3 -> R.string.third_floor
                                                        4 -> R.string.fourth_floor
                                                        5 -> R.string.fifth_floor
                                                        else -> R.string.other
                                                    }
                                                ),
                                            )
                                        },
                                        selected = selectFloorIndex.value == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                newsPagerState.animateScrollToPage(index)
                                            }
                                        },
                                        selectedContentColor = colorScheme.primary,
                                        unselectedContentColor = colorScheme.onSurface,
                                    )
                                }
                            }
                        }
                        HorizontalPager(state = newsPagerState) {
                            val currentRoomList = roomListSortByFloor[it + 1] ?: emptyList()
                            LazyVerticalGridCustom(
                                list = currentRoomList,
                                columnSize = if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 3,
                                ifEqualWeight = true
                            ) { index, room ->
                                SingleRoomState(
                                    label = room.roomName,
                                    state = index % 2 == 0,
                                    timeRange = "",
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

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate =
                            timeStamp2DateStr(datePickerState.selectedDateMillis ?: 0)
                        showDatePicker = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
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

fun timeStamp2DateStr(timeStamp: Long): String {
    if (timeStamp == 0L) {
        return getCurrentDates()
    }
    val instant = Instant.ofEpochMilli(timeStamp)
    val formatter = DateTimeFormatter.ofPattern("MM月dd日")
        .withZone(ZoneId.systemDefault())
    val formattedDateTime = formatter.format(instant)
    return formattedDateTime
}

fun getCurrentDates(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM月dd日")
    val formattedDate = currentDate.format(formatter)
    return formattedDate
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