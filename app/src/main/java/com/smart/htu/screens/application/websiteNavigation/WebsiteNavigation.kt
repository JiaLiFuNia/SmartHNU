package com.smart.htu.screens.application.websiteNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.Status
import com.smart.htu.api.module.WebsiteNavigationEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebsiteNavigation(
    navController: NavController,
    viewModel: WebsiteNavigationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val pullToRefreshState = rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.getWebsiteNavigation()
            }
        }
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = stringResource(id = R.string.website_navigation)) },
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
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp, 12.dp)
            ) {
                when (uiState.websiteList.status) {
                    Status.LOADING -> {
                        item {
                            CircularProgressIndicator()
                        }
                    }

                    Status.SUCCESS -> {
                        if (uiState.websiteList.data.isNullOrEmpty()) {
                            item {
                                EmptyContent(
                                    text = "暂无数据",
                                    image = DrawableVectors.emptyData()
                                )
                            }
                        } else {
                            items(uiState.websiteList.data ?: emptyList()) {
                                WebsiteItem(
                                    onClick = {
                                        navController.navigateToWebView(it.url, it.name)
                                    },
                                    websiteNavigation = it
                                )
                            }
                        }
                    }

                    else -> {
                        item {
                            EmptyContent(
                                text = "暂无数据",
                                image = DrawableVectors.emptyData()
                            )
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