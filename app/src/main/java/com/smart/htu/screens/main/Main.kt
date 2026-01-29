package com.smart.htu.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.api.module.WarningWeatherData
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.screens.application.AddTaskBottomSheet
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigateWithCheckLoginState
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.navigateToNewsDetail
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import com.smart.htu.utils.startCalendar
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.FloatingActionButton
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Main(
    mainViewModel: MainViewModel,
    airConditionViewModel: AirConditionViewModel,
    loginViewModel: LoginViewModel,
    messageViewModel: MessageViewModel,
    navController: NavController,
    contentPadding: PaddingValues
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val airConditionUiState by airConditionViewModel.uiState.collectAsState()
    val messageUiState by messageViewModel.uiState.collectAsState()

    val hazeState = rememberHazeState()
    val isNotLoggedIn = remember {
        derivedStateOf { loginUiState.jwcLoginState != 1 && loginUiState.jwcLoginState != -2 }
    }
    val holidayState = remember {
        derivedStateOf { uiState.holiday != null }
    }
    val messageCount = remember {
        derivedStateOf { messageUiState.notReadNoticeIdCount }
    }

    val isAddTaskBottomSheetShow = remember { mutableStateOf(false) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            mainViewModel.getCurrentWeather()
            messageViewModel.getNotice()
            mainViewModel.getTodayCourse()
            isRefreshing = false
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val scrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "主页",
                largeTitle = "欢迎！${uiState.username}",
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Destinations.Message.route)
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (messageCount.value > 0)
                                    Badge { androidx.compose.material3.Text(text = messageCount.value.toString()) }

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null
                            )
                        }
                    }
                },
                color = Color.Transparent,
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    }
            )
        },
        popupHost = {},
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = contentPadding.calculateBottomPadding()),
                onClick = {
                    isAddTaskBottomSheetShow.value = true
                }
            ) {
                Icon(
                    MiuixIcons.Add,
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onPrimary
                )
            }
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 12.dp
            )
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding() + 12.dp
                ),
                modifier = Modifier
                    .hazeSource(hazeState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                overscrollEffect = null
            ) {
                if (isNotLoggedIn.value || holidayState.value) {
                    item {
                        if (isNotLoggedIn.value) {
                            SuggestChip(
                                onClick = { navController.navigate(Destinations.Login.route) },
                                text = "暂未登录，登录后即可体验全部功能",
                                type = SuggestChipType.ERROR,
                                icon = Icons.AutoMirrored.Filled.ArrowForward
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        if (holidayState.value) {
                            uiState.holiday.let {
                                SuggestChip(
                                    onClick = { },
                                    text = if (it?.isLieu == true) "今天是${it.holiday}，需要调休" else "今天是${it?.holiday}假期，放假愉快",
                                    type = SuggestChipType.INFO,
                                    icon = if (it?.isLieu == true) Icons.Outlined.Info else Icons.Outlined.Celebration
                                )
                            }
                        }
                    }
                }
                item {
                    FocusCard(navController, loginUiState, airConditionUiState, uiState)
                }
                item {
                    Card {
                        TodayCourseCard(
                            todayCourseList = uiState.todayCourseList,
                            loginState = isNotLoggedIn.value,
                            onSearchCourse = {
                                navController.navigate("${Destinations.CourseSearchRepo.route}/${it}")
                            }
                        )
                    }
                }
                item {
                    TodayTaskCard(
                        taskList = uiState.taskList,
                        navController = navController
                    )
                }
            }
        }
        AddTaskBottomSheet(
            show = isAddTaskBottomSheetShow,
            onTask = {

            }
        )
    }
}

