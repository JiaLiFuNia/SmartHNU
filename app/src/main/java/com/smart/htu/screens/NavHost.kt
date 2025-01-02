package com.smart.htu.screens

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.component.WebView
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.application.Application
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.application.classroom.ClassroomSearchScreen
import com.smart.htu.screens.application.librarySearch.LibrarySearchScreen
import com.smart.htu.screens.login.LoginNavHostScreen
import com.smart.htu.screens.login.LoginScreen
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageScreen
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsScreen
import com.smart.htu.screens.person.PersonScreen
import com.smart.htu.screens.setting.AppSettingScreen
import com.smart.htu.screens.setting.AppreciateScreen
import com.smart.htu.screens.setting.DynamicColorSettingScreen
import com.smart.htu.screens.setting.MainSettingScreen
import com.smart.htu.screens.setting.NewsSettingScreen
import com.smart.htu.screens.setting.SettingScreen
import com.smart.htu.screens.setting.SettingViewModel

@Composable
fun NavHostScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    applicationViewModel: ApplicationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.App.route
    ) {
        animatedComposable(Destinations.App.route) {
            MainFrame(
                navController = navController,
                mainViewModel = mainViewModel,
                loginViewModel = loginViewModel,
                applicationViewModel = applicationViewModel
            )
        }
        animatedComposable(Destinations.News.route) {
            NewsScreen(
                navController = navController
            )
        }
        animatedComposable(Destinations.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.Application.route) {
            Application(
                navController = navController,
                viewModel = applicationViewModel,
                loginViewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.Person.route) {
            PersonScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.Message.route) {
            MessageScreen(
                navController = navController
            )
        }
        animatedComposable(Destinations.Setting.route) {
            SettingScreen(
                navController = navController,
                viewModel = settingViewModel,
                loginViewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.DynamicColorSetting.route) {
            DynamicColorSettingScreen(
                navController = navController,
                viewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.MainSetting.route) {
            MainSettingScreen(
                navController = navController,
                viewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.AppSetting.route) {
            AppSettingScreen(
                navController = navController,
                viewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.NewsSetting.route) {
            NewsSettingScreen(
                navController = navController,
                viewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.ClassroomSearch.route) {
            ClassroomSearchScreen(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.WebView.route}/{url}/{title}",
            arguments = listOf(
                navArgument(name = "url") {
                    type = NavType.StringType
                },
                navArgument(name = "title") {
                    type = NavType.IntType
                }
            )
        ) { webview ->
            WebView(
                navController = navController,
                url = Uri.decode(webview.arguments?.getString("url") ?: ""),
                initTitle = webview.arguments?.getInt("title") ?: 0
            )
        }
        animatedComposable(Destinations.Appreciate.route) {
            AppreciateScreen(navController = navController)
        }
        animatedComposable(Destinations.LibrarySearch.route) {
            LibrarySearchScreen(navController = navController)
        }
    }
}

fun NavController.navigateWithAuthCheck(
    route: String? = null,
    url: String? = null,
    label: Int,
    logState: Boolean,
    loginRoute: String = Destinations.Login.route
) {
    Log.i("TAG nav", "$route $url $logState")
    if (logState) {
        if (route != null)
            this.navigate(route)
        if (url != null)
            this.navigate("${Destinations.WebView.route}/${Uri.encode(url)}/${label}")
    } else {
        this.navigate(loginRoute)
    }
}
