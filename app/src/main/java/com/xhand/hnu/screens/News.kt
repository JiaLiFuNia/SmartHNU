package com.xhand.hnu.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.material.placeholder
import com.xhand.hnu.R
import com.xhand.hnu.components.ArticleListItem
import com.xhand.hnu.components.ModalBottomSheet
import com.xhand.hnu.model.entity.ArticleListEntity
import com.xhand.hnu.network.PictureListItem
import com.xhand.hnu.screens.navigation.Destinations
import com.xhand.hnu.viewmodel.NewsUiState
import com.xhand.hnu.viewmodel.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class NewsOptions(
    val title: String,
    val source: String
)

private val newsOptions = listOf(
    NewsOptions("历史记录",""),
    NewsOptions("通知公告", "河南师范大学主页"),
    NewsOptions("师大要闻", "河南师范大学主页"),
    NewsOptions("新闻速递", "河南师范大学主页"),
    NewsOptions("教务通知", "河南师范大学教务处"),
    NewsOptions("公示公告", "河南师范大学教务处"),
    NewsOptions("考务管理", "河南师范大学教务处"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = viewModel()
) {
    val uiState by newsViewModel.uiState.collectAsState()

    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val newsTypes = listOf(
        "河南师范大学主页",
        "河南师范大学教务处",
        "数学与信息科学学院"
    )
    var selectedOption by remember { mutableIntStateOf(0) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1500)
            newsViewModel.newsList()
            isRefreshing = false
        }
    }
    val scrollState = rememberScrollState()
    val newsPagerState = rememberPagerState(
        pageCount = { newsOptions.size },
        initialPage = 1
    )
    val selectedTabIndex = remember { derivedStateOf { newsPagerState.currentPage } }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (!uiState.hadGetNew) {
            newsViewModel.newsList()
            newsViewModel.imageLoad()
        }
    }
    LaunchedEffect(newsViewModel.searchText) {
        if (newsViewModel.searchText != "" && newsViewModel.searchBarExpand && newsViewModel.searchList.isEmpty()) {
            newsViewModel.searchRes(newsViewModel.searchText)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier,
            inputField = {
                SearchBarDefaults.InputField(
                    query = newsViewModel.searchText,
                    onQueryChange = { newsViewModel.searchText = it },
                    onSearch = { },
                    expanded = newsViewModel.searchBarExpand,
                    onExpandedChange = {
                        newsViewModel.searchBarExpand = it
                    },
                    placeholder = { Text("搜索新闻和通知") },
                    leadingIcon = {
                        if (newsViewModel.searchBarExpand)
                            IconButton(
                                onClick = {
                                    newsViewModel.searchBarExpand = false
                                    newsViewModel.searchText = ""
                                    newsViewModel.searchList.clear()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "返回"
                                )
                            }
                        else
                            IconButton(onClick = { /*showBottomSheet.value = true*/ }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "其他"
                                )
                            }
                    },
                    trailingIcon = {
                        if (!newsViewModel.searchBarExpand)
                            IconButton(onClick = { onRefresh() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.hnu),
                                    contentDescription = "hnu",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        else
                            IconButton(
                                onClick = {
                                    if (newsViewModel.searchText == "")
                                        newsViewModel.searchBarExpand = false
                                    else
                                        newsViewModel.searchText = ""
                                    newsViewModel.searchList.clear()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "close"
                                )
                            }
                    }
                )
            },
            expanded = newsViewModel.searchBarExpand,
            onExpandedChange = {
                newsViewModel.searchBarExpand = it
            }
        ) {
            if (uiState.isSearching && newsViewModel.searchText != "" && newsViewModel.searchList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (newsViewModel.searchList.isEmpty() && newsViewModel.searchText != "")
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "没有找到相关新闻或通知", color = Color.Gray)
                    }
                else {
                    if (newsViewModel.searchText != "")
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            newsViewModel.searchList.forEach { article ->
                                ArticleListItem(
                                    article = article,
                                    loaded = false,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(Destinations.NewsDetail.route)
                                            newsViewModel.article = article
                                        },
                                    color = ListItemDefaults.colors(
                                        Color.Transparent
                                    )
                                )
                            }
                        }
                    else
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "近期搜索",
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .padding(start = 10.dp),
                                    textAlign = TextAlign.Left
                                )
                                TextButton(
                                    onClick = {
                                        newsViewModel.clearHistoryList()
                                    },
                                    enabled = uiState.searchHistory.isNotEmpty(),
                                    modifier = Modifier
                                        .padding(end = 10.dp),
                                ) {
                                    Text(
                                        text = "全部清除",
                                        fontSize = 13.sp,
                                    )
                                }
                            }
                            uiState.searchHistory.forEach { article ->
                                ListItem(
                                    headlineContent = { Text(text = article) },
                                    leadingContent = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_history_24),
                                            contentDescription = "历史"
                                        )
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .clickable { newsViewModel.searchText = article }
                                )
                            }
                        }
                }
            }
        }
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex.value,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                    width = 40.dp,
                    shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                )
            }
        ) {
            newsOptions.forEachIndexed { index, newsOption ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {
                        scope.launch {
                            newsPagerState.animateScrollToPage(index)
                        }
                    },
                    selectedContentColor = colorScheme.primary,
                    unselectedContentColor = colorScheme.onSurface,
                    text = { Text(text = newsOption.title) }
                )
            }
        }
        HorizontalPager(state = newsPagerState) { newsIndex ->
            PullToRefreshBox(
                    state = state,
                    onRefresh = onRefresh,
                    isRefreshing = isRefreshing,
                    contentAlignment = Alignment.TopStart
            ) {
                if (uiState.isLoadingNewsList) {
                        Box(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        NewsList(
                            newsViewModel = newsViewModel,
                            navController = navController,
                            selectedTabIndex = newsIndex,
                            uiState = uiState
                        )
                    }
                }
        }
    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "配置新闻源") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            newsTypes.forEachIndexed { index, newsType ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = newsTypes[selectedOption] == newsType,
                            onClick = { selectedOption = index }
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = newsTypes[selectedOption] == newsType,
                        onClick = null
                    )
                    Text(
                        text = newsType,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NewsList(
    newsViewModel: NewsViewModel,
    navController: NavController,
    selectedTabIndex: Int,
    pictures: List<PictureListItem> = newsViewModel.pictures,
    uiState: NewsUiState
) {
    val pagerState = rememberPagerState(pageCount = { pictures.size })
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            with(pagerState) {
                animateScrollToPage((currentPage + 1) % pageCount)
            }
        }
    }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(scrollState),
        ) {
            if (selectedTabIndex == 1) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .padding(10.dp)
                ) { index ->
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pictures[index].url)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.placeholder),
                            contentDescription = "pictures",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .aspectRatio(16 / 9f)
                                .clickable {
                                    newsViewModel.article = ArticleListEntity(
                                        pictures[index].title,
                                        "",
                                        pictures[index].newsUrl,
                                        "师大要闻",
                                        false
                                    )
                                    navController.navigate(Destinations.NewsDetail.route)
                                },
                            contentScale = ContentScale.Crop
                        )
                        /*Text(
                            text = pictures[index].title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 5.dp, bottom = 10.dp),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )*/
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(5.dp)
                                .height(25.dp)
                                .width(50.dp)
                                .placeholder(visible = uiState.isLoadingNewsList)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "${index + 1}/${pictures.size}",
                                    color = colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            uiState.list.forEach { article ->
                if (article.type == newsOptions[selectedTabIndex].title) {
                    ArticleListItem(
                        article = article,
                        loaded = uiState.isLoadingNewsList,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Destinations.NewsDetail.route)
                                newsViewModel.article = article
                            }
                    )
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            visible = scrollState.value > 0,
            enter = slideInVertically(initialOffsetY = { it * 2 }),
            exit = slideOutVertically(targetOffsetY = { it * 2 }),
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview
fun NewsScreenPreview() {
    NewsScreen(
        navController = NavController(LocalContext.current), newsViewModel = NewsViewModel(
            LocalContext.current
        )
    )
}