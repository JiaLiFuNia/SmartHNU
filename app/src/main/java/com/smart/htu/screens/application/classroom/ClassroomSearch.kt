package com.smart.htu.screens.application.classroom

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DatePicker
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.Constants.Companion.COURSE_PERIOD
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import com.smart.htu.utils.DateUtil.toStringDate
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TabRowWithContour
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Help
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate
import kotlin.math.ceil

@Composable
fun ClassroomSearchScreen(
    viewModel: ClassroomSearchViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val (selectedRoomIndex, onSelectedRoomIndex) = rememberSaveable { mutableIntStateOf(0) }
    val (selectedCampusIndex, onSelectedCampusIndex) = rememberSaveable { mutableIntStateOf(0) }
    val campusList: List<String> = viewModel.campusList
    val classroomList by remember(selectedCampusIndex) {
        mutableStateOf(viewModel.buildingsList[selectedCampusIndex])
    }

    val (selectedTimeIndex, onSelectedTimeIndex) = rememberSaveable {
        mutableIntStateOf(checkTimeInterval())
    }

    val showTooltip = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val showDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(selectedRoomIndex, selectedCampusIndex, selectedDate.value) {
        viewModel.getClassroomOccupation(
            date = selectedDate.value.toString(),
            campusIndex = selectedCampusIndex,
            buildingIndex = selectedRoomIndex
        )
    }

    val scrollBehavior = MiuixScrollBehavior()

    val backdrop = rememberBlurBackdrop(uiState.blurEffect)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = stringResource(id = R.string.classroom_search),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
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
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .scrollEndHaptic(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        ArrowPreference(
                            title = "选择日期",
                            endActions = {
                                Text(
                                    text = selectedDate.value.toStringDate("YYYY年M月d日 E"),
                                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                )
                            },
                            onClick = {
                                showDatePicker.value = true
                            }
                        )
                        OverlayDropdownPreference(
                            items = COURSE_PERIOD.keys.toList().map { stringResource(it) },
                            selectedIndex = selectedTimeIndex,
                            title = "选择时间段",
                            onSelectedIndexChange = {
                                onSelectedTimeIndex(it)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Card {
                        OverlayDropdownPreference(
                            items = campusList,
                            selectedIndex = selectedCampusIndex,
                            title = "选择校区",
                            onSelectedIndexChange = {
                                onSelectedRoomIndex(0)
                                onSelectedCampusIndex(it)
                            }
                        )
                        OverlayDropdownPreference(
                            items = viewModel.buildingsList[selectedCampusIndex].map { it.buildingName },
                            selectedIndex = selectedRoomIndex,
                            title = "选择教学楼",
                            onSelectedIndexChange = {
                                onSelectedRoomIndex(it)
                            }
                        )
                    }
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
                                imageVector = MiuixIcons.Help,
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
                            // 某栋教学楼的占用情况
                            val selectedBuildingOccupationState =
                                uiState.buildingsOccupation[selectedRoomIndex]
                            // 按楼层分组
                            val allRoomListGroupByFloor =
                                (selectedBuildingOccupationState?.allRoomList?.groupBy {
                                    it.floorNumber
                                }?.values?.toList()
                                    ?: emptyList()).sortedBy { it.first().floorNumber }
                            val floorPagerState = rememberPagerState(
                                pageCount = { allRoomListGroupByFloor.size }
                            )
                            val selectFloorIndex =
                                remember { derivedStateOf { floorPagerState.currentPage } }
                            val tabRowItem =
                                allRoomListGroupByFloor.map {
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
                            TabRowWithContour(
                                tabs = tabRowItem,
                                selectedTabIndex = selectFloorIndex.value,
                                onTabSelected = {
                                    coroutineScope.launch {
                                        floorPagerState.animateScrollToPage(it)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val columns = 3
                            HorizontalPager(
                                verticalAlignment = Alignment.Top,
                                state = floorPagerState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // 当前楼层的所有教室
                                val currentFloorAllRoomList = allRoomListGroupByFloor[it]
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(columns),
                                    modifier = Modifier.height((ceil(currentFloorAllRoomList.size / columns.toFloat()) * 58).dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(currentFloorAllRoomList) {
                                        val busyStateList =
                                            selectedBuildingOccupationState?.getBusyState(it)
                                                ?: ArrayList(10)
                                        SingleRoom(
                                            label = it.roomName,
                                            formerPeriodBusyState = busyStateList[selectedTimeIndex * 2],
                                            latterPeriodBusyState = busyStateList[selectedTimeIndex * 2 + 1],
                                            onClick = {
                                                coroutineScope.launch {
                                                    viewModel.getClassroomOccupationDetail(
                                                        date = selectedDate.value.toString(),
                                                        classroom = it.roomName,
                                                        onResult = {
                                                            showToast(context, it)
                                                        }
                                                    )
                                                }
                                            },
                                            modifier = Modifier,
                                            date = selectedDate.value,
                                            occupationDetail = uiState.occupationDetail
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        DatePicker(
            date = selectedDate.value,
            showDatePicker = showDatePicker.value,
            onConfirmClick = {
                selectedDate.value = it
                showDatePicker.value = false
            },
            onDismissRequest = {
                showDatePicker.value = false
            }
        )
        TipDialog(showTooltip.value) {
            showTooltip.value = false
        }
    }
}

@Composable
fun TipDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit
) {
    OverlayDialog(
        show = showDialog,
        title = "说明",
        summary = "若当天教学楼为考场，请以实际为准",
        onDismissRequest = onDismissRequest
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
                            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.surface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                SingleRoom(
                                    label = "启智楼10${it + 1}",
                                    formerPeriodBusyState = it == 1,
                                    latterPeriodBusyState = it == 1,
                                    modifier = Modifier.width(120.dp),
                                    occupationDetail = null,
                                    date = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (it == 0) "空闲教室" else "非空闲教室",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MiuixTheme.textStyles.main
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            top.yukonga.miuix.kmp.basic.TextButton(
                text = "我知道了",
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}