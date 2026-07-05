package com.smart.htu.screens.application.campusCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DatePicker
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.utils.DateUtil.toStringDate
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate

@Composable
fun ConsumptionRecordScreen(
    viewModel: CampusCardViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    var beginDate by remember { mutableStateOf(LocalDate.now().minusDays(30)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var showBeginDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (uiState.consumptionRecords == null && uiState.loginState) {
            viewModel.getConsumptionRecord(
                beginDate = beginDate.toStringDate(),
                endDate = endDate.toStringDate()
            )
        }
    }

    LaunchedEffect(uiState.isLastPage, lazyListState.canScrollForward) {
        if (!uiState.isLastPage && !lazyListState.canScrollForward && uiState.consumptionRecords != null && !uiState.isRefreshing) {
            viewModel.getConsumptionRecord(
                beginDate = beginDate.toStringDate(),
                endDate = endDate.toStringDate(),
                beginIndex = uiState.consumptionRecords?.size ?: 0,
                pageSize = 20,
                isAppend = true
            )
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = "消费记录",
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            UpFloatingActionButton(fabVisible = !fabVisible) {
                scope.launch { lazyListState.animateScrollToItem(0) }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .let {
                    if (backdrop != null) it.layerBackdrop(backdrop) else it
                }
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 16.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .scrollEndHaptic(),
                overscrollEffect = null,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showBeginDatePicker = true },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "开始日期",
                                    style = MiuixTheme.textStyles.headline2,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                )
                                Text(
                                    text = beginDate.toStringDate(),
                                    style = MiuixTheme.textStyles.body1
                                )
                            }
                            Text("至")
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showEndDatePicker = true },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "结束日期",
                                    style = MiuixTheme.textStyles.headline2,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                )
                                Text(
                                    text = endDate.toStringDate(),
                                    style = MiuixTheme.textStyles.body1
                                )
                            }
                        }
                    }
                }
                if (uiState.consumptionRecords == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else if (uiState.consumptionRecords!!.isEmpty()) {
                    item {
                        EmptyContent(text = "暂无消费记录")
                    }
                } else {
                    items(uiState.consumptionRecords!!) { record ->
                        Card {
                            BasicComponent(
                                title = record.location,
                                summary = record.businessOpDate,
                                endActions = {
                                    if (record.tradeFlag == "1") {
                                        Text(
                                            text = "-${record.amount} 元",
                                            color = MiuixTheme.colorScheme.error
                                        )
                                    } else {
                                        Text(
                                            text = "+${record.amount} 元",
                                            color = MiuixTheme.colorScheme.primary
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        DatePicker(
            date = beginDate,
            showDatePicker = showBeginDatePicker,
            onConfirmClick = {
                beginDate = it
                showBeginDatePicker = false
                scope.launch {
                    viewModel.getConsumptionRecord(
                        beginDate = beginDate.toStringDate(),
                        endDate = endDate.toStringDate()
                    )
                }
            },
            onDismissRequest = { showBeginDatePicker = false }
        )

        DatePicker(
            date = endDate,
            showDatePicker = showEndDatePicker,
            onConfirmClick = {
                endDate = it
                showEndDatePicker = false
                scope.launch {
                    viewModel.getConsumptionRecord(
                        beginDate = beginDate.toStringDate(),
                        endDate = endDate.toStringDate()
                    )
                }
            },
            onDismissRequest = { showEndDatePicker = false }
        )
    }
}
