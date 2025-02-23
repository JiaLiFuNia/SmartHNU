package com.smart.htu.screens.application.librarySearch

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.MainActivity
import com.smart.htu.R
import com.smart.htu.component.BasicBottomSheet
import com.smart.htu.component.card.LargeCardDisplay
import com.smart.htu.utils.copyContent
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun LibrarySearchScreen(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    val hazeState = remember { HazeState() }
    val (showBottomSheet, onShowBottomSheet) = rememberSaveable { mutableStateOf(false) }
    var expand by rememberSaveable { mutableStateOf(false) }
    var isSearching by rememberSaveable { mutableStateOf(false) }
    val searchTextFieldState = rememberTextFieldState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            delay(2000)
            isRefreshing = false
        }
    }

    BackHandler {
        if (expand || isSearching) {
            expand = false
            isSearching = false
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = MainActivity.snackBarHostState)
        },
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surfaceContainer
                ),
                title = {
                    Text(text = "图书查询")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Info, contentDescription = "info")
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
                },
            )
        },
        floatingActionButton = {
                AnimatedVisibility(
                    modifier = Modifier,
                    visible = lazyListState.firstVisibleItemIndex != 0,
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
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = it.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = state
                )
            },
            modifier = Modifier
                .padding()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 15.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                state = lazyListState,
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    DockedSearchBar(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                state = searchTextFieldState,
                                onSearch = {
                                    expand = false
                                    isSearching = true
                                    viewModel.librarySearch(it, 1)
                                    viewModel.addSearchHistory(it)
                                },
                                expanded = expand,
                                onExpandedChange = { expand = it },
                                placeholder = { Text(text = "搜索书名、作者、ISBN...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (isSearching || expand)
                                        TextButton(
                                            onClick = {
                                                isSearching = false
                                                expand = false
                                                searchTextFieldState.clearText()
                                            }
                                        ) {
                                            Text(text = "取消")
                                        }
                                }
                            )
                        },
                        expanded = expand,
                        onExpandedChange = { expand = it },
                    ) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            uiState.searchHistoryList.forEachIndexed { index, resultText ->
                                ListItem(
                                    headlineContent = { Text(resultText) },
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
                                            isSearching = true
                                            expand = false
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
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularWavyProgressIndicator()
                            }
                        }
                    } else {
                        if (uiState.searchResult.isNotEmpty()) {
                            itemsIndexed(uiState.searchResult) { _, item ->
                                LibrarySingleBook(
                                    state = uiState.rentList.any { item.id == it.id },
                                    content = item,
                                    onClick = {
                                        onShowBottomSheet(true)
                                        viewModel.libraryBookDetail(item.id)
                                    },
                                    onFavorite = {
                                        viewModel.changeRentBookState(
                                            RentBookEntity(item.title, item.publisher, item.id)
                                        )
                                        val message = when {
                                            uiState.rentList.size > 4 && !uiState.rentList.any { item.id == it.id } -> "最多添加 5 本"
                                            uiState.rentList.any { item.id == it.id } -> "已取消"
                                            else -> "已添加到待借清单"
                                        }
                                        scope.launch {
                                            val result =
                                                MainActivity.snackBarHostState.showSnackbar(
                                                message = message,
                                                    actionLabel = "取消",
                                                duration = SnackbarDuration.Short
                                            )
                                            when (result) {
                                                SnackbarResult.ActionPerformed -> viewModel.changeRentBookState(
                                                    RentBookEntity(
                                                        item.title,
                                                        item.publisher,
                                                        item.id
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "没有搜索结果")
                                }
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
                                onShowBottomSheet(true)
                            }
                        )
                    }
                }
            }
        }
        BookRentDetailBottomSheet(
            isBottomSheetShow = showBottomSheet,
            onDismissRequest = { onShowBottomSheet(false) },
            uiState = uiState,
            sheetState = bottomSheetState
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun BookRentDetailBottomSheet(
    isBottomSheetShow: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    uiState: LibrarySearchUiState,
) {
    BasicBottomSheet(
        sheetState = sheetState,
        isBottomSheetShow = isBottomSheetShow,
        title = "详情",
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularWavyProgressIndicator()
                    }
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
        Column(
            modifier = Modifier.heightIn(min = 120.dp)
        ) {
            if (uiState.rentList.isNotEmpty()) {
                uiState.rentList.forEachIndexed { index, it ->
                    Card(
                        colors = CardDefaults.cardColors(Color.Transparent),
                        onClick = {
                            onClick()
                            viewModel.libraryBookDetail(it.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = if (index == 0 || index == uiState.rentList.size - 1) 10.dp else 5.dp
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_book_24),
                                contentDescription = "book",
                                tint = colorScheme.primary,
                                modifier = Modifier
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = it.bookName,
                                    fontSize = 18.sp,
                                    maxLines = 1,
                                    modifier = Modifier.basicMarquee(
                                        repeatDelayMillis = 2_000,
                                    )
                                )
                                Text(
                                    text = it.publisher,
                                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            FilledTonalIconButton(onClick = { viewModel.changeRentBookState(it) }) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            } else
                Card(
                    colors = CardDefaults.cardColors(Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "搜索以添加待借书籍",
                            color = colorScheme.onBackground.copy(0.38f)
                        )
                    }
                }
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
    Card(modifier = Modifier.fillMaxWidth(), onClick = { onClick() }) {
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publisher,
                    maxLines = 1,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publishPlace,
                    maxLines = 1,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.publishYear,
                    maxLines = 1,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onFavorite() }) {
                Icon(
                    imageVector = if (state) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "like",
                    tint = colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LibrarySingleBookDetailNoImage(content: LibraryBookDetail) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
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
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
                        MainActivity.snackBarHostState.showSnackbar("已复制到剪切板")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.content_copy_24px),
                    contentDescription = "copy"
                )
            }
        }

    }
}

@Composable
fun LibrarySingleBookDetail(content: LibraryBookDetail) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
                            fontSize = 16.sp
                        )
                    else {
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                        ) {
                            Text(
                                text = content.bookPosition,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = content.library,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = content.description,
                            maxLines = 1,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis
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
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = content,
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}