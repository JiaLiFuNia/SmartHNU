package com.smart.htu.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.smart.htu.screens.application.airCondition.AirConditionViewModel
import com.smart.htu.screens.application.librarySearch.LibrarySearchViewModel
import com.smart.htu.screens.login.LoginScreen
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.main.MainViewModel
import com.smart.htu.screens.navigation.Login
import com.smart.htu.screens.navigation.MainFrame
import com.smart.htu.screens.news.NewsViewModel

@Composable
fun AppNavHost() {

    val backStack = rememberNavBackStack(MainFrame)

    val mainViewModel: MainViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val newsViewModel: NewsViewModel = hiltViewModel()
    val airConditionViewModel: AirConditionViewModel = hiltViewModel()
    val librarySearchViewModel: LibrarySearchViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<MainFrame> {
                MainFrame(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    loginViewModel = loginViewModel,
                    newsViewModel = newsViewModel,
                    airConditionViewModel = airConditionViewModel
                )
            }
            entry<Login> {
                LoginScreen(
                    navController = navController,
                    viewModel = loginViewModel
                )
            }
        },
        onBack = { count ->
            repeat(count) {
                if (backStack.isNotEmpty()) {
                    backStack.removeLastOrNull()
                }
            }
        }
    )

}