package com.xhand.hnu.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xhand.hnu.BottomNavigationItem
import com.xhand.hnu.R
import com.xhand.hnu.components.UpdateDialog
import com.xhand.hnu.model.NetworkConnectionState
import com.xhand.hnu.model.rememberConnectivityState
import com.xhand.hnu.ui.icon.rememberWifiOff
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
import com.xhand.hnu.viewmodel.GradeViewModel
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun MainFrame(
    viewModel: SettingsViewModel,
    newsViewModel: NewsViewModel,
    gradeViewModel: GradeViewModel,
    navController: NavHostController,
    context: Context
) {
    val connectionState by rememberConnectivityState()

    val isConnected by remember(connectionState) {
        derivedStateOf { connectionState == NetworkConnectionState.Available }
    }
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = "我的",
            selectedIcon = R.drawable.ic_filled_person,
            unselectedIcon = R.drawable.ic_outline_person,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "新闻",
            selectedIcon = R.drawable.ic_filled_article,
            unselectedIcon = R.drawable.ic_outline_article,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "设置",
            selectedIcon = R.drawable.ic_filled_settings,
            unselectedIcon = R.drawable.ic_outline_settings,
            hasNews = viewModel.ifNeedUpdate
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    if (isConnected) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                },
                                label = {
                                    Text(text = bottomNavigationItem.title)
                                },
                                alwaysShowLabel = true,
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (bottomNavigationItem.hasNews) {
                                                Badge()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            painterResource(
                                                id = if (index == selectedItemIndex) {
                                                    bottomNavigationItem.selectedIcon
                                                } else
                                                    bottomNavigationItem.unselectedIcon
                                            ),
                                            contentDescription = bottomNavigationItem.title
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    when (selectedItemIndex) {
                        0 -> PersonScreen(
                            navController = navController,
                            viewModel = viewModel,
                            gradeViewModel = gradeViewModel,
                            context = context
                        )

                        1 -> NewsScreen(
                            navController = navController,
                            newsViewModel = newsViewModel
                        )

                        2 -> SettingScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
            UpdateDialog(viewModel = viewModel)
        }
    } else {
        NetworkScreen()
    }
}

@Composable
fun NetworkScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = rememberWifiOff(), contentDescription = null)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "无网络连接",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}