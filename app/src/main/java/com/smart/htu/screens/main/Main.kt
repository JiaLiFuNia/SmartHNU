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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
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
import com.smart.htu.api.module.Course
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SingleCourseCard
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.card.SmallCardDisplay
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.application.airCondition.AirConditionUiState
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.entity.RouteType
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsItem
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.startCalendar
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.MiuixTheme
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
    applicationViewModel: ApplicationViewModel,
    airConditionViewModel: AirConditionViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController,
    contentPadding: PaddingValues,
    themeMode: Int
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val airConditionUiState by airConditionViewModel.uiState.collectAsState()

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            pullToRefreshState.completeRefreshing {
                mainViewModel.getCurrentWeather()
                mainViewModel.refreshGiteeConfig()
                mainViewModel.getNewsList()
                if (loginUiState.loginJWCState == 1)
                    mainViewModel.getTodayCourse()
            }
        }
    }

    val loginState = remember {
        derivedStateOf { mutableStateOf(!loginUiState.isLogSuccess) }
    }

    top.yukonga.miuix.kmp.basic.PullToRefresh(
        pullToRefreshState = pullToRefreshState,
        refreshTexts = PULL_TO_REFRESH_TEXT,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        top.yukonga.miuix.kmp.basic.LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (loginState.value.value) item {
                SuggestChip(
                    onClick = { navController.navigate(Destinations.Login.route) },
                    onActionClick = { navController.navigate(Destinations.Login.route) },
                    text = "暂未登录，登录后即可体验全部功能",
                    type = SuggestChipType.ERROR,
                    visibility = loginState.value,
                    icon = Icons.AutoMirrored.Filled.ArrowForward
                )
                // Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                FocusCard(themeMode, navController, loginUiState, airConditionUiState, uiState)
                // Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                TodayCourseCard(uiState.todayCourseList, themeMode, loginUiState)
                // Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                CommonAppsCard(
                    uiState = uiState,
                    navController = navController,
                    applicationViewModel = applicationViewModel,
                    loginUiState = loginUiState,
                    themeMode = themeMode
                )
                // Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                NewsCard(
                    themeMode = themeMode,
                    navController = navController,
                    newsListStatus = uiState.newsList
                )
            }
        }
    }

}

@Composable
fun NewsCard(
    themeMode: Int,
    navController: NavController,
    newsListStatus: ResultWithStatus<List<NewsItemEntity>>
) {
    LargeCardDisplay(
        themeMode = themeMode,
        modifier = Modifier,
        title = "学术预告",
        actionText = stringResource(id = R.string.all),
        leadingIconPainting = R.drawable.ic_outline_article,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 320.dp, max = 320.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
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
                                newsListStatus.data.forEach { news ->
                                    NewsItem(news = news, maxLines = 2) {
                                        navController.navigateToWebView(
                                            url = news.url,
                                            label = context.getString(news.label.label)
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        },
        navigateTo = {},
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant
    )
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
        leadingIconPainting = R.drawable.center_focus_weak_24px,
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
    todayCourseResult: ResultWithStatus<List<Course>>,
    themeMode: Int,
    loginUiState: LoginUiState
) {
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
                            }
                        }
                    }
                }

                else -> {
                    if (loginUiState.loginJWCState == 1)
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(86.dp)
                                .fillMaxWidth()
                        )
                    else
                        EmptyContent(
                            modifier = Modifier
                                .height(86.dp)
                                .fillMaxWidth(),
                            text = "请登录教务系统"
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
    applicationViewModel: ApplicationViewModel,
    loginUiState: LoginUiState,
    themeMode: Int
) {
    val rowCount = remember { derivedStateOf { ceil(uiState.appListIsCommonList.size / 5.0) } }
    val lazyVerticalGridHeight by remember { derivedStateOf { rowCount.value * 70 + (rowCount.value - 1) * 12 } }
    LargeCardDisplay(
        themeMode = themeMode,
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier,
        title = stringResource(id = R.string.common_applications),
        leadingIconPainting = R.drawable.app_registration_24px,
        actionText = "编辑",
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
                    verticalArrangement = Arrangement.spacedBy(4.dp),
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
            navController.navigate(Destinations.ApplicationEdit.route)
        }
    )
}