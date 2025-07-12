package com.smart.htu.screens.application

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MediumCardDisplay
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithCheckLoginState
import com.smart.htu.screens.navigation.Destinations
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.utils.overScrollVertical

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Application(
    contentPadding: PaddingValues,
    navController: NavController,
    viewModel: ApplicationViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val loginState = remember {
        derivedStateOf { loginUiState.loginJWCState != 1 && loginUiState.loginJWCState != -2 }
    }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    LazyVerticalGrid(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        columns = GridCells.Fixed(
            if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND))
                4 else 2
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(contentPadding)
            .overScrollVertical(),
        overscrollEffect = null,
    ) {
        if (loginState.value) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestChip(
                    onClick = { navController.navigate(Destinations.Login.route) },
                    onActionClick = { navController.navigate(Destinations.Login.route) },
                    text = "暂未登录，登录后即可体验全部功能",
                    type = SuggestChipType.ERROR,
                    icon = Icons.AutoMirrored.Filled.ArrowForward
                )
            }
        }

        ApplicationCategory.entries.forEach { item ->
            val appList = uiState.appList.filter { app ->
                app.category == item
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                SmallTitle(
                    text = stringResource(item.category),
                    insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                )
            }
            items(appList) { app ->
                MediumCardDisplay(
                    enabled = (loginUiState.isGuestModeEnable && app.guestMode) || !loginState.value,
                    content = app,
                    modifier = Modifier,
                    onClick = {
                        navController.navigateWithCheckLoginState(
                            isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                            route = app.route,
                            routeType = app.routeType,
                            logState = !loginState.value,
                            label = app.label
                        )
                    }
                )
                /*SmallCardDisplay(
                    enabled = (loginUiState.isGuest && app.guestEnable) || loginUiState.loginJWCState == 1,
                    content = app,
                    onClick = {
                        navController.navigateWithAuthCheck(
                            isGuest = loginUiState.isGuest && app.guestEnable,
                            routeType = app.routeType,
                            route = app.route,
                            logState = loginUiState.loginJWCState == 1,
                            label = app.label
                        )
                    }
                )*/
            }
        }
    }
}
