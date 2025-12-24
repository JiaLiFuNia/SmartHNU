package com.smart.htu.screens.message

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.NoticeType
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = rememberHazeState()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.refreshNoticeData()
            isRefreshing = false
        }
    }
    val scrollBehavior = MiuixScrollBehavior()
    top.yukonga.miuix.kmp.basic.Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(id = R.string.message_center),
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
                            viewModel.readAllNotice()
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.checklist_rtl_24px),
                            contentDescription = "check"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = uiState.blurEffect
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
                    top = it.calculateTopPadding() + 8.dp,
                    bottom = 12.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                if (uiState.noticeList.isNotEmpty()) {
                    items(uiState.noticeList.sortedByDescending { it.id }) { notice ->
                        SingleMessage(
                            isRead = notice.id in uiState.readNoticeIdList,
                            read = {
                                viewModel.addReadNoticeId(notice.id)
                            },
                            title = notice.title,
                            content = notice.content,
                            noticeId = notice.id,
                            action = notice.action,
                            type = notice.type,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                    item {
                        EmptyContent(
                            text = "暂无消息",
                            image = emptyData()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SingleMessage(
    isRead: Boolean,
    read: (String) -> Unit,
    title: String = "",
    content: String = "",
    noticeId: String = "",
    action: String = "",
    type: NoticeType = NoticeType.COMMON,
    color: Color = MiuixTheme.colorScheme.surfaceContainer,
    navController: NavController
) {
    Surface(
        onClick = {
            read(noticeId)
            when (type) {
                NoticeType.URL -> {
                    navController.navigateToWebView(
                        url = action,
                        label = title
                    )
                }

                NoticeType.UPDATE -> {
                }

                NoticeType.SCREEN -> {
                    navController.navigate(action)
                }

                NoticeType.QUESTIONNAIRE -> {
                    startWebUrl(action)
                }

                NoticeType.COMMON -> {
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = color,
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius)
    ) {
        BasicComponent(
            title = title,
            summary = content,
            rightActions = {
                if (!isRead) {
                    Badge()
                }
            }
        )
    }
}