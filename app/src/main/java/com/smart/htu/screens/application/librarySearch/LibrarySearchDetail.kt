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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.R
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.utils.ToastUtil.showSnackbar
import com.smart.htu.utils.copyContent
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun LibrarySearchDetail(
    navController: NavController,
    viewModel: LibrarySearchViewModel = hiltViewModel(),
    bookId: String
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val hazeState = rememberHazeState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(bookId) {
        viewModel.libraryBookDetail(bookId)
    }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(uiState.libraryBookDetail?.imageUrl)
        .crossfade(true)
        .build()

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
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
                            scope.launch {
                                copyContent(
                                    textContent =
                                        "书名：${uiState.libraryBookDetail?.title}\n" +
                                                "作者：${uiState.libraryBookDetail?.author}\n" +
                                                "ISBN：${uiState.libraryBookDetail?.isbn}\n"
                                )
                                showSnackbar(snackBarHostState, "已复制书籍信息到剪贴板")
                            }
                        },
                        enabled = uiState.libraryBookDetail != null
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.content_copy_24px),
                            contentDescription = "favorite"
                        )
                    }
                },
                modifier = Modifier/*.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.ultraThin(
                        MiuixTheme.colorScheme.background
                    )
                ) {
                    blurRadius = 50.dp
                }*/
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hazeSource(hazeState)
        ) {
            val backdropGradientColors = listOf(
                Color.Transparent,
                MiuixTheme.colorScheme.background,
            )
            AsyncImage(
                model = imageRequest,
                contentDescription = "picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .hazeEffect(
                        style = HazeMaterials.ultraThin(
                            MiuixTheme.colorScheme.background
                        )
                    ) {
                        blurRadius = 20.dp
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
                placeholder = painterResource(id = R.drawable.ic_placeholder_vertical_loading),
                onError = { result ->
                    println("错误原因: ${result}")
                },
            )

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp
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
                                        Surface(
                                            color = MiuixTheme.colorScheme.surface,
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                text = it,
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 4.dp
                                                ),
                                                color = MiuixTheme.colorScheme.onSurface
                                            )
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
            top.yukonga.miuix.kmp.basic.Text(
                text = label,
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
            top.yukonga.miuix.kmp.basic.Text(
                text = content,
                fontSize = MiuixTheme.textStyles.headline1.fontSize,
                fontWeight = FontWeight.Medium,
                color = MiuixTheme.colorScheme.onSurface,
            )
        }
    }
}