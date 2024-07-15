package com.xhand.hnu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.xhand.hnu.screens.NavigationPersonScreen
import com.xhand.hnu.screens.NavigationScreen
import com.xhand.hnu.screens.NavigationSettingScreen
import com.xhand.hnu.screens.SettingScreen
import com.xhand.hnu.ui.theme.MyApplicationTheme
import com.xhand.hnu.viewmodel.CourseSearchViewModel
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
            MyApplicationTheme {
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
                        hasNews = remember { mutableStateOf(true) }
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
                        CompositionLocalProvider(
                            LocalUserViewModel provides SettingsViewModel(LocalContext.current)
                        ) {
                            val viewModel = LocalUserViewModel.current
                            Box(modifier = Modifier.padding(it)) {
                                when (selectedItemIndex) {
                                    0 -> NavigationPersonScreen(viewModel, PersonViewModel(), CourseSearchViewModel(LocalContext.current))
                                    1 -> NavigationScreen(viewModel, NewsViewModel(LocalContext.current))
                                    2 -> NavigationSettingScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}