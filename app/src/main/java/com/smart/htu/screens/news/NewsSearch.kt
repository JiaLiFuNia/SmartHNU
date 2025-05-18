package com.smart.htu.screens.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearch(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val textFieldState = rememberTextFieldState()
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex >= totalItemsCount && totalItemsCount > 0
        }
    }

    val currentSearchKeyword = remember { mutableStateOf("") }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.hasMoreSearchResults && currentSearchKeyword.value.isNotEmpty()) {
            viewModel.searchNews(currentSearchKeyword.value, loadMore = true)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(text = "搜索")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                modifier = Modifier,
                visible = !fabVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = { scope.launch { lazyListState.scrollToItem(0) } }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                        contentDescription = "up"
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(MiuixTheme.colorScheme.background)
            ) {
                SearchBarDefaults.InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    state = textFieldState,
                    onSearch = {
                        currentSearchKeyword.value = it
                        scope.launch {
                            viewModel.searchNews(it)
                        }
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("搜索新闻、公告和通知...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "search"
                        )
                    },
                    trailingIcon = {
                        if (textFieldState.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    textFieldState.clearText()
                                    currentSearchKeyword.value = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "clear"
                                )
                            }
                        }
                    },
                    colors = inputFieldColors(
                        focusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh
                    )
                )
            }
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(16.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                if (uiState.searchList.status == Status.LOADING)
                    item {
                        CircularProgressIndicator()
                    }
                else {
                    if (uiState.searchList.data?.isEmpty() == true) {
                        item {
                            EmptyContent(
                                text = "没有相关新闻或通知",
                                image = DrawableVectors.emptyData()
                            )
                        }
                    }
                    items(uiState.searchList.data ?: emptyList()) {
                        NewsItem(
                            news = it,
                            onClick = {
                                navController.navigateToNewsDetail(
                                    url = it.url,
                                    label = context.getString(it.label.label)
                                )
                            }
                        )
                    }
                    item {
                        if (uiState.hasMoreSearchResults && uiState.searchList.data?.isNotEmpty() == true) {
                            if (isAtBottom) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                )
                            }
                        } else if (!uiState.hasMoreSearchResults && uiState.searchList.data?.isNotEmpty() == true) {
                            Text(
                                text = "没有更多内容了",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}