package com.smart.htu.screens.application.messageBoard

import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.App.Companion.context
import com.smart.htu.R
import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostDetailData.CommentData
import com.smart.htu.api.module.PostDetailData.ReplyData
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.ImagePreviewDialog
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageBoardDetail(
    postID: String,
    navController: NavController,
    viewModel: MessageBoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val hazeState = rememberHazeState()
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
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(R.string.detail),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
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
                if (uiState.postDetailData == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    item {
                        Text(
                            text = uiState.postDetailData!!.title,
                            color = MiuixTheme.colorScheme.onSurface,
                            style = MiuixTheme.textStyles.title2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
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
                        fileName = "${uiState.postDetailData!!.title}.jpg",
                        targetDirectory = Environment.DIRECTORY_PICTURES
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
    BasicComponent(
        title = "提问人 ${post.userName}",
        summary = post.createDateTime,
        startAction = {
            Image(
                painter = painterResource(R.drawable.ic_avator_poster),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp)
            )
        },
        endActions = {
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
        },
        insideMargin = PaddingValues(horizontal = 0.dp)
    )
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = post.content.replace("<br/>", "\n"),
            style = MiuixTheme.textStyles.main,
            textAlign = TextAlign.Justify,
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
                        .build(),
                    contentDescription = "picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            onImgClick(it.url)
                        },
                    alignment = Alignment.Center,
                    error = painterResource(id = R.drawable.ic_loading_placeholder_horizontal),
                    placeholder = painterResource(id = R.drawable.ic_loading_placeholder_horizontal)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
                    summary = it.createDateTime,
                    startAction = {
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
                        style = MiuixTheme.textStyles.main,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row {
                        it.pics.forEach {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.url)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        onImgClick(it.url)
                                    },
                                alignment = Alignment.Center,
                                error = painterResource(id = R.drawable.ic_loading_placeholder_horizontal),
                                placeholder = painterResource(id = R.drawable.ic_loading_placeholder_horizontal)
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
    Card(
        insideMargin = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = comment.content,
            style = MiuixTheme.textStyles.main
        )
        Spacer(modifier = Modifier.height(5.dp))
        val score = ceil(comment.score / 20.0)
        Row {
            repeat(score.toInt()) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}