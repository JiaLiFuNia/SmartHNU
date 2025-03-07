package com.smart.htu.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.R
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.screens.navigateToWebView
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    themeMode: Int,
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            viewModel.getBannerImgList()
            isRefreshing = false
        }
    }

    val textFieldState = rememberTextFieldState()
    val (expanded, onExpand) = rememberSaveable { mutableStateOf(false) }

    val tabItems = uiState.newsOptionItems.map { it.label.label }
    val newsPagerState = rememberPagerState(
        pageCount = { tabItems.size },
        initialPage = 1
    )
    val selectedTabIndex = remember { derivedStateOf { newsPagerState.currentPage } }

    LaunchedEffect(selectedTabIndex.value) {
        if (uiState.newsList[selectedTabIndex.value].data?.isEmpty() != false)
            viewModel.getNewsList(selectedTabIndex.value)
    }

    Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
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
                        colors = inputFieldColors(unfocusedContainerColor = Color.Transparent),
                    )
                },
                colors = SearchBarDefaults.colors(
                    inputFieldColors = inputFieldColors(
                        unfocusedContainerColor = Color.Transparent
                    )
                ),
                expanded = expanded,
                onExpandedChange = { onExpand(it) },
                modifier = Modifier
            ) {

            }
            PrimaryScrollableTabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = newsPagerState.currentPage,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(newsPagerState.currentPage),
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                        width = 24.dp
                    )
                },
                divider = {}
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex.value,
                        onClick = {
                            scope.launch {
                                newsPagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Text(text = stringResource(id = item), modifier = Modifier.padding(8.dp))
                    }
                }
            }

            val bannerPicUrl = uiState.bannerPicList.data?.map { it.imgUrl } ?: emptyList()
            val bannerTitle = uiState.bannerPicList.data?.map { it.title } ?: emptyList()
            val bannerUrl = uiState.bannerPicList.data?.map { it.url } ?: emptyList()
            HorizontalMultiBrowseCarousel(
                state = rememberCarouselState { bannerPicUrl.count() },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                preferredItemWidth = 320.dp,
                itemSpacing = 4.dp
            ) { index ->
                Box(
                    modifier = Modifier.clickable {
                        navController.navigateToWebView(
                            bannerUrl[index],
                            bannerTitle[index]
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
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() },
                state = state,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .hazeSource(state = hazeState)
                        .fillMaxSize()
                ) {
                    HorizontalPager(
                        state = newsPagerState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (uiState.newsList[it].status == Status.LOADING) {
                            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                        } else {
                            top.yukonga.miuix.kmp.basic.LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = contentPadding
                            ) {
                                itemsIndexed(
                                    uiState.newsList[it].data ?: emptyList()
                                ) { _, news ->
                                    ListItem(
                                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                        headlineContent = { Text(text = news.title) },
                                        modifier = Modifier.clickable {
                                            navController.navigateToWebView(
                                                news.url,
                                                news.title
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
}


@Composable
fun ColorItem(label: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display color box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = RoundedCornerShape(CornerSize(8.dp)))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "$label - ${color.toArgb().toString(16).uppercase()}",
            style = TextStyle(fontSize = 16.sp)
        )
    }
}