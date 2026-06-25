package com.smart.htu.screens.application.websiteNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.api.module.WebsiteNavigationEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic


@Composable
fun WebsiteNavigation(
    viewModel: WebsiteNavigationViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.getWebsiteNavigation()
            isRefreshing = false
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = R.string.website_navigation),
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },

                            ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                )
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .scrollEndHaptic()
                        .overScrollVertical(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (uiState.websiteList) {
                        null -> {
                            item { CircularProgressIndicator() }
                        }

                        else -> {
                            if (uiState.websiteList.isNullOrEmpty()) {
                                item {
                                    EmptyContent(
                                        text = "暂无数据",
                                        image = emptyData()
                                    )
                                }
                            } else {
                                items(uiState.websiteList ?: emptyList()) {
                                    WebsiteItem(
                                        onClick = {
                                            navigator.push(
                                                Route.ApplicationWebView(
                                                    it.url,
                                                    it.name
                                                )
                                            )
                                        },
                                        websiteNavigation = it
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WebsiteItem(
    onClick: () -> Unit,
    websiteNavigation: WebsiteNavigationEntity
) {
    Card {
        BasicComponent(
            title = websiteNavigation.name,
            summary = websiteNavigation.description ?: websiteNavigation.name,
            onClick = onClick
        )
    }
}