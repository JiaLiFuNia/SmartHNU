package com.smart.htu.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nlf.calendar.Lunar
import com.smart.htu.R
import com.smart.htu.api.module.Course
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SingleCourseCard
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.card.MediumCardDisplay
import com.smart.htu.component.card.SmallCardDisplay
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.entity.RouteType
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.startCalendar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

@SuppressLint("RestrictedApi", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Main(
    mainViewModel: MainViewModel,
    applicationViewModel: ApplicationViewModel,
    airConditionViewModel: AirConditionViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController,
    navigateToApplication: () -> Unit,
    themeMode: Int
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val airConditionUiState by airConditionViewModel.uiState.collectAsState()

    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(500)
            mainViewModel.getNowWeather()
            mainViewModel.getGiteeConfigService()
            if (loginUiState.loginJWCState == 1)
                mainViewModel.getTodayCourse()
            isRefreshing = false
        }
    }

    val visibility = remember {
        derivedStateOf { mutableStateOf(!loginUiState.isLogSuccess) }
    }

    Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surface
                    },
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    },
                ),
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
                },
                title = {
                    Text(text = "欢迎！${uiState.username}")
                },
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
                                val messageCount = uiState.config?.notice?.filter {
                                    !uiState.hadReadIdList.contains(it.id)
                                }?.size ?: 0
                                if (messageCount != 0)
                                    Badge {
                                        Text(
                                            text = messageCount.toString()
                                        )
                                    }
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
                            badge = { Badge() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "setting"
                            )
                        }
                    }
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
                .fillMaxSize(),
        ) {
            top.yukonga.miuix.kmp.basic.LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
            ) {
                if (visibility.value.value)
                    item {
                        SuggestChip(
                            onClick = { navController.navigate(Destinations.Login.route) },
                            onActionClick = { navController.navigate(Destinations.Login.route) },
                            text = "暂未登录，登录后即可体验全部功能",
                            type = SuggestChipType.ERROR,
                            visibility = visibility.value,
                            icon = Icons.AutoMirrored.Filled.ArrowForward
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                /*item {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CalendarCard(modifier = Modifier.weight(0.5f))
                        Spacer(modifier = Modifier.width(20.dp))
                        WeatherCard(modifier = Modifier.weight(0.5f))
                    }
                }*/
                item {
                    FocusCard(themeMode, navController, loginUiState, airConditionUiState, uiState)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    TodayCourseCard(uiState.toDayCourseList, themeMode)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    CommonAppsCard(
                        uiState = uiState,
                        navController = navController,
                        navigateToApplication = navigateToApplication,
                        applicationViewModel = applicationViewModel,
                        loginUiState = loginUiState,
                        themeMode = themeMode
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    LargeCardDisplay(
                        themeMode = themeMode,
                        modifier = Modifier,
                        title = "通知公告",
                        leadingIconPainting = R.drawable.ic_outline_article,
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                EmptyContent(
                                    text = "开发中...",
                                    modifier = Modifier.fillMaxSize(),
                                    image = DrawableVectors.emptyData()
                                )
                            }
                        },
                        navigateTo = {},
                        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun FocusCard(
    themeMode: Int,
    navController: NavController,
    loginUiState: LoginUiState,
    airConditionUiState: AirConditionUiState,
    mainUiState: AppUiState
) {
    val context = LocalContext.current
    LargeCardDisplay(
        themeMode = themeMode,
        modifier = Modifier,
        title = "聚焦",
        leadingIconPainting = R.drawable.contract_edit_24px,
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant
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
                    title = today.format(formatter),
                    content = "第 3 周 $dayOfWeek",
                    onClick = { startCalendar() },
                    modifier = Modifier.weight(0.5f)
                )
                FocusCardItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(
                                id = mainUiState.nowWeather.data?.getIconResourceId(
                                    context = context
                                ) ?: R.drawable.qweather101
                            ),
                            contentDescription = "weather",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    title = "当前天气",
                    content = "${mainUiState.nowWeather.data?.weather ?: "--"} ${mainUiState.nowWeather.data?.temperature ?: "--"} ℃",
                    onClick = { /*TODO*/ },
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
                    content = "625 学时",
                    onClick = { /*TODO*/ },
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
                    content = "${airConditionUiState.billData?.data?.soc ?: 0.0} 度",
                    onClick = {
                        navController.navigateWithAuthCheck(
                            isGuest = false,
                            route = Destinations.AirCondition.route,
                            routeType = RouteType.SCREEN,
                            logState = loginUiState.isLogSuccess,
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
                style = MaterialTheme.typography.labelMedium
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
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = modifier
            .clickable {
                onClick()
            }
    )
}

@Composable
fun TodayCourseCard(todayCourseResult: ResultWithStatus<List<Course>>, themeMode: Int) {
    LargeCardDisplay(
        themeMode = themeMode,
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier,
        title = stringResource(id = R.string.today_course),
        leadingIconPainting = R.drawable.today_24px,
        content = {
            when (todayCourseResult.status) {
                Status.SUCCESS -> {
                    Column(
                        modifier = Modifier
                    ) {
                        if (todayCourseResult.data.isNullOrEmpty()) {
                            EmptyContent(
                                modifier = Modifier
                                    .height(86.dp)
                                    .fillMaxWidth(),
                                text = "今日无课程"
                            )
                        } else {
                            todayCourseResult.data.forEachIndexed { index, it ->
                                SingleCourseCard(
                                    modifier = Modifier.fillMaxSize(),
                                    onClick = {},
                                    message = it
                                )
                                if (index < todayCourseResult.data.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                                }
                            }
                        }
                    }
                }

                else -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(86.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Composable
fun CommonAppsCard(
    uiState: AppUiState,
    navController: NavController,
    navigateToApplication: () -> Unit,
    applicationViewModel: ApplicationViewModel,
    loginUiState: LoginUiState,
    themeMode: Int
) {
    val lazyVerticalGridHeight by remember { derivedStateOf { ((ceil(uiState.appListIsCommonList.size / 5.0)) * 70).toInt() + 16 } }
    LargeCardDisplay(
        themeMode = themeMode,
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier,
        title = stringResource(id = R.string.common_applications),
        leadingIconPainting = R.drawable.app_registration_24px,
        content = {
            if (uiState.appListIsCommonList.isEmpty())
                EmptyContent(
                    text = "前往应用页面添加常用应用",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                )
            else
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .height(lazyVerticalGridHeight.dp),
                    contentPadding = PaddingValues(8.dp),
                    userScrollEnabled = false
                ) {
                    items(uiState.appListIsCommonList) { app ->
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            SmallCardDisplay(
                                enabled = (loginUiState.isGuest && app.guestEnable) || loginUiState.isLogSuccess,
                                content = app,
                                onLongClick = {
                                    applicationViewModel.changeCommonAppListState(app, false)
                                },
                                onCLick = {
                                    navController.navigateWithAuthCheck(
                                        isGuest = loginUiState.isGuest && app.guestEnable,
                                        routeType = app.routeType,
                                        route = app.route,
                                        logState = loginUiState.isLogSuccess,
                                        label = app.label
                                    )
                                }
                            )
                        }
                    }
                }
        },
        navigateTo = {
            navigateToApplication()
        }
    )
}


@Composable
fun CalendarCard(modifier: Modifier) {
    val today = LocalDate.now() // 阳历
    val dayOfWeek = today.dayOfWeek
    val dayOfWeekName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
    val chineseToday = Lunar.fromDate(Date()) // 农历
    MediumCardDisplay(
        onClick = { },
        modifier = modifier,
        title = "${today.year}/${today.month.value}",
        leadingContent = {
            Text(
                text = "${today.dayOfMonth}",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "${chineseToday.monthInChinese}月${chineseToday.dayInChinese} $dayOfWeekName",
                    color = Color.Gray
                )
            }
        },
        navigateTo = {
            startCalendar()
        }
    )
}