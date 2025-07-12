package com.smart.htu.screens.message

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.smart.htu.R
import com.smart.htu.api.module.NoticeEntity
import com.smart.htu.api.module.NoticeType
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hazeState = remember { HazeState() }
    val scope = rememberCoroutineScope()

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.refreshNoticeData()
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
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
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.readAllNotice()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.checklist_rtl_24px),
                            contentDescription = "check"
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
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
                    .overScrollVertical(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.noticeList.isNotEmpty()) {
                    items(uiState.noticeList.sortedByDescending { it.id }) { notice ->
                        SingleMessage(
                            isRead = notice.id in uiState.readNoticeIdList,
                            read = {
                                viewModel.addReadNoticeId(it)
                            },
                            notice = notice,
                            navController = navController
                        )
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
    read: (Int) -> Unit,
    notice: NoticeEntity,
    navController: NavController
) {
    Surface(
        onClick = {
            read(notice.id)
            notice.action.let {
                when (notice.type) {
                    NoticeType.URL -> {
                        navController.navigateToWebView(
                            url = it,
                            label = notice.title
                        )
                    }

                    NoticeType.UPDATE -> {
                    }

                    NoticeType.SCREEN -> {
                        navController.navigate(it)
                    }

                    NoticeType.QUESTIONNAIRE -> {
                        startWebUrl(it)
                    }

                    NoticeType.COMMON -> {
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = SmoothRoundedCornerShape(top.yukonga.miuix.kmp.basic.ButtonDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface,
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = MiuixTheme.colorScheme.surface),
            leadingContent = {
                BadgedBox(
                    badge = {
                        if (!isRead) Badge()
                    }
                ) {
                    when (notice.type) {
                        NoticeType.URL -> {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_language_24),
                                contentDescription = "url"
                            )
                        }

                        NoticeType.UPDATE -> {
                            Icon(
                                painter = painterResource(id = R.drawable.deployed_code_update_24px),
                                contentDescription = "url"
                            )
                        }

                        NoticeType.SCREEN -> {
                            Icon(
                                painter = painterResource(id = R.drawable.circle_add),
                                contentDescription = "url"
                            )
                        }

                        NoticeType.QUESTIONNAIRE -> {
                            Icon(
                                painter = painterResource(id = R.drawable.contract_edit_24px),
                                contentDescription = "url"
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "notice"
                            )
                        }
                    }
                }
            },
            headlineContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notice.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = notice.time, style = MaterialTheme.typography.labelMedium)
                }
            },
            supportingContent = {
                Text(
                    text = notice.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}