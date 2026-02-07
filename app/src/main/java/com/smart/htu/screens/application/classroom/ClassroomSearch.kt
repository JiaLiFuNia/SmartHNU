package com.smart.htu.screens.application.classroom

import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DatePicker
import com.smart.htu.utils.Constants.Companion.COURSE_PERIOD
import com.smart.htu.utils.CourseTimeRange.checkTimeInterval
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import kotlin.math.ceil

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
    val (selectedCampusIndex, onSelectedCampusIndex) = rememberSaveable { mutableIntStateOf(0) }
    val campusList: List<String> = viewModel.campusList
    val classroomList by remember(selectedCampusIndex) {
        mutableStateOf(viewModel.buildingsList[selectedCampusIndex])
    }
    Log.i("TAG666", classroomList.toString())

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

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(id = R.string.classroom_search),
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        top.yukonga.miuix.kmp.basic.Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onBackground
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = uiState.blurEffect
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical()
                .hazeSource(hazeState),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card {
                    SuperArrow(
                        title = "选择日期",
                        endActions = {
                            Text(
                                text = convertLocalDateToStringDate(
                                    selectedDate.value,
                                    "YY年M月d日 E"
                                ),
                                fontSize = MiuixTheme.textStyles.body2.fontSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantActions
                            )
                        },
                        onClick = {
                            showDatePicker.value = true
                        }
                    )
                    SuperDropdown(
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
                    SuperDropdown(
                        items = campusList,
                        selectedIndex = selectedCampusIndex,
                        title = "选择校区",
                        onSelectedIndexChange = {
                            onSelectedRoomIndex(0)
                            onSelectedCampusIndex(it)
                        }
                    )
                    SuperDropdown(
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
                        // 某栋教学楼的占用情况
                        val selectedBuildingOccupationState =
                            uiState.buildingsOccupation[selectedRoomIndex]
                        // 按楼层分组
                        val allRoomListGroupByFloor =
                            (selectedBuildingOccupationState?.allRoomList?.groupBy {
                                it.floorNumber
                            }?.values?.toList() ?: emptyList()).sortedBy { it.first().floorNumber }
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
                        top.yukonga.miuix.kmp.basic.TabRow(
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

        DatePicker(
            date = selectedDate.value,
            showDatePicker = showDatePicker,
            onConfirmClick = {
                selectedDate.value = it
            }
        )
        TipDialog(showTooltip)
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