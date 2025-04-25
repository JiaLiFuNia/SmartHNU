package com.smart.htu.screens

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.component.WebViewContent
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.application.ApplicationEdit
import com.smart.htu.screens.application.ApplicationViewModel
import com.smart.htu.screens.application.airCondition.AirCondition
import com.smart.htu.screens.application.airCondition.AirConditionSetting
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.classroom.ClassroomSearchScreen
import com.smart.htu.screens.application.entity.RouteType
import com.smart.htu.screens.application.grade.Grade
import com.smart.htu.screens.application.librarySearch.LibrarySearchScreen
import com.smart.htu.screens.application.teacherEvaluation.TeacherEvaluation
import com.smart.htu.screens.application.textbook.Textbook
import com.smart.htu.screens.application.textbook.TextbookSelect
import com.smart.htu.screens.login.LoginScreen
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageScreen
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.person.AccountManage
import com.smart.htu.screens.setting.About
import com.smart.htu.screens.setting.AppSettingScreen
import com.smart.htu.screens.setting.DynamicColorSettingScreen
import com.smart.htu.screens.setting.Licence
import com.smart.htu.screens.setting.MainSettingScreen
import com.smart.htu.screens.setting.NewsSettingScreen
import com.smart.htu.screens.setting.SettingScreen
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.utils.startAppUrl
import com.smart.htu.utils.startLaunchAPK

@Composable
fun NavHostScreen() {
    val settingViewModel: SettingViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val applicationViewModel: ApplicationViewModel = hiltViewModel()
    val newsViewModel: NewsViewModel = hiltViewModel()
    val messageViewModel: MessageViewModel = hiltViewModel()
    val airConditionViewModel: AirConditionViewModel = hiltViewModel()
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
                applicationViewModel = applicationViewModel,
                newsViewModel = newsViewModel,
                settingViewModel = settingViewModel,
                airConditionViewModel = airConditionViewModel
            )
        }
        animatedComposable(Destinations.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.Message.route) {
            MessageScreen(
                navController = navController,
                viewModel = messageViewModel
            )
        }
        animatedComposable(Destinations.Setting.route) {
            SettingScreen(
                navController = navController,
                viewModel = settingViewModel
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
        animatedComposable(Destinations.ApplicationEdit.route) {
            ApplicationEdit(navController = navController, viewModel = applicationViewModel)
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
                    type = NavType.StringType
                }
            )
        ) { webview ->
            WebViewContent(
                navController = navController,
                url = Uri.decode(webview.arguments?.getString("url") ?: ""),
                title = webview.arguments?.getString("title") ?: ""
            )
        }
        animatedComposable(Destinations.Appreciate.route) {
            Licence(navController = navController, viewModel = settingViewModel)
        }
        animatedComposable(Destinations.LibrarySearch.route) {
            LibrarySearchScreen(navController = navController)
        }
        animatedComposable(Destinations.AirCondition.route) {
            AirCondition(
                navController = navController,
                viewModel = airConditionViewModel
            )
        }
        animatedComposable(Destinations.AirConditionSetting.route) {
            AirConditionSetting(
                navController = navController,
                viewModel = airConditionViewModel
            )
        }
        animatedComposable(Destinations.AccountManage.route) {
            AccountManage(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.About.route) {
            About(
                navController = navController,
                viewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.Grade.route) {
            Grade(navController = navController)
        }
        animatedComposable(Destinations.TeacherEvaluation.route) {
            TeacherEvaluation(navController = navController)
        }
        animatedComposable(Destinations.Textbook.route) {
            Textbook(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.TextbookSelect.route}/{courseTaskCode}/{termCode}",
            arguments = listOf(
                navArgument(name = "courseTaskCode") {
                    type = NavType.StringType
                },
                navArgument(name = "termCode") {
                    type = NavType.StringType
                }
            )
        ) {
            TextbookSelect(
                navController = navController,
                courseTaskCode = it.arguments?.getString("courseTaskCode") ?: "",
                termCode = it.arguments?.getString("termCode") ?: ""
            )
        }
    }
}

fun NavController.navigateWithAuthCheck(
    isGuest: Boolean = false,
    route: String? = null,
    routeType: RouteType? = null,
    label: Int,
    logState: Boolean,
    loginRoute: String = Destinations.Login.route
) {
    Log.i("TAG nav", "$route $routeType $logState")
    if (logState || isGuest) {
        when (routeType) {
            RouteType.URL -> {
                this.navigateToWebView(url = route ?: "", label = context.getString(label))
            }

            RouteType.SCREEN -> {
                this.navigate(route!!)
            }

            RouteType.ALIPAY -> {
                startAppUrl(route!!)
            }

            RouteType.APP -> {
                startLaunchAPK(route!!, context.getString(label))
            }

            else -> {
            }
        }
    } else {
        this.currentBackStackEntry?.savedStateHandle?.set("original_route", route)
        this.currentBackStackEntry?.savedStateHandle?.set("original_url", route)
        this.currentBackStackEntry?.savedStateHandle?.set("original_label", label)
        this.navigate(loginRoute)
    }
}

fun NavController.navigateToWebView(
    url: String,
    label: String
) {
    this.navigate("${Destinations.WebView.route}/${Uri.encode(url)}/${label}")
}
