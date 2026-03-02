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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MediumAppCard
import com.smart.htu.component.card.SmallAppCard
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Icon
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
    viewModel: ApplicationViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val hazeState = rememberHazeState()
    val scope = rememberCoroutineScope()
    val showAuthLoginDialog = remember { mutableStateOf(false) }
    val isLoginSuccess = remember {
        derivedStateOf { loginUiState.jwcLoginState == 1 }
    }
    val displayMode = remember { mutableIntStateOf(0) } // 0 矩形 1 方形

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
            columns = GridCells.Fixed(if (displayMode.intValue == 1) 4 else 2),
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
            if (!isLoginSuccess.value) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SuggestChip(
                        onClick = { navigator.push(Route.Login) },
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
                        MediumAppCard(
                            enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || isLoginSuccess.value) && app.enabled,
                            content = app,
                            modifier = Modifier,
                            onClick = {
                                navigator.pushWithLoginCheck(
                                    route = app.screenRoute,
                                    url = app.url,
                                    label = context.getString(app.label),
                                    isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                    loginState = isLoginSuccess.value, // 只检查jwc_app的登录状态
                                    onLoginRequired = {
                                        if (app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1) {
                                            showAuthLoginDialog.value = true
                                        }
                                    }
                                )
                            }
                        )
                    } else {
                        SmallAppCard(
                            enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || isLoginSuccess.value) && app.enabled,
                            content = app,
                            modifier = Modifier
                                .size(76.dp),
                            onClick = {
                                navigator.pushWithLoginCheck(
                                    route = app.screenRoute,
                                    url = app.url,
                                    label = context.getString(app.label),
                                    isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                    loginState = isLoginSuccess.value,
                                    onLoginRequired = {
                                        if (app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1) {
                                            showAuthLoginDialog.value = true
                                        }
                                    }
                                )
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
        loginState = loginUiState.authLoginState
    )

}
