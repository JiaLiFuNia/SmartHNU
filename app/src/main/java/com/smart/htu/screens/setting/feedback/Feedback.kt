package com.smart.htu.screens.setting.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.api.module.FeedbackType
import com.smart.htu.component.TextButtonWithProgressIndicator
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TextFieldDefaults
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun Feedback(
    viewModel: FeedbackViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.feedback),
                color = barColor,
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },

                        ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
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
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .scrollEndHaptic()
                    .overScrollVertical(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        OverlayDropdownPreference(
                            title = "反馈类型",
                            items = FeedbackType.entries.map { it.type },
                            onSelectedIndexChange = {
                                viewModel.changeFeedbackType(FeedbackType.entries[it])
                            },
                            selectedIndex = FeedbackType.entries.indexOf(uiState.feedbackType)
                        )
                    }
                }
                item {
                    TextField(
                        value = uiState.detailMessage,
                        label = "请详细描述你遇到的问题或者建议：",
                        onValueChange = {
                            viewModel.changeDetailMessage(it)
                        },
                        useLabelAsPlaceholder = true,
                        colors = TextFieldDefaults.textFieldColors(MiuixTheme.colorScheme.surfaceContainer),
                        minLines = 4
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
                        colors = TextFieldDefaults.textFieldColors(MiuixTheme.colorScheme.surfaceContainer),
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButtonWithProgressIndicator(
                        text = if (uiState.isSubmitting) "正在提交..." else "提交",
                        onClick = {
                            focusManager.clearFocus()
                            if (uiState.detailMessage.isEmpty()) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("内容不能为空")
                                }
                            } else {
                                viewModel.submitFeedback(
                                    onSuccess = {
                                        scope.launch {
                                            showToast(context, it)
                                        }
                                    },
                                    onError = {
                                        scope.launch {
                                            showToast(context, it)
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
}