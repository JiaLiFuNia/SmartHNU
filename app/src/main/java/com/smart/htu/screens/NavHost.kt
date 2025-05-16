package com.smart.htu.screens

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.application.ApplicationEdit
import com.smart.htu.screens.application.WebsiteNavigation
import com.smart.htu.screens.application.airCondition.AirCondition
import com.smart.htu.screens.application.airCondition.AirConditionSetting
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.classroom.ClassroomSearchScreen
import com.smart.htu.screens.application.courseTable.CourseTable
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
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsSearch
import com.smart.htu.screens.news.NewsStar
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.person.AccountManage
import com.smart.htu.screens.setting.About
import com.smart.htu.screens.setting.License
import com.smart.htu.screens.setting.SettingScreen
import com.smart.htu.screens.setting.feedback.Feedback
import com.smart.htu.screens.webview.WebViewContent
import com.smart.htu.utils.startAppUrl
import com.smart.htu.utils.startLaunchAPK

@Composable
fun NavHostScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val newsViewModel: NewsViewModel = hiltViewModel()
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
                newsViewModel = newsViewModel,
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
            MessageScreen(navController = navController)
        }
        animatedComposable(Destinations.Setting.route) {
            SettingScreen(navController = navController)
        }
        animatedComposable(Destinations.ApplicationEdit.route) {
            ApplicationEdit(navController = navController)
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
            val url = Uri.decode(webview.arguments?.getString("url") ?: "")
            // webViewViewModel.loadCookiesForUrl(url)
            WebViewContent(
                navController = navController,
                url = url,
                title = webview.arguments?.getString("title") ?: ""
            )
        }
        animatedComposable(Destinations.License.route) {
            License(navController = navController)
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
            About(navController = navController)
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
        animatedComposable(Destinations.NewsSearch.route) {
            NewsSearch(navController = navController)
        }
        animatedComposable(Destinations.NewsStar.route) {
            NewsStar(navController = navController)
        }
        animatedComposable(Destinations.CourseTable.route) {
            CourseTable(navController = navController)
        }
        animatedComposable(Destinations.Feedback.route) {
            Feedback(navController = navController)
        }
        animatedComposable(Destinations.WebsiteNavigation.route) {
            WebsiteNavigation(navController = navController)
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
    // Log.i("TAG nav", "$route $routeType $logState")
    if (logState || isGuest) {
        when (routeType) {
            RouteType.URL -> {
                this.navigateToWebView(
                    url = route ?: "",
                    label = context.getString(label)
                )
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