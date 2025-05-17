package com.smart.htu.screens.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.smart.htu.R
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.setDefaultSettings
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ListPopup
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.extra.DropdownImpl
import top.yukonga.miuix.kmp.theme.MiuixTheme

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewContent(
    url: String,
    title: String,
    navController: NavController,
    headers: Map<String, String> = emptyMap(),
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    val state = rememberWebViewState(url = url, additionalHttpHeaders = headers)
    val navigator = rememberWebViewNavigator()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val (currentUrl, onCurrentUrl) = remember { mutableStateOf(url) }
    var showDropDownMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // val cookies = webViewViewModel.cookies.collectAsState().value
    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                url?.let {
                    onCurrentUrl(it)
                }
            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun doUpdateVisitedHistory(
                view: WebView,
                url: String?,
                isReload: Boolean,
            ) {
                super.doUpdateVisitedHistory(view, url, isReload)
                url?.let {
                    onCurrentUrl(it)
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?,
            ): Boolean {
                request?.let {
                    // Don't attempt to open blobs as webpages
                    if (it.url.toString().startsWith("blob:http")) {
                        return false
                    }

                    // Ignore intents urls
                    if (it.url.toString().startsWith("intent://")) {
                        return true
                    }

                    // Continue with request, but with custom headers
                    view?.loadUrl(it.url.toString(), headers)
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }
    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { navigator.reload() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")

                    }
                    val dropdownOptions = listOf(
                        "分享",
                        "复制链接",
                        stringResource(id = R.string.open_outside),
                        stringResource(id = R.string.forward)
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
                                                    copyContent(currentUrl)
                                                    snackBarHostState.showSnackbar("已复制到剪贴板")
                                                }
                                            }

                                            2 -> {
                                                startWebUrl(url)
                                            }

                                            3 -> {
                                                if (navigator.canGoForward) navigator.navigateForward()
                                            }
                                        }
                                    },
                                    index = index
                                )
                            }
                        }
                    }
                    IconButton(onClick = { showDropDownMenu.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "more")
                    }
                },
                navigationIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (navigator.canGoBack)
                                    navigator.navigateBack()
                                else
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                        }
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "close")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (val loadingState = state.loadingState) {
                    is LoadingState.Initializing -> LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                    is LoadingState.Loading -> LinearProgressIndicator(
                        progress = { loadingState.progress },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                    else -> {}
                }
                WebView(
                    state = state,
                    modifier = Modifier
                        .fillMaxSize(),
                    navigator = navigator,
                    onCreated = { webView ->
                        webView.setDefaultSettings()

                        /*Log.i("TAG666", "cookies: $cookies")
                        if (cookies.isNotEmpty()) {
                            val cookieManager = CookieManager.getInstance()
                            cookieManager.setAcceptCookie(true)
                            cookieManager.setAcceptThirdPartyCookies(webView, true)

                            cookies.forEach { (key, value) ->
                                val cookieString = "$key=$value"
                                Log.d("TAG666 WebViewCookie", "Setting cookie: $cookieString for $url")
                                cookieManager.setCookie(url, cookieString)
                            }
                            cookieManager.flush()
                        }*/

                        headers["user-agent"]?.let {
                            webView.settings.userAgentString = it
                        }
                    },
                    client = webClient
                )
            }
            if (content != null) {
                content()
            }
        }
    }
}