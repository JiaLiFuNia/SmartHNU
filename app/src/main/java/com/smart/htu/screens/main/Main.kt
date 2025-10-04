package com.smart.htu.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.card.SmallCardDisplay
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigateWithCheckLoginState
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.Constants.Companion.SECOND_CLASS_URL
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import com.smart.htu.utils.startCalendar
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    mainViewModel: MainViewModel,
    airConditionViewModel: AirConditionViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController,
    contentPadding: PaddingValues
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val airConditionUiState by airConditionViewModel.uiState.collectAsState()

    val loginState = remember {
        derivedStateOf { loginUiState.jwcLoginState != 1 && loginUiState.jwcLoginState != -2 }
    }
    val holidayState = remember {
        derivedStateOf { uiState.holiday != null }
    }
    val messageCount = remember {
        derivedStateOf { uiState.noticeIdList.size - uiState.readNoticeIdList.size }
    }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            mainViewModel.getCurrentWeather()
            mainViewModel.refreshNoticeAndUpdateMessage()
            mainViewModel.getTodayCourse()
            mainViewModel.getCurrentWeek()
            isRefreshing = false
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(MiuixTheme.colorScheme.background),
            title = { Text(text = "欢迎！${uiState.username}") },
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(
                            route = Destinations.Message.route
                        )
                    }
                ) {
                    BadgedBox(
                        badge = {
                            if (messageCount.value > 0)
                                Badge(
                                    content = {
                                        Text(text = messageCount.value.toString())
                                    }
                                )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    }
                }
                IconButton(
                    onClick = {
                        navController.navigate(Destinations.Setting.route)
                    }
                ) {
                    BadgedBox(
                        badge = { if (uiState.update.isNeedUpdate) Badge() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "setting"
                        )
                    }
                }
            }
        )
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding())
        ) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
                Row(modifier = Modifier.fillMaxWidth()) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp, 12.dp),
                        modifier = Modifier
                            .weight(0.5f)
                            .overScrollVertical()
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        overscrollEffect = null
                    ) {
                        if (loginState.value) {
                            item {
                                SuggestChip(
                                    onClick = { navController.navigate(Destinations.Login.route) },
                                    onActionClick = { navController.navigate(Destinations.Login.route) },
                                    text = "暂未登录，登录后即可体验全部功能",
                                    type = SuggestChipType.ERROR,
                                    icon = Icons.AutoMirrored.Filled.ArrowForward
                                )
                            }
                        }
                        if (holidayState.value) {
                            item {
                                uiState.holiday.let {
                                    SuggestChip(
                                        onClick = { },
                                        onActionClick = { },
                                        text = if (it?.isLieu == true) "今天是${it.holiday}，放假调休" else "今天是${it?.holiday}，放假愉快",
                                        type = SuggestChipType.INFO,
                                        icon = Icons.Outlined.Info
                                    )
                                }
                            }
                        }
                        item {
                            FocusCard(navController, loginUiState, airConditionUiState, uiState)
                        }
                        item {
                            CommonAppsCard(
                                uiState = uiState,
                                navController = navController,
                                loginUiState = loginUiState,
                                loginState = loginState.value
                            )
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp, 12.dp),
                        modifier = Modifier
                            .weight(0.5f)
                            .overScrollVertical()
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        overscrollEffect = null
                    ) {
                        item {
                            ExamScheduleCard(uiState.examScheduleList, navController)
                        }
                        item {
                            TodayCourseCard(
                                uiState.todayCourseList,
                                loginState.value,
                                navController
                            )
                        }
                    }
                }
            else
                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 12.dp),
                    modifier = Modifier
                        .overScrollVertical()
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    overscrollEffect = null
                ) {
                    if (loginState.value || holidayState.value) {
                        item {
                            if (loginState.value) {
                                SuggestChip(
                                    onClick = { navController.navigate(Destinations.Login.route) },
                                    onActionClick = { navController.navigate(Destinations.Login.route) },
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
                                        onActionClick = { },
                                        text = if (it?.isLieu == true) "今天是${it.holiday}，放假调休" else "今天是${it?.holiday}假期，放假愉快",
                                        type = SuggestChipType.INFO,
                                        icon = if (it?.isLieu == true) Icons.Outlined.Info else R.drawable.celebration_24px
                                    )
                                }
                            }
                        }
                    }
                    item {
                        FocusCard(navController, loginUiState, airConditionUiState, uiState)
                    }
                    item {
                        TodayCourseCard(uiState.todayCourseList, loginState.value, navController)
                    }
                    item {
                        ExamScheduleCard(uiState.examScheduleList, navController)
                    }
                    item {
                        CommonAppsCard(
                            uiState = uiState,
                            navController = navController,
                            loginUiState = loginUiState,
                            loginState = loginState.value
                        )
                    }
                }
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
    val context = LocalContext.current
    LargeCardDisplay(
        modifier = Modifier,
        title = "聚焦",
        leadingIconPainting = R.drawable.center_focus_weak_24px,
        containerColor = MiuixTheme.colorScheme.surface
    ) {
        Column {
            Row {
                val today = LocalDate.now()
                val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.CHINA)
                val formatter = DateTimeFormatter.ofPattern("MM-dd")
                FocusCardItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.today_24px),
                            contentDescription = "today"
                        )
                    },
                    trailingContent = {
                    },
                    title = "${today.format(formatter)}",
                    content = "第 ${mainUiState.courseSchedule?.week ?: "-"} 周 $dayOfWeek",
                    onClick = { startCalendar() },
                    modifier = Modifier.weight(0.5f)
                )
                FocusCardItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(
                                id = mainUiState.currentWeather.data?.getIconResourceId(
                                    context = context
                                ) ?: R.drawable.qweather101
                            ),
                            contentDescription = "weather",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    title = "即时天气",
                    content = "${mainUiState.currentWeather.data?.weather ?: "--"} ${mainUiState.currentWeather.data?.temperature ?: "--"} ℃",
                    onClick = { },
                    modifier = Modifier.weight(0.5f)
                )
            }
            Row {
                FocusCardItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.format_paint_24px),
                            contentDescription = "two"
                        )
                    },
                    title = "第二课堂",
                    content = "-- 学时",
                    onClick = {
                        navController.navigateToWebView(
                            url = SECOND_CLASS_URL,
                            label = context.getString(R.string.second_class)
                        )
                    },
                    modifier = Modifier.weight(0.5f)
                )
                FocusCardItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.bolt_24px),
                            contentDescription = "two"
                        )
                    },
                    title = "寝室电费",
                    content = "${airConditionUiState.billData?.data?.soc ?: "--"} 度",
                    onClick = {
                        navController.navigateWithCheckLoginState(
                            isGuest = false,
                            route = Destinations.AirCondition.route,
                            routeType = RouteType.SCREEN,
                            logState = loginUiState.jwcLoginState == 1,
                            label = R.string.dorm_air_conditioner
                        )
                    },
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}


@Composable
fun FocusCardItem(
    containerColor: Color = Color.Transparent,
    leadingContent: @Composable () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
    title: String,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = containerColor),
        leadingContent = {
            leadingContent()
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        },
        trailingContent = {
            if (trailingContent != null) {
                trailingContent()
            }
        },
        supportingContent = {
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
        },
        modifier = modifier
            .clickable {
                onClick()
            }
    )
}

@Composable
fun TodayCourseCard(
    todayCourseList: List<CourseEntity>?,
    loginState: Boolean,
    navController: NavController
) {
    LargeCardDisplay(
        containerColor = MiuixTheme.colorScheme.surface,
        modifier = Modifier,
        title = stringResource(id = R.string.today_course),
        actionText = "课程表",
        navigateTo = {
            navController.navigateWithCheckLoginState(
                route = Destinations.CourseTable.route,
                routeType = RouteType.SCREEN,
                logState = !loginState
            )
        },
        leadingIconPainting = R.drawable.today_24px,
        content = {
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
                        todayCourseList.forEachIndexed { index, it ->
                            SingleCourseCard(
                                modifier = Modifier.fillMaxSize(),
                                onClick = {},
                                message = it
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
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
                            SmallCardDisplay(
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
                                },
                                disableContainerColor = Color.Transparent
                            )
                        }
                    }
                }
        },
        navigateTo = {
            navController.navigate(Destinations.ApplicationEdit.route)
        }
    )
}

@Composable
fun ExamScheduleCard(
    examScheduleList: List<ExamEntity>,
    navController: NavController
) {
    LargeCardDisplay(
        containerColor = MiuixTheme.colorScheme.surface,
        modifier = Modifier,
        title = "考试安排",
        actionText = "全部",
        navigateTo = {
            navController.navigate(Destinations.ExamSchedule.route)
        },
        leadingIconPainting = R.drawable.lab_profile_24px,
        content = {
            Column(
                modifier = if (examScheduleList.isEmpty()) Modifier
                    .height(86.dp) else Modifier
            ) {
                if (examScheduleList.isEmpty()) {
                    EmptyContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "暂无考试安排"
                    )
                } else {
                    examScheduleList.filter { it.date.isEqual(LocalDate.now()) }.forEach { exam ->
                        ExamScheduleCard(
                            exam = exam,
                            isInProgress = LocalDateTime.now().isBefore(
                                LocalDateTime.of(exam.date, exam.endTime) // 结束之前
                            ) && LocalDateTime.now().isAfter(
                                LocalDateTime.of(exam.date, exam.startTime) // 开始之后
                            ),
                            isPassed = LocalDateTime.now().isAfter(
                                LocalDateTime.of(exam.date, exam.endTime) // 结束之后
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ExamScheduleCard(
    exam: ExamEntity,
    isInProgress: Boolean,
    isPassed: Boolean
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.defaultColors(
            color = if (isPassed) MiuixTheme.colorScheme.surface
            else MiuixTheme.colorScheme.surface
        )
    ) {
        val textColor = if (isPassed) MiuixTheme.colorScheme.disabledOnSecondaryVariant
        else MiuixTheme.colorScheme.onSurface
        val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
        else MiuixTheme.colorScheme.primary
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(exam.examType.color))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exam.examName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MiuixTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.schedule_24px),
                            contentDescription = "time",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = iconColor
                        )
                        Text(
                            text = "${
                                convertLocalTimeToStringTime(
                                    time = exam.startTime,
                                    pattern = "HH:mm"
                                )
                            } - ${
                                convertLocalTimeToStringTime(
                                    time = exam.endTime,
                                    pattern = "HH:mm"
                                )
                            }",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = textColor
                            ),
                            textAlign = TextAlign.Start
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location_on_24px),
                            contentDescription = "building",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp),
                            tint = iconColor
                        )
                        Text(
                            text = exam.examRoom,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = textColor
                            ),
                        )
                    }
                }
            }
        }
    }
}