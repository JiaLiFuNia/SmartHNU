package com.smart.htu.screens.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearch(
    navController: NavController,
    viewModel: NewsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val textValue = rememberSaveable { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    val isSearching = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = "搜索",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Back,
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
            modifier = Modifier
                .padding(it)
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            SearchBar(
                modifier = Modifier.padding(bottom = 8.dp),
                inputField = {
                    InputField(
                        query = textValue.value,
                        onQueryChange = { textValue.value = it },
                        onSearch = {
                            scope.launch {
                                isSearching.value = true
                                viewModel.searchNews(it)
                            }
                        },
                        expanded = false,
                        onExpandedChange = {  },
                        label = "搜索新闻、公告和通知...",
                    )
                },
                expanded = false,
                onExpandedChange = {  }
            ) { }
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(16.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                if (!isSearching.value) {
                    item {
                        EmptyContent(
                            text = "输入关键词进行搜索",
                            image = emptyData()
                        )
                    }
                } else {
                    if (uiState.searchList == null) {
                        item {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (uiState.searchList.isNullOrEmpty()) {
                            item {
                                EmptyContent(
                                    text = "\"${textValue.value}\"\n没有相关新闻或通知",
                                    image = emptyData()
                                )
                            }
                        } else {
                            items(uiState.searchList ?: emptyList()) {
                                NewsItem(
                                    news = it,
                                    onClick = {
                                        navController.navigateToNewsDetail(
                                            url = it.url,
                                            title = it.title,
                                            label = context.getString(it.label.label)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}