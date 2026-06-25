package com.smart.htu.screens.application.librarySearch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.R
import com.smart.htu.api.module.LibraryBorrowedBookRes.BorrowedBookEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.SearchBookData
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.convertStringDateToLocalDate
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.Duration
import java.time.LocalDate
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchScreen(
    viewModel: LibrarySearchViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    val (expand, onExpand) = rememberSaveable { mutableStateOf(false) }
    val (isSearching, onSearch) = rememberSaveable { mutableStateOf(false) }
    var searchValue by rememberSaveable { mutableStateOf("") }

    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val showLoginDialog = remember(uiState.authLoginState) {
        mutableStateOf(uiState.authLoginState != 1)
    }
    val loginState = remember(uiState.libraryLoginState, uiState.authLoginState) {
        mutableStateOf(uiState.libraryLoginState == 1 || uiState.authLoginState == 1)
    }

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
            navigator.pop()
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        },
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = "图书查询",
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.syncCookieToWebView()
                                    navigator.pushWebView(
                                        url = "https://opac.htu.edu.cn/space/reader/readerHome",
                                        title = "图书馆"
                                    )
                                }
                            }
                        ) {
                            Icon(imageVector = MiuixIcons.Regular.Info, contentDescription = "info")
                        }
                    },
                    bottomContent = {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .padding(bottom = 8.dp),
                            inputField = {
                                InputField(
                                    query = searchValue,
                                    onQueryChange = { searchValue = it },
                                    onSearch = {
                                        onSearch(true)
                                        viewModel.librarySearch(it, 1)
                                    },
                                    expanded = expand,
                                    onExpandedChange = { onExpand(it) },
                                    label = "搜索书名、作者、ISBN...",
                                )
                            },
                            outsideEndAction = {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clickable(
                                            interactionSource = null,
                                            indication = null
                                        ) {
                                            onExpand(false)
                                            onSearch(false)
                                            searchValue = ""
                                        },
                                    text = "取消",
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MiuixTheme.colorScheme.primary
                                )
                            },
                            expanded = expand,
                            onExpandedChange = { onExpand(it) },
                            insideMargin = DpSize(16.dp, 0.dp)
                        ) { }
                    }
                )
            }
        },
        floatingActionButton = {
            UpFloatingActionButton(
                fabVisible = fabVisible,
            ) {
                scope.launch { lazyListState.scrollToItem(0) }
            }
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            contentPadding = PaddingValues(top = it.calculateTopPadding() + 12.dp),
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (backdrop != null) it.layerBackdrop(backdrop) else it
                },
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .scrollEndHaptic(),
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
                                        navigator.push(Route.LibrarySearchDetail(it.bookId))
                                    }
                                )
                            }
                            item {
                                LaunchedEffect(Unit) {
                                    viewModel.loadNextPage(searchValue)
                                }
                            }
                        } else {
                            item {
                                EmptyContent(
                                    text = "\"${searchValue}\"\n没有搜索结果",
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
                                navigator.push(Route.LibrarySearchDetail(it))
                            }
                        )
                    }
                    item {
                        WaitingToBorrowedBookList(
                            bookList = uiState.waitingBorrowedBookList,
                            onClick = {
                                navigator.push(Route.LibrarySearchDetail(it))
                            }
                        )
                    }
                    item {
                        BorrowedBookList(
                            bookList = uiState.borrowedBookList,
                            loginState = loginState,
                            onClick = {
                                navigator.push(Route.LibrarySearchDetail(it))
                            }
                        )
                    }
                }
            }
        }
    }

    LoginDialog(
        showDialog = showLoginDialog.value,
        title = "统一身份认证登录",
        summary = "该应用需要进行统一身份认证，请认证后使用。密码与寝室校园网密码一致。",
        onLogin = { studentID, password, _ ->
            scope.launch {
                loginViewModel.authLogin(
                    studentID = studentID,
                    password = password,
                    onSuccess = {
                        scope.launch {
                            showToast(context, "登录成功!")
                            viewModel.libLogin()
                        }
                    },
                    onFailure = {
                        showToast(context, "登录失败！请检查账号密码是否正确")
                    }
                )
            }
        },
        loginState = uiState.libraryLoginState,
        onDismissRequest = {
            showLoginDialog.value = false
        }
    )

}

