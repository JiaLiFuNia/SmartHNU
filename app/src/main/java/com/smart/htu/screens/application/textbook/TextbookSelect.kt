package com.smart.htu.screens.application.textbook

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.MainActivity
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextbookSelect(
    viewModel: TextbookViewModel = hiltViewModel(),
    navController: NavController,
    courseTaskCode: String,
    termCode: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val state = rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
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

    ScaffoldWithHazeLazyColumn(
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
        isRefreshing = isRefreshing,
        refreshState = state,
        onRefresh = { onRefresh() },
        itemSpacePadding = 12.dp
    ) {
        item {
            TabRow(
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
    Card(
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            SingleInfo("书名", textbook.textbookName)
            SingleInfo("编著", textbook.editor)
            SingleInfo("出版社", textbook.publisher)
            SingleInfo("ISBN", textbook.isbn)
            SingleInfo("定价", textbook.price.toString())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            copyContent("${textbook.textbookName} ${textbook.isbn}")
                            MainActivity.snackBarHostState.showSnackbar("已复制到剪切板")
                        }
                    }
                ) {
                    Text(text = "复制")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if (textbook.isSelected) "选订" else "退订")
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(0.25f),
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold
        )
        Text(text = content, Modifier.weight(0.75f))
    }
}