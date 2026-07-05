package com.smart.htu.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.component.AdaptiveTopAppBar
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.screens.main.TaskType
import com.smart.htu.screens.navigation.Route
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.formatDateToFriendly
import com.smart.htu.utils.DateUtil.toTimeStamp
import com.smart.htu.utils.MD5Util.md5
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TabRowDefaults
import top.yukonga.miuix.kmp.basic.TabRowWithContour
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDateTime

@Composable
fun NewsScreen(
    contentPadding: PaddingValues,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

    val tabItems = uiState.newsOptionItems.map { it.label.label }
    val newsPagerState = rememberPagerState(
        pageCount = { tabItems.size },
        initialPage = 1
    )
    val selectedTabIndex = remember { derivedStateOf { newsPagerState.currentPage } }

    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.getBannerImgList()
            viewModel.getNewsList(selectedTabIndex.value)
            isRefreshing = false
        }
    }

    LaunchedEffect(selectedTabIndex.value) {
        snapshotFlow { newsPagerState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { inProgress -> !inProgress }
            .collectLatest {
                val page = newsPagerState.currentPage
                if (uiState.newsList[page] == null) {
                    viewModel.getNewsList(page, 1)
                }
            }
    }

    var collapsedFraction by remember { mutableFloatStateOf(scrollBehavior.state.collapsedFraction) }
    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        snapshotFlow { scrollBehavior.state.collapsedFraction }.collectLatest {
            collapsedFraction = it
        }
    }
    val dynamicTopPadding by remember { derivedStateOf { 12.dp * (1f - collapsedFraction) } }

    val backdrop = rememberBlurBackdrop(uiState.blurEffect)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                AdaptiveTopAppBar(
                    title = stringResource(R.string.news),
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    actions = {
                        IconButton(onClick = { navigator.push(Route.NewsMark) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.bookmark_24px),
                                contentDescription = "history"
                            )
                        }
                        IconButton(
                            onClick = { navigator.push(Route.NewsSearch) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search"
                            )
                        }
                    },
                    bottomContent = {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = dynamicTopPadding, bottom = 4.dp)
                        ) {
                            TabRowWithContour(
                                tabs = tabItems.map { stringResource(id = it) },
                                selectedTabIndex = selectedTabIndex.value,
                                onTabSelected = { index ->
                                    scope.launch {
                                        newsPagerState.animateScrollToPage(index)
                                    }
                                },
                                maxWidth = 140.dp,
                                itemSpacing = 16.dp,
                                colors = TabRowDefaults.tabRowColors(
                                    backgroundColor = Color.Transparent,
                                    selectedBackgroundColor = Color.Transparent
                                )
                            )
                        }
                    }
                )
            }
        }
    ) {

        val bannerPicUrl = uiState.bannerPicList.map { it.imgUrl }
        val bannerTitle = uiState.bannerPicList.map { it.title }
        val bannerUrl = uiState.bannerPicList.map { it.url }

        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            contentPadding = PaddingValues(
                top = it.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (backdrop != null) {
                        it.layerBackdrop(backdrop)
                    } else {
                        it
                    }
                }
        ) {
            HorizontalPager(
                state = newsPagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                pageSpacing = 12.dp
            ) { pageIndex ->
                Box {
                    val lazyListState = rememberLazyListState()
                    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
                    val pageNumber = remember { mutableIntStateOf(1) }
                    if (uiState.newsList[pageIndex] == null) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    } else {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier
                                .fillMaxSize()
                                .overScrollVertical(),
                            overscrollEffect = null,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = it.calculateTopPadding(),
                                bottom = contentPadding.calculateBottomPadding() + 16.dp
                            ),
                        ) {
                            if (pageIndex == 1 && uiState.loadImgEnabled) {
                                item {
                                    HorizontalBanner(
                                        bannerPicUrl = bannerPicUrl,
                                        bannerUrl = bannerUrl,
                                        bannerTitle = bannerTitle
                                    ) { url, label ->
                                        navigator.push(Route.NewsDetail(url, label, "Banner"))
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            itemsIndexed(
                                uiState.newsList[pageIndex] ?: emptyList()
                            ) { _, news ->
                                NewsItem(
                                    news = news,
                                    imageLoadEnabled = uiState.loadImgEnabled,
                                    onClick = {
                                        viewModel.addNewsHistory(
                                            NewsMarkEntity(
                                                title = news.title,
                                                url = news.url,
                                                timeStamp = LocalDateTime.now().toTimeStamp(),
                                                source = context.getString(news.label.label)
                                            )
                                        )
                                        navigator.push(
                                            Route.NewsDetail(
                                                url = news.url,
                                                title = news.title,
                                                source = context.getString(news.label.label)
                                            )
                                        )
                                    },
                                    onAddClick = {
                                        viewModel.addTaskList(it)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            item {
                                LaunchedEffect(Unit) {
                                    pageNumber.intValue += 1
                                    viewModel.getNewsList(pageIndex, pageNumber.intValue)
                                }
                            }
                        }
                    }
                    UpFloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = contentPadding.calculateBottomPadding())
                            .padding(14.dp)
                            .zIndex(1f),
                        fabVisible = fabVisible,
                        onClick = { scope.launch { lazyListState.scrollToItem(0) } }
                    )
                }
            }
        }
    }
}