@Composable
fun WaitingToBorrowedBookList(
    bookList: List<LibraryDetailEntity>,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        BasicComponent(
            title = "待借清单",
            insideMargin = PaddingValues(16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        if (bookList.isNotEmpty()) {
            val rowCount = remember {
                derivedStateOf { ceil(bookList.size / 3.0) }
            }
            val lazyVerticalGridHeight by remember { derivedStateOf { rowCount.value * 185 + (rowCount.value - 1) * 8 } }
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
                    Surface(
                        onClick = {
                            onClick(it.bookId.toString())
                        },
                        shape = RoundedCornerShape(CardDefaults.CornerRadius),
                        modifier = Modifier,
                        color = MiuixTheme.colorScheme.surfaceContainer,
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
    bookList: List<BorrowedBookEntity>? = null,
    loginState: MutableState<Boolean>,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (bookList.isNullOrEmpty()) 120.dp else Dp.Unspecified),
    ) {
        BasicComponent(
            title = "借阅历史",
            insideMargin = PaddingValues(16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        if (loginState.value) {
            if (bookList.isNullOrEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            } else if (bookList.isNotEmpty()) {
                bookList.forEach {
                    BasicComponent(
                        title = it.title,
                        summary = it.author,
                        onClick = { onClick(it.bibId) })
                }
            } else {
                EmptyContent(
                    text = "暂无借阅历史",
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            EmptyContent(
                text = "请先登录图书馆账号",
                modifier = Modifier.fillMaxSize()
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
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        BasicComponent(
            title = "当前借阅",
            insideMargin = PaddingValues(16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        if (loginState.value) {
            if (bookList == null) {
                CircularProgressIndicator(modifier = modifier.height(120.dp))
            } else if (bookList.isNotEmpty()) {
                Column {
                    bookList.forEach {
                        BasicComponent(
                            title = "${it.title}-${it.author}",
                            summary = "应还日期：${it.dueDate}",
                            onClick = { onClick(it.bibId) },
                            endActions = {
                                val remainingDays = remember {
                                    derivedStateOf {
                                        try {
                                            if (it.dueDate != null) {
                                                val dueDate = convertStringDateToLocalDate(
                                                    it.dueDate,
                                                    "yyyy-MM-dd"
                                                )
                                                val currentDate = LocalDate.now()
                                                Duration.between(
                                                    currentDate.atStartOfDay(),
                                                    dueDate.atStartOfDay()
                                                ).toDays().toFloat()
                                            } else {
                                                0f
                                            }
                                        } catch (e: Exception) {
                                            0f
                                        }
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier
                                ) {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = MiuixTheme.colorScheme.onBackground,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 26.sp
                                                )
                                            ) {
                                                append(
                                                    if (remainingDays.value >= 0) {
                                                        "${remainingDays.value.toInt()}"
                                                    } else {
                                                        "${-remainingDays.value.toInt()}"
                                                    }
                                                )
                                            }

                                            withStyle(
                                                style = SpanStyle(
                                                    color = MiuixTheme.colorScheme.onBackground,
                                                    fontSize = 16.sp
                                                )
                                            ) {
                                                append(" 天")
                                            }
                                        }
                                    )
                                }
                            }
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
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        pressFeedbackType = PressFeedbackType.Sink
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookContent.imageUrl)
                    .build(),
                contentDescription = "picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(56.dp)
                    .aspectRatio(10 / 15f)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(id = R.drawable.ic_placeholder_vertical_error),
                placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = bookContent.title.toString(),
                    fontSize = 17.sp,
                    fontWeight = FontWeight(550),
                )
                Text(
                    text = "编者：${bookContent.author ?: "未知作者"}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "出版信息：${bookContent.publishYear ?: "未知出版年份"} | ${bookContent.publisher ?: "未知出版社"}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "可借数目：${bookContent.borrowableCount}本",
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 4
                )
            }
        }
    }
}