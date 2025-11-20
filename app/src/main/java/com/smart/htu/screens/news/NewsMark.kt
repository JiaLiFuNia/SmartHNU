package com.smart.htu.screens.news

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.TabRow
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.utils.DateUtil.formatDateToFriendly
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsMark(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val tabSelectedIndex by remember { derivedStateOf { pagerState.currentPage } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(text = "历史与收藏")
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding() + 8.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            TabRow(
                tabs = listOf("历史", "收藏"),
                selectedTabIndex = tabSelectedIndex,
                onTabSelected = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                pageSpacing = 12.dp
            ) {
                NewsMarkList(list = if (it == 0) uiState.newsHistoryList else uiState.newsFavoriteList) { url, title, label ->
                    navController.navigateToNewsDetail(
                        url = url,
                        title = title,
                        label = label
                    )
                }
            }
        }
    }
}

@Composable
fun PagerScope.NewsMarkList(
    list: List<NewsMarkEntity>,
    onClick: (String, String, String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .overScrollVertical(),
        overscrollEffect = null
    ) {
        if (list.isEmpty()) {
            item {
                EmptyContent(
                    text = "无记录",
                    image = emptyData()
                )
            }
        } else {
            items(list) {
                NewsItem(
                    news = it,
                    onClick = { onClick(it.url, it.title, it.source) }
                )
            }
        }
    }
}

@Composable
fun NewsItem(
    news: NewsMarkEntity,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface,
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "来源：${news.source}")
                    Text(text = formatDateToFriendly(news.time))
                }
            }
        )
    }
}