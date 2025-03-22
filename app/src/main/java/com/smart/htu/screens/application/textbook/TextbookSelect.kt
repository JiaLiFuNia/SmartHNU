package com.smart.htu.screens.application.textbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.App.Companion.context
import com.smart.htu.MainActivity.Companion.snackBarHostState
import com.smart.htu.R
import com.smart.htu.api.module.ResultWithStatus
import com.smart.htu.api.module.Status
import com.smart.htu.api.module.Textbook
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.sendToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextbookSelect(
    themeMode: Int,
    viewModel: TextbookViewModel = hiltViewModel(),
    navController: NavController,
    courseTaskCode: String,
    termCode: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            viewModel.getSelectableTextbookService(courseTaskCode, termCode)
            viewModel.getSelectedTextbookService(courseTaskCode, termCode)
            pullToRefreshState.completeRefreshing {
                sendToast(context, "刷新成功")
            }
        }
    }

    LaunchedEffect(courseTaskCode, termCode) {
        viewModel.getSelectableTextbookService(courseTaskCode, termCode)
        viewModel.getSelectedTextbookService(courseTaskCode, termCode)
    }

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectIndex by remember { derivedStateOf { pagerState.currentPage } }
    val tabItem = listOf("可选教材", "已选教材")

    ScaffoldWithHazeLazyColumn(
        snackBarHost = { SnackbarHost(hostState = snackBarHostState) },
        themeMode = themeMode,
        isMediumTopAppBar = true,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        blurEnabledState = uiState.blurEffect,
        title = { Text(text = stringResource(id = R.string.textbook_select)) },
        actions = { },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        refreshState = pullToRefreshState,
        onRefresh = { onRefresh() },
        itemSpacePadding = 12.dp,
        headContent = {
            TabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        width = tabPositions[pagerState.currentPage].width / 2f,
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                    )
                },
                divider = {}
            ) {
                tabItem.forEachIndexed { index, item ->
                    Tab(
                        text = { Text(text = item) },
                        selected = selectIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    ) {
        item {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSpacing = 8.dp
            ) {
                SelectTextbook(
                    textbook = if (it == 0) uiState.selectableList
                    else uiState.selectedList,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun LazyItemScope.SelectTextbook(
    textbook: ResultWithStatus<List<Textbook>>,
    viewModel: TextbookViewModel
) {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (textbook.status == Status.LOADING) {
            CircularProgressIndicator()
        } else {
            if (textbook.status == Status.SUCCESS) {
                if (textbook.data?.isEmpty() == true) {
                    EmptyContent(
                        text = "没有教材",
                        image = DrawableVectors.emptyData()
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        textbook.data?.forEach {
                            SingleCourseTextbook(viewModel, it)
                        }
                    }
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
                        snackBarHostState.showSnackbar("已复制到剪切板")
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            text = if (textbook.isSelected) "选订" else "退订",
            colors = if (textbook.isSelected) ButtonDefaults.textButtonColors() else ButtonDefaults.textButtonColorsPrimary()
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