package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewStateWithHTMLData
import com.smart.htu.component.WebView
import com.smart.htu.screens.news.newsView.NewsHTML
import com.smart.htu.screens.news.newsView.NewsStyle
import com.smart.htu.screens.news.newsView.NewsStyle.HORIZONTAL_MARGIN
import com.smart.htu.screens.news.newsView.WebViewScript
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleStyle(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val navigator = rememberWebViewNavigator()

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = "新闻正文样式") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        WebView(
                            navigator = navigator,
                            url = "about:blank",
                            captureBackPresses = false,
                            webViewState = rememberWebViewStateWithHTMLData(
                                data = NewsHTML.HTML.format(
                                    NewsStyle.get(
                                        fontSize = uiState.newsFontSize.toInt(),
                                        lineHeight = 1.0F,
                                        letterSpacing = 0.5F,
                                        textMargin = HORIZONTAL_MARGIN,
                                        textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        textBold = false,
                                        textAlign = "start",
                                        boldTextColor = MaterialTheme.colorScheme.onSurface.toArgb(),
                                        subheadBold = false,
                                        subheadUpperCase = false,
                                        imgMargin = HORIZONTAL_MARGIN,
                                        imgBorderRadius = 4,
                                        imgDisplayMode = if (uiState.loadImgEnabled) "block" else "none",
                                        linkTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        codeTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        codeBgColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        tableMargin = 0,
                                        selectionTextColor = MaterialTheme.colorScheme.onSurface.toArgb(),
                                        selectionBgColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                        signatureColor = Color.Gray.toArgb()
                                    ),
                                    "",
                                    "<h2>这是标题</h2><h4>二级标题</h4>" +
                                            "<p>仅修改文章字体样式和大小，软件内其他字体不受该设置影响。</p>" +
                                            "<p>Bionic Reading是一种新的阅读方式，通过强调词语的关键部分来引导眼睛更快地移动，从而提高阅读速度和理解力。</p>",
                                    WebViewScript.get(uiState.bionicReadingEnabled)
                                )
                            ),
                            isShowLinearProgressIndicator = false,
                        )
                    }
                }
            }
            item {
                val fontSizeProgress = remember { mutableFloatStateOf(uiState.newsFontSize) }
                // BasicComponent()
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        top.yukonga.miuix.kmp.basic.Text(
                            text = "字体大小",
                            fontSize = MiuixTheme.textStyles.headline1.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            progress = fontSizeProgress.floatValue,
                            onProgressChange = {
                                fontSizeProgress.floatValue = it
                                viewModel.changeNewsFontSize(fontSizeProgress.floatValue.toInt())
                            },
                            decimalPlaces = 1,
                            minValue = 15f,
                            maxValue = 25f
                        )
                    }
                }
            }
            item {
                Card {
                    SuperDropdown(
                        title = "字体样式",
                        summary = "更改字体样式",
                        items = listOf("系统默认", "Serif", "Sans-serif", "Monospace"),
                        selectedIndex = 0,
                        mode = DropDownMode.AlwaysOnRight,
                        onSelectedIndexChange = { mode ->
                        },
                        enabled = false
                    )
                }
            }
            item {
                Card {
                    SuperSwitch(
                        checked = uiState.bionicReadingEnabled,
                        title = "字体加粗",
                        summary = "开启后会对新闻正文部分词语加粗，提升阅读体验",
                        onCheckedChange = {
                            viewModel.changeBionicReadingEnabled(it)
                        }
                    )
                }
            }
        }
    }
}