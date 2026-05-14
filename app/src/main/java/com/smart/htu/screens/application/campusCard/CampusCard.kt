package com.smart.htu.screens.application.campusCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.login.LoginViewModel
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.ToastUtil.showToast
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
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusCardScreen(
    viewModel: CampusCardViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val showLoginDialog = remember(uiState.authLoginState) {
        mutableStateOf(uiState.authLoginState != 1)
    }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            viewModel.getCardBalance()
            if (uiState.loginState) {
                viewModel.getConsumptionRecord(
                    beginDate = convertLocalDateToStringDate(LocalDate.now().minusDays(30)),
                    endDate = getCurrentDate(),
                    pageSize = 100 // 一次获取较多数据以准确计算30天统计
                )
            }
            isRefreshing = false
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    val scrollBehavior = MiuixScrollBehavior()
    Scaffold(
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        },
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = "校园卡",
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
            UpFloatingActionButton(fabVisible = fabVisible) {
                scope.launch { lazyListState.scrollToItem(0) }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .let {
                    if (backdrop != null) it.layerBackdrop(backdrop) else it
                },
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                contentPadding = PaddingValues(top = it.calculateTopPadding() + 16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
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
                            BasicComponent(
                                title = "余额",
                                summary = "数据来源于完美校园",
                                startAction = {
                                    Icon(
                                        imageVector = Icons.Outlined.CreditCard,
                                        contentDescription = "balance"
                                    )
                                },
                                endActions = {
                                    Text(
                                        text = if (uiState.balance == null) "-- 元" else "${uiState.balance} 元",
                                        color = MiuixTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f)
                            ) {
                                BasicComponent(
                                    title = String.format(
                                        Locale.US,
                                        "%.0f%%",
                                        uiState.breakfastFrequency * 100
                                    ),
                                    summary = "早餐频率"
                                )
                            }
                            Card(
                                modifier = Modifier.weight(1f)
                            ) {
                                BasicComponent(
                                    title = "${
                                        String.format(
                                            Locale.US,
                                            "%.2f",
                                            uiState.averageExpenditure
                                        )
                                    } 元",
                                    summary = "日均支出"
                                )
                            }
                        }
                    }
                    item {
                        val threeDaysAgo = remember { LocalDate.now().minusDays(3) }
                        val recentRecords = remember(uiState.consumptionRecords) {
                            uiState.consumptionRecords?.filter {
                                try {
                                    val date =
                                        LocalDate.parse(it.businessOpDate.substring(0, 10))
                                    !date.isBefore(threeDaysAgo)
                                } catch (_: Exception) {
                                    false
                                }
                            } ?: emptyList()
                        }
                        Card {
                            BasicComponent(
                                title = "近3天消费情况",
                                endActions = {
                                    Text(
                                        text = "更多",
                                        color = MiuixTheme.colorScheme.primary,
                                        style = MiuixTheme.textStyles.headline2,
                                        modifier = Modifier.clickable {
                                            navigator.push(Route.ConsumptionRecord)
                                        }
                                    )
                                },
                                insideMargin = PaddingValues(16.dp)
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (uiState.consumptionRecords == null) {
                                    if (uiState.authLoginState != 1) {
                                        EmptyContent(
                                            text = "请先进行统一认证登录",
                                            modifier = Modifier.height(120.dp)
                                        )
                                    } else {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .height(120.dp)
                                                .fillMaxWidth()
                                        )
                                    }
                                } else {
                                    if (recentRecords.isEmpty()) {
                                        EmptyContent(
                                            text = "近3天没有消费记录",
                                            modifier = Modifier.height(120.dp)
                                        )
                                    } else {
                                        recentRecords.forEach {
                                            BasicComponent(
                                                title = it.location,
                                                summary = it.businessOpDate,
                                                endActions = {
                                                    if (it.tradeFlag == "1") {
                                                        Text(
                                                            text = "-${it.amount} 元",
                                                            color = MiuixTheme.colorScheme.error
                                                        )
                                                    } else {
                                                        Text(
                                                            text = "+${it.amount} 元",
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
                    }
                    item {
                        Text(
                            text = "前往完美校园进行充值",
                            style = MiuixTheme.textStyles.main,
                            color = MiuixTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }
            }
        }

        LoginDialog(
            showDialog = showLoginDialog.value,
            summary = "统一身份认证系统",
            onLogin = { studentID, password, _ ->
                scope.launch {
                    loginViewModel.authLogin(
                        studentID = studentID,
                        password = password,
                        onSuccess = {
                            scope.launch {
                                showToast(context, "登录成功!")
                                viewModel.login()
                            }
                        },
                        onFailure = {
                            showToast(context, "登录失败！请检查账号密码是否正确")
                        }
                    )
                }
            },
            loginState = uiState.authLoginState,
            onDismissRequest = {
                showLoginDialog.value = false
            }
        )
    }
}