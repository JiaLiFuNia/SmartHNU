package com.smart.htu.screens.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.smart.htu.R
import com.smart.htu.api.module.Notice
import com.smart.htu.api.module.NoticeType
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyList
import com.smart.htu.screens.navigateToWebView
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: MessageViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    val hazeState = remember { HazeState() }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            viewModel.getNoticeByGiteeService()
            isRefreshing = false
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
        containerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surface,
        scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else colorScheme.surfaceContainer
        ),
                title = { Text(text = stringResource(id = R.string.message_center)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular()
                ) {
                    blurRadius = 30.dp
                    blurEnabled = uiState.blurEffect
                }
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = it.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = state
                )
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            val haveNotReadList =
                uiState.noticeList.filter { it.id !in uiState.hadReadIdList }
            val hadReadList =
                uiState.noticeList.filter { it.id in uiState.hadReadIdList }
            val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
            val selectIndex by remember { derivedStateOf { pagerState.currentPage } }
            val tabItem = listOf("未读(${haveNotReadList.size})", "已读")
            LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                selectedContentColor = colorScheme.primary,
                                unselectedContentColor = colorScheme.onSurface,
                            )
                        }
                    }
                }
                item {
                    HorizontalPager(
                        verticalAlignment = Alignment.Top,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        pageSpacing = 8.dp
                    ) {
                        NoticeList(
                            modifier = Modifier.fillMaxSize(),
                            list = when (it) {
                                0 -> haveNotReadList; 1 -> hadReadList; else -> emptyList()
                            },
                            uiState = uiState,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun NoticeList(
    modifier: Modifier,
    list: List<Notice>,
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    navController: NavController
) {
    if (list.isNotEmpty())
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            list.forEach { notice ->
                SingleMessage(
                    readState = notice.id in uiState.hadReadIdList,
                    title = notice.title,
                    content = notice.content,
                    type = notice.type,
                    time = notice.time,
                    hadRead = {
                        viewModel.addHadReadList(notice.id)
                    },
                    action = {
                        if (notice.type == NoticeType.URL) {
                            navController.navigateToWebView(
                                url = notice.content,
                                label = notice.title
                            )
                        }
                    }
                )
            }
        }
    else
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberVectorPainter(image = DrawableVectors.emptyList())
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(0.6f)
                        .widthIn(max = 240.dp),
                )
                Text(
                    text = stringResource(id = R.string.no_message),
                    style = MaterialTheme.typography.labelLarge,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }
}

@Composable
fun SingleMessage(
    readState: Boolean,
    title: String,
    content: String,
    type: NoticeType? = null,
    time: String,
    action: () -> Unit,
    hadRead: () -> Unit
) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = "notice")
                },
                headlineContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = title, style = MaterialTheme.typography.labelLarge)
                        Text(text = time, style = MaterialTheme.typography.labelMedium)
                    }
                },
                supportingContent = {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (type == NoticeType.URL)
                    TextButton(onClick = { action() }) {
                        Text(text = "打开链接")
                    }
                if (!readState)
                    TextButton(onClick = { hadRead() }) {
                        Text(text = "已读")
                    }
            }
        }
    }
}