@Composable
fun NewsItem(
    news: NewsItemEntity,
    imageLoadEnabled: Boolean = true,
    maxLines: Int = 2,
    onClick: () -> Unit,
    onAddClick: (TaskEntity) -> Unit
) {
    Card(
        onClick = onClick,
        showIndication = true,
        modifier = Modifier
            .fillMaxWidth(),
        insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = news.title,
                    fontSize = MiuixTheme.textStyles.main.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = MiuixTheme.colorScheme.onSurface,
                    letterSpacing = 0.2.sp,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatDateToFriendly(news.time),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = MiuixTheme.textStyles.subtitle.fontSize
                )
            }
            if (imageLoadEnabled && news.imgUrl.isNotEmpty()) {
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
                    placeholder = painterResource(id = R.drawable.ic_loading_placeholder_horizontal),
                    error = painterResource(id = R.drawable.ic_loading_placeholder_horizontal)
                )
            }
            if (news.label == NewsType.RESEARCH || news.label == NewsType.MATH_LECTURES) {
                IconButton(
                    backgroundColor = MiuixTheme.colorScheme.secondaryContainer.copy(
                        alpha = 0.8f
                    ),
                    minHeight = 35.dp,
                    minWidth = 35.dp,
                    onClick = {
                        val task = TaskEntity(
                            id = md5(news.title + news.time),
                            type = TaskType.Event,
                            title = news.title,
                            location = news.label.name,
                            startDateTime = LocalDateTime.of(2026, 1, 5, 10, 0, 0),
                            endDateTime = LocalDateTime.of(2026, 1, 5, 11, 0, 0),
                            actionType = ApplicationEntity.RouteType.Url,
                            action = news.url
                        )
                        onAddClick(task)
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Outlined.Add,
                        tint = MiuixTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun HorizontalBanner(
    bannerPicUrl: List<String>,
    bannerUrl: List<String>,
    bannerTitle: List<String>,
    onClick: (String, String) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { bannerPicUrl.count() },
        initialPage = 0
    )
    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.clickable {
                    onClick(bannerUrl[it], "河南师范大学")
                },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bannerPicUrl[it])
                        .crossfade(true)
                        .build(),
                    contentDescription = "picture",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xAA000000)
                                    ),
                                    startY = 300f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                        },
                    error = painterResource(id = R.drawable.ic_placeholder_large),
                    placeholder = painterResource(id = R.drawable.ic_placeholder_large)
                )
                Text(
                    text = bannerTitle[it],
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = Color.White,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            bannerPicUrl.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (i == pagerState.currentPage) Color.White else Color.White.copy(
                                alpha = 0.5f
                            )
                        )
                )
            }
        }
    }
}