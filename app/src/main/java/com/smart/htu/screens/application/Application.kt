package com.smart.htu.screens.application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.component.SmallMediumCardDisplay
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithAuthCheck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Application(
    navController: NavHostController,
    viewModel: ApplicationViewModel,
    loginViewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "应用") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 2),
                modifier = Modifier
            ) {
                items(uiState.appList.size) { item ->
                    SmallMediumCardDisplay(
                        content = uiState.appList[item],
                        modifier = Modifier
                            .padding(5.dp),
                        onLongClick = {
                            viewModel.changeCommonAppListState(item)
                        },
                        onCLick = {
                            navController.navigateWithAuthCheck(
                                route = uiState.appList[item].route,
                                url = uiState.appList[item].url,
                                logState = loginUiState.isLogSuccess,
                                label = uiState.appList[item].label
                            )
                        },
                        isCommon = !uiState.appListIsCommonList.contains(uiState.appList[item])
                    )
                }
            }
        }
    }
}
