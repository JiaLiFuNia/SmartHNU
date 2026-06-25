package com.smart.htu.screens.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.card.MediumAppCard
import com.smart.htu.component.card.SmallAppCard
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun Application(
    contentPadding: PaddingValues,
    viewModel: ApplicationViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val showAuthLoginDialog = remember { mutableStateOf(false) }
    val isLoginSuccess = remember {
        derivedStateOf { loginUiState.jwcLoginState == 1 }
    }
    val displayMode = remember { mutableIntStateOf(0) } // 0 矩形 1 方形

    val scrollBehavior = MiuixScrollBehavior()
    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    title = stringResource(R.string.application),
                    largeTitle = stringResource(R.string.application),
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    actions = {
                        /*top.yukonga.miuix.kmp.basic.IconButton(
                            onClick = {
                                displayMode.intValue = if (displayMode.intValue == 0) 1 else 0
                            }
                        ) {
                            Icon(
                                imageVector = if (displayMode.intValue == 0) Icons.Outlined.ViewAgenda else Icons.Outlined.GridView,
                                contentDescription = null
                            )
                        }*/
                    }
                )
            }
        }
    ) {
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            LazyVerticalGrid(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = contentPadding.calculateBottomPadding() + 16.dp
                ),
                columns = GridCells.Fixed(if (displayMode.intValue == 1) 4 else 2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
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
                        val appLabel = stringResource(app.label)
                        if (displayMode.intValue == 0) {
                            MediumAppCard(
                                enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || isLoginSuccess.value) && app.enabled,
                                content = app,
                                modifier = Modifier,
                                onClick = {
                                    if (app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1) {
                                        showAuthLoginDialog.value = true
                                        return@MediumAppCard
                                    } else {
                                        navigator.pushWithLoginCheck(
                                            route = app.screenRoute,
                                            url = app.url,
                                            label = appLabel,
                                            isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                            loginState = isLoginSuccess.value, // 只检查jwc_app的登录状态
                                            onLoginRequired = { }
                                        )
                                    }
                                }
                            )
                        } else {
                            SmallAppCard(
                                enabled = ((loginUiState.isGuestModeEnable && app.guestMode) || isLoginSuccess.value) && app.enabled,
                                content = app,
                                modifier = Modifier
                                    .size(76.dp),
                                onClick = {
                                    if (app.loginMode == ApplicationEntity.LoginMode.AUTH_SERVER && loginUiState.authLoginState != 1) {
                                        showAuthLoginDialog.value = true
                                        return@SmallAppCard
                                    } else {
                                        navigator.pushWithLoginCheck(
                                            route = app.screenRoute,
                                            url = app.url,
                                            label = appLabel,
                                            isGuest = loginUiState.isGuestModeEnable && app.guestMode,
                                            loginState = isLoginSuccess.value, // 只检查jwc_app的登录状态
                                            onLoginRequired = { }
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    LoginDialog(
        showDialog = showAuthLoginDialog.value,
        title = "统一身份认证登录",
        summary = "该应用需要进行统一身份认证，请认证后使用。密码与寝室校园网密码一致。",
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
        loginState = loginUiState.authLoginState,
        onDismissRequest = {
            showAuthLoginDialog.value = false
        }
    )

}
