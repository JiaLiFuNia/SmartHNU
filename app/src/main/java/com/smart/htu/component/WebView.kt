package com.smart.htu.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.multiplatform.webview.web.AccompanistWebViewClient
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.smart.htu.R
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.sendToast
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.MiuixTheme

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebView(
    themeMode: Int,
    navController: NavController,
    url: String,
    headers: Map<String, String> = emptyMap(),
    initTitle: String,
    content: (@Composable () -> Unit)? = null,
) {
    val state = rememberWebViewState(url = url, additionalHttpHeaders = headers)
    val navigator = rememberWebViewNavigator()
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    var currentUrl by remember { mutableStateOf(url) }
    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                url?.let {
                    currentUrl = it
                }
            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                scope.launch {
                }
            }

            override fun doUpdateVisitedHistory(
                view: WebView,
                url: String?,
                isReload: Boolean,
            ) {
                super.doUpdateVisitedHistory(view, url, isReload)
                url?.let {
                    currentUrl = it
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
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surface
                    },
                    scrolledContainerColor = when (themeMode) {
                        0 -> MiuixTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
                ),
                title = {
                    Text(
                        text = initTitle,
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
                    IconButton(onClick = { showDropDownMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "more")
                    }
                    DropdownMenu(
                        expanded = showDropDownMenu,
                        onDismissRequest = { showDropDownMenu = false },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "复制链接") },
                            onClick = {
                                copyContent(currentUrl)
                                sendToast(context, "已复制")
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.content_copy_24px),
                                    contentDescription = "copy"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.open_outside)) },
                            onClick = { startWebUrl(url) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.public_24px),
                                    contentDescription = "outside"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.forward)) },
                            onClick = { if (navigator.canGoForward) navigator.navigateForward() },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "forward"
                                )
                            },
                            enabled = navigator.canGoForward
                        )
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
            if (content != null) {
                content()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val loadingState = state.loadingState) {
                is LoadingState.Initializing -> LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                is LoadingState.Loading -> LinearProgressIndicator(
                    progress = { (loadingState as? LoadingState.Loading)?.progress ?: 1f },
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
                onCreated = { webview ->
                    webview.settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        allowFileAccess = true
                        allowContentAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        setSupportMultipleWindows(true)
                    }
                    headers["user-agent"]?.let {
                        webview.settings.userAgentString = it
                    }
                    webview.webViewClient = webClient
                }
            )
        }
    }
}