package com.smart.htu.component

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.WebViewNavigator
import com.kevinnzou.web.WebViewState
import com.kevinnzou.web.rememberWebViewState
import com.smart.htu.screens.news.newsView.JavaScriptInterface
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil.showSnackbar
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.getHtml
import com.smart.htu.utils.setDefaultSettings
import kotlinx.coroutines.launch
import okhttp3.Cookie
import org.json.JSONTokener
import org.jsoup.Jsoup
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHostState

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String,
    headers: Map<String, String> = emptyMap(),
    cookie: List<Cookie> = emptyList(),
    webViewState: WebViewState = rememberWebViewState(url, headers),
    onError: (String) -> Unit = { },
    onFinished: (Boolean) -> Unit = { },
    onLogin: (Boolean) -> Unit = { },
    onCurrentUrl: (String) -> Unit = { },
    onImageClick: (imgUrl: String) -> Unit = { },
    onDownloadClick: (fileUrl: String, fileName: String) -> Unit = { _, _ -> },
    isShowLinearProgressIndicator: Boolean = true,
    captureBackPresses: Boolean = true,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigator: WebViewNavigator
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onFinished(false)
                url?.let {
                    onCurrentUrl(it)
                    if (it.contains("/authserver/login?service=")) {
                        onLogin(true)
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                onFinished(true)
                scope.launch {
                    try {
                        val html = view.getHtml()
                        val actualHtml = JSONTokener(html).nextValue() as String
                        val document = Jsoup.parse(actualHtml)
                        val errorMessage = document.select(".wp_error_msg").text()
                        onError(errorMessage)
                    } catch (e: Exception) {
                        // showSnackbar(snackBarHostState, "获取网页内容失败：${e.message}")
                    }
                }
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
                    if (it.url.toString().startsWith("weixin://")) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, it.url)
                            context.startActivity(intent)
                            return true
                        } catch (_: Exception) {
                            scope.launch {
                                showSnackbar(snackBarHostState, "未安装微信或无法打开微信")
                            }
                            return true
                        }
                    }
                    // email
                    if (it.url.toString().startsWith("mailto:")) {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO, it.url)
                            context.startActivity(intent)
                            return true
                        } catch (_: Exception) {
                            scope.launch {
                                showSnackbar(snackBarHostState, "无法打开邮件客户端")
                            }
                            return true
                        }
                    }

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

            // 添加下载请求处理
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                request?.let {
                    val requestUrl = it.url.toString()
                    val isDownloadable = requestUrl.contains(".pdf") ||
                            requestUrl.contains(".doc") ||
                            requestUrl.contains(".docx") ||
                            requestUrl.contains(".xls") ||
                            requestUrl.contains(".xlsx") ||
                            requestUrl.contains(".zip") ||
                            requestUrl.contains(".rar")

                    if (isDownloadable &&
                        !requestUrl.startsWith("blob:") &&
                        !requestUrl.startsWith("data:")
                    ) {
                        scope.launch {
                            val fileName = requestUrl.substringAfterLast('/')
                            onDownloadClick(requestUrl, fileName)
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }

        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val loadingState = webViewState.loadingState
        if (isShowLinearProgressIndicator) {
            when (loadingState) {
                is LoadingState.Loading -> {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }

                is LoadingState.Initializing -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                else -> {
                }
            }
        }
        WebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
            navigator = navigator,
            captureBackPresses = captureBackPresses,
            onCreated = { webView ->
                webView.setDefaultSettings()
                webView.setBackgroundColor(Color.Transparent.toArgb())

                headers["user-agent"]?.let {
                    webView.settings.userAgentString = it
                }

                webView.addJavascriptInterface(object : JavaScriptInterface {
                    @JavascriptInterface
                    override fun onImgTagClick(imgUrl: String?) {
                        onImageClick(imgUrl ?: "")
                    }
                }, JavaScriptInterface.NAME)

                updateWebViewCookies(url, cookie)
            },
            onDispose = {

            },
            client = webClient
        )
    }
}

fun updateWebViewCookies(url: String, cookie: List<Cookie>) {
    val cookieManager = CookieManager.getInstance()
    cookieManager.setAcceptCookie(true)
    cookie.forEach { cookie ->
        cookieManager.setCookie(url, "${cookie.name}=${cookie.value}")
    }
    cookieManager.flush()
}