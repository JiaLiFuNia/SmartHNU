package com.smart.htu.screens.setting.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.FeedbackType
import com.smart.htu.screens.login.TextWithProgressIndicatorButton
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feedback(
    navController: NavController,
    viewModel: FeedbackViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = { Text(text = stringResource(id = R.string.feedback)) },
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
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(16.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TabRow(
                    tabs = FeedbackType.entries.map { it.type },
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                            viewModel.changeType(
                                when (it) {
                                    0 -> FeedbackType.FEEDBACK
                                    else -> FeedbackType.SUGGESTION
                                }
                            )
                        }
                    }
                )
            }
            item {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    pageSpacing = 12.dp,
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    userScrollEnabled = false
                ) { page ->
                    when (page) {
                        0 -> FeedbackContent(
                            page = page,
                            uiState = uiState,
                            viewModel = viewModel,
                            onSuccess = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(it)
                                    navController.popBackStack()
                                }
                            },
                            onError = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(it)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            onMessageIsEmpty = {
                                scope.launch {
                                    snackBarHostState.showSnackbar("请输入反馈内容")
                                }
                            }
                        )

                        1 -> FeedbackContent(
                            page = page,
                            uiState = uiState,
                            viewModel = viewModel,
                            onSuccess = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(it)
                                    navController.popBackStack()
                                }
                            },
                            onError = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(it)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            onMessageIsEmpty = {
                                scope.launch {
                                    snackBarHostState.showSnackbar("请输入建议内容")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PagerScope.FeedbackContent(
    page: Int = 0,
    uiState: FeedbackUiState,
    viewModel: FeedbackViewModel,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
    onMessageIsEmpty: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = uiState.message,
            label = if (page == 0) "请详细描述你遇到的问题：\n1、当前设备的网络状态\n2、打开了某个页面\n3、其他" else "请详细描述你在使用师韵(SmartHNU)时的建议：",
            onValueChange = {
                viewModel.changeMessage(it)
            },
            useLabelAsPlaceholder = true,
            backgroundColor = MiuixTheme.colorScheme.surface,
            modifier = Modifier.height(140.dp)
        )
        TextField(
            value = uiState.functionModule,
            label = "功能模块",
            onValueChange = {
                viewModel.changeFunctionModule(it)
            },
            useLabelAsPlaceholder = true,
            backgroundColor = MiuixTheme.colorScheme.surface
        )
        TextField(
            value = uiState.email,
            label = "邮箱(可空)",
            onValueChange = {
                viewModel.changeEmail(it)
            },
            useLabelAsPlaceholder = true,
            backgroundColor = MiuixTheme.colorScheme.surface
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextWithProgressIndicatorButton(
            text = if (uiState.isSubmitting) "正在提交..." else "提交",
            onClick = {
                if (uiState.message.isEmpty()) {
                    onMessageIsEmpty()
                } else {
                    viewModel.submitFeedback(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !uiState.isSubmitting,
        )
    }
}