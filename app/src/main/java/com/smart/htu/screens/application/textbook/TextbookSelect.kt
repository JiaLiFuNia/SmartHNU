package com.smart.htu.screens.application.textbook

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.Textbook
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.TabRow
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Confirm
import top.yukonga.miuix.kmp.icon.icons.useful.Search
import top.yukonga.miuix.kmp.icon.icons.useful.Undo
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextbookSelect(
    viewModel: TextbookViewModel = hiltViewModel(),
    navController: NavController,
    courseTaskCode: String,
    termCode: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(1000)
            viewModel.refreshTermList()
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

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.textbook_select),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding() + 16.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                TabRow(
                    tabs = tabItem,
                    selectedTabIndex = selectIndex,
                    onTabSelected = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                    pageSpacing = 12.dp
                ) {
                    SelectTextbook(
                        textbook = if (it == 0) uiState.selectableList else uiState.selectedList,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PagerScope.SelectTextbook(
    textbook: List<Textbook>?,
    viewModel: TextbookViewModel
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .overScrollVertical(),
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
                    CourseTextbookItem(it, {})
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CourseTextbookItem(
    textbook: Textbook,
    onClick: () -> Unit
) {
    val context = LocalContext.current
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
                top.yukonga.miuix.kmp.basic.Text(
                    text = textbook.textbookName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(550),
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = "ISBN：${textbook.isbn}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = "定价：${textbook.price}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                top.yukonga.miuix.kmp.basic.Text(
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
                onClick = { showToast(context, "开发中") },
                backgroundColor = if (textbook.isSelected) {
                    MiuixTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                } else {
                    MiuixTheme.colorScheme.secondaryContainer
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = MiuixIcons.Useful.Search,
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
                backgroundColor = if (textbook.isSelected) {
                    MiuixTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                } else {
                    MiuixTheme.colorScheme.secondaryContainer
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = if (textbook.isSelected) {
                            MiuixIcons.Useful.Undo
                        } else {
                            MiuixIcons.Useful.Confirm
                        },
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp, end = 3.dp),
                        text = if (textbook.isSelected) "退订" else "选订",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SingleInfo(
    label: String,
    content: String
) {
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