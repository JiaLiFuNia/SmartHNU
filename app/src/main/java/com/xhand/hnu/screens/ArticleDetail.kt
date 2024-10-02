package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xhand.hnu.R
import com.xhand.hnu.components.WebView
import com.xhand.hnu.components.copyText
import com.xhand.hnu.model.viewWebsite
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.NewsViewModelFactory
import net.dankito.readability4j.Article
import net.dankito.readability4j.Readability4J
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    context: Context,
    onClick: () -> Unit,
    url: String,
    title: String
) {
    val newsViewModel: NewsViewModel = viewModel(
        factory = NewsViewModelFactory(context)
    )
    val uiState by newsViewModel.uiState.collectAsState()
    val cbManager = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }
    if (url != "") {
        val domain = URL(url).host
        if (!domain.endsWith("htu.edu.cn")) newsViewModel.switchDisplayStyle(true)
    }
    var webView: WebView? by remember { mutableStateOf(null) }
    val scrollState = rememberScrollState()
    LaunchedEffect(url) {
        newsViewModel.changeWebViewError("")
        if (url != "" && !uiState.displayStyle) {
            newsViewModel.loadDetail() // true
            newsViewModel.detailService(url)
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (scrollState.value > 0) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface
                ),
                title = { Text(text = "详情") },
                navigationIcon = {
                    IconButton(onClick = { onClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(
                            onClick = {
                                Intent(Intent.ACTION_SEND).also {
                                    it.putExtra(Intent.EXTRA_TEXT, url)
                                    it.type = "text/plain"
                                    if (it.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(it)
                                    }
                                }
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share, contentDescription = "分享"
                        )
                    }
                    IconButton(
                        onClick = { showMenu = !showMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, contentDescription = "其他"
                        )
                    }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            shadowElevation = 4.dp,
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "刷新页面") },
                                onClick = {
                                    webView?.reload()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "刷新"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    if (!uiState.displayStyle) Text(text = "查看原文") else Text(
                                        text = "提取内容"
                                    )
                                },
                                onClick = {
                                    newsViewModel.switchDisplayStyle(!uiState.displayStyle)
                                },
                                leadingIcon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_baseline_library_books_24),
                                        contentDescription = "切换"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "复制链接") },
                                onClick = {
                                    copyText(cbManager, url)
                                    Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                                },
                                leadingIcon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_baseline_content_copy_24),
                                        contentDescription = "复制"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "外部打开") },
                                onClick = {
                                    viewWebsite(url, context)
                                },
                                leadingIcon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_public),
                                        contentDescription = "外部"
                                    )
                                }
                            )
                        }
                    }
            )
        }
    ) {
        val readability4J = Readability4J(url, uiState.htmlParsing)
        val article: Article = readability4J.parse()
        val content: String = article.articleContent.toString()
        Log.i("TAG666", "title: $content")
        val fontColor =
            "rgb(${(MaterialTheme.colorScheme.onSurface.red) * 256}, ${(MaterialTheme.colorScheme.onSurface.green) * 256}, ${(MaterialTheme.colorScheme.onSurface.blue) * 256})"
            // html头部
        val htmlHeader = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <title></title>
                <style>
                    body {
                      font-size: 17px;
                      background-color: rgb(${(MaterialTheme.colorScheme.background.red) * 256}, ${(MaterialTheme.colorScheme.background.green) * 256}, ${(MaterialTheme.colorScheme.background.blue) * 256});
                      color: ${fontColor};
                      line-height: 30.6px;
                      font-weight: normal;
                      font-family: Arial, Helvetica;
                      text-align: justify;
                      word-wrap: break-word;
                      max-width: 100vw;
                      overflow-x: hidden;
                    }
                    img {
                        display: block;
                        margin-left: auto;
                        margin-right: auto;
                        width: 100%;
                        height: auto;
                        border-radius: 10px;
                    }
                    img.attach {
                        display: inline;
                        width: auto;
                        margin-left: 0;
                        margin-right: 0;
                        height: auto;
                    }
                    a:link {
                        color: ${fontColor};
                        text-decoration:underline;
                    }
                    a:visited {
                        color: ${fontColor};
                        text-decoration:underline;
                    }
                    p:not(:first-of-type) {  
                        text-indent: 2em; /* 设置首行缩进为2em */  
                    }
                    .right-align {
                        text-align: right;
                    }
                    .indent {
                        text-indent: 2em;
                    }
                    </style>
            </head>
            <body>
        """
        // html尾部
        val htmlFooter = """
            <script>
                document.querySelectorAll('img').forEach(img => {
                    img.removeAttribute('width');
                    img.removeAttribute('height');
                });
                document.querySelectorAll('img').forEach(img => {
                    if (img.src.startsWith('http://www.htu.edu.cn/_ueditor/themes/') || img.src.startsWith('https://www.htu.edu.cn/_ueditor/themes/')) {
                        img.classList.add('attach'); // 为符合条件的 img 元素添加类名 'attach'
                    }
                });
                document.addEventListener('DOMContentLoaded', function () {
                    var paragraphs = document.querySelectorAll('p'); // 获取所有的p标签  
                
                    var firstParagraph = document.querySelector('p'); // 获取第一个p标签 
                    if (firstParagraph.textContent.trim().length > 10) {
                        firstParagraph.classList.add('indent'); // 如果文本长度大于10，则添加缩进类  
                    }
                
                    var lastParagraph = paragraphs[paragraphs.length - 1]; // 获取最后一个p标签  
                    if (lastParagraph.textContent.trim().startsWith('（')) {
                        lastParagraph.style.textAlign = 'right'; // 将文本居右对齐  
                    }
                
                    var lastTwo = Array.from(paragraphs).slice(-2); // 选取最后两个p标签  
                    lastTwo.forEach(function (paragraph) {
                        if (!paragraph.querySelector('img') && !paragraph.textContent.trim().startsWith('附') && paragraph.textContent.trim().length < 15) {
                            paragraph.style.textAlign = 'right'; // 将每个p标签的文本居右对齐  
                        }
                    });
                });
            </script>
            </body>
            </html>
        """
        if (url == "") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "选择一个进行浏览")
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it)
                    .verticalScroll(scrollState)
            ) {
                if (!uiState.displayStyle) {
                    if (uiState.isDetailLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        ) { view ->
                            view.loadDataWithBaseURL(
                                "",
                                "$htmlHeader\n<h2>${title}</h1>\n$content$htmlFooter",
                                null,
                                "utf-8",
                                null
                            )
                        }
                    }
                } else {
                    if (uiState.webViewError != "") {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(300.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "加载失败，错误：${uiState.webViewError}")
                            TextButton(onClick = { webView?.reload() }) {
                                Text(text = "重新加载")
                            }
                            TextButton(onClick = { viewWebsite(url, context) }) {
                                Text(text = "点击此处跳转到浏览器打开")
                            }
                        }
                    } else {
                        WebView(
                            url = url,
                            webViewSetter = { web ->
                                webView = web
                            },
                            onError = { error ->
                                newsViewModel.changeWebViewError(error)
                            }
                        )
                    }

                }
            }
        }
    }
}