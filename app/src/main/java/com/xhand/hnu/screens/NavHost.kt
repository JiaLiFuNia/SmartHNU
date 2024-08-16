package com.xhand.hnu.screens

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xhand.hnu.screens.navigation.Destinations
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import com.xhand.hnu.viewmodel.CourseTaskViewModel
import com.xhand.hnu.viewmodel.GradeViewModel
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun NavHostScreen(
    viewModel: SettingsViewModel,
    newsViewModel: NewsViewModel,
    gradeViewModel: GradeViewModel,
    context: Context
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Person.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
            )
        }
    ) {
        composable(Destinations.Person.route) {
            MainFrame(
                viewModel = viewModel,
                newsViewModel = newsViewModel,
                navController = navController,
                gradeViewModel = gradeViewModel,
                context = context
            )
        }
        composable(Destinations.Grade.route) {
            GradeScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.Message.route) {
            MessageScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(Destinations.ClassroomSearch.route) {
            ClassroomScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                roomSearchViewModel = CourseSearchViewModel()
            )
        }
        composable(Destinations.CourseSearch.route) {
            CourseSearchScreen(
                onBack = { navController.popBackStack() },
                courseSearchViewModel = CourseSearchViewModel()
            )
        }
        composable(Destinations.BookSelect.route) {
            ChooseBookScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(Destinations.ClassTask.route) {
            CourseTaskScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.Teacher.route) {
            TeacherScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(Destinations.SecondClass.route) {
            SecondClassScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.News.route) {
            NewsScreen(
                navController = navController,
                newsViewModel = newsViewModel
            )
        }
        composable(Destinations.NewsDetail.route) {
            ArticleDetailScreen(
                viewModel = viewModel,
                newsViewModel = newsViewModel,
                onClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Destinations.Setting.route) {
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Destinations.Guide.route) {
            GuideScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}