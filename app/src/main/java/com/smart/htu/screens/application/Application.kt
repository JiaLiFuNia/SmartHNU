package com.smart.htu.screens.application

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.component.SmallMediumCardDisplay
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Application(
    navController: NavHostController,
    viewModel: ApplicationViewModel,
    loginViewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer
                ),
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
                },
                title = { Text(text = "应用") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                }
            )
        },
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(
                top = it.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            columns = GridCells.Fixed(if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .hazeSource(state = hazeState),
        ) {
            items(uiState.appList.size) { item ->
                SmallMediumCardDisplay(
                    enabled = (loginUiState.isGuest && uiState.appList[item].guestEnable) || loginUiState.isLogSuccess,
                    content = uiState.appList[item],
                    modifier = Modifier,
                    onLongClick = {
                        viewModel.changeCommonAppListState(item)
                    },
                    onCLick = {
                        navController.navigateWithAuthCheck(
                            isGuest = loginUiState.isGuest && uiState.appList[item].guestEnable,
                            route = uiState.appList[item].route,
                            url = uiState.appList[item].url,
                            appUrl = uiState.appList[item].appUrl,
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
