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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.xhand.hnu2.viewmodel.SettingsViewModel


@Composable
fun NavigationPersonScreen(settingsViewModel: SettingsViewModel) {
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
                settingsViewModel = settingsViewModel
            )
        }
        composable("room_screen") {
            ClassRoom(
                onBack = { navController.popBackStack() })
        }
        composable("class_screen") {
            ClassSchedule(
                onBack = { navController.popBackStack() })
        }
        composable("other_screen") {
            OtherWay(
                onBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    data class ToggleableInfo(
        val isChecked: Boolean,
        val text: String,
        val imageVector: ImageVector
    )

    val context = LocalContext.current
    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }
    val showModalBottomSheetEdit = rememberSaveable { mutableStateOf(false) }
    val checkboxes = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = true,
                text = "今日课程",
                imageVector = Icons.Default.DateRange

            ),
            ToggleableInfo(
                isChecked = true,
                text = "消息中心",
                imageVector = Icons.Default.Email
            ),
            ToggleableInfo(
                isChecked = true,
                text = "课程成绩",
                imageVector = Icons.Default.Edit
            ),
        )
    }
    val scrollState = rememberScrollState()
    var text by remember {
        mutableStateOf("")
    }
    val userInfo = settingsViewModel.userInfo
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
                            text = if (settingsViewModel.logined) {
                                "你好！${userInfo?.name}"
                            } else {
                                "你好！"
                            }
                        )
                    } else {
                        Text(text = "我的")
                    }
                },
                actions = {
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
                    text = userInfo?.name ?: "请先登录"
                    showModalBottomSheet.value = !showModalBottomSheet.value
                    if (userInfo == null) {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
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
                            text = userInfo?.name ?: "请先登录",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(text = userInfo?.studentID ?: "", fontSize = 15.sp, color = Color.Gray)
                        Text(text = userInfo?.academy ?: "", fontSize = 15.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            val smallCard = Modifier
                .height(70.dp)
                .weight(1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = smallCard,
                    onClick = { navController.navigate("class_screen") }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 3.dp)
                                    .size(25.dp)
                            )
                            Text(
                                text = "课表",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = smallCard,
                    onClick = { navController.navigate("room_screen") }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 3.dp)
                                    .size(25.dp)
                            )
                            Text(
                                text = "教室",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = smallCard,
                    onClick = { navController.navigate("other_screen") }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 3.dp)
                                    .size(25.dp)
                            )
                            Text(
                                text = "其他",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            checkboxes.forEach { cards ->
                PersonCardItem(
                    onclick = { },
                    text = cards.text,
                    imageVector = cards.imageVector,
                    ifShowCard = cards.isChecked
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
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
