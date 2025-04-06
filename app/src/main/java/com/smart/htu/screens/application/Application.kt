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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.smart.htu.R
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.SmallMediumCardDisplay
import com.smart.htu.screens.application.entity.SmallCardCategory
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Application(
    themeMode: Int,
    contentPadding: PaddingValues,
    navController: NavController,
    viewModel: ApplicationViewModel,
    loginViewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    val visibility = remember {
        derivedStateOf { mutableStateOf(!loginUiState.isLogSuccess) }
    }
    val hazeState = remember { HazeState() }
    val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

        LazyVerticalGrid(
            contentPadding = PaddingValues(16.dp),
            columns = GridCells.Fixed(if (windowWidthClass == WindowWidthSizeClass.EXPANDED) 4 else 2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(contentPadding),
        ) {
            if (visibility.value.value) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SuggestChip(
                        onClick = { navController.navigate(Destinations.Login.route) },
                        onActionClick = { navController.navigate(Destinations.Login.route) },
                        text = "暂未登录，登录后即可体验全部功能",
                        type = SuggestChipType.ERROR,
                        visibility = visibility.value,
                        icon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestChip(
                    onClick = { },
                    onActionClick = { },
                    text = "点击反馈提交你的需求",
                    type = SuggestChipType.INFO,
                    visibility = mutableStateOf(true),
                    icon = Icons.Outlined.Info
                )
            }

            SmallCardCategory.entries.forEach { item ->
                val appList = uiState.appList.filter { app ->
                    app.category == item
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SmallTitle(
                        text = item.category,
                        insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                    )
                }
                items(appList) { app ->
                    SmallMediumCardDisplay(
                        themeMode = themeMode,
                        enabled = (loginUiState.isGuest && app.guestEnable) || loginUiState.isLogSuccess,
                        content = app,
                        modifier = Modifier,
                        onLongClick = {
                            viewModel.changeCommonAppListState(app)
                        },
                        onCLick = {
                            navController.navigateWithAuthCheck(
                                isGuest = loginUiState.isGuest && app.guestEnable,
                                route = app.route,
                                routeType = app.routeType,
                                logState = loginUiState.isLogSuccess,
                                label = app.label
                            )
                        },
                        isCommon = !uiState.appListIsCommonList.contains(app)
                    )
                }
            }
        }
}
