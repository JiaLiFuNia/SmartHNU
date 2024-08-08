package com.xhand.hnu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xhand.hnu.components.UpdateDialog
import com.xhand.hnu.model.NetworkConnectionState
import com.xhand.hnu.model.rememberConnectivityState
import com.xhand.hnu.screens.NavigationPersonScreen
import com.xhand.hnu.screens.NavigationScreen
import com.xhand.hnu.screens.NavigationSettingScreen
import com.xhand.hnu.ui.icon.rememberWifiOff
import com.xhand.hnu.ui.theme.MyApplicationTheme
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
import com.xhand.hnu.viewmodel.GradeViewModel
import com.xhand.hnu.viewmodel.LocalNewsViewModel
import com.xhand.hnu.viewmodel.LocalUserViewModel
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.PersonViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel

data class BottomNavigationItem(
    var title: String,
    var selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: MutableState<Boolean>
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val connectionState by rememberConnectivityState()

            val isConnected by remember(connectionState) {
                derivedStateOf { connectionState == NetworkConnectionState.Available }
            }
            CompositionLocalProvider(
                LocalUserViewModel provides SettingsViewModel(LocalContext.current),
                LocalNewsViewModel provides NewsViewModel(LocalContext.current),
            ) {
                val viewModel = LocalUserViewModel.current
                val newsViewModel = LocalNewsViewModel.current
                val currentVersion = stringResource(id = R.string.version)
                LaunchedEffect(Unit) {
                    viewModel.updateService(currentVersion)
                    if (viewModel.ifNeedUpdate)
                        viewModel.isShowUpdateDialog = true
                }
                MyApplicationTheme(
                    viewModel = viewModel
                ) {
                    val bottomNavigationItems = listOf(
                        BottomNavigationItem(
                            title = "我的",
                            selectedIcon = R.drawable.ic_filled_person,
                            unselectedIcon = R.drawable.ic_outline_person,
                            hasNews = remember { mutableStateOf(false) }
                        ),
                        BottomNavigationItem(
                            title = "新闻",
                            selectedIcon = R.drawable.ic_filled_article,
                            unselectedIcon = R.drawable.ic_outline_article,
                            hasNews = remember { mutableStateOf(false) }
                        ),
                        BottomNavigationItem(
                            title = "设置",
                            selectedIcon = R.drawable.ic_filled_settings,
                            unselectedIcon = R.drawable.ic_outline_settings,
                            hasNews = remember { mutableStateOf(false) }
                        )
                    )
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
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
                                                    bottomNavigationItem.hasNews.value = false
                                                },
                                                label = {
                                                    Text(text = bottomNavigationItem.title)
                                                },
                                                alwaysShowLabel = true,
                                                icon = {
                                                    BadgedBox(
                                                        badge = {
                                                            if (bottomNavigationItem.hasNews.value) {
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
                                        0 -> NavigationPersonScreen(
                                            viewModel = viewModel,
                                            personViewModel = PersonViewModel(),
                                            courseSearchViewModel = CourseSearchViewModel(
                                                LocalContext.current
                                            ),
                                            courseTaskViewModel = CourseTaskViewModel(LocalContext.current),
                                            gradeViewModel = GradeViewModel(viewModel)
                                        )

                                        1 -> NavigationScreen(viewModel, newsViewModel)
                                        2 -> NavigationSettingScreen(viewModel)
                                    }
                                }
                            }
                            UpdateDialog(viewModel = viewModel)
                        }
                    } else {
                        NetworkScreen()
                    }
                }
            }
        }
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

@Composable
@Preview
fun PreviewNetworkScreen() {
    NetworkScreen()
}