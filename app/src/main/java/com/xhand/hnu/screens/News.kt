package com.xhand.hnu.screens

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xhand.hnu.R
import com.xhand.hnu.components.ArticleListItem
import com.xhand.hnu.components.ModalBottomSheet
import com.xhand.hnu.viewmodel.NewsViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun NavigationScreen(viewModel: SettingsViewModel, newsViewModel: NewsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "newsList_screen",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {
        composable("newsList_screen") {
            NewsScreen(
                navController = navController,
                newsViewModel = newsViewModel,
                viewModel = viewModel
            )
        }
        composable("detail_screen") {
            ArticleDetailScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel,
                newsViewModel = newsViewModel
            )
        }
    }
}

data class NewsOptions(
    val title: String,
    val source: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = viewModel(),
    viewModel: SettingsViewModel
) {
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val newsOptions = listOf(
        NewsOptions("通知公告", "河南师范大学主页"),
        NewsOptions("师大要闻", "河南师范大学主页"),
        NewsOptions("新闻速递", "河南师范大学主页"),
        NewsOptions("教务通知", "河南师范大学教务处"),
        NewsOptions("公示公告", "河南师范大学教务处"),
        NewsOptions("考务管理", "河南师范大学教务处"),
    )
    val newsTypes = listOf(
        "河南师范大学主页",
        "河南师范大学教务处",
        "数学与信息科学学院"
    )
    var selectedOption by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

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
    val pictures = newsViewModel.pictures
    LaunchedEffect(Unit) {
        newsViewModel.isRefreshing = true
        newsViewModel.newsList()
        newsViewModel.imageLoad()
        newsViewModel.isRefreshing = false
    }
    val pagerState = rememberPagerState(pageCount = { pictures.size })
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            with(pagerState) {
                animateScrollToPage((currentPage + 1) % pageCount)
            }
        }
    }
    var searchBarExpand by remember {
        mutableStateOf(false)
    }
    var searchText by remember {
        mutableStateOf("")
    }
    var historyListIndex by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(searchText) {
        newsViewModel.isSearching = true
        if (searchText.isNotEmpty())
            newsViewModel.searchRes(searchText)
        newsViewModel.isSearching = false
    }
    // 获取SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()
    val statueBarColor = colorScheme.background
    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(color = statueBarColor)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier,
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { searchBarExpand = false },
                    expanded = searchBarExpand,
                    onExpandedChange = {
                        searchBarExpand = it
                    },
                    placeholder = { Text("搜索新闻和通知") },
                    leadingIcon = {
                        if (searchBarExpand)
                            IconButton(
                                onClick = {
                                    searchBarExpand = false
                                    searchText = ""
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "返回"
                                )
                            }
                        else
                            IconButton(onClick = { showBottomSheet.value = true }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "其他"
                                )
                            }
                    },
                    trailingIcon = {
                        if (!searchBarExpand)
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
                                    if (searchText == "")
                                        searchBarExpand = false
                                    else
                                        searchText = ""
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
            expanded = searchBarExpand,
            onExpandedChange = {
                searchBarExpand = it
            }
        ) {
            if (newsViewModel.isSearching && searchText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (newsViewModel.searchList.isEmpty() && searchText.isNotEmpty())
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "没有找到相关新闻或通知", color = Color.Gray)
                    }
                else {
                    if (searchText.isNotEmpty())
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            newsViewModel.searchList.forEach { article ->
                                ArticleListItem(
                                    article = article,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("detail_screen")
                                            viewModel.url = article.url
                                        }
                                )
                            }
                        }
                    else
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "近期搜索",
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 10.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Left
                            )
                            newsViewModel.searchHistory.forEachIndexed { index, article ->
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
                                        .clickable { searchText = article }
                                )
                            }
                        }
                }
            }
        }
        val newsPagerState = rememberPagerState(pageCount = { newsOptions.size })
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        LaunchedEffect(newsPagerState) {
            snapshotFlow { newsPagerState.currentPage }.collect { page ->
                selectedTabIndex = page
            }
        }
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            newsOptions.forEachIndexed { index, newsOption ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = newsOption.title) }
                )
            }
        }
        HorizontalPager(state = newsPagerState) {
            Log.i("TAG6667", "${newsViewModel.searchList}")
            if (newsViewModel.isRefreshing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                PullToRefreshBox(
                    state = state,
                    onRefresh = onRefresh,
                    isRefreshing = isRefreshing,
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // if (!isShowSearchBar) {
                        if (selectedTabIndex == 0)
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .padding(10.dp)
                            ) { index ->
                                Box {
                                    AsyncImage(
                                        model = pictures[index],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .aspectRatio(16 / 9f),
                                        contentScale = ContentScale.Crop
                                    )
                                    Card(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(5.dp)
                                            .height(25.dp)
                                            .width(50.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = "${index + 1}/${pictures.size}"
                                            )
                                        }
                                    }
                                }
                            }
                        newsViewModel.list.forEach { article ->
                            if (article.type == newsOptions[selectedTabIndex].title) {
                                ArticleListItem(
                                    article = article,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("detail_screen")
                                            viewModel.url = article.url
                                        }
                                )
                            }
                        }
                        }
                    }
            }
        }
    }

    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "选择新闻源") {
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
