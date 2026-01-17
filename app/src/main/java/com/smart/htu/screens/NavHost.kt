package com.smart.htu.screens

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.component.PdfReaderView
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.application.ApplicationEdit
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.application.airCondition.AirCondition
import com.smart.htu.screens.application.airCondition.AirConditionSetting
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.campusLife.CampusLife
import com.smart.htu.screens.application.classroom.ClassroomSearchScreen
import com.smart.htu.screens.application.courseHelper.CourseHelperNavHost
import com.smart.htu.screens.application.courseSearch.CourseSearchNavHost
import com.smart.htu.screens.application.courseSearch.CourseSearchRepo
import com.smart.htu.screens.application.courseTable.CourseTable
import com.smart.htu.screens.application.examSchedule.AddExamSchedule
import com.smart.htu.screens.application.examSchedule.ExamSchedule
import com.smart.htu.screens.application.grade.Grade
import com.smart.htu.screens.application.librarySearch.LibrarySearchDetail
import com.smart.htu.screens.application.librarySearch.LibrarySearchScreen
import com.smart.htu.screens.application.messageBoard.MessageBoard
import com.smart.htu.screens.application.messageBoard.MessageBoardDetail
import com.smart.htu.screens.application.secondClass.SecondClass
import com.smart.htu.screens.application.teacherEvaluation.TeacherEvaluation
import com.smart.htu.screens.application.teacherEvaluation.TeacherEvaluationDetail
import com.smart.htu.screens.application.textbook.Textbook
import com.smart.htu.screens.application.textbook.TextbookSelect
import com.smart.htu.screens.application.websiteNavigation.WebsiteNavigation
import com.smart.htu.screens.application.webview.ApplicationWebView
import com.smart.htu.screens.login.LoginScreen
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.message.MessageScreen
import com.smart.htu.screens.message.MessageViewModel
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsMark
import com.smart.htu.screens.news.NewsSearch
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.news.newsView.NewsDetail
import com.smart.htu.screens.person.AccountManage
import com.smart.htu.screens.person.PersonScreen
import com.smart.htu.screens.setting.AIConfigurationScreen
import com.smart.htu.screens.setting.About
import com.smart.htu.screens.setting.ArticleStyle
import com.smart.htu.screens.setting.License
import com.smart.htu.screens.setting.SettingViewModel
import com.smart.htu.screens.setting.feedback.Feedback
import com.smart.htu.utils.startAppUrl
import com.smart.htu.utils.startLaunchAPK
import kotlinx.serialization.json.Json

