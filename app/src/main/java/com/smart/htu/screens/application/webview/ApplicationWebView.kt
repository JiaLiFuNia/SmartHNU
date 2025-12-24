package com.smart.htu.screens.application.webview

import android.content.Intent
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.smart.htu.R
import com.smart.htu.component.WebView
import com.smart.htu.component.updateWebViewCookies
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ListPopup
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.extra.DropdownImpl
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.icon.icons.useful.Cancel
import top.yukonga.miuix.kmp.icon.icons.useful.ImmersionMore
import top.yukonga.miuix.kmp.icon.icons.useful.Refresh
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationWebView(
    url: String,
    title: String,
    appWebViewViewModel: AppWebViewViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val loginUiState = loginViewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val navigator = rememberWebViewNavigator()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val showDropDownMenu = remember { mutableStateOf(false) }

    val showLoginDialog = remember { mutableStateOf(false) }
    val currentUrl = remember { mutableStateOf(url) }

    val cookie = appWebViewViewModel.cookies.collectAsState()
    val cookieManager = CookieManager.getInstance()
    val isLoadingCookie = remember { mutableStateOf(true) }
    LaunchedEffect(Unit, url) {
        appWebViewViewModel.loadCookiesForUrl(url)
        isLoadingCookie.value = false
    }

    LaunchedEffect(loginUiState.authLoginState) {
        if (loginUiState.authLoginState == 1) {
            showLoginDialog.value = false
            updateWebViewCookies(url, cookie.value)
            navigator.reload()
        }
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.surfaceContainer,
        topBar = {
            SmallTopAppBar(
                color = Color.Transparent,
                title = title,
                actions = {
                    IconButton(
                        onClick = {
                            navigator.reload()
                            appWebViewViewModel.loadCookiesForUrl(url)
                        }
                    ) {
                        Icon(MiuixIcons.Useful.Refresh, contentDescription = "Refresh")
                    }
                    top.yukonga.miuix.kmp.basic.IconButton(
                        onClick = {
                            showDropDownMenu.value = true
                        },
                        holdDownState = showDropDownMenu.value,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(MiuixIcons.Useful.ImmersionMore, contentDescription = "more")
                    }
                    val dropdownOptions = listOf(
                        "分享",
                        "复制链接",
                        stringResource(id = R.string.open_outside),
                        stringResource(id = R.string.forward),
                        "清除 Cookie"
                    )
                    ListPopup(
                        show = showDropDownMenu,
                        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                        alignment = PopupPositionProvider.Align.TopRight,
                        onDismissRequest = {
                            showDropDownMenu.value = false
                        }
                    ) {
                        ListPopupColumn {
                            dropdownOptions.forEachIndexed { index, item ->
                                DropdownImpl(
                                    text = item,
                                    isSelected = false,
                                    optionSize = dropdownOptions.size,
                                    onSelectedIndexChange = {
                                        showDropDownMenu.value = false
                                        when (index) {
                                            0 -> {
                                                Intent(Intent.ACTION_SEND).also {
                                                    it.putExtra(Intent.EXTRA_TEXT, url)
                                                    it.type = "text/plain"
                                                    if (it.resolveActivity(context.packageManager) != null) {
                                                        context.startActivity(it)
                                                    }
                                                }
                                            }

                                            1 -> {
                                                scope.launch {
                                                    copyContent(currentUrl.value)
                                                    snackBarHostState.showSnackbar("已复制到剪贴板")
                                                }
                                            }

                                            2 -> {
                                                startWebUrl(url)
                                            }

                                            3 -> {
                                                if (navigator.canGoForward) navigator.navigateForward()
                                            }

                                            4 -> {
                                                cookieManager.removeAllCookies(null)
                                            }
                                        }
                                    },
                                    index = index
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (navigator.canGoBack)
                                    navigator.navigateBack()
                                else
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(MiuixIcons.Useful.Back, contentDescription = "back")
                        }
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(MiuixIcons.Useful.Cancel, contentDescription = "close")
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            if (!isLoadingCookie.value)
                WebView(
                    url = url,
                    webViewState = rememberWebViewState(url),
                    cookie = cookie.value,
                    onLogin = {
                        scope.launch {
                            delay(1000)
                            if (it) showLoginDialog.value = true
                        }
                    },
                    onCurrentUrl = {
                        currentUrl.value = it
                    },
                    navigator = navigator,
                    snackBarHostState = snackBarHostState
                )
        }

        LoginDialog(
            showDialog = showLoginDialog,
            summary = "统一身份认证系统",
            onLogin = { studentID, password, _ ->
                scope.launch {
                    loginViewModel.authLogin(
                        studentID = studentID,
                        password = password,
                        onSuccess = {
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

        /*BottomCircularProgressIndicator(
            loadingState = loggingState,
            loadingText = "正在登录..."
        )*/
    }
}