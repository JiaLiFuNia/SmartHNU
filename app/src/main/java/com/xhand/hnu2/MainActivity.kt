package com.xhand.hnu2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xhand.hnu2.screens.NavigationScreen
import com.xhand.hnu2.screens.PersonScreen
import com.xhand.hnu2.screens.SettingScreen
import com.xhand.hnu2.ui.theme.MyApplicationTheme
import com.xhand.hnu2.network.getNewsList
import com.xhand.hnu2.viewmodel.SettingsViewModel

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
            val list = getNewsList()
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
                        hasNews = remember { mutableStateOf(true) }
                    )
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shadowElevation = 3.dp
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
                                0 -> PersonScreen()
                                1 -> NavigationScreen(list)
                                2 -> SettingScreen(settingsViewModel = SettingsViewModel())
                            }
                        }
                    }
                }
            }
        }
    }
}