package com.smart.htu.screens.application.textbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.api.module.Textbook
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.librarySearch.LibrarySingleBook
import com.smart.htu.screens.navigation.Navigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.TabRowDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.icon.extended.Search
import top.yukonga.miuix.kmp.icon.extended.Undo
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun TextbookSelect(
    viewModel: TextbookViewModel = hiltViewModel(),
    courseTaskCode: String,
    termCode: String
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(1000)
            viewModel.refreshTermCalendar()
            viewModel.getSelectableTextbookService(courseTaskCode, termCode)
            viewModel.getSelectedTextbookService(courseTaskCode, termCode)
            isRefreshing = false
        }
    }

    LaunchedEffect(courseTaskCode, termCode) {
        viewModel.getSelectableTextbookService(courseTaskCode, termCode)
        viewModel.getSelectedTextbookService(courseTaskCode, termCode)
    }

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectIndex by remember { derivedStateOf { pagerState.currentPage } }
    val tabItem = listOf("可选教材", "已选教材")

    var collapsedFraction by remember { mutableFloatStateOf(scrollBehavior.state.collapsedFraction) }
    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        snapshotFlow { scrollBehavior.state.collapsedFraction }.collectLatest {
            collapsedFraction = it
        }
    }
    val dynamicTopPadding by remember { derivedStateOf { 12.dp * (1f - collapsedFraction) } }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = R.string.textbook_select),
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },

                            ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    },
                    bottomContent = {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = dynamicTopPadding, bottom = 6.dp)
                        ) {
                            TabRow(
                                tabs = tabItem,
                                selectedTabIndex = selectIndex,
                                onTabSelected = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(it)
                                    }
                                },
                                colors = TabRowDefaults.tabRowColors(
                                    backgroundColor = barColor
                                )
                            )
                        }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                )
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .overScrollVertical(),
                    pageSpacing = 12.dp,
                    contentPadding = PaddingValues(
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    )
                ) {
                    SelectTextbook(
                        scrollBehavior = scrollBehavior,
                        textbook = if (it == 0) uiState.selectableList else uiState.selectedList,
                        pageIndex = it,
                        uiState = uiState,
                        viewModel = viewModel,
                        navigator = navigator
                    )
                }
            }
        }
    }
}

@Composable
fun SelectTextbook(
    scrollBehavior: ScrollBehavior,
    textbook: List<Textbook>?,
    pageIndex: Int,
    uiState: TextbookUiState,
    viewModel: TextbookViewModel,
    navigator: Navigator
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .overScrollVertical()
            .scrollEndHaptic(),
        overscrollEffect = null
    ) {
        if (textbook == null) {
            item {
                CircularProgressIndicator()
            }
        } else {
            if (textbook.isEmpty()) {
                item {
                    EmptyContent(
                        text = "没有教材",
                        image = emptyData()
                    )
                }
            } else {
                items(textbook) {
                    CourseTextbookItem(
                        textbook = it,
                        onClick = {},
                        pageIndex = pageIndex,
                        uiState = uiState,
                        onSearch = {
                            scope.launch {
                                viewModel.librarySearch(it, 1)
                            }
                        },
                        navigator = navigator
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CourseTextbookItem(
    textbook: Textbook,
    onClick: () -> Unit,
    pageIndex: Int,
    uiState: TextbookUiState,
    onSearch: (String) -> Unit,
    navigator: Navigator
) {
    val context = LocalContext.current
    val isSearchBottomSheetShow = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        pressFeedbackType = PressFeedbackType.Sink,
        insideMargin = PaddingValues(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = textbook.textbookName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(550),
                )
                Text(
                    text = "ISBN：${textbook.isbn}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "定价：${textbook.price}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "编著和出版社：${textbook.editor} | ${textbook.publisher}",
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 4
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                minHeight = 35.dp,
                minWidth = 35.dp,
                onClick = {
                    onSearch(textbook.isbn)
                    isSearchBottomSheetShow.value = true
                },
                backgroundColor = MiuixTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = MiuixIcons.Regular.Search,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp, end = 3.dp),
                        text = "搜索",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                minHeight = 35.dp,
                minWidth = 35.dp,
                onClick = { showToast(context, "开发中") },
                backgroundColor = MiuixTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = if (pageIndex == 1) {
                            MiuixIcons.Regular.Undo
                        } else {
                            MiuixIcons.Regular.Ok
                        },
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp, end = 3.dp),
                        text = if (pageIndex == 1) "退订" else "选订",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
    OverlayBottomSheet(
        show = isSearchBottomSheetShow.value,
        title = "搜索结果",
        onDismissRequest = {
            isSearchBottomSheetShow.value = false
        },
        insideMargin = DpSize(16.dp, 0.dp),
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        if (uiState.isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                top.yukonga.miuix.kmp.basic.CircularProgressIndicator()
            }
        } else {
            if (uiState.bookSearchList.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.bookSearchList.forEach {
                        LibrarySingleBook(
                            bookContent = it,
                            onClick = {
                                isSearchBottomSheetShow.value = false
                                navigator.push(Route.LibrarySearchDetail(it.bookId))
                            }
                        )
                    }
                }
            } else {
                EmptyContent(text = "没有该书籍", modifier = Modifier.height(100.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}