@Composable
fun TodayTaskCard(
    taskList: List<TaskEntity>,
    navController: NavController
) {
    if (taskList.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            taskList.forEach {
                Card {
                    BasicTaskCard(
                        title = it.title,
                        description = buildAnnotatedString {
                            append(
                                "${
                                    convertLocalTimeToStringTime(
                                        it.startDateTime.toLocalTime(),
                                        "hh:mm"
                                    )
                                } - ${
                                    convertLocalTimeToStringTime(
                                        it.endDateTime.toLocalTime(),
                                        "hh:mm"
                                    )
                                }"
                            )
                            if (it.location.isNotEmpty())
                                append(" | ${it.location}")
                            if (!it.remarkableInfo.isNullOrEmpty())
                                append(" | ${it.remarkableInfo}")
                        }.toString(),
                        taskColor = it.type.lightColor.primaryColor,
                        modifier = Modifier,
                        onClick = {
                            when (it.actionType) {
                                RouteType.Url -> {
                                    navController.navigateToNewsDetail(
                                        url = it.action.toString(),
                                        title = "新闻",
                                        label = "新闻"
                                    )
                                }

                                else -> {
                                    navController.navigate(Destinations.TaskManager.route)
                                }
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    } else {
        Card {
            EmptyContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                text = "没有任务"
            )
        }
    }
}

@Composable
fun FocusCard(
    navController: NavController,
    loginUiState: LoginUiState,
    airConditionUiState: AirConditionUiState,
    mainUiState: AppUiState
) {
    val isShowWeatherBottomSheet = remember { mutableStateOf(false) }
    val isWarningWeather = remember {
        derivedStateOf { mainUiState.warningWeatherData.isNotEmpty() }
    }
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.CHINA)
    val formatter = DateTimeFormatter.ofPattern("MM-dd")
    val airConditionString = stringResource(R.string.dorm_air_conditioner)
    MessageCardDisplay(
        modifier = Modifier.fillMaxWidth(),
        message = listOf(
            SingleInfo(
                label = "${today.format(formatter)}",
                content = (if (mainUiState.isTermEnded) "放假中" else "第 ${mainUiState.weekIndex} 周") + " $dayOfWeek",
                rowIndex = 1,
                leadingIcon = Icons.Outlined.Today,
                onClick = {
                    startCalendar()
                }
            ),
            SingleInfo(
                label = "即时天气",
                content = "${mainUiState.currentWeather.data?.weather ?: "--"} ${mainUiState.currentWeather.data?.temperature ?: "--"} ℃",
                rowIndex = 1,
                leadingIcon = Icons.Outlined.WbSunny,
                rightContent = {
                    if (isWarningWeather.value) Badge()
                },
                onClick = {
                    if (isWarningWeather.value) {
                        isShowWeatherBottomSheet.value = true
                    }
                }
            ),
            SingleInfo(
                label = "第二课堂",
                content = "${mainUiState.totalHour?.toInt() ?: "--"} 学时",
                rowIndex = 2,
                leadingIcon = Icons.Outlined.Palette,
                onClick = {
                    navController.navigate(Destinations.SecondClass.route)
                }
            ),
            SingleInfo(
                label = "寝室电费",
                content = "${airConditionUiState.billData?.data?.soc ?: "--"} 度",
                rowIndex = 2,
                leadingIcon = Icons.Outlined.Bolt,
                onClick = {
                    navController.navigateWithCheckLoginState(
                        isGuest = false,
                        route = Destinations.AirCondition.route,
                        routeType = RouteType.Screen,
                        logState = loginUiState.jwcLoginState == 1,
                        label = airConditionString
                    )
                }
            )
        )
    )
    WeatherBottomSheet(
        isShowWeatherBottomSheet = isShowWeatherBottomSheet,
        warningWeatherData = mainUiState.warningWeatherData
    )
}

@Composable
fun TodayCourseCard(
    todayCourseList: List<CourseEntity>?,
    loginState: Boolean,
    onSearchCourse: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = if (todayCourseList == null || todayCourseList.isEmpty()) Modifier
            .height(86.dp) else Modifier
    ) {
        if (todayCourseList == null) {
            if (loginState) {
                EmptyContent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "请登录教务系统"
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp)
                )
            }
        } else {
            if (todayCourseList.isEmpty()) {
                EmptyContent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "今日无课程"
                )
            } else {
                todayCourseList.forEachIndexed { _, it ->
                    SingleCourseCard(
                        modifier = Modifier.fillMaxSize(),
                        course = it,
                        onSearchCourse = {
                            scope.launch {
                                val searchInfo = CourseSearchPostEntity(
                                    date = getCurrentDate(),
                                    termCode = "202501",
                                    courseName = it
                                )
                                val searchInfoString = Json.encodeToString(searchInfo)
                                onSearchCourse(searchInfoString)
                            }
                        }
                    )
                }
            }
        }
    }
}

/*@Composable
fun CommonAppsCard(
    uiState: AppUiState,
    navController: NavController,
    loginUiState: LoginUiState,
    loginState: Boolean
) {
    val rowCount = remember { derivedStateOf { ceil(uiState.commonAppList.size / 5.0) } }
    val lazyVerticalGridHeight by remember { derivedStateOf { rowCount.value * 70 + (rowCount.value - 1) * 4 + 16 } }
    LargeCardDisplay(
        containerColor = MiuixTheme.colorScheme.surface,
        modifier = Modifier,
        title = stringResource(id = R.string.common_applications),
        leadingIconPainting = R.drawable.app_registration_24px,
        actionText = "编辑",
        content = {
            if (uiState.commonAppList.isEmpty())
                EmptyContent(
                    text = "点击右上角编辑以添加常用应用",
                    modifier = Modifier
                        .height(86.dp)
                        .fillMaxWidth()
                )
            else
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .height(lazyVerticalGridHeight.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(uiState.commonAppList) { app ->
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            SmallAppCard(
                                enabled = (loginUiState.isGuestModeEnable && app.guestMode) || !loginState,
                                content = app,
                                onClick = {
                                    navController.navigateWithCheckLoginState(
                                        isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                        routeType = app.routeType,
                                        route = app.route,
                                        logState = !loginState,
                                        label = app.label
                                    )
                                }
                            )
                        }
                    }
                }
        },
        navigateTo = {
            navController.navigate(Destinations.ApplicationEdit.route)
        }
    )
}*/

@Composable
fun WeatherBottomSheet(
    isShowWeatherBottomSheet: MutableState<Boolean>,
    warningWeatherData: List<WarningWeatherData>
) {
    SuperBottomSheet(
        show = isShowWeatherBottomSheet,
        title = "天气预警",
        onDismissRequest = {
            isShowWeatherBottomSheet.value = false
        },
        insideMargin = DpSize(16.dp, 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(warningWeatherData) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MiuixTheme.colorScheme.surface,
                ) {
                    BasicComponent(
                        title = it.title,
                        summary = it.content,
                        insideMargin = PaddingValues(horizontal = 0.dp)
                    )
                }
            }
        }
    }
}