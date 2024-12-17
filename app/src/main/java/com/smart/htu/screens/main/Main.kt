package com.smart.htu.screens.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.smart.htu.component.LargeCardDisplay
import com.smart.htu.component.MediumCardDisplay
import com.smart.htu.component.SingleCourseCard
import com.smart.htu.component.SmallCardDisplay
import com.smart.htu.screens.application.ApplicationUiState
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.ui.icon.WeatherIcon
import com.smart.htu.ui.icon.weatherIcon._303
import com.smart.htu.component.DynamicIsland
import com.smart.htu.utils.openCalendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    applicationViewModel: ApplicationViewModel,
    navigateToApplication: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val appUiState by applicationViewModel.uiState.collectAsState()

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(2000)
            isRefreshing = false
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    DynamicIsland(
                        text = if (!loginUiState.isLogSuccess)
                            stringResource(id = R.string.login_now)
                        else
                            if (isRefreshing)
                                stringResource(id = R.string.loading)
                            else
                                loginUiState.editableMessage.customUsername,
                        onClick = {
                            navController.navigate(Destinations.Login.route)
                        },
                        clickAble = !isRefreshing
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Destinations.Message.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notifications_24px),
                            contentDescription = null
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Destinations.Setting.route) },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        if (loginUiState.isLogSuccess)
                            Image(
                                painter = painterResource(id = R.drawable.avator_1),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        else
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "account",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CalendarCard(context = context, modifier = Modifier.weight(0.5f))
                        Spacer(modifier = Modifier.width(20.dp))
                        WeatherCard(modifier = Modifier.weight(0.5f))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    TodayCourseCard(uiState = uiState)
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    CommonAppsCard(
                        uiState = appUiState,
                        navController = navController,
                        navigateToApplication = navigateToApplication,
                        applicationViewModel = applicationViewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    LargeCardDisplay(
                        modifier = Modifier.height(200.dp),
                        title = stringResource(id = R.string.course_grade),
                        leadingIconPainting = R.drawable.finance_24px,
                        content = {},
                        navigateTo = {}
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}


@Composable
fun CalendarCard(context: Context, modifier: Modifier) {
    val today = LocalDate.now() // 阳历
    val dayOfWeek = today.dayOfWeek
    val dayOfWeekName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
    val chineseToday = Lunar.fromDate(Date()) // 农历
    MediumCardDisplay(
        onClick = {
        },
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
            openCalendar(context)
        }
    )
}

@Composable
fun WeatherCard(modifier: Modifier) {
    MediumCardDisplay(
        onClick = {
        },
        modifier = modifier,
        title = "多云转晴",
        leadingContent = {
            Icon(
                imageVector = WeatherIcon._303,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            /*Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "11 ℃",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }*/
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "-1~11 ℃ | 1 级风",
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    )
}

@Composable
fun TodayCourseCard(uiState: AppUiState) {
    LargeCardDisplay(
        modifier = Modifier,
        title = stringResource(id = R.string.today_course),
        leadingIconPainting = R.drawable.today_24px,
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                uiState.toDayCourseList.forEachIndexed { index, it ->
                    SingleCourseCard(
                        modifier = Modifier.fillMaxSize(),
                        onClick = {},
                        message = it
                    )
                    if (index % 2 == 0 && uiState.toDayCourseList.size >= 2) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun CommonAppsCard(
    uiState: ApplicationUiState,
    navController: NavController,
    navigateToApplication: () -> Unit,
    applicationViewModel: ApplicationViewModel
) {
    val appList = uiState.appListIsCommonList
    val lazyVerticalGridHeight by remember {
        mutableIntStateOf(((ceil(appList.size / 5.0)) * 90).toInt())
    }
    LargeCardDisplay(
        modifier = Modifier,
        title = stringResource(id = R.string.common_applications),
        leadingIconPainting = R.drawable.app_registration_24px,
        content = {
            if (appList.isEmpty())
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { }) {
                        Text(text = "点击添加常用应用")
                    }
                }
            else
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .height(lazyVerticalGridHeight.dp)
                        .padding(5.dp)
                ) {
                    items(appList.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SmallCardDisplay(
                                content = appList[index],
                                onLongClick = {
                                    applicationViewModel.changeCommonAppListState(index, false)
                                },
                                navController = navController
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