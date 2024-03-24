package com.xhand.hnu2.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
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
import com.xhand.hnu2.R
import com.xhand.hnu2.components.ModalBottomSheet
import com.xhand.hnu2.components.PersonCardItem
import com.xhand.hnu2.viewmodel.GradeViewModel
import com.xhand.hnu2.viewmodel.PersonViewModel
import com.xhand.hnu2.viewmodel.SettingsViewModel


@Composable
fun NavigationPersonScreen(viewModel: SettingsViewModel, personViewModel: PersonViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "person_screen",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
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
                gradeViewModel = GradeViewModel()
            )
        }
        composable("schedule_screen") {
            ScheduleScreen(
                onBack = { navController.popBackStack() })
        }
        composable("other_screen") {
            OtherScreen(
                onBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
    personViewModel: PersonViewModel
) {
    val context = LocalContext.current
    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }
    val showModalBottomSheetEdit = rememberSaveable { mutableStateOf(false) }
    val checkboxes = viewModel.checkboxes
    val scrollState = rememberScrollState()
    var text by remember {
        mutableStateOf("")
    }
    val userInfo = viewModel.userInfo
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val schedule = viewModel.todaySchedule
    LaunchedEffect(Unit) {
        viewModel.todaySchedule()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(
                            MaterialTheme.colorScheme.surface.copy()
                        )
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
                    IconButton(onClick = { personViewModel.hasMessage = false }) {
                        BadgedBox(
                            badge = {
                                if (personViewModel.hasMessage) {
                                    Badge()
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        }
                    }
                    IconButton(
                        onClick = {
                            showModalBottomSheetEdit.value = !showModalBottomSheetEdit.value
                            text = "编辑"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "编辑"
                        )
                    }

                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
                .padding(start = 15.dp, end = 15.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = {
                    if (userInfo == null) {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                    } else {
                        text = userInfo.name
                        showModalBottomSheet.value = !showModalBottomSheet.value
                    }
                },
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    // val random = Random(System.currentTimeMillis())
                    // val randomNumber = random.nextInt(1, 12)
                    Image(
                        painter = painterResource(id = ic_ids[6]),
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
                        Text(
                            text = userInfo?.name ?: "未登录",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(text = userInfo?.studentID ?: "", fontSize = 15.sp, color = Color.Gray)
                        Text(text = userInfo?.academy ?: "", fontSize = 15.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            PersonCardItem(
                isChecked = checkboxes[0].isChecked,
                onclick = {
                    if (userInfo == null) {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                    } else {
                        checkboxes[0].route?.let { navController.navigate(it) }
                    }
                },
                text = checkboxes[0].text,
                imageVector = checkboxes[0].imageVector,
                content = {
                    if (schedule.size != 0)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp, top = 8.dp),
                        ) {
                            schedule.forEach { schedule ->
                                Column {
                                    Text(text = "课程名称 ${schedule.kcmc}")
                                    Text(text = "授课教师 ${schedule.teaxms}")
                                    Text(text = "上课地点 ${schedule.jxcdmc}")
                                    Text(text = "上课时间 ${schedule.qssj} - ${schedule.jssj}")
                                    Text(text = "评价方式 ${schedule.khfsmc}")
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    else
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (viewModel.isLoginSuccess)
                                Text(text = "今日无课程", color = Color.Gray)
                            else
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
                imageVector = checkboxes[1].imageVector,
                content = {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "暂无信息", color = Color.Gray)
                    }
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
    ModalBottomSheet(showModalBottomSheet, text)

    ModalBottomSheet(showModalBottomSheetEdit, text) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = "显示板块",
                fontWeight = FontWeight.W900,
                color = MaterialTheme.colorScheme.primary
            )
        }
        checkboxes.forEachIndexed { index, info ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .clickable {
                        checkboxes[index] = info.copy(
                            isChecked = !info.isChecked
                        )
                    }
            ) {
                Checkbox(
                    checked = info.isChecked,
                    onCheckedChange = { isChecked ->
                        checkboxes[index] = info.copy(
                            isChecked = isChecked
                        )
                    }
                )
                Text(text = info.text)
            }
        }
    }
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
