package com.xhand.hnu.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xhand.hnu.R
import com.xhand.hnu.components.CardCourseList
import com.xhand.hnu.components.Chart.GPAChangeLineChart
import com.xhand.hnu.components.PersonCardItem
import com.xhand.hnu.components.PersonFunctionCardItem
import com.xhand.hnu.components.ShowLoginDialog
import com.xhand.hnu.components.ShowSecondClassLoginDialog
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
import com.xhand.hnu.viewmodel.GradeViewModel
import com.xhand.hnu.viewmodel.PersonViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun NavigationPersonScreen(
    viewModel: SettingsViewModel,
    personViewModel: PersonViewModel,
    courseSearchViewModel: CourseSearchViewModel,
    courseTaskViewModel: CourseTaskViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "person_screen",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        }
    ) {
        composable("person_screen") {
            PersonScreen(
                navController = navController,
                viewModel = viewModel,
                personViewModel = personViewModel
            )
        }
        composable("grade_screen") {
            GradeScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                gradeViewModel = GradeViewModel(viewModel)
            )
        }
        composable("schedule_screen") {
            ScheduleScreen(onBack = { navController.popBackStack() })
        }
        composable("message_screen") {
            MessageScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("classroom_screen") {
            ClassroomScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                roomSearchViewModel = courseSearchViewModel
            )
        }
        composable("courseSearch_screen") {
            CourseSearchScreen(
                onBack = { navController.popBackStack() },
                courseSearchViewModel = courseSearchViewModel
            )
        }
        composable("search_screen") {
            ChooseBookScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("task_screen") {
            CourseTaskScreen(
                onBack = { navController.popBackStack() },
                viewModel = courseTaskViewModel
            )
        }
        composable("teacher_screen") {
            TeacherScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("secondClass_screen") {
            SecondClassScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController, viewModel: SettingsViewModel, personViewModel: PersonViewModel
) {
    val context = LocalContext.current
    val checkboxes = viewModel.checkboxes
    val otherCards = viewModel.functionCards
    val scrollState = rememberScrollState()
    val userInfo = viewModel.userInfo
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val schedule = viewModel.todaySchedule
    val hasMessage = viewModel.hasMessage
    LaunchedEffect(viewModel.isLoginSuccess) {
        viewModel.checkToken()
        if (viewModel.isLoginSuccess) {
            viewModel.isGettingGrade = true
            viewModel.todaySchedule()
            viewModel.messageService()
            viewModel.JDService()
            viewModel.isGettingCourse = false
            // viewModel.holidayService()
        }
    }
    LaunchedEffect(viewModel.stateCode == 1) {
        if (viewModel.stateCode == 1)
            viewModel.getHourList()
    }
    LaunchedEffect(viewModel.stateCode == 0) {
        if (viewModel.stateCode == 0)
            viewModel.secondClassService()
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh = {
        isRefreshing = true
        coroutineScope.launch {
            delay(timeMillis = 1000)
            if (viewModel.isLoginSuccess) {
                viewModel.todaySchedule()
                viewModel.messageService()
            }
            isRefreshing = false
        }
    }
    // 获取SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()
    val statueBarColor = MaterialTheme.colorScheme.surfaceContainer
    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(color = statueBarColor)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = {
                    if (scrollBehavior.state.heightOffset < 0f) {
                        Text(
                            text = if (viewModel.isLoginSuccess) {
                                "欢迎！${userInfo?.name}"
                            } else {
                                "欢迎！"
                            }
                        )
                    } else {
                        Text(text = "欢迎！")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (viewModel.isLoginSuccess) {
                                if (viewModel.hasMessage.size == 0)
                                    Toast.makeText(context, "没有消息", Toast.LENGTH_SHORT).show()
                                else
                                    navController.navigate("message_screen")
                            }
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (viewModel.hasMessage.size != 0) {
                                    Badge {
                                        Text(text = if (viewModel.hasMessage.size <= 9) "${viewModel.hasMessage.size}" else "9+")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (hasMessage.size > 0) Icons.Default.Email else Icons.Outlined.Email,
                                contentDescription = "消息"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { values ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            modifier = Modifier
                .padding(values),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    onClick = {
                        if (userInfo == null) {
                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                            viewModel.isShowDialog = true
                        }
                    },
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    ListItem(
                        leadingContent = {
                            Image(
                                painter = painterResource(id = ic_ids[9]),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                        },
                        headlineContent = {
                            Text(
                                text = userInfo?.name ?: "未登录",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        supportingContent = {
                            if (viewModel.isLoginSuccess)
                                Text(
                                    text = "${userInfo?.academy}",
                                    color = Color.Gray
                                )
                        },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                PersonCardItem(
                    onclick = { },
                    text = "快捷方式",
                    rightText = null,
                    imageVector = Icons.Default.Home,
                    content = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                otherCards.forEach { functionCard ->
                                    PersonFunctionCardItem(
                                        modifier = Modifier
                                            .weight(0.2f),
                                        title = functionCard.title,
                                        painterResource = functionCard.painterResource,
                                        onClick = {
                                            if (userInfo == null) {
                                                viewModel.isShowDialog = true
                                                Toast.makeText(
                                                    context,
                                                    "请先登录",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                navController.navigate(functionCard.route)
                                            }
                                        }
                                    )
                                }
                            }
                        }

                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                val currentDate = LocalDate.now()
                val dayOfMonth = currentDate.dayOfMonth
                val month = currentDate.monthValue
                val dayOfWeek =
                    currentDate.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.SIMPLIFIED_CHINESE
                    )
                PersonCardItem(
                    onclick = {
                        /*if (userInfo == null) {
                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                        } else {*/
                        // checkboxes[0].route?.let { navController.navigate(it) }
                        //  }
                    },
                    text = checkboxes[0].text,
                    rightText = "${month}月${dayOfMonth}日 $dayOfWeek",
                    imageVector = checkboxes[0].imageVector,
                    content = {
                        if (viewModel.isLoginSuccess) {
                            if (viewModel.isGettingCourse)
                                Box(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            else {
                                if (schedule.size != 0)
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 10.dp, top = 8.dp, end = 10.dp)
                                    ) {
                                        schedule.forEach { schedule ->
                                            CardCourseList(
                                                schedule = schedule,
                                                onClick = {
                                                    if (userInfo == null) {
                                                        viewModel.isShowDialog = true
                                                        Toast.makeText(
                                                            context,
                                                            "请先登录",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                    } else {
                                                        /*checkboxes[0].route?.let {
                                                            navController.navigate(
                                                                it
                                                            )
                                                        }*/
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                        }
                                    }
                                else
                                    Box(
                                        modifier = Modifier
                                            .height(150.dp)
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "今天没课啦！", color = Color.Gray)
                                    }
                            }
                        } else
                            Box(
                                modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "暂无信息", color = Color.Gray)
                            }
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                PersonCardItem(
                    onclick = {
                        if (userInfo == null) {
                            viewModel.isShowDialog = true
                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                        } else {
                            checkboxes[1].route?.let { navController.navigate(it) }
                        }
                    },
                    text = checkboxes[1].text,
                    rightText = "详情 ->",
                    imageVector = checkboxes[1].imageVector,
                    content = {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .padding(10.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                        if (viewModel.isLoginSuccess) {
                            if (viewModel.isGettingJD)
                                CircularProgressIndicator()
                            else {
                                if (viewModel.jdList.size == 0)
                                    Text(
                                        text = "未能获取到你的成绩信息",
                                        color = Color.Gray
                                    )
                                else
                                    GPAChangeLineChart(viewModel.jdList)
                            }
                        } else
                                Text(text = "暂无信息", color = Color.Gray)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                PersonCardItem(
                    onclick = {
                        if (viewModel.stateCode != 1) {
                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                        }
                    },
                    text = checkboxes[2].text,
                    imageVector = checkboxes[2].imageVector,
                    content = {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .padding(10.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (viewModel.stateCode != 1) {
                                TextButton(
                                    onClick = {
                                        viewModel.isShowScDialog = true
                                    }
                                ) {
                                    Text(text = "点击登录")
                                }
                            } else {
                                if (viewModel.isGettingHourList)
                                    CircularProgressIndicator()
                                else
                                    Column {
                                        viewModel.scHourList.forEach { scHour ->
                                            ListItem(
                                                headlineContent = { Text(text = scHour.term) },
                                                trailingContent = {
                                                    Text(text = scHour.total.toString())
                                                }
                                            )
                                        }
                                    }
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
    if (viewModel.isShowDialog) {
        ShowLoginDialog(viewModel)
    }
    if (viewModel.isShowScDialog) {
        ShowSecondClassLoginDialog(viewModel = viewModel)
    }
}

// 判断时间
fun isCurrentTimeBetween(endTimeString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val currentTime = LocalTime.now()
    val currentTimeFormatted = currentTime.format(formatter)

    val givenTime = LocalTime.parse(endTimeString, formatter)
    val givenTimeFormatted = givenTime.format(formatter)

    // 使用 Duration.between 来计算两个时间之间的差值
    val duration = Duration.between(currentTime, givenTime)
    // 获取分钟数
    val minutes = duration.toMinutes()

    // 如果大于就是真
    return minutes
}

val ic_ids = listOf(
    R.drawable.ic_1,
    R.drawable.ic_2,
    R.drawable.ic_3,
    R.drawable.ic_4,
    R.drawable.ic_5,
    R.drawable.ic_6,
    R.drawable.ic_7,
    R.drawable.ic_8,
    R.drawable.ic_9,
    R.drawable.ic_10,
    R.drawable.ic_11,
    R.drawable.ic_12
)
