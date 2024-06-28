package com.xhand.hnu.screens

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
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
                viewModel = viewModel
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = viewModel(),
    viewModel: SettingsViewModel
) {
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val options = listOf(
        "通知公告", "师大要闻", "新闻速递", "教务通知", "公示公告", "考务管理"
    )
    var selectedOption by remember { mutableIntStateOf(0) }
    var isShowSearchBar by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    var isSearch by remember { mutableStateOf(false) }

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

    // val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    /*Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        topBar = {
            /*TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { },
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().background(color = Color.Transparent),
                        contentAlignment = Alignment.Center,
                    ) {
                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            inputField = {
                                SearchBarDefaults.InputField(
                                    query = "输入关键词搜索...",
                                    onQueryChange = {},
                                    onSearch = {},
                                    expanded = false,
                                    onExpandedChange = {},
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "搜索"
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.hnu),
                                            contentDescription = "hnu",
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                )
                            },
                            expanded = false,
                            onExpandedChange = {}
                        ) {
                        }
                    }
                }
            )*/

            /*TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(colorScheme.surface.copy())

                ),
                title = { Text("新闻") },
                actions = {
                    if (!isShowSearchBar) {
                        IconButton(
                            onClick = {
                                isShowSearchBar = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isShowSearchBar = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                        val interactionSource = remember { MutableInteractionSource() }
                        BasicTextField(
                            value = newsViewModel.content,
                            onValueChange = { newsViewModel.content = it },
                            interactionSource = interactionSource,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = colorScheme.onSurface
                            )
                        ) {
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = newsViewModel.content,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                interactionSource = interactionSource,
                                visualTransformation = VisualTransformation.None,
                                leadingIcon = {
                                    IconButton(onClick = { isSearch = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "搜索"
                                        )
                                    }
                                },
                                placeholder = { Text(text = "请输入关键词...") },
                                container = {
                                    OutlinedTextFieldDefaults.ContainerBox(
                                        enabled = true,
                                        isError = false,
                                        interactionSource = interactionSource,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                        ),
                                        focusedBorderThickness = 0.dp,
                                        unfocusedBorderThickness = 0.dp,
                                    )
                                }
                            )
                        }
                    }
                }
            )*/
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_filter),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {*/
        val pagerState = rememberPagerState(pageCount = { pictures.size })
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
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
    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty())
            newsViewModel.searchRes(searchText)
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
                        newsViewModel.isSearching = true
                    },
                    placeholder = { Text("搜索新闻和通知") },
                    leadingIcon = {
                        IconButton(onClick = { searchBarExpand = true }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "搜索"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.hnu),
                                contentDescription = "hnu",
                                modifier = Modifier.size(30.dp)
                            )
                        }

                    }
                )
            },
            expanded = searchBarExpand,
            onExpandedChange = {
                searchBarExpand = it
                newsViewModel.isSearching = true
            }
        ) {
        }
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            options.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title.substring(0, 2)) }
                )
            }
        }
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
            if (!newsViewModel.isSearching) {
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
                                    .clip(RoundedCornerShape(8.dp))
                            ) { index ->
                                Box {
                                    AsyncImage(
                                        model = pictures[index],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
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
                            if (article.type == options[selectedTabIndex]) {
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
            } else {
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
            }
        }
    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "选择新闻源") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            options.forEachIndexed() { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = options[selectedOption] == option,
                            onClick = { selectedOption = index }
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = options[selectedOption] == option,
                        onClick = null
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}
