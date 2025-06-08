package com.smart.htu.screens.application.librarySearch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical
import kotlin.math.ceil

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun LibrarySearchScreen(
    navController: NavController,
    viewModel: LibrarySearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    val (expand, onExpand) = rememberSaveable { mutableStateOf(false) }
    val (isSearching, onSearch) = rememberSaveable { mutableStateOf(false) }
    val searchTextFieldState = rememberTextFieldState()

    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                delay(500)
            }
        }
    }

    BackHandler {
        if (expand || isSearching) {
            onExpand(false)
            onSearch(false)
        } else {
            navController.popBackStack()
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(text = "图书查询")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigateToWebView(
                                url = "https://opac.htu.edu.cn/space/index",
                                label = "图书馆"
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Info, contentDescription = "info")
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
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            onRefresh = onRefresh,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DockedSearchBar(
                    colors = SearchBarDefaults.colors(containerColor = MiuixTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier
                        .heightIn(max = 240.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    inputField = {
                        SearchBarDefaults.InputField(
                            colors = inputFieldColors(
                                focusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MiuixTheme.colorScheme.surfaceContainerHigh
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            state = searchTextFieldState,
                            onSearch = {
                                onExpand(false)
                                onSearch(true)
                                viewModel.librarySearch(it, 1)
                                viewModel.addSearchHistory(it)
                            },
                            expanded = expand,
                            onExpandedChange = { onExpand(it) },
                            placeholder = { Text(text = "搜索书名、作者、ISBN...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (expand) {
                                            onExpand(false)
                                        } else {
                                            if (isSearching)
                                                onSearch(false)
                                            else
                                                onExpand(true)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = when {
                                            expand -> Icons.Default.KeyboardArrowUp
                                            isSearching -> Icons.Outlined.Clear
                                            else -> Icons.Default.KeyboardArrowDown
                                        },
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                    },
                    expanded = expand,
                    onExpandedChange = { onExpand(it) },
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        uiState.searchHistoryList.forEachIndexed { index, resultText ->
                            ListItem(
                                headlineContent = { Text(text = resultText) },
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_history_24),
                                        contentDescription = null
                                    )
                                },
                                trailingContent = {
                                    IconButton(onClick = { viewModel.deleteSearchHistory(index) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Clear,
                                            contentDescription = "delete"
                                        )
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                modifier = Modifier
                                    .clickable {
                                        searchTextFieldState.setTextAndPlaceCursorAtEnd(
                                            resultText
                                        )
                                        viewModel.librarySearch(resultText, page = 1)
                                        onSearch(true)
                                        onExpand(false)
                                    }
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 12.dp),
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .overScrollVertical(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isSearching) {
                        if (uiState.isSearching) {
                            item {
                                CircularProgressIndicator()
                            }
                        } else {
                            if (uiState.bookSearchList.isNotEmpty()) {
                                items(uiState.bookSearchList) {
                                    LibrarySingleBook(
                                        bookContent = it,
                                        onClick = {
                                            navController.navigate("${Destinations.LibrarySearchDetail.route}/${it.bookId}")
                                        }
                                    )
                                }
                                item {
                                    LaunchedEffect(Unit) {
                                        viewModel.loadNextPage(searchTextFieldState.text.toString())
                                    }
                                }
                            } else {
                                item {
                                    EmptyContent(
                                        text = "\"${searchTextFieldState.text}\"\n没有搜索结果",
                                        image = DrawableVectors.emptyData()
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            WaitingBorrowedBookList(
                                uiState = uiState,
                                onClick = {
                                    navController.navigate("${Destinations.LibrarySearchDetail.route}/$it")
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            HistoricalBorrowedBookList(
                                uiState = uiState,
                                onClick = {
                                    navController.navigate("${Destinations.LibrarySearchDetail.route}/$it")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaitingBorrowedBookList(
    uiState: LibrarySearchUiState,
    onClick: (String) -> Unit = {}
) {
    LargeCardDisplay(
        modifier = Modifier,
        title = "待借清单",
        leadingIconPainting = R.drawable.book_4_24px
    ) {
        if (uiState.waitingBorrowedBookList.isNotEmpty()) {
            val rowCount = remember {
                derivedStateOf { ceil(uiState.waitingBorrowedBookList.size / 3.0) }
            }
            val lazyVerticalGridHeight by remember { derivedStateOf { rowCount.value * 180 + (rowCount.value - 1) * 8 } }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(lazyVerticalGridHeight.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(uiState.waitingBorrowedBookList) {
                    Card(
                        colors = CardDefaults.cardColors(Color.Transparent),
                        onClick = {
                            onClick(it.bookId.toString())
                        },
                        modifier = Modifier
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.imageUrl)
                                    .crossfade(true)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .error(R.drawable.book_failure)
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                placeholder = painterResource(id = R.drawable.book_failure)
                            )
                            Text(
                                text = it.title.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .basicMarquee(
                                        repeatDelayMillis = 2_000,
                                    )
                            )
                            Text(
                                text = it.author.toString(),
                                style = MiuixTheme.textStyles.footnote1.copy(color = MiuixTheme.colorScheme.onSurfaceVariantSummary),
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        } else {
            EmptyContent(text = "搜索以添加待借书籍", modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun HistoricalBorrowedBookList(
    uiState: LibrarySearchUiState,
    onClick: (String) -> Unit = {}
) {
    LargeCardDisplay(
        modifier = Modifier,
        title = "借阅历史",
        leadingIconPainting = R.drawable.overview_24px
    ) {
        if (uiState.waitingBorrowedBookList.isNotEmpty()) {
            val rowCount = remember {
                derivedStateOf { ceil(uiState.waitingBorrowedBookList.size / 3.0) }
            }
            val lazyVerticalGridHeight by remember { derivedStateOf { rowCount.value * 180 + (rowCount.value - 1) * 8 } }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(lazyVerticalGridHeight.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(uiState.waitingBorrowedBookList) {
                    Card(
                        colors = CardDefaults.cardColors(Color.Transparent),
                        onClick = {
                            onClick(it.bookId.toString())
                        },
                        modifier = Modifier
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.imageUrl)
                                    .crossfade(true)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .error(R.drawable.book_failure)
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                placeholder = painterResource(id = R.drawable.book_failure)
                            )
                            Text(
                                text = it.title.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .basicMarquee(
                                        repeatDelayMillis = 2_000,
                                    )
                            )
                            Text(
                                text = it.author.toString(),
                                style = MiuixTheme.textStyles.footnote1.copy(color = MiuixTheme.colorScheme.onSurfaceVariantSummary),
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        } else {
            EmptyContent(text = "搜索以添加待借书籍", modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun LibrarySingleBook(
    bookContent: SearchBookData,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = MiuixTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookContent.imageUrl)
                    .crossfade(true)
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .error(R.drawable.book_failure)
                    .build(),
                contentDescription = "picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .width(70.dp)
                    .aspectRatio(10 / 15f)
                    .clip(RoundedCornerShape(10.dp)),
                placeholder = painterResource(id = R.drawable.book_failure)
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.8f)
            ) {
                Text(
                    text = bookContent.title.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MiuixTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookContent.author ?: "未知作者",
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookContent.publisher ?: "未知出版社",
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookContent.publishYear ?: "未知出版年份",
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "可借\n${bookContent.borrowableCount}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

/*@Composable
fun LibrarySingleBookDetailNoImage(content: SearchBookData) {
    val scope = rememberCoroutineScope()
    Surface(
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        modifier = Modifier.fillMaxWidth(),
        color = MiuixTheme.colorScheme.secondaryContainer,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MiuixTheme.colorScheme.onBackground
                    ),
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis
                )
                SingleMessage("编著", content.publisher)
                SingleMessage("出版社", content.bookId)
                SingleMessage("出版年份", content.publishYear)
                SingleMessage("ISBN", content.isbn)
            }
            IconButton(
                onClick = {
                    copyContent("${content.title} ${content.publisher} ${content.isbn}")
                    scope.launch {
                        snackBarHostState.showSnackbar("已复制到剪切板")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.content_copy_24px),
                    contentDescription = "copy",
                    tint = MiuixTheme.colorScheme.onBackground,
                )
            }
        }
    }
}*/

/*@Composable
fun LibrarySingleBookDetail(content: LibraryBookDetail) {
    Surface(
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        modifier = Modifier.fillMaxWidth(),
        color = MiuixTheme.colorScheme.secondaryContainer,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if ("尚无复本信息" in content.bookPosition)
                Text(
                    text = content.bookPosition,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MiuixTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(10.dp)
                )
            else
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (content.library == "" && content.bookPosition == "")
                        Text(
                            text = content.description,
                            fontSize = 16.sp,
                            color = MiuixTheme.colorScheme.onBackground
                        )
                    else {
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                        ) {
                            Text(
                                text = content.bookPosition,
                                fontSize = 20.sp,
                                color = MiuixTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = content.library,
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = content.description,
                            maxLines = 1,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.onBackground
                        )
                    }
                }
        }
    }
}*/

/*
@Composable
fun SingleMessage(label: String, content: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.3f),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MiuixTheme.colorScheme.onBackground
            )
        )
        Text(
            text = content,
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
            )
        )
    }
}*/
