package com.xhand.hnu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.xhand.hnu.screens.NavHostScreen
import com.xhand.hnu.ui.theme.MyApplicationTheme
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
import com.xhand.hnu.viewmodel.LocalNewsViewModel
import com.xhand.hnu.viewmodel.LocalUserViewModel
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

data class BottomNavigationItem(
    var title: String,
    var selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            CompositionLocalProvider(
                LocalUserViewModel provides SettingsViewModel(context),
                LocalNewsViewModel provides NewsViewModel(context),
            ) {
                val viewModel = LocalUserViewModel.current
                val newsViewModel = LocalNewsViewModel.current
                val currentVersion = stringResource(id = R.string.version)
                LaunchedEffect(Unit) {
                    viewModel.updateService(currentVersion)
                    viewModel.noticeService()
                    if (viewModel.ifNeedUpdate)
                        viewModel.isShowUpdateDialog = true
                }
                MyApplicationTheme(
                    viewModel = viewModel
                ) {
                    NavHostScreen(
                        viewModel = viewModel,
                        newsViewModel = newsViewModel,
                        courseSearchViewModel = CourseSearchViewModel(context),
                        courseTaskViewModel = CourseTaskViewModel(),
                        context = context
                    )
                }
            }
        }
    }
}