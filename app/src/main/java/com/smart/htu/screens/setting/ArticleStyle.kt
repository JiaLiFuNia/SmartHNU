package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewStateWithHTMLData
import com.smart.htu.component.SuperSlider
import com.smart.htu.component.WebView
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.news.newsView.NewsHTML
import com.smart.htu.screens.news.newsView.NewsStyle
import com.smart.htu.screens.news.newsView.NewsStyle.HORIZONTAL_MARGIN
import com.smart.htu.screens.news.newsView.WebViewScript
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun ArticleStyle(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val fontSizeProgress = remember { mutableFloatStateOf(uiState.newsFontSize) }
    val scrollBehavior = MiuixScrollBehavior()
    val hazeState = rememberHazeState()

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = "字体设置",
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                        
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = uiState.blurEnabled
                    }
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
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
                            navigator = rememberWebViewNavigator(),
                            url = "about:blank",
                            captureBackPresses = false,
                            webViewState = rememberWebViewStateWithHTMLData(
                                data = NewsHTML.HTML.format(
                                    NewsStyle.get(
                                        fontSize = uiState.newsFontSize.toInt(),
                                        lineHeight = 1.0F,
                                        letterSpacing = 0.5F,
                                        textMargin = HORIZONTAL_MARGIN,
                                        textColor = MiuixTheme.colorScheme.onBackground.copy(0.8f)
                                            .toArgb(),
                                        textBold = false,
                                        textAlign = "start",
                                        boldTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                            0.8f
                                        ).toArgb(),
                                        subheadBold = false,
                                        subheadUpperCase = false,
                                        imgMargin = HORIZONTAL_MARGIN,
                                        imgBorderRadius = 4,
                                        imgDisplayMode = if (uiState.loadImgEnabled) "block" else "none",
                                        linkTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                            0.8f
                                        ).toArgb(),
                                        codeTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                            0.8f
                                        ).toArgb(),
                                        codeBgColor = MiuixTheme.colorScheme.onBackground.copy(0.8f)
                                            .toArgb(),
                                        tableMargin = 0,
                                        selectionTextColor = MiuixTheme.colorScheme.onBackground.toArgb(),
                                        selectionBgColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                        signatureColor = Color.Gray.toArgb()
                                    ),
                                    "",
                                    "<p>字体设置</p>" +
                                            "<p>仅修改新闻正文的字体样式和大小，软件内其他字体不受该设置影响。</p>" +
                                            "<p>师韵 SmartHNU 是一个河南师范大学校园资讯聚合应用，提供新闻、课程表、成绩查询等功能，旨在为师生提供便捷的校园信息服务。</p>",
                                    WebViewScript.get()
                                )
                            ),
                            isShowLinearProgressIndicator = false,
                        )
                    }
                }
            }
            item {
                // BasicComponent()
                Card(modifier = Modifier.fillMaxWidth()) {
                    SuperSlider(
                        title = "字体大小",
                        endText = "${fontSizeProgress.floatValue.toInt()} sp",
                        value = fontSizeProgress.floatValue,
                        onValueChange = {
                            fontSizeProgress.floatValue = it
                        },
                        onValueChangeFinished = {
                            viewModel.changeNewsFontSize(fontSizeProgress.floatValue.toInt())
                        },
                        valueRange = 15f..25f,
                        showKeyPoints = false,
                        keyPoints = listOf(15f, 17f, 19f, 21f, 23f, 25f),
                        isShowValueDialog = false
                    )
                }
            }
            item {
                Card {
                    OverlayDropdownPreference(
                        title = "字体样式",
                        summary = "更改字体样式",
                        items = listOf("系统默认", "Serif", "Sans-serif", "Monospace"),
                        selectedIndex = 0,
                        onSelectedIndexChange = { mode ->
                        },
                        enabled = false
                    )
                }
            }
        }
    }
}