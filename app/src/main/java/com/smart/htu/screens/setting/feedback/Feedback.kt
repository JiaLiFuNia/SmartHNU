package com.smart.htu.screens.setting.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.smart.htu.component.TextButtonWithProgressIndicator
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.DropDownMode
import top.yukonga.miuix.kmp.extra.SuperDropdown
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
                .padding(
                    top = it.calculateTopPadding() + 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                )
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TextField(
                    value = uiState.detailMessage,
                    label = "请详细描述你遇到的问题或者建议：",
                    onValueChange = {
                        viewModel.changeDetailMessage(it)
                    },
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface,
                    minLines = 4
                )
            }
            item {
                TextField(
                    value = uiState.functionalModule,
                    label = "功能模块",
                    onValueChange = {
                        viewModel.changeFunctionalModule(it)
                    },
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface
                )
            }
            item {
                TextField(
                    value = uiState.submitterEmail,
                    label = "邮箱(可空)",
                    onValueChange = {
                        viewModel.changeSubmitterEmail(it)
                    },
                    useLabelAsPlaceholder = true,
                    backgroundColor = MiuixTheme.colorScheme.surface
                )
            }
            item {
                Card {
                    SuperDropdown(
                        title = "反馈类型",
                        items = FeedbackType.entries.map { it.type },
                        mode = DropDownMode.AlwaysOnRight,
                        onSelectedIndexChange = {
                            viewModel.changeFeedbackType(FeedbackType.entries[it])
                        },
                        selectedIndex = FeedbackType.entries.indexOf(uiState.feedbackType)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
                TextButtonWithProgressIndicator(
                    text = if (uiState.isSubmitting) "正在提交..." else "提交",
                    onClick = {
                        if (uiState.detailMessage.isEmpty()) {
                            scope.launch {
                                snackBarHostState.showSnackbar("内容不能为空")
                            }
                        } else {
                            viewModel.submitFeedback(
                                onSuccess = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(it)
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(it)
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isLoading = uiState.isSubmitting,
                )
            }
        }
    }
}