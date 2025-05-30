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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.component.BasicDialog
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.copyContent
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun LibrarySearchScreen(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val hazeState = remember { HazeState() }
    val showBottomSheet = rememberSaveable { mutableStateOf(false) }
    val (expand, onExpand) = rememberSaveable { mutableStateOf(false) }
    val (isSearching, onSearch) = rememberSaveable { mutableStateOf(false) }
    val searchTextFieldState = rememberTextFieldState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val scope = rememberCoroutineScope()

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
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

    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
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
                    IconButton(onClick = { /*TODO*/ }) {
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 12.dp),
                state = lazyListState,
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    DockedSearchBar(
                        colors = SearchBarDefaults.colors(containerColor = MiuixTheme.colorScheme.surfaceContainerHigh),
                        modifier = Modifier
                            .heightIn(max = 240.dp)
                            .fillMaxWidth(),
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
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
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
                }
                if (isSearching) {
                    if (uiState.isSearching) {
                        item {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (uiState.searchResult.isNotEmpty()) {
                            itemsIndexed(uiState.searchResult) { _, item ->
                                LibrarySingleBook(
                                    state = uiState.rentList.any { item.id == it.id },
                                    content = item,
                                    onClick = {
                                        showBottomSheet.value = true
                                        viewModel.libraryBookDetail(item.id)
                                    },
                                    onFavorite = {
                                        viewModel.addRentBookList(
                                            BorrowedBookEntity(
                                                bookName = item.title,
                                                publisher = item.publisher,
                                                id = item.id,
                                                imageUrl = item.imageUrl
                                            )
                                        )
                                        val message = when {
                                            uiState.rentList.size > 4 && !uiState.rentList.any { item.id == it.id } -> "最多添加 5 本"
                                            uiState.rentList.any { item.id == it.id } -> "已取消"
                                            else -> "已添加到待借清单"
                                        }
                                        scope.launch {
                                            val result =
                                                snackBarHostState.showSnackbar(
                                                    message = message,
                                                    actionLabel = "取消",
                                                    duration = SnackbarDuration.Short
                                                )
                                            when (result) {
                                                SnackbarResult.ActionPerformed -> viewModel.addRentBookList(
                                                    BorrowedBookEntity(
                                                        bookName = item.title,
                                                        publisher = item.publisher,
                                                        id = item.id,
                                                        imageUrl = item.imageUrl
                                                    )
                                                )

                                                SnackbarResult.Dismissed -> {}
                                            }
                                        }
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
                                    text = "没有搜索结果",
                                    image = DrawableVectors.emptyData()
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        RentBooksList(
                            uiState = uiState,
                            viewModel = viewModel,
                            onClick = {
                                showBottomSheet.value = true
                            }
                        )
                    }
                }
            }
        }
        BookRentDetailBottomSheet(
            isBottomSheetShow = showBottomSheet,
            uiState = uiState,
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun BookRentDetailBottomSheet(
    isBottomSheetShow: MutableState<Boolean>,
    uiState: LibrarySearchUiState,
) {
    BasicDialog(
        showDialog = isBottomSheetShow,
        insideMargin = DpSize(16.dp, 24.dp),
        title = "详情"
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                    )
                }
            } else {
                stickyHeader {
                    LibrarySingleBookDetailNoImage(content = uiState.singleBookDetail.first())
                }
                itemsIndexed(uiState.singleBookDetail.takeLast(uiState.singleBookDetail.size - 1)) { _, item ->
                    LibrarySingleBookDetail(content = item)
                }
            }
        }

    }
}

@Composable
fun RentBooksList(
    uiState: LibrarySearchUiState,
    viewModel: LibrarySearchViewModel,
    onClick: () -> Unit = {}
) {
    LargeCardDisplay(
        modifier = Modifier,
        title = "待借清单(${uiState.rentList.size}/5)",
        leadingIconPainting = R.drawable.book_4_24px
    ) {
        if (uiState.rentList.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.padding(4.dp)
            ) {
                itemsIndexed(uiState.rentList) { index, it ->
                    Card(
                        colors = CardDefaults.cardColors(Color.Transparent),
                        onClick = {
                            onClick()
                            viewModel.libraryBookDetail(it.id)
                        },
                        modifier = Modifier.widthIn(max = 108.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.imageUrl)
                                    .crossfade(true)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .error(R.drawable.book_failure)
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(100.dp)
                                    .aspectRatio(10 / 15f)
                                    .clip(RoundedCornerShape(10.dp)),
                                placeholder = painterResource(id = R.drawable.book_failure)
                            )
                            Text(
                                text = it.bookName,
                                style = MiuixTheme.textStyles.body2,
                                maxLines = 1,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .basicMarquee(
                                        repeatDelayMillis = 2_000,
                                    )
                                    .fillMaxWidth()
                            )
                            Text(
                                text = it.publisher,
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
    state: Boolean,
    content: LibraryBookListEntity,
    onClick: () -> Unit = {},
    onFavorite: () -> Unit = {}
) {
    top.yukonga.miuix.kmp.basic.Surface(
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = MiuixTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(content.imageUrl)
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
                    text = content.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MiuixTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publisher,
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publishPlace,
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publishYear,
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onFavorite() }) {
                Icon(
                    imageVector = if (state) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "like",
                    tint = MiuixTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun LibrarySingleBookDetailNoImage(content: LibraryBookDetail) {
    val scope = rememberCoroutineScope()
    top.yukonga.miuix.kmp.basic.Surface(
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
                    text = content.bookName,
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
                SingleMessage("出版社", content.publishPlace)
                SingleMessage("出版年份", content.publishYear)
                SingleMessage(label = "ISBN", content = content.isbn)
            }
            IconButton(
                onClick = {
                    copyContent("${content.bookName} ${content.publisher} ${content.isbn}")
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
}

@Composable
fun LibrarySingleBookDetail(content: LibraryBookDetail) {
    top.yukonga.miuix.kmp.basic.Surface(
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
}

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
}