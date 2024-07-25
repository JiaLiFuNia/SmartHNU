package com.xhand.hnu

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.xhand.hnu.components.UpdateDialog
import com.xhand.hnu.screens.NavigationPersonScreen
import com.xhand.hnu.screens.NavigationScreen
import com.xhand.hnu.screens.NavigationSettingScreen
import com.xhand.hnu.ui.theme.MyApplicationTheme
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
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
                                        viewModel,
                                        PersonViewModel(),
                                        CourseSearchViewModel(LocalContext.current),
                                        CourseTaskViewModel(LocalContext.current)
                                    )
                                    1 -> NavigationScreen(viewModel, newsViewModel)
                                    2 -> NavigationSettingScreen(viewModel)
                                }
                            }
                        }
                    UpdateDialog(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
