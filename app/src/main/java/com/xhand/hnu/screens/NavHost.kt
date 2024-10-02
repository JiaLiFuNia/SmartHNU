package com.xhand.hnu.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xhand.hnu.components.animatedComposable
import com.xhand.hnu.screens.navigation.Destinations
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun NavHostScreen(
    viewModel: SettingsViewModel,
    context: Context
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Person.route
    ) {
        animatedComposable(Destinations.Person.route) {
            MainFrame(
                viewModel = viewModel,
                navController = navController,
                context = context
            )
        }
        animatedComposable(Destinations.Grade.route) {
            GradeScreen(
                onBack = { navController.popBackStack() }
            )
        }
        animatedComposable(Destinations.Message.route) {
            MessageScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                navController = navController
            )
        }
        animatedComposable(Destinations.ClassroomSearch.route) {
            ClassroomScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                roomSearchViewModel = CourseSearchViewModel()
            )
        }
        animatedComposable(Destinations.CourseSearch.route) {
            CourseSearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
        animatedComposable(Destinations.BookSelect.route) {
            ChooseBookScreen(
                onBack = { navController.popBackStack() }
            )
        }
        animatedComposable(Destinations.ClassTask.route) {
            CourseTaskScreen(
                onBack = { navController.popBackStack() }
            )
        }
        animatedComposable(Destinations.Teacher.route) {
            TeacherScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        animatedComposable(Destinations.SecondClass.route) {
            SecondClassScreen(
                onBack = { navController.popBackStack() }
            )
        }
        animatedComposable(Destinations.News.route) {
            NewsScreen(
                navController = navController,
                context = context
            )
        }
        animatedComposable(
            route = "${Destinations.NewsDetail.route}/{url}/{title}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                },
                navArgument("title") {
                    type = NavType.StringType
                })
        ) { news ->
            ArticleDetailScreen(
                context = context,
                onClick = {
                    navController.popBackStack()
                },
                url = news.arguments?.getString("url") ?: "",
                title = news.arguments?.getString("title") ?: ""
            )
        }
        animatedComposable(Destinations.Setting.route) {
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        animatedComposable(Destinations.Guide.route) {
            GuideScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                context = context
            )
        }
    }
}