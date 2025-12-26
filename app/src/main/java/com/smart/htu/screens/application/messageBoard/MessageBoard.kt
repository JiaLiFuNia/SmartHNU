package com.smart.htu.screens.application.messageBoard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SpeakerNotes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.PostsListData.PostsEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.dateFormatter
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageBoard(
    navController: NavController,
    viewModel: MessageBoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val hazeState = rememberHazeState()

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
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(R.string.message_board),
                color = Color.Transparent,
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Back,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                viewModel.authLoginToMessageBoard()
                                navController.navigateToWebView(
                                    url = "https://yjfk.htu.edu.cn/h5/?plat=h5&token=${uiState.token}",
                                    label = "发布留言"
                                )
                            }
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.SpeakerNotes,
                            contentDescription = "add"
                        )
                    }
                },
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    }
            )
        },
        floatingActionButton = {
            UpFloatingActionButton(
                fabVisible = fabVisible,
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
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
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding() + 12.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .hazeSource(hazeState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
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
        color = MiuixTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = post.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(550),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = post.cateName,
                    fontSize = 12.sp,
                    color = MiuixTheme.colorScheme.onTertiaryContainer.copy(0.8f),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(ContinuousRoundedRectangle(6.dp))
                        .background(MiuixTheme.colorScheme.tertiaryContainer.copy(0.6f))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    fontWeight = FontWeight(750),
                    maxLines = 1
                )
            }
            val publishDate = post.createTime.split(" ")[0]
            val publishTime = post.createTime.split(" ")[1]
            Text(
                text = "${
                    dateFormatter(
                        publishDate,
                        "yyyy/MM/dd",
                        "yyyy-MM-dd"
                    )
                } $publishTime",
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp),
                fontWeight = FontWeight(550),
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}