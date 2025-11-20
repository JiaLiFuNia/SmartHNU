package com.smart.htu.screens.application.messageBoard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.PostsListData.PostsEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.AUTH_LOGIN_URL
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.dateFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBoard(
    navController: NavController,
    viewModel: MessageBoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val pageNumber = remember { mutableIntStateOf(1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.getMessageBoardPosts()
            isRefreshing = false
        }
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = stringResource(R.string.message_board)) },
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
                            navController.navigateToWebView(
                                // https://authserver2.htu.edu.cn/authserver/login?service=https://yjfk.htu.edu.cn/h5/pages/ssp/post_edit
                                url = AUTH_LOGIN_URL + "https://yjfk.htu.edu.cn/h5/pages/ssp/post_edit",
                                label = "发布留言"
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "add")
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                modifier = Modifier,
                visible = !fabVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = { scope.launch { lazyListState.scrollToItem(0) } }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                        contentDescription = "up"
                    )
                }
            }
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
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 8.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                if (uiState.postsListData == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    if (uiState.postsListData.isNullOrEmpty()) {
                        item {
                            EmptyContent(text = "暂无内容", image = emptyData())
                        }
                    } else {
                        items(uiState.postsListData ?: emptyList()) {
                            PostsCard(
                                post = it,
                                onClick = {
                                    navController.navigate("${Destinations.MessageBoardDetail.route}/${it}")
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        item {
                            LaunchedEffect(Unit) {
                                pageNumber.intValue = pageNumber.intValue + 1
                                viewModel.getMessageBoardPosts(pageNumber.intValue)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostsCard(
    post: PostsEntity,
    onClick: (String) -> Unit
) {
    Surface(
        onClick = { onClick(post.postID) },
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = post.title,
                color = MiuixTheme.colorScheme.onSurface,
                style = MiuixTheme.textStyles.headline1
            )
            val publishDate = post.createTime.split(" ")[0]
            val publishTime = post.createTime.split(" ")[1]
            Text(
                text = "${post.cateName}  ${
                    dateFormatter(
                        publishDate,
                        "yyyy/MM/dd",
                        "yyyy-MM-dd"
                    )
                } $publishTime",
                color = MiuixTheme.colorScheme.disabledOnSecondaryVariant,
                style = MiuixTheme.textStyles.subtitle,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = post.content,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                style = MiuixTheme.textStyles.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}