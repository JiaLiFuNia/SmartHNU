package com.smart.htu.screens.message

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.NoticeType
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Navigator
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = rememberHazeState()
    val scope = rememberCoroutineScope()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500)
            viewModel.getNotice()
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
                        onClick = { navigator.pop() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                viewModel.readAllNotice()
                            }
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Checklist,
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
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding() + 12.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .padding(top = 16.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                overscrollEffect = null
            ) {
                if (uiState.noticeList == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    if (uiState.noticeList.isNullOrEmpty() && uiState.jwcNoticeList.isNullOrEmpty()) {
                        item {
                            EmptyContent(
                                text = "暂无消息",
                                image = emptyData()
                            )
                        }
                    } else {
                        items(
                            (uiState.noticeList
                                ?: emptyList()).sortedByDescending { it.id }
                        ) { notice ->
                            SingleMessage(
                                isRead = notice.id in uiState.readNoticeIdList,
                                read = {
                                    scope.launch {
                                        viewModel.addReadNoticeId(notice.id)
                                    }
                                },
                                title = notice.title,
                                content = notice.content,
                                action = notice.action,
                                type = notice.type,
                                navigator = navigator
                            )
                        }
                        items(
                            (uiState.jwcNoticeList ?: emptyList())
                        ) { notice ->
                            SingleMessage(
                                isRead = notice.noticeId in uiState.readNoticeIdList,
                                read = {
                                    scope.launch {
                                        viewModel.addReadNoticeId(notice.noticeId)
                                        viewModel.getJWCNoticeDetail(notice.noticeId)
                                    }
                                },
                                title = "教务通知",
                                content = notice.content,
                                type = NoticeType.JWC,
                                navigator = navigator
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SingleMessage(
    isRead: Boolean,
    read: () -> Unit,
    title: String = "",
    content: String = "",
    action: String = "",
    type: NoticeType = NoticeType.COMMON,
    color: Color = MiuixTheme.colorScheme.surfaceContainer,
    navigator: Navigator
) {
    Surface(
        onClick = {
            read()
            when (type) {
                NoticeType.URL -> {
                    navigator.pushWebView(
                        url = action,
                        title = title
                    )
                }

                NoticeType.UPDATE -> {
                }

                NoticeType.SCREEN -> {

                }

                NoticeType.QUESTIONNAIRE -> {
                    startWebUrl(action)
                }

                NoticeType.COMMON -> {
                }

                NoticeType.JWC -> {
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
            endActions = {
                if (!isRead) {
                    Badge()
                }
            }
        )
    }

}