package com.smart.htu.screens.application.messageBoard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostDetailData.CommentData
import com.smart.htu.api.module.PostDetailData.ReplyData
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.ImagePreviewDialog
import com.smart.htu.component.InfoBadge
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBoardDetail(
    postID: String,
    navController: NavController,
    viewModel: MessageBoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val showImagePreview = remember { mutableStateOf(false) }
    val selectedImageData = remember { mutableStateOf("") }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.getMessageBoardPostDetail(postID)
            isRefreshing = false
        }
    }

    LaunchedEffect(postID) {
        viewModel.getMessageBoardPostDetail(postID)
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
                title = { Text(text = stringResource(R.string.detail)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            LazyColumn(
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
                if (uiState.postDetailData == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    item {
                        PostCard(
                            post = uiState.postDetailData!!,
                            onImgClick = {
                                selectedImageData.value = it
                                showImagePreview.value = true
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item {
                        HorizontalDivider()
                        SmallTitle(text = "回复", insideMargin = PaddingValues(12.dp, 8.dp))
                        ReplyCard(
                            replyList = uiState.postDetailData!!.replyList,
                            onImgClick = {
                                selectedImageData.value = it
                                showImagePreview.value = true
                            }
                        )
                    }
                    item {
                        SmallTitle(text = "评价", insideMargin = PaddingValues(12.dp, 8.dp))
                        CommentCard(uiState.postDetailData!!.comment)
                    }
                }
            }
        }
    }

    if (showImagePreview.value) {
        ImagePreviewDialog(
            imageUrl = selectedImageData.value,
            onDismiss = { showImagePreview.value = false },
            onDownload = {
                scope.launch {
                    ToastUtil.showToast(context, "正在下载图片")
                    downloadFile(
                        context = context,
                        url = selectedImageData.value,
                        fileName = "${uiState.postDetailData!!.title}.jpg"
                    )
                    ToastUtil.showToast(context, "下载成功")
                }
            }
        )
    }
}

@Composable
fun PostCard(
    post: PostDetailData,
    onImgClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
    ) {
        BasicComponent(
            title = "提问人 ${post.userName}",
            summary = post.createTime,
            leftAction = {
                Image(
                    painter = painterResource(R.drawable.ic_avator_poster),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp)
                )
            },
            rightActions = {
                InfoBadge(post.cateName)
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = post.title,
                color = MiuixTheme.colorScheme.onSurface,
                style = MiuixTheme.textStyles.title3
            )
            Text(
                text = post.content.replace("<br/>", "\n"),
                style = MiuixTheme.textStyles.body1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                post.pics.forEach {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.url)
                            .crossfade(true)
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .error(R.drawable.book_failure)
                            .build(),
                        contentDescription = "picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                onImgClick(it.url)
                            },
                        alignment = Alignment.Center,
                        placeholder = painterResource(id = R.drawable.book_failure)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "分类：${post.teamName} / ${post.projectName}",
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "责任部门：${post.organizationName}",
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ReplyCard(
    replyList: List<ReplyData>,
    onImgClick: (String) -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        replyList.forEach {
            Card {
                BasicComponent(
                    title = it.userName,
                    summary = it.createTime,
                    leftAction = {
                        Image(
                            painter = painterResource(
                                id = when (it.label) {
                                    "楼主" -> R.drawable.ic_avator_poster
                                    else -> R.drawable.ic_avator_official
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 8.dp)
                        )
                    }
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = it.content.replace("<br/>", "\n"),
                        style = MiuixTheme.textStyles.body2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row {
                        it.pics.forEach {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.url)
                                    .crossfade(true)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .error(R.drawable.book_failure)
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        onImgClick(it.url)
                                    },
                                alignment = Alignment.Center,
                                placeholder = painterResource(id = R.drawable.book_failure)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentCard(
    comment: CommentData
) {
    Card {
        BasicComponent(
            title = comment.content,
            rightActions = {
                InfoBadge(text = "${comment.score}")
            }
        )
    }
}