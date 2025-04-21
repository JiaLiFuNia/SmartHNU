package com.smart.htu.screens.news

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.formatDateToFriendly
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun NewsScreen(
    contentPadding: PaddingValues,
    navController: NavController,
    viewModel: NewsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    TopAppBarDefaults.pinnedScrollBehavior()

    val tabItems = uiState.newsOptionItems.map { it.label.label }
    val newsPagerState = rememberPagerState(
        pageCount = { tabItems.size },
        initialPage = 1
    )
    val selectedTabIndex = remember { derivedStateOf { newsPagerState.currentPage } }

    val scope = rememberCoroutineScope()
    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.getBannerImgList()
                viewModel.getNewsList(selectedTabIndex.value)
            }
        }
    }

    val textFieldState = rememberTextFieldState()
    val (expanded, onExpand) = rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(selectedTabIndex.value) {
        if (uiState.newsList[selectedTabIndex.value].data?.isEmpty() != false)
            viewModel.getNewsList(selectedTabIndex.value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(bottom = contentPadding.calculateBottomPadding())
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f)
                .background(MiuixTheme.colorScheme.background)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                colors = SearchBarDefaults.colors(containerColor = MiuixTheme.colorScheme.surfaceContainerHigh),
                inputField = {
                    SearchBarDefaults.InputField(
                        modifier = Modifier,
                        state = textFieldState,
                        onSearch = { onExpand(false) },
                        expanded = expanded,
                        onExpandedChange = { onExpand(it) },
                        placeholder = { Text("搜索新闻、公告和通知...") },
                        leadingIcon = {
                            if (expanded) {
                                IconButton(onClick = { onExpand(false) }) {
                                    Icon(
                                        Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.MoreVert, contentDescription = null)
                            }
                        },
                        colors = inputFieldColors(
                            focusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                },
                expanded = expanded,
                onExpandedChange = { onExpand(it) },
                modifier = Modifier
            ) {

            }
            PrimaryScrollableTabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = newsPagerState.currentPage,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                indicator = { },
                divider = { }
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex.value,
                        onClick = {
                            scope.launch {
                                newsPagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = MiuixTheme.colorScheme.onSurface,
                        unselectedContentColor = MiuixTheme.colorScheme.onSurface
                    ) {
                        Text(
                            text = stringResource(id = item),
                            modifier = Modifier.padding(8.dp),
                            fontSize = if (index == selectedTabIndex.value) 16.sp else 15.sp,
                            fontWeight = if (index == selectedTabIndex.value) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
        val bannerPicUrl = uiState.bannerPicList.data?.map { it.imgUrl } ?: emptyList()
        val bannerTitle = uiState.bannerPicList.data?.map { it.title } ?: emptyList()
        val bannerUrl = uiState.bannerPicList.data?.map { it.url } ?: emptyList()

        top.yukonga.miuix.kmp.basic.PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp)
        ) {
            Column(
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
                    .padding(top = 4.dp)
            ) {
                HorizontalPager(
                    state = newsPagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    pageSpacing = 12.dp
                ) {
                    if (uiState.newsList[it].status != Status.SUCCESS) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    } else {
                        top.yukonga.miuix.kmp.basic.LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (it == 1)
                                item {
                                    HorizontalMultiBrowseCarousel(
                                        state = rememberCarouselState { bannerPicUrl.count() },
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth(),
                                        preferredItemWidth = 320.dp,
                                        itemSpacing = 4.dp
                                    ) { index ->
                                        Box(
                                            modifier = Modifier.clickable {
                                                navController.navigateToWebView(
                                                    url = bannerUrl[index],
                                                    label = "河南师范大学"
                                                )
                                            },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(bannerPicUrl[index])
                                                    .crossfade(true)
                                                    .addHeader("User-Agent", "Mozilla/5.0")
                                                    .error(R.drawable.image_placeholder)
                                                    .build(),
                                                contentDescription = "picture",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(16 / 9f)
                                                    .maskClip(MaterialTheme.shapes.extraLarge),
                                                placeholder = painterResource(id = R.drawable.image_placeholder)
                                            )
                                            Text(
                                                text = bannerTitle[index],
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.BottomEnd)
                                                    .padding(
                                                        horizontal = 16.dp,
                                                        vertical = 8.dp
                                                    )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            itemsIndexed(
                                uiState.newsList[it].data ?: emptyList()
                            ) { _, news ->
                                NewsItem(
                                    news = news,
                                    onClick = {
                                        navController.navigateToWebView(
                                            url = news.url,
                                            label = context.getString(news.label.label)
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NewsItem(news: NewsItemEntity, maxLines: Int = 2, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
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
                Text(text = formatDateToFriendly(news.time))
            },
            trailingContent = {
                if (news.imgUrl.endsWith(".jpg") || news.imgUrl.endsWith(".png")) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(news.imgUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(90.dp)
                            .aspectRatio(16 / 10f)
                            .clip(RoundedCornerShape(8.dp)),
                        placeholder = painterResource(id = R.drawable.image_placeholder)
                    )
                }
            }
        )
    }
}
