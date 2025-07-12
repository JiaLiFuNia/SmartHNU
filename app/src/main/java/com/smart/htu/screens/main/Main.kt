package com.smart.htu.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.card.SmallCardDisplay
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigateWithCheckLoginState
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsItem
import com.smart.htu.screens.news.navigateToNewsDetail
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.Constants.Companion.SECOND_CLASS_URL
import com.smart.htu.utils.startCalendar
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate
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
        derivedStateOf { loginUiState.loginJWCState != 1 && loginUiState.loginJWCState != -2 }
    }
    val pullToRefreshState = rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            pullToRefreshState.completeRefreshing {
                mainViewModel.getCurrentWeather()
                mainViewModel.refreshNoticeAndUpdate()
                mainViewModel.getNewsList()
                mainViewModel.getTodayCourse()
                mainViewModel.getCurrentWeek()
            }
        }
    }

    PullToRefresh(
        pullToRefreshState = pullToRefreshState,
        refreshTexts = PULL_TO_REFRESH_TEXT,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp, 12.dp),
            modifier = Modifier
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
            item {
                FocusCard(navController, loginUiState, airConditionUiState, uiState)
            }
            item {
                TodayCourseCard(uiState.todayCourseList, loginState.value, navController)
            }
            item {
                CommonAppsCard(
                    uiState = uiState,
                    navController = navController,
                    loginUiState = loginUiState,
                    loginState = loginState.value
                )
            }
            item {
                NewsCard(
                    navController = navController,
                    newsListStatus = uiState.newsList
                )
            }
        }
    }

}

@Composable
fun NewsCard(
    navController: NavController,
    newsListStatus: ResultWithStatus<List<NewsItemEntity>>
) {
    LargeCardDisplay(
        modifier = Modifier,
        title = "学术预告",
        leadingIconPainting = R.drawable.ic_outline_article,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (newsListStatus.status) {
                        Status.SUCCESS -> {
                            if (newsListStatus.data.isNullOrEmpty()) {
                                EmptyContent(
                                    modifier = Modifier.fillMaxSize(),
                                    text = "获取失败"
                                )
                            } else {
                                newsListStatus.data.take(4).forEach { news ->
                                    NewsItem(news = news, maxLines = 2) {
                                        navController.navigateToNewsDetail(
                                            url = news.url,
                                            label = context.getString(news.label.label)
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(300.dp)
                            )
                        }
                    }
                }
            }
        },
        navigateTo = {},
        containerColor = MiuixTheme.colorScheme.surface
    )
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
                    title = "${today.format(formatter)} " + if (mainUiState.holidayEntity?.holiday != null) mainUiState.holidayEntity.holiday.holiday else "",
                    content = "第 ${mainUiState.courseSchedule.data?.week ?: "-"} 周 $dayOfWeek",
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
                            logState = loginUiState.loginJWCState == 1,
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
                modifier = Modifier
                    .height(86.dp)
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