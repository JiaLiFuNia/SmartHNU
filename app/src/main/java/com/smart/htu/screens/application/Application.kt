package com.smart.htu.screens.application

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.smart.htu.R
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MediumCardDisplay
import com.smart.htu.component.card.SmallCardDisplay
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigateWithCheckLoginState
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

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
    val context = LocalContext.current

    val hazeState = rememberHazeState()
    val scope = rememberCoroutineScope()
    val showAuthLoginDialog = remember { mutableStateOf(false) }
    val loginState = remember {
        derivedStateOf { loginUiState.jwcLoginState != 1 && loginUiState.jwcLoginState != -2 }
    }
    val displayMode = remember { mutableIntStateOf(0) } // 0 矩形 1 方形
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val scrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                horizontalPadding = 16.dp,
                title = stringResource(R.string.application),
                largeTitle = stringResource(R.string.application),
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    },
                actions = {
                    top.yukonga.miuix.kmp.basic.IconButton(
                        onClick = {
                            displayMode.intValue = if (displayMode.intValue == 0) 1 else 0
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = if (displayMode.intValue == 0) Icons.Outlined.ViewAgenda else Icons.Outlined.GridView,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        popupHost = {},
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 12.dp
            ),
            columns = GridCells.Fixed(
                if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
                    if (displayMode.intValue == 1) 8 else 4
                } else {
                    if (displayMode.intValue == 1) 4 else 2
                }
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .hazeSource(hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical()
                .scrollEndHaptic(),
            overscrollEffect = null,
        ) {
            if (loginState.value) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SuggestChip(
                        onClick = { navController.navigate(Destinations.Login.route) },
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
                    if (displayMode.intValue == 0) {
                        MediumCardDisplay(
                            enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || !loginState.value) && app.enabled,
                            content = app,
                            modifier = Modifier,
                            onClick = {
                                when {
                                    app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1 -> {
                                        showAuthLoginDialog.value = true
                                    }

                                    /*app.loginMode == ApplicationEntity.LoginMode.SECOND_CLASS && loginUiState.scLoginState != 1 -> {
                                        scope.launch {
                                            loginViewModel.loadSecondClassSid()
                                            showSCLoginDialog.value = true
                                        }
                                    }*/

                                    else -> {
                                        navController.navigateWithCheckLoginState(
                                            isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                            route = app.route,
                                            routeType = app.routeType,
                                            logState = !loginState.value,
                                            label = app.label
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        SmallCardDisplay(
                            enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || !loginState.value) && app.enabled,
                            content = app,
                            modifier = Modifier
                                .size(76.dp),
                            onClick = {
                                when {
                                    app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1 -> {
                                        showAuthLoginDialog.value = true
                                    }

                                    /*app.loginMode == ApplicationEntity.LoginMode.SECOND_CLASS && loginUiState.scLoginState != 1 -> {
                                        scope.launch {
                                            loginViewModel.loadSecondClassSid()
                                            showSCLoginDialog.value = true
                                        }
                                    }*/

                                    else -> {
                                        navController.navigateWithCheckLoginState(
                                            isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                            route = app.route,
                                            routeType = app.routeType,
                                            logState = !loginState.value,
                                            label = app.label
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    LoginDialog(
        showDialog = showAuthLoginDialog,
        summary = "统一身份认证系统",
        onLogin = { studentID, password, _ ->
            scope.launch {
                loginViewModel.authLogin(
                    studentID = studentID,
                    password = password,
                    onSuccess = {
                        showAuthLoginDialog.value = false
                        showToast(context, "登录成功!")
                    },
                    onFailure = {
                        showToast(context, "登录失败！请检查账号密码是否正确")
                    }
                )
            }
        },
        logState = loginUiState.authLoginState
    )

}
