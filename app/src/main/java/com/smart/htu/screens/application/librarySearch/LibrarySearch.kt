package com.smart.htu.screens.application.librarySearch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.LibraryBorrowedBookRes.BorrowedBookEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.updateWebViewCookies
import com.smart.htu.di.NetworkModule.ApiConstants.LIBRARY_BASE_URL
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.setting.SettingItemCard
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Cookie
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchScreen(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    val (expand, onExpand) = rememberSaveable { mutableStateOf(false) }
    val (isSearching, onSearch) = rememberSaveable { mutableStateOf(false) }
    val searchTextFieldState = rememberTextFieldState()

    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val showLoginDialog = rememberSaveable { mutableStateOf(false) }
    val loginState =
        remember(uiState.libraryLoginState) { mutableStateOf(uiState.libraryLoginState == 1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            viewModel.getLibraryBorrowedBook()
            viewModel.getCurrentBorrowingBook()
            isRefreshing = false
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
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
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
                    if (uiState.libraryLoginState != 1) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.createSession()
                                    showLoginDialog.value = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "info",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    updateWebViewCookies(
                                        url = LIBRARY_BASE_URL,
                                        cookie = listOf(
                                            Cookie.Builder()
                                                .name("meta-opac.session")
                                                .value(uiState.session)
                                                .domain("opac.htu.edu.cn")
                                                .build()
                                        )
                                    )
                                    navController.navigateToWebView(
                                        url = "https://opac.htu.edu.cn/space/index",
                                        label = "图书馆"
                                    )
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Outlined.Info, contentDescription = "info")
                        }
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
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding() + 8.dp)
            ) {
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
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 12.dp
                    ),
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .overScrollVertical(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                        image = emptyData()
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            CurrentBorrowingBookList(
                                modifier = Modifier.fillMaxWidth(),
                                bookList = uiState.currentBorrowingBookList,
                                loginState = loginState,
                                onClick = {
                                    navController.navigate("${Destinations.LibrarySearchDetail.route}/$it")
                                }
                            )
                        }
                        item {
                            WaitingToBorrowedBookList(
                                bookList = uiState.waitingBorrowedBookList,
                                onClick = {
                                    navController.navigate("${Destinations.LibrarySearchDetail.route}/$it")
                                }
                            )
                        }
                        item {
                            BorrowedBookList(
                                modifier = Modifier.fillMaxWidth(),
                                bookList = uiState.borrowedBookList,
                                loginState = loginState,
                                onClick = {
                                    navController.navigate("${Destinations.LibrarySearchDetail.route}/$it")
                                }
                            )
                        }
                    }
                }
            }
        }

        var verifyCodeRefreshKey by remember { mutableIntStateOf(0) }
        val verifyCodeModel = remember(verifyCodeRefreshKey, uiState.session) {
            val headers = NetworkHeaders.Builder()
                .set("Cookie", "meta-opac.session=${uiState.session}")
                .build()
            ImageRequest.Builder(context)
                .data("https://opac.htu.edu.cn/meta-local/opac/sys/pic_check?rdm=${Math.random()}")
                .httpHeaders(headers)
                .crossfade(true)
                .build()
        }

        LoginDialog(
            showDialog = showLoginDialog,
            summary = "图书馆书目检索系统",
            isNeedVerifyCode = true,
            verifyCodeModel = verifyCodeModel,
            onClickVerifyCode = {
                verifyCodeRefreshKey++
            },
            onLogin = { studentID, password, verifyCode ->
                scope.launch {
                    viewModel.libraryLogin(
                        username = studentID,
                        password = password,
                        verifyCode = verifyCode,
                        onSuccess = {
                            showLoginDialog.value = false
                            isRefreshing = true
                            showToast(context, "登录成功!")
                        },
                        onFailure = {
                            showToast(context, it)
                        }
                    )
                    verifyCodeRefreshKey++
                }
            },
            logState = uiState.libraryLoginState
        )
    }
}

@Composable
fun WaitingToBorrowedBookList(
    bookList: List<LibraryDetailEntity>,
    onClick: (String) -> Unit
) {
    SettingItemCard(
        modifier = Modifier,
        label = "待借清单"
    ) {
        if (bookList.isNotEmpty()) {
            val rowCount = remember {
                derivedStateOf { ceil(bookList.size / 3.0) }
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
                items(bookList) {
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
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .aspectRatio(1 / 1f)
                                    .clip(RoundedCornerShape(10.dp)),
                                error = painterResource(id = R.drawable.ic_placeholder_vertical_error),
                                placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading)
                            )
                            Text(
                                text = it.title.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                textAlign = TextAlign.Left,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = MiuixTheme.colorScheme.onSurface
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
fun BorrowedBookList(
    modifier: Modifier = Modifier,
    bookList: List<BorrowedBookEntity>? = null,
    loginState: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    SettingItemCard(
        modifier = modifier,
        label = "借阅历史"
    ) {
        if (loginState.value) {
            if (bookList == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            } else if (bookList.isNotEmpty()) {
                Column {
                    bookList.forEach {
                        BasicComponent(
                            title = it.title,
                            summary = it.author,
                            onClick = { onClick(it.bibId) })
                    }
                }
            } else {
                EmptyContent(
                    text = "暂无借阅历史",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        } else {
            EmptyContent(
                text = "请先登录图书馆账号",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

@Composable
fun CurrentBorrowingBookList(
    modifier: Modifier = Modifier,
    bookList: List<BorrowedBookEntity>? = null,
    loginState: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    SettingItemCard(
        modifier = modifier,
        label = "当前借阅"
    ) {
        if (loginState.value) {
            if (bookList == null) {
                CircularProgressIndicator(modifier = modifier.height(120.dp))
            } else if (bookList.isNotEmpty()) {
                Column {
                    bookList.forEach {
                        BasicComponent(
                            title = "${it.title}-${it.author}",
                            summary = "应还日期：${it.dueDate}",
                            onClick = { onClick(it.bibId) }
                        )
                    }
                }
            } else {
                EmptyContent(text = "当前暂无借阅", modifier = modifier.height(120.dp))
            }
        } else {
            EmptyContent(text = "请先登录图书馆账号", modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun LibrarySingleBook(
    bookContent: SearchBookData,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = ContinuousRoundedRectangle(top.yukonga.miuix.kmp.basic.CardDefaults.CornerRadius),
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
                    .build(),
                contentDescription = "picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .width(70.dp)
                    .aspectRatio(10 / 15f)
                    .clip(RoundedCornerShape(10.dp)),
                error = painterResource(id = R.drawable.ic_placeholder_vertical_error),
                placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading)
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
                    color = MiuixTheme.colorScheme.onSurface,
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
                modifier = Modifier.padding(end = 8.dp),
                color = MiuixTheme.colorScheme.onSurface
            )
        }
    }
}