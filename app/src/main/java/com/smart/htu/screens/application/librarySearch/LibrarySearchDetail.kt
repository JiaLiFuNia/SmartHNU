package com.smart.htu.screens.application.librarySearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchDetail(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel(),
    bookId: String
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.libraryBookDetail(bookId)
            }
        }
    }

    LaunchedEffect(bookId) {
        viewModel.libraryBookDetail(bookId)
    }

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
                    Text(text = "详情")
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
                            viewModel.addWaitingBorrowedBookList(
                                book = uiState.libraryBookDetail ?: LibraryDetailEntity()
                            )
                        },
                        enabled = uiState.libraryBookDetail != null
                    ) {
                        Icon(
                            imageVector = if (uiState.waitingBorrowedBookList
                                    .map { it.bookId }.contains(bookId)
                            ) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "favorite"
                        )
                    }
                    IconButton(
                        onClick = {
                            copyContent(
                                textContent =
                                    "书名：${uiState.libraryBookDetail?.title}\n" +
                                            "作者：${uiState.libraryBookDetail?.author}\n" +
                                            "ISBN：${uiState.libraryBookDetail?.isbn}\n"
                            )
                        },
                        enabled = uiState.libraryBookDetail != null
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.content_copy_24px),
                            contentDescription = "favorite"
                        )
                    }
                }
            )
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
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.libraryBookDetail == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier
                                    .weight(0.42f)
                                    .fillMaxHeight()
                                    .height(220.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(uiState.libraryBookDetail?.imageUrl)
                                        .crossfade(true)
                                        .addHeader("User-Agent", "Mozilla/5.0")
                                        .error(R.drawable.book_failure)
                                        .build(),
                                    contentDescription = "picture",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    alignment = Alignment.Center,
                                    placeholder = painterResource(id = R.drawable.book_failure)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.58f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                BookInfo("书名", uiState.libraryBookDetail?.title ?: "未知书名")
                                BookInfo("作者", uiState.libraryBookDetail?.author ?: "未知作者")
                                BookInfo("ISBN", uiState.libraryBookDetail?.isbn ?: "")
                            }
                        }
                    }
                    if (uiState.libraryBookDetail?.abstract?.isNotEmpty() == true) {
                        item {
                            SmallTitle(
                                text = "简介",
                                insideMargin = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            )
                            Card {
                                Text(
                                    text = uiState.libraryBookDetail?.abstract.toString(),
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(12.dp),
                                    color = MiuixTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SmallTitle(
                                text = "馆藏情况",
                                insideMargin = PaddingValues(start = 12.dp, top = 8.dp)
                            )
                            uiState.libraryBookBorrowingDetail.forEach {
                                BookStateCard(book = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookStateCard(
    book: BookBorrowingDetails
) {
    Card {
        BasicComponent(
            title = book.callNo,
            summary = book.location,
            rightActions = {
                Text(
                    text = book.status,
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (book.status == "可借") MiuixTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
fun BookInfo(
    label: String,
    content: String
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MiuixTheme.colorScheme.primary
                )
            )
        },
        supportingContent = {
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}