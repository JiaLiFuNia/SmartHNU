package com.smart.htu.screens.message

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.smart.htu.R
import com.smart.htu.api.module.Notice
import com.smart.htu.api.module.NoticeType
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.navigateToWebView
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: MessageViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    val hazeState = remember { HazeState() }
    val scope = rememberCoroutineScope()

    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.refreshGiteeConfig()
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
                    .hazeSource(state = hazeState)
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                item {
                    NoticeList(
                        modifier = Modifier.fillMaxSize(),
                        list = uiState.noticeList,
                        uiState = uiState,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun LazyItemScope.NoticeList(
    modifier: Modifier,
    list: List<Notice>,
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    navController: NavController
) {
    if (list.isNotEmpty())
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            list.sortedByDescending { it.id }.forEach { notice ->
                SingleMessage(
                    readState = notice.id in uiState.hadReadIdList,
                    hadRead = {
                        viewModel.addReadNoticeId(notice.id)
                    },
                    notice = notice,
                    navController = navController
                )
            }
        }
    else
        EmptyContent(
            text = "暂无消息",
            image = DrawableVectors.emptyData()
        )
}

@Composable
fun SingleMessage(
    readState: Boolean,
    hadRead: () -> Unit,
    notice: Notice,
    navController: NavController
) {
    Surface(
        onClick = {
            hadRead()
        },
        modifier = Modifier
            .semantics { role = Role.Button }
            .fillMaxWidth()
            .animateContentSize(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                leadingContent = {
                    BadgedBox(
                        badge = {
                            if (!readState) Badge()
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
                        verticalAlignment = Alignment.CenterVertically
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                notice.action.let {
                    when (notice.type) {
                        NoticeType.URL -> {
                            TextButton(
                                onClick = {
                                    navController.navigateToWebView(
                                        url = it,
                                        label = notice.title
                                    )
                                }
                            ) {
                                Text(text = "打开链接")
                            }
                        }

                        NoticeType.UPDATE -> {
                            TextButton(onClick = { }) {
                                Text(text = "立即更新")
                            }
                        }

                        NoticeType.SCREEN -> {
                            TextButton(onClick = { navController.navigate(it) }) {
                                Text(text = "查看详情")
                            }
                        }

                        NoticeType.QUESTIONNAIRE -> {
                            TextButton(onClick = { startWebUrl(it) }) {
                                Text(text = "去填写")
                            }
                        }

                        NoticeType.COMMON -> {
                            AnimatedVisibility(visible = !readState) {
                                TextButton(onClick = { hadRead() }) {
                                    Text(text = "已读")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}