@Composable
fun NavHostScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val newsViewModel: NewsViewModel = hiltViewModel()
    val airConditionViewModel: AirConditionViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()
    val messageViewModel: MessageViewModel = hiltViewModel()
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
                messageViewModel = messageViewModel,
                airConditionViewModel = airConditionViewModel,
                settingViewModel = settingViewModel
            )
        }
        animatedComposable(Destinations.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        animatedComposable(Destinations.Person.route) {
            PersonScreen(navController = navController, viewModel = loginViewModel)
        }
        animatedComposable(Destinations.Message.route) {
            MessageScreen(navController = navController, viewModel = messageViewModel)
        }
        animatedComposable(Destinations.ApplicationEdit.route) {
            ApplicationEdit(navController = navController)
        }
        animatedComposable(Destinations.ClassroomSearch.route) {
            ClassroomSearchScreen(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.ApplicationWebView.route}/{url}/{title}",
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
            ApplicationWebView(
                url = url,
                title = webview.arguments?.getString("title") ?: "",
                navController = navController,
            )
        }
        animatedComposable(Destinations.License.route) {
            License(navController = navController)
        }
        animatedComposable(Destinations.LibrarySearch.route) {
            LibrarySearchScreen(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.LibrarySearchDetail.route}/{bookId}",
            arguments = listOf(
                navArgument(name = "bookId") {
                    type = NavType.StringType
                }
            )
        ) {
            LibrarySearchDetail(
                navController = navController,
                bookId = it.arguments?.getString("bookId").toString()
            )
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
        animatedComposable(
            route = "${Destinations.TeacherEvaluationDetail.route}/{syllabusEvaluateCode}/{teacherCode}",
            arguments = listOf(
                navArgument(name = "syllabusEvaluateCode") {
                    type = NavType.StringType
                },
                navArgument(name = "teacherCode") {
                    type = NavType.StringType
                }
            )
        ) {
            TeacherEvaluationDetail(
                navController = navController,
                syllabusEvaluateCode = it.arguments?.getString("syllabusEvaluateCode") ?: "",
                teacherCode = it.arguments?.getString("teacherCode") ?: ""
            )
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
            NewsSearch(navController = navController, viewModel = newsViewModel)
        }
        animatedComposable(Destinations.NewsHistory.route) {
            NewsMark(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.NewsDetail.route}/{url}/{title}/{source}",
            arguments = listOf(
                navArgument(name = "url") {
                    type = NavType.StringType
                },
                navArgument(name = "title") {
                    type = NavType.StringType
                },
                navArgument(name = "source") {
                    type = NavType.StringType
                }
            )
        ) { webview ->
            val url = webview.arguments?.getString("url") ?: ""
            NewsDetail(
                navController = navController,
                url = Uri.decode(url),
                title = webview.arguments?.getString("title") ?: "",
                source = webview.arguments?.getString("source") ?: ""
            )
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
        animatedComposable(Destinations.AIConfiguration.route) {
            AIConfigurationScreen(navController = navController)
        }
        animatedComposable(
            route = "${Destinations.PdfReaderView.route}/{url}/{title}",
            arguments = listOf(
                navArgument(name = "url") {
                    type = NavType.StringType
                },
                navArgument(name = "title") {
                    type = NavType.StringType
                }
            )
        ) { webview ->
            val url = webview.arguments?.getString("url") ?: ""
            PdfReaderView(
                navController = navController,
                url = Uri.decode(url),
                title = webview.arguments?.getString("title") ?: ""
            )
        }
        animatedComposable(Destinations.MessageBoard.route) {
            MessageBoard(navController)
        }
        animatedComposable(
            route = "${Destinations.MessageBoardDetail.route}/{postID}",
            arguments = listOf(
                navArgument(name = "postID") {
                    type = NavType.StringType
                }
            )
        ) {
            MessageBoardDetail(
                postID = it.arguments?.getString("postID").toString(),
                navController = navController
            )
        }
        animatedComposable(Destinations.CampusLife.route) {
            CampusLife(navController)
        }
        animatedComposable(Destinations.ArticleStyle.route) {
            ArticleStyle(navController)
        }
        animatedComposable(Destinations.ExamSchedule.route) {
            ExamSchedule(navController)
        }
        animatedComposable(
            route = "${Destinations.AddExamSchedule.route}/{exam}",
            arguments = listOf(
                navArgument(name = "exam") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val examString = it.arguments?.getString("exam")
            val exam = if (examString.isNullOrEmpty() || examString == "null") {
                null
            } else {
                Json.decodeFromString<ExamEntity>(examString)
            }
            AddExamSchedule(navController = navController, exam = exam)
        }
        animatedComposable(Destinations.SecondClass.route) {
            SecondClass(navController = navController)
        }
        animatedComposable(Destinations.CourseHelperNavHost.route) {
            CourseHelperNavHost(navController)
        }
        animatedComposable(Destinations.CourseSearchNavHost.route) {
            CourseSearchNavHost(navController)
        }
        animatedComposable(
            route = "${Destinations.CourseSearchRepo.route}/{searchInfo}",
            arguments = listOf(
                navArgument(name = "searchInfo") {
                    type = NavType.StringType
                }
            )
        ) {
            val searchInfoString = it.arguments?.getString("searchInfo") ?: ""
            val searchInfo = Json.decodeFromString<CourseSearchPostEntity>(searchInfoString)
            CourseSearchRepo(navController, searchInfo)
        }
    }
}

fun NavController.navigateWithCheckLoginState(
    isGuest: Boolean = false,
    route: String? = null,
    routeType: RouteType? = null,
    label: Int = 0,
    logState: Boolean,
    loginRoute: String = Destinations.Login.route
) {
    // Log.i("TAG nav", "$route $routeType $logState")
    if (logState || isGuest) {
        when (routeType) {
            RouteType.Url -> {
                this.navigateToWebView(
                    url = route ?: "",
                    label = context.getString(label)
                )
            }

            RouteType.Screen -> {
                this.navigate(route!!)
            }

            RouteType.ALIPAY -> {
                startAppUrl(route!!)
            }

            RouteType.ExternalApp -> {
                startLaunchAPK(route!!)
            }

            else -> {
            }
        }
    } else {/*
        this.currentBackStackEntry?.savedStateHandle?.set("original_route", route)
        this.currentBackStackEntry?.savedStateHandle?.set("original_url", route)
        this.currentBackStackEntry?.savedStateHandle?.set("original_label", label)*/
        this.navigate(loginRoute)
    }
}

fun NavController.navigateToWebView(
    url: String,
    label: String
) {
    this.navigate("${Destinations.ApplicationWebView.route}/${Uri.encode(url)}/${label}")
}