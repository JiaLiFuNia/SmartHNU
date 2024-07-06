package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xhand.hnu.R
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import net.dankito.readability4j.Article
import net.dankito.readability4j.Readability4J

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    viewModel: SettingsViewModel, onBack: () -> Unit,
    newsViewModel: NewsViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cbManager = LocalClipboardManager.current
    var showHtml by remember {
        mutableStateOf(false)
    }
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(viewModel.url)
        }
    }
    // 获取SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()
    val statueBarColor = MaterialTheme.colorScheme.surfaceContainer
    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(color = statueBarColor)
    }
    Surface(
        color = Color.White
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    title = { Text(text = "详情") },
                    navigationIcon = {
                        IconButton(
                            onClick = { onBack() }) {
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
                                    it.putExtra(Intent.EXTRA_TEXT, viewModel.url)
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
                        )
                        {
                            Icon(
                                imageVector = Icons.Default.MoreVert, contentDescription = "其他"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            val ifStar = remember { mutableStateOf(false) }
                            val ifStarText = remember { mutableStateOf("收藏") }
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
                                text = { if (!showHtml) Text(text = "查看原网页") else Text(text = "内容提取") },
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
                                text = { Text(text = "复制文章链接") },
                                onClick = {
                                    viewModel.copyText(cbManager, viewModel.url)
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
                                text = { Text(text = "使用外部浏览器") },
                                onClick = {
                                    Intent(Intent.ACTION_VIEW).also {
                                        it.data = Uri.parse(viewModel.url)
                                        if (it.resolveActivity(context.packageManager) != null) {
                                            context.startActivity(it)
                                        }
                                    }
                                    Toast.makeText(
                                        context, "正在打开外部浏览器", Toast.LENGTH_SHORT
                                    ).show()
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
            // Create a reusable object configured with the default set of plugins.
            LaunchedEffect(Unit) {
                viewModel.detailService()
            }
            val readability4J = Readability4J(viewModel.url, viewModel.htmlParsing)
            val article: Article = readability4J.parse()

            // 标题
            val title: String = article.title ?: ""
            // 内容
            val content: String = article.articleContent.toString()
            val fontColor: String = if (isSystemInDarkTheme()) "rgb(255,255,255)" else "rgb(0,0,0)"
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
                  font-size: 120%;
                  background-color: rgb(${(MaterialTheme.colorScheme.surface.red) * 256}, ${(MaterialTheme.colorScheme.surface.green) * 256}, ${(MaterialTheme.colorScheme.surface.blue) * 256});
                  color: ${fontColor};
                  font-family:"Microsoft YaHei",微软雅黑;
                  line-height:30px;
                }
                img {
                    max-width: 100% !important;
                }
            </style>
        </head>
        <body>
    """
            // html尾部
            val htmlFooter = """
        </body>
        </html>
    """
            if (viewModel.isDetailLoad)
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            else
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues = it)
                ) {
                    item {
                        if (!showHtml) {
                            Text(
                                text = title,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.W700,
                                lineHeight = 35.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                            // HorizontalDivider(modifier = Modifier.padding(10.dp))
                        }
                    }
                    item {
                        if (!showHtml) {
                            AndroidView(
                                factory = { context ->
                                    WebView(context)
                                },
                                modifier = Modifier.padding(5.dp)
                            ) { view ->
                                view.loadDataWithBaseURL(
                                    "", "$htmlHeader$content$htmlFooter", null, "utf-8", null
                                )
                            }
                        } else {
                            AndroidView(factory = { webView })
                        }
                    }
                }
        }
    }
}