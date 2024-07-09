package com.xhand.hnu.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xhand.hnu.R
import com.xhand.hnu.components.CardCourseList
import com.xhand.hnu.components.Chart.GPAChangeLineChart
import com.xhand.hnu.components.ModalBottomSheet
import com.xhand.hnu.components.PersonCardItem
import com.xhand.hnu.components.PersonFunctionCardItem
import com.xhand.hnu.components.ShowLoginDialog
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
fun NavigationPersonScreen(viewModel: SettingsViewModel, personViewModel: PersonViewModel) {
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
            MessageScreen(onBack = { navController.popBackStack() }, viewModel = viewModel)
        }
        composable("classroom_screen") {
            ClassroomScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
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
                viewModel = viewModel
            )
        }
        composable("plan_screen") {
            StudyPlanScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("teacher_screen") {
            TeacherScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
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
    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }
    val showModalBottomSheetEdit = rememberSaveable { mutableStateOf(false) }
    val checkboxes = viewModel.checkboxes
    val otherCards = viewModel.functionCards
    val scrollState = rememberScrollState()
    var text by remember {
        mutableStateOf("")
    }
    val userInfo = viewModel.userInfo
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val schedule = viewModel.todaySchedule
    val hasMessage = viewModel.hasMessage
    LaunchedEffect(viewModel.isLoginSuccess) {
        if (viewModel.isLoginSuccess) {
            viewModel.isGettingGrade = true
            viewModel.todaySchedule()
            viewModel.messageService()
            viewModel.JDService()
            viewModel.isGettingCourse = false
            // viewModel.holidayService()
        }
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
                            if (viewModel.hasMessage.size == 0)
                                Toast.makeText(context, "没有消息", Toast.LENGTH_SHORT).show()
                            else
                                navController.navigate("message_screen")
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (viewModel.hasMessage.size != 0) {
                                    Badge {
                                        Text(text = "${viewModel.hasMessage.size}")
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
                    /*IconButton(
                        onClick = {
                            showModalBottomSheetEdit.value = !showModalBottomSheetEdit.value
                            text = "编辑"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit, contentDescription = "编辑"
                        )
                    }*/
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
                        } else {
                            text = userInfo.name
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
                                    .size(75.dp)
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
                                    text = "${userInfo?.studentID}\n${userInfo?.academy}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    /*Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        // val random = Random(System.currentTimeMillis())
                        // val randomNumber = random.nextInt(1, 12)
                        Image(
                            painter = painterResource(id = ic_ids[9]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(75.dp)
                                .padding(start = 15.dp)
                        ) {
                            if (viewModel.isLoginSuccess) {
                                Text(
                                    text = userInfo?.name ?: "",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = userInfo?.studentID ?: "",
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = userInfo?.academy ?: "", fontSize = 15.sp, color = Color.Gray
                                )
                            } else {
                                Text(text = "未登录", fontSize = 25.sp)
                            }
                        }
                    }*/
                }
                Spacer(modifier = Modifier.height(14.dp))
                PersonCardItem(
                    isChecked = true,
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
                    isChecked = checkboxes[0].isChecked,
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
                    isChecked = checkboxes[1].isChecked,
                    onclick = {
                        if (userInfo == null) {
                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                        } else {
                            checkboxes[1].route?.let { navController.navigate(it) }
                        }
                    },
                    text = checkboxes[1].text,
                    rightText = null,
                    imageVector = checkboxes[1].imageVector,
                    content = {
                        Box(
                            modifier = Modifier
                                .height(200.dp).padding(10.dp)
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

            }
        }
    }
    if (viewModel.isShowDialog) {
        ShowLoginDialog(viewModel)
    }
    ModalBottomSheet(showModalBottomSheet, text) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "个人信息",
                fontWeight = FontWeight.W900,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    ModalBottomSheet(showModalBottomSheetEdit, text) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "显示板块",
                fontWeight = FontWeight.W900,
                color = MaterialTheme.colorScheme.primary
            )
        }
        checkboxes.forEachIndexed { index, info ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .clickable {
                        checkboxes[index] = info.copy(
                            isChecked = !info.isChecked
                        )
                    }) {
                Checkbox(checked = info.isChecked, onCheckedChange = { isChecked ->
                    checkboxes[index] = info.copy(
                        isChecked = isChecked
                    )
                })
                Text(text = info.text)
            }
        }
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
