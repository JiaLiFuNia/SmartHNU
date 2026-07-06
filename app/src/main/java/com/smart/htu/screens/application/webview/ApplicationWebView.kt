package com.smart.htu.screens.application.webview

import android.content.Intent
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.smart.htu.R
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.WebView
import com.smart.htu.component.updateWebViewCookies
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.icon.extended.Refresh
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ApplicationWebView(
    url: String,
    title: String,
    appWebViewViewModel: AppWebViewViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val loginUiState = loginViewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val webViewNavigator = rememberWebViewNavigator()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val showDropDownMenu = remember { mutableStateOf(false) }

    var isFinished by remember { mutableStateOf(false) }
    var currentUrl by remember { mutableStateOf(url) }
    val isLogging = remember(currentUrl, isFinished) {
        mutableStateOf(!isFinished && currentUrl.contains("/login?service="))
    }

    val webviewState = rememberWebViewState(
        url = url,
        additionalHttpHeaders = appWebViewViewModel.cookies.collectAsState().value
            .filter { it.domain == url.toHttpUrl().host }
            .associate { it.name to it.value }
    )
    /*val isLoginDialogShow = remember(currentUrl, isFinished) {
        mutableStateOf(isFinished && currentUrl.contains("/authserver/login?service="))
    }*/
    val isLoginDialogShow = remember { mutableStateOf(false) }

    val cookie = appWebViewViewModel.cookies.collectAsState()
    val cookieManager = CookieManager.getInstance()
    LaunchedEffect(Unit, url) {
        appWebViewViewModel.loadCookiesForUrl(url)
    }

    LaunchedEffect(loginUiState.authLoginState) {
        if (loginUiState.authLoginState == 1) {
            isLoginDialogShow.value = false
            updateWebViewCookies(url, cookie.value)
        }
    }
    val isLogInfoShow = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = title,
                // subtitle = currentUrl.toHttpUrl().host,
                actions = {
                    IconButton(
                        onClick = {
                            webViewNavigator.reload()
                            appWebViewViewModel.loadCookiesForUrl(url)
                        }
                    ) {
                        Icon(MiuixIcons.Regular.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(
                        onClick = {
                            showDropDownMenu.value = true
                        },
                        holdDownState = showDropDownMenu.value
                    ) {
                        Icon(MiuixIcons.Regular.More, contentDescription = "more")
                    }
                    val dropdownOptions = listOf(
                        "分享",
                        "复制链接",
                        stringResource(id = R.string.open_outside),
                        stringResource(id = R.string.forward),
                        "清除 Cookie",
                        "显示调试信息"
                    )
                    OverlayListPopup(
                        show = showDropDownMenu.value,
                        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                        alignment = PopupPositionProvider.Align.TopEnd,
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
                                                    copyContent(currentUrl)
                                                    snackBarHostState.showSnackbar("已复制到剪贴板")
                                                }
                                            }

                                            2 -> {
                                                startWebUrl(url)
                                            }

                                            3 -> {
                                                if (webViewNavigator.canGoForward) webViewNavigator.navigateForward()
                                            }

                                            4 -> {
                                                cookieManager.removeAllCookies(null)
                                            }

                                            5 -> {
                                                isLogInfoShow.value = !isLogInfoShow.value
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

                        ) {
                        IconButton(
                            onClick = {
                                if (webViewNavigator.canGoBack)
                                    webViewNavigator.navigateBack()
                                else
                                    navigator.pop()
                            }
                        ) {
                            Icon(MiuixIcons.Regular.Back, contentDescription = "back")
                        }
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(MiuixIcons.Close, contentDescription = "close")
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
            if (isLogInfoShow.value) {
                Text(
                    text = "url: \n$url\n" +
                            "loginState: \n${loginUiState.authLoginState}\n" +
                            "isFinished: \n$isFinished\n" +
                            "currentUrl: \n$currentUrl\n" +
                            "cookie: \n${cookie.value.joinToString(separator = ";\n")}",
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (!isLoginDialogShow.value) {
                WebView(
                    url = url,
                    webViewState = webviewState,
                    cookie = cookie.value,
                    onLogin = {
                        scope.launch {
                            delay(500.milliseconds)
                            if (it) isLoginDialogShow.value = true
                        }
                    },
                    onFinished = {
                        isFinished = it
                    },
                    onCurrentUrl = {
                        currentUrl = it
                    },
                    navigator = webViewNavigator,
                    onError = {
//                        scope.launch {
//                            snackBarHostState.showSnackbar(it)
//                        }
                    }
                )
            }
        }

        LoginDialog(
            showDialog = isLoginDialogShow.value,
            title = "统一身份认证登录",
            summary = "该应用需要进行统一身份认证，请认证后使用。密码与寝室校园网密码一致。",
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
                    webViewNavigator.loadUrl(url)
                }
            },
            loginState = loginUiState.authLoginState,
            onDismissRequest = {
                isLoginDialogShow.value = false
                navigator.pop()
            }
        )

        BottomCircularProgressIndicator(
            loadingState = isLogging.value,
            loadingText = "正在认证中..."
        )
    }
}