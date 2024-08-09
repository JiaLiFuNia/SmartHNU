package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.xhand.hnu.R
import com.xhand.hnu.model.entity.DarkMode
import com.xhand.hnu.model.viewWebsite
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import net.dankito.readability4j.Article
import net.dankito.readability4j.Readability4J

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    viewModel: SettingsViewModel, newsViewModel: NewsViewModel, onClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cbManager = LocalClipboardManager.current
    var showHtml by remember {
        mutableStateOf(false)
    }
    val articleTitle = newsViewModel.article
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            settings.builtInZoomControls = true
            loadUrl(newsViewModel.article.url)
        }
    }
    LaunchedEffect(Unit) {
        newsViewModel.isDetailLoading = true
        newsViewModel.detailService()
    }
    val scrollState = rememberScrollState()
    // val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        // modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                // scrollBehavior = scrollBehavior,
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
                                    it.putExtra(Intent.EXTRA_TEXT, newsViewModel.article.url)
                                    it.type = "text/plain"
                                    if (it.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(it)
                                    }
                                }
                                Toast.makeText(context, "请选择分享的应用", Toast.LENGTH_SHORT)
                                    .show()
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
                                    webView.reload()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "刷新"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { if (!showHtml) Text(text = "查看原文") else Text(text = "提取内容") },
                                onClick = {
                                    showHtml = !showHtml
                                },
                                leadingIcon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_baseline_library_books_24),
                                        contentDescription = "切换"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "复制原文链接") },
                                onClick = {
                                    viewModel.copyText(cbManager, newsViewModel.article.url)
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
                                    viewWebsite(newsViewModel.article.url, context)
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
        val readability4J = Readability4J(newsViewModel.article.url, newsViewModel.htmlParsing)
            val article: Article = readability4J.parse()

        val content: String = article.articleContent.toString()
        /*.replace("img src", "imgFLAGsrc")
        .replace("a href", "aFLAGhref").replace("span lang", "spanFLAGlang").replace("img data-layer="photo" src", "imgFLAGdata-layer="photo"FLAGsrc")
        .replace(" ", "").replace("FLAG", " ")*/
        Log.i("TAG666", "title: $content")

        val darkTheme = when (viewModel.darkModeIndex) {
            DarkMode.ON.ordinal -> true
            DarkMode.OFF.ordinal -> false
            else -> isSystemInDarkTheme()
        }

        val fontColor: String = if (darkTheme) "rgb(255,255,255)" else "rgb(0,0,0)"
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
                }
                img[data-layer="photo"] {
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                    width: 80%;
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
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .verticalScroll(scrollState)
        ) {
            if (!showHtml) {
                Text(
                    text = articleTitle.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W800,
                    lineHeight = 35.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Justify
                )
                if (newsViewModel.isDetailLoading) {
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
                            "$htmlHeader$content$htmlFooter",
                            null,
                            "utf-8",
                            null
                        )
                    }
                }
            } else {
                AndroidView(factory = { webView })
            }
        }
    }
}