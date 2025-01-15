package com.smart.htu.screens.main

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
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
import com.smart.htu.component.LargeCardDisplay
import com.smart.htu.component.MediumCardDisplay
import com.smart.htu.component.SingleCourseCard
import com.smart.htu.component.SmallCardDisplay
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.login.LoginUiState
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.person.loginStateString
import com.smart.htu.ui.icon.WeatherIcon
import com.smart.htu.ui.icon.weatherIcon._303
import com.smart.htu.utils.startCalendar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    loginViewModel: LoginViewModel,
    navController: NavController,
    navigateToApplication: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer,
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
                            navController.navigateWithAuthCheck(
                                route = Destinations.Message.route,
                                label = R.string.message_center,
                                logState = uiState.isLogSuccess
                            )
                        }
                    ) {
                        BadgedBox(
                            badge = { Badge { Text(text = "2") } }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notifications_24px),
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
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 15.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CalendarCard(context = context, modifier = Modifier.weight(0.5f))
                        Spacer(modifier = Modifier.width(20.dp))
                        WeatherCard(modifier = Modifier.weight(0.5f))
                    }
                }
                item {
                    TodayCourseCard(uiState = uiState)
                }
                item {
                    CommonAppsCard(
                        appList = uiState.appListIsCommonList,
                        navController = navController,
                        navigateToApplication = navigateToApplication,
                        applicationViewModel = applicationViewModel,
                        uiState = loginUiState
                    )
                }
                item {
                    LargeCardDisplay(
                        modifier = Modifier.height(200.dp),
                        title = stringResource(id = R.string.course_grade),
                        leadingIconPainting = R.drawable.finance_24px,
                        content = {},
                        navigateTo = {}
                    )
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
            startCalendar()
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
                modifier = Modifier
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
    appList: List<SmallCardContent>,
    navController: NavController,
    navigateToApplication: () -> Unit,
    applicationViewModel: ApplicationViewModel,
    uiState: LoginUiState
) {
    val lazyVerticalGridHeight by remember { mutableIntStateOf(((ceil(appList.size / 5.0)) * 70).toInt() + 16) }
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
                        .height(lazyVerticalGridHeight.dp),
                    contentPadding = PaddingValues(8.dp),
                    userScrollEnabled = false
                ) {
                    items(appList.size) { index ->
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            SmallCardDisplay(
                                enabled = (uiState.isGuest && appList[index].guestEnable) || uiState.isLogSuccess,
                                content = appList[index],
                                onLongClick = {
                                    applicationViewModel.changeCommonAppListState(index, false)
                                },
                                onCLick = {
                                    navController.navigateWithAuthCheck(
                                        isGuest = uiState.isGuest && appList[index].guestEnable,
                                        appUrl = appList[index].appUrl,
                                        route = appList[index].route,
                                        url = appList[index].url,
                                        logState = uiState.isLogSuccess,
                                        label = appList[index].label
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