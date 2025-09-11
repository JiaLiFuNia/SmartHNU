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
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.Textbook
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.TabRow
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.textButtonPrimaryColors
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = { Text(text = stringResource(id = R.string.textbook_select)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
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
            if (textbook.isEmpty() == true) {
                item {
                    EmptyContent(
                        text = "没有教材",
                        image = emptyData()
                    )
                }
            } else {
                items(textbook) {
                    SingleCourseTextbook(viewModel, it)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SingleCourseTextbook(
    viewModel: TextbookViewModel,
    textbook: Textbook
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    top.yukonga.miuix.kmp.basic.Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SingleInfo("书名", textbook.textbookName)
                SingleInfo("编著", textbook.editor)
                SingleInfo("出版社", textbook.publisher)
                SingleInfo("ISBN", textbook.isbn)
                SingleInfo("定价", textbook.price.toString())
            }
            IconButton(
                onClick = {
                    scope.launch {
                        copyContent("${textbook.textbookName} ${textbook.isbn}")
                        viewModel.showSnackBar("已复制到剪切板")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.content_copy_24px),
                    contentDescription = "copy"
                )
            }
        }
        top.yukonga.miuix.kmp.basic.TextButton(
            onClick = { showToast(context, "开发中") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            text = if (textbook.isSelected) "选订" else "退订",
            colors = if (textbook.isSelected) ButtonDefaults.textButtonColors() else ButtonDefaults.textButtonPrimaryColors()
        )
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