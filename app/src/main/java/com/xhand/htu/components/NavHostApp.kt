package com.xhand.htu.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.xhand.htu.navigation.Destinations
import com.xhand.htu.screens.ArticleDetailScreen
import com.xhand.htu.screens.MainFrame

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavHostApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Destinations.HomeFrame.route
    ){
        //首页框架
        composable(
            Destinations.HomeFrame.route,
            enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ){
            MainFrame(onNavigateToArticle = {
                navController.navigate(Destinations.ArticleDetail.route)
            })
        }

        //文章详情页
        composable(
            Destinations.ArticleDetail.route,
            enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
        },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right)
            }
        ){
            ArticleDetailScreen(onBack = {
                navController.popBackStack()
            })
        }
    }

}

@Preview
@Composable
fun NavHostAppPreview() {
    NavHostApp()
}