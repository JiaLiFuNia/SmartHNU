package com.smart.htu.screens.application.librarySearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.R
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Copy
import top.yukonga.miuix.kmp.icon.extended.Favorites
import top.yukonga.miuix.kmp.icon.extended.FavoritesFill
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@Composable
fun LibrarySearchDetail(
    viewModel: LibrarySearchViewModel = hiltViewModel(),
    bookId: String
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollBehavior = MiuixScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val backdrop = rememberBlurBackdrop(true)

    LaunchedEffect(bookId) {
        viewModel.libraryBookDetail(bookId)
    }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(uiState.libraryBookDetail?.imageUrl)
        .crossfade(true)
        .build()

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = "详情",
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
                            viewModel.addWaitingBorrowedBookList(
                                book = uiState.libraryBookDetail ?: LibraryDetailEntity()
                            )
                        },
                        enabled = uiState.libraryBookDetail != null
                    ) {
                        Icon(
                            imageVector = if (uiState.waitingBorrowedBookList
                                    .map { it.bookId }.contains(bookId)
                            ) MiuixIcons.Regular.FavoritesFill
                            else MiuixIcons.Regular.Favorites,
                            contentDescription = "favorite"
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                copyContent(
                                    textContent =
                                        "书名：${uiState.libraryBookDetail?.title}\n" +
                                                "作者：${uiState.libraryBookDetail?.author}\n" +
                                                "ISBN：${uiState.libraryBookDetail?.isbn}\n"
                                )
                                showToast(context, "已复制书籍信息到剪贴板")
                            }
                        },
                        enabled = uiState.libraryBookDetail != null
                    ) {
                        Icon(
                            MiuixIcons.Regular.Copy,
                            contentDescription = "copy"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val backdropGradientColors = listOf(
                Color.Transparent,
                MiuixTheme.colorScheme.background,
            )
            /*AsyncImage(
                model = imageRequest,
                contentDescription = "picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .let {
                        if (backdrop != null) it
                            .layerBackdrop(backdrop)
                            .textureBlur(
                                backdrop = backdrop,
                                shape = RectangleShape,
                                blurRadius = 150f,
                                contentBlendMode = ComposeBlendMode.DstIn
                            )
                        else it
                    }
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            Brush.verticalGradient(
                                colors = backdropGradientColors,
                                startY = 0f,
                                endY = size.height / 3
                            )
                        )
                    }
                    .fillMaxHeight(),
                alignment = Alignment.TopCenter,
                error = painterResource(id = R.drawable.ic_placeholder_vertical_error),
                placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading)
            )*/

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = it.calculateBottomPadding() + 12.dp
                ),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .weight(0.4f)
                            ) {
                                AsyncImage(
                                    model = imageRequest,
                                    contentDescription = "picture",
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(10 / 15f),
                                    alignment = Alignment.Center,
                                    error = painterResource(id = R.drawable.ic_placeholder_vertical_error),
                                    placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.6f)
                                    .aspectRatio(1f),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                BookInfo(
                                    "书名",
                                    uiState.libraryBookDetail?.title ?: "未知书名"
                                )
                                BookInfo(
                                    "作者",
                                    uiState.libraryBookDetail?.author ?: "未知作者"
                                )
                                BookInfo(
                                    "ISBN",
                                    uiState.libraryBookDetail?.isbn ?: ""
                                )
                            }
                        }
                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.libraryBookDetail?.topic?.forEach {
                                item {
                                    if (it.isNotEmpty()) {
                                        Card(
                                            colors = CardDefaults.defaultColors(
                                                MiuixTheme.colorScheme.surfaceContainer,
                                                MiuixTheme.colorScheme.onSurfaceContainer
                                            ),
                                            insideMargin = PaddingValues(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
                                            cornerRadius = 6.dp,
                                        ) {
                                            Text(text = it)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (uiState.libraryBookDetail?.abstract?.isNotEmpty() == true) {
                        item {
                            SmallTitle(
                                text = "摘要",
                                insideMargin = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            )
                            Card(
                                colors = CardDefaults.defaultColors(
                                    MiuixTheme.colorScheme.surfaceContainer,
                                    MiuixTheme.colorScheme.onSurfaceContainer
                                ),
                                insideMargin = PaddingValues(12.dp)
                            ) {
                                Text(
                                    text = uiState.libraryBookDetail?.abstract.toString(),
                                    fontSize = MiuixTheme.textStyles.paragraph.fontSize,
                                    letterSpacing = 0.2.sp
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
            endActions = {
                Text(
                    text = book.status,
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (book.status == "可借") MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
fun BookInfo(
    label: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
            Text(
                text = content,
                fontSize = MiuixTheme.textStyles.headline1.fontSize,
                fontWeight = FontWeight.Medium,
                color = MiuixTheme.colorScheme.onSurface,
            )
        }
    }
}