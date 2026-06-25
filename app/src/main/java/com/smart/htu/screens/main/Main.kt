package com.smart.htu.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.Navigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.ui.theme.isInDarkTheme
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.formatDateToFriendly
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.isDynamicColor
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.floor
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("RestrictedApi")
@Composable
fun Main(
    mainViewModel: MainViewModel,
    airConditionViewModel: AirConditionViewModel,
    loginViewModel: LoginViewModel,
    messageViewModel: MessageViewModel,
    onClickToNewsScreen: () -> Unit,
    contentPadding: PaddingValues
) {
    val navigator = LocalNavigator.current
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val airConditionUiState by airConditionViewModel.uiState.collectAsState()
    val messageUiState by messageViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val isLoginFailure = remember {
        derivedStateOf { loginUiState.jwcLoginState == 0 || loginUiState.jwcLoginState == -1 } // 未登录或登陆失败
    }
    val isHolidaySuggestChipShow = remember {
        derivedStateOf { uiState.holiday != null }
    }
    val messageCount = remember {
        derivedStateOf { messageUiState.notReadNoticeIdCount }
    }

    val isAddTaskBottomSheetShow = remember { mutableStateOf(false) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing || uiState.loginJWCState == 1) {
            mainViewModel.getCurrentWeather()
            messageViewModel.getNotice()
            mainViewModel.getTodayCourse()
            isRefreshing = false
        }
    }

    val scrollBehavior = MiuixScrollBehavior()
    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    title = "主页",
                    largeTitle = "欢迎！${uiState.username}",
                    scrollBehavior = scrollBehavior,
                    actions = {
                        IconButton(
                            onClick = {
                                navigator.push(Route.Message)
                            }
                        ) {
                            // Badge { androidx.compose.material3.Text(text = messageCount.value.toString()) }
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null
                            )
                        }
                    },
                    color = barColor
                )
            }
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = contentPadding.calculateBottomPadding() + 16.dp
                )
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = contentPadding.calculateBottomPadding() + 16.dp
                    ),
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .overScrollVertical()
                        .fillMaxSize()
                        .scrollEndHaptic(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    overscrollEffect = null
                ) {
                    if (isLoginFailure.value) {
                        item {
                            SuggestChip(
                                onClick = {
                                    navigator.push(Route.Login)
                                },
                                text = "暂未登录，登录后即可体验全部功能",
                                type = SuggestChipType.ERROR,
                                icon = Icons.AutoMirrored.Filled.ArrowForward
                            )
                        }
                    }
                    if (isHolidaySuggestChipShow.value) {
                        item {
                            uiState.holiday.let {
                                SuggestChip(
                                    onClick = { },
                                    text = if (it?.isLieu == true) "今天是${it.holiday}假期调休，注意安排时间。" else "今天是${it?.holiday}假期，放假愉快。",
                                    type = SuggestChipType.INFO,
                                    icon = if (it?.isLieu == true) Icons.Outlined.Info else Icons.Outlined.Celebration
                                )
                            }
                        }
                    }
                    if (uiState.homeCourseStateEnabled && !isLoginFailure.value) {
                        item { CourseStateCard(uiState.todayCourseList) }
                    }
                    if (uiState.homeFocusEnabled) {
                        item { FocusCard(!isLoginFailure.value, airConditionUiState, uiState) }
                    }
                    if (uiState.homeTodayCourseEnabled) {
                        item {
                            TodayCourseCard(
                                todayCourseList = uiState.todayCourseList,
                                loginState = !isLoginFailure.value,
                                termCode = uiState.termCode,
                                isShowAllCourse = uiState.homeShowAllTodayCourseEnabled,
                                onSearchCourse = {
                                    navigator.push(Route.CourseSearchRepo(it))
                                }
                            )
                        }
                    }
                    if (uiState.homeTodayTaskEnabled && uiState.taskList.isNotEmpty()) {
                        item {
                            TodayTaskCard(
                                taskList = uiState.taskList,
                                navigator = navigator
                            )
                        }
                    }
                    if (uiState.homeNewsEnabled) {
                        item {
                            RecentImportantNews(
                                newsList = uiState.importantNewsList,
                                onClickToNewsScreen = onClickToNewsScreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayTaskCard(
    taskList: List<TaskEntity>,
    navigator: Navigator
) {
    if (taskList.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            taskList.forEach {
                Card {
                    BasicTaskCard(
                        title = it.title,
                        summary = buildString {
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
                        },
                        taskColor = it.type.lightColor.primaryColor,
                        modifier = Modifier,
                        onClick = {
                            when (it.actionType) {
                                RouteType.Url -> {
                                    navigator.push(
                                        Route.NewsDetail(
                                            url = it.action.toString(),
                                            title = "新闻",
                                            source = "新闻"
                                        )
                                    )
                                }

                                else -> {
                                    navigator.push(Route.TaskManager)
                                }
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CourseStateCard(
    todayCourseList: List<CourseEntity>?
) {
    val currentTime = LocalTime.now()
    // 今天没课
    val isToadyNoCourse = todayCourseList.isNullOrEmpty()
    val currentCourse: CourseEntity? = todayCourseList.let {
        if (!it.isNullOrEmpty()) {
            it.find { it.startTime.isBefore(currentTime) && it.endTime.isAfter(currentTime) }
        } else {
            null
        }
    }
    // 下一节课
    val nextCourse: CourseEntity? = todayCourseList.let {
        if (!it.isNullOrEmpty()) {
            it.find { it.startTime.isAfter(currentTime) }
        } else {
            null
        }
    }
    val diffMinutesState = remember { mutableLongStateOf(0L) }
    LaunchedEffect(nextCourse) {
        if (nextCourse != null) {
            while (true) {
                val now = LocalTime.now()
                val diff = ChronoUnit.MINUTES.between(now, nextCourse.startTime)
                diffMinutesState.longValue = diff
                val delayMs = (60 - now.second) * 1000L - now.nano / 1000000L
                delay(delayMs.milliseconds)
            }
        }
    }
    // 今天课上完了
    val isAllCourseFinished = nextCourse == null && currentCourse == null
    /*todayCourseList.let {
        if (!it.isNullOrEmpty()) {
            it.map { it.endTime }.all { it.isBefore(currentTime) }
        } else {
            true
        }
    }*/
    val containerColor = if (isToadyNoCourse || isAllCourseFinished) {
        when {
            isDynamicColor -> MiuixTheme.colorScheme.secondaryContainer
            isInDarkTheme() -> Color(0xFF1A3825)
            else -> Color(0xFFDFFAE4)
        }
    } else {
        when {
            isDynamicColor -> MiuixTheme.colorScheme.secondaryContainer
            else -> MiuixTheme.colorScheme.primary
        }
    }
    val contentColor = if (isToadyNoCourse || isAllCourseFinished) {
        MiuixTheme.colorScheme.onSurface
    } else {
        when {
            isDynamicColor -> MiuixTheme.colorScheme.onSurface
            else -> MiuixTheme.colorScheme.onPrimary
        }
    }
    val emoji = when {
        currentTime.isAfter(LocalTime.of(22, 0)) -> R.drawable.sentiment_worried_24px
        isToadyNoCourse -> R.drawable.sentiment_excited_24px
        isAllCourseFinished -> R.drawable.sentiment_excited_24px
        currentTime.isBefore(LocalTime.of(8, 30)) -> R.drawable.sentiment_calm_24px
        currentTime.isBefore(LocalTime.of(14, 30)) &&
                currentTime.isAfter(LocalTime.of(12, 0)) -> R.drawable.sentiment_content_24px

        !isAllCourseFinished -> R.drawable.sentiment_neutral_24px

        else -> R.drawable.sentiment_excited_24px
    }
    val emojiColor = if (isToadyNoCourse || isAllCourseFinished) {
        if (isDynamicColor) {
            MiuixTheme.colorScheme.primary.copy(alpha = 0.8f)
        } else {
            Color(0xFF36D167)
        }
    } else {
        if (isDynamicColor) {
            MiuixTheme.colorScheme.primary.copy(alpha = 0.8f)
        } else {
            MiuixTheme.colorScheme.onPrimaryVariant
        }
    }

    val lineCount = remember { mutableIntStateOf(1) }

    Card(
        colors = CardDefaults.defaultColors(
            color = containerColor,
            contentColor = contentColor
        ),
        pressFeedbackType = PressFeedbackType.Tilt,
        modifier = Modifier
            .height((128 + 12 * (lineCount.intValue - 1)).dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(28.dp, (32 + 12 * (lineCount.intValue - 1)).dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    painter = painterResource(emoji),
                    contentDescription = null,
                    modifier = Modifier
                        .size(124.dp)
                        .align(Alignment.BottomEnd),
                    tint = emojiColor
                )
            }
            when {
                isToadyNoCourse -> {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "今天无课程",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "Have a good day!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                isAllCourseFinished -> {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "今日课程已全部结束",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "Have a good day!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                else -> {
                    val course = currentCourse ?: nextCourse
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = if (currentCourse == null) "下一节课" else "当前课程",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = course?.courseName ?: "无课程名称",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp),
                            onTextLayout = {
                                lineCount.intValue = it.lineCount
                            }
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "时间",
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(
                                text = "${
                                    convertLocalTimeToStringTime(
                                        time = course?.startTime ?: currentTime,
                                        pattern = "HH:mm"
                                    )
                                }-${
                                    convertLocalTimeToStringTime(
                                        time = course?.endTime ?: currentTime,
                                        pattern = "HH:mm"
                                    )
                                }",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "地点",
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(
                                text = course?.classroomName ?: "无教室",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                    val diffMinutes = diffMinutesState.longValue
                    val diffHours = floor(diffMinutes / 60.0).toInt()
                    Text(
                        text = when {
                            diffHours >= 1 -> "距离开始还有 $diffHours 时 ${diffMinutes - diffHours * 60} 分钟"
                            diffHours < 1 && diffMinutes <= 2 -> "即将开始"
                            else -> "距离开始还有 $diffMinutes 分钟"
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(0.3f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MiuixTheme.textStyles.footnote1
                    )
                }
            }
        }
    }
}

@Composable
fun FocusCard(
    loginState: Boolean,
    airConditionUiState: AirConditionUiState,
    mainUiState: AppUiState
) {
    val navigator = LocalNavigator.current
    /*val isShowWeatherBottomSheet = remember { mutableStateOf(false) }
    val isWarningWeather = remember {
        derivedStateOf { mainUiState.warningWeatherData.isNotEmpty() }
    }*/
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.CHINA)
    val formatter = DateTimeFormatter.ofPattern("M月d日")
    val airConditionString = stringResource(R.string.dorm_air_conditioner)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height((74 * 2 + 12).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card {
                BasicComponent(
                    title = "${today.format(formatter)}",
                    summary = (if (mainUiState.isTermEnded) "放假中" else "第 ${mainUiState.weekIndex} 周") + " $dayOfWeek",
                    startAction = {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(ApplicationEntity.ApplicationColor.BLUE.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Today,
                                contentDescription = "icon",
                                modifier = Modifier.size(20.dp),
                                tint = MiuixTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    onClick = {
                        navigator.pushWithLoginCheck(
                            route = Route.CourseTable,
                            loginState = loginState,
                            isGuest = false
                        )
                    }
                )
            }
        }
        item {
            Card {
                BasicComponent(
                    title = "${mainUiState.currentWeather?.temperature ?: "--"} ℃",
                    summary = mainUiState.currentWeather?.weather ?: "--",
                    startAction = {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(ApplicationEntity.ApplicationColor.GREEN.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.NearMe,
                                contentDescription = "icon",
                                modifier = Modifier.size(20.dp),
                                tint = MiuixTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    onClick = {
                        navigator.push(Route.CampusLife)
                    }
                )
            }
        }
        item {
            Card {
                BasicComponent(
                    title = "第二课堂",
                    summary = "${mainUiState.totalHour?.toInt() ?: "--"} 学时",
                    startAction = {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(ApplicationEntity.ApplicationColor.ORANGE.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Palette,
                                contentDescription = "icon",
                                modifier = Modifier.size(20.dp),
                                tint = MiuixTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    onClick = {
                        navigator.pushWithLoginCheck(
                            isGuest = false,
                            loginState = loginState,
                            route = Route.SecondClass
                        )
                    }
                )
            }
        }
        item {
            Card {
                BasicComponent(
                    title = airConditionString,
                    summary = "${airConditionUiState.billData?.data?.soc ?: "--"} 度",
                    startAction = {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(ApplicationEntity.ApplicationColor.GREEN.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WaterDrop,
                                contentDescription = "icon",
                                modifier = Modifier.size(20.dp),
                                tint = MiuixTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    onClick = {
                        navigator.pushWithLoginCheck(
                            isGuest = false,
                            loginState = loginState,
                            route = Route.AirCondition
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TodayCourseCard(
    todayCourseList: List<CourseEntity>?,
    isShowAllCourse: Boolean,
    termCode: String,
    loginState: Boolean,
    onSearchCourse: (CourseSearchPostEntity) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current
    val notFinishedCourseList = todayCourseList?.filter {
        it.endTime.isAfter(LocalTime.now())
    }
    val courseList = if (isShowAllCourse) todayCourseList else notFinishedCourseList
    Card {
        BasicComponent(
            title = "今日课程",
            insideMargin = PaddingValues(16.dp),
            endActions = {
                Text(
                    text = "查看课表",
                    color = MiuixTheme.colorScheme.primary,
                    style = MiuixTheme.textStyles.headline2,
                    modifier = Modifier.clickable {
                        navigator.pushWithLoginCheck(
                            route = Route.CourseTable,
                            loginState = loginState,
                            isGuest = false
                        )
                    }
                )
            }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Column(
            modifier = if (courseList.isNullOrEmpty()) Modifier
                .height(96.dp) else Modifier
        ) {
            if (courseList == null || !loginState) {
                if (!loginState) {
                    EmptyContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "请登录后查看今日课程"
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                    )
                }
            } else {
                if (courseList.isEmpty()) {
                    EmptyContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "今日无课程"
                    )
                } else {
                    courseList.forEachIndexed { index, it ->
                        SingleCourseCard(
                            modifier = Modifier.fillMaxSize(),
                            course = it,
                            index = index,
                            onSearchSameCourse = {
                                scope.launch {
                                    val searchInfo = CourseSearchPostEntity(
                                        date = getCurrentDate(),
                                        termCode = termCode,
                                        courseName = it
                                    )
                                    onSearchCourse(searchInfo)
                                }
                            },
                            onSearchSameRoom = {
                                scope.launch {
                                    val searchInfo = CourseSearchPostEntity(
                                        date = getCurrentDate(),
                                        termCode = termCode,
                                        teachingVenueName = it
                                    )
                                    onSearchCourse(searchInfo)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentImportantNews(
    newsList: List<NewsItemEntity>?,
    onClickToNewsScreen: () -> Unit
) {
    val navigator = LocalNavigator.current
    Card {
        BasicComponent(
            title = "重要通知",
            insideMargin = PaddingValues(16.dp),
            endActions = {
                Row(
                    modifier = Modifier.clickable {
                        onClickToNewsScreen()
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "全部新闻",
                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                        color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                    )
                    Icon(
                        modifier = Modifier
                            .size(width = 10.dp, height = 16.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = MiuixIcons.Basic.ArrowRight,
                        contentDescription = null
                    )
                }
            }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Column(
            modifier = if (newsList.isNullOrEmpty()) Modifier
                .height(96.dp) else Modifier
        ) {
            if (newsList == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                )
            } else {
                if (newsList.isEmpty()) {
                    EmptyContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "无数据"
                    )
                } else {
                    newsList.forEach {
                        val source = stringResource(it.label.label)
                        BasicComponent(
                            title = it.title,
                            summary = "${formatDateToFriendly(it.time)} · $source",
                            onClick = {
                                navigator.push(
                                    Route.NewsDetail(
                                        url = it.url,
                                        title = it.title,
                                        source = source
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}