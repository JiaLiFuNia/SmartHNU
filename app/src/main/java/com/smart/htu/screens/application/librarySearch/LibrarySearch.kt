package com.smart.htu.screens.application.librarySearch

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.R
import com.smart.htu.component.LargeCardDisplay
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.sendToast
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchScreen(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    val hazeState = remember { HazeState() }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var expand by rememberSaveable { mutableStateOf(false) }
    val keyword = rememberSaveable { mutableStateOf("概率论") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomSheetState = rememberModalBottomSheetState()
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
        if (expand) {
            expand = false
            keyword.value = ""
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer
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
            if (expand)
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
                    TextField(
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(30.dp),
                        value = keyword.value,
                        onValueChange = { keyword.value = it },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "搜索书名、作者、ISBN...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "search"
                            )
                        },
                        trailingIcon = {
                            if (expand)
                                TextButton(
                                    onClick = {
                                        expand = false
                                        keyword.value = ""
                                    }
                                ) {
                                    Text(text = "取消")
                                }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                expand = true
                                viewModel.librarySearch(keyword.value)
                            }
                        )
                    )
                }
                if (expand) {
                    if (uiState.isSearching) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(strokeWidth = 5.dp)
                            }
                        }
                    } else {
                        if (uiState.searchResult.isNotEmpty()) {
                            itemsIndexed(uiState.searchResult) { _, item ->
                                LibrarySingleBook(
                                    state = uiState.rentList.any { item.id == it.id },
                                    content = item,
                                    onClick = {
                                        showBottomSheet = true
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
                                        sendToast(
                                            context = context,
                                            text = message
                                        )
                                    }
                                )
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
                                showBottomSheet = true
                            },
                            onBlankCardClick = {
                                expand = true
                            }
                        )
                    }
                }
            }
        }
        if (showBottomSheet)
            BookRentDetailBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                uiState = uiState,
                sheetState = bottomSheetState
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRentDetailBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    uiState: LibrarySearchUiState,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ) {
        Text(
            text = "详情", style = MaterialTheme.typography.titleLarge, modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(), textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeWidth = 5.dp)
                    }
                }
            } else {
                itemsIndexed(uiState.singleBookDetail) { index, item ->
                    when (index) {
                        0 -> LibrarySingleBookDetailNoImage(content = item)
                        else -> LibrarySingleBookDetail(content = item)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

    }
}

@Composable
fun RentBooksList(
    uiState: LibrarySearchUiState,
    viewModel: LibrarySearchViewModel,
    onBlankCardClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    LargeCardDisplay(
        modifier = Modifier,
        title = "待借清单(${uiState.rentList.size}/5)",
        leadingIconPainting = R.drawable.book_4_24px
    ) {
        Column(
            modifier = Modifier
        ) {
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
                            vertical = if (index == 0) 10.dp else 5.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_book_24),
                            contentDescription = "book",
                            tint = MaterialTheme.colorScheme.primary,
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
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
            if (uiState.rentList.size != 5)
                Card(
                    colors = CardDefaults.cardColors(Color.Transparent),
                    onClick = {
                        onBlankCardClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 5.dp)
                            .fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "add",
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(text = "添加书籍", fontSize = 18.sp)
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
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LibrarySingleBookDetailNoImage(content: LibraryBookDetail) {
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = content.bookName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                SingleMessage("编著", content.publisher)
                SingleMessage("出版社", content.publishPlace)
                SingleMessage("出版年份", content.publishYear)
                SingleMessage(label = "ISBN", content = content.isbn)
            }
            IconButton(
                onClick = {
                    copyContent("${content.bookName} ${content.publisher} ${content.isbn}")
                    sendToast(context, "已复制")
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
            modifier = Modifier
                .weight(0.3f),
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
        )
        Text(
            text = content,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.weight(0.8f)
        )
    }
}