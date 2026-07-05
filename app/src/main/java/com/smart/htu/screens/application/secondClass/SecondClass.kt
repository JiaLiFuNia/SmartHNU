package com.smart.htu.screens.application.secondClass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.chart.BasicColumnChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.component.updateWebViewCookies
import com.smart.htu.di.NetworkModule.ApiConstants.SECOND_CLASS_BASE_URL
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Cookie
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
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
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SecondClass(
    viewModel: SecondClassViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()

    val selectedTermIndex = remember(uiState.termList) {
        mutableIntStateOf(0)
    }
    val showSCLoginDialog = remember { mutableStateOf(false) }
    val loginState = remember(uiState.scLoginState) { mutableStateOf(uiState.scLoginState == 1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000.milliseconds)
            viewModel.getHourList()
            isRefreshing = false
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = stringResource(id = R.string.second_class),
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },

                            ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        if (uiState.scLoginState != 1) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        viewModel.loadSecondClassSid()
                                        showSCLoginDialog.value = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = "Login",
                                    tint = MiuixTheme.colorScheme.error
                                )
                            }
                        }
                        if (uiState.scLoginState == 1) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        updateWebViewCookies(
                                            url = SECOND_CLASS_BASE_URL,
                                            cookie = listOf(
                                                Cookie.Builder()
                                                    .name("sid")
                                                    .value(uiState.cookie)
                                                    .domain(
                                                        SECOND_CLASS_BASE_URL
                                                            .replace("/", "")
                                                            .substringAfter("http:")
                                                    )
                                                    .build()
                                            )
                                        )
                                        navigator.push(
                                            Route.ApplicationWebView(
                                                SECOND_CLASS_BASE_URL,
                                                "第二课堂"
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.format_paint_24px),
                                    contentDescription = "paint"
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
        }
    ) {
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            PullToRefresh(
                pullToRefreshState = pullToRefreshState,
                refreshTexts = PULL_TO_REFRESH_TEXT,
                onRefresh = { isRefreshing = true },
                isRefreshing = isRefreshing,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                )
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = it.calculateTopPadding() + 12.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .scrollEndHaptic()
                        .overScrollVertical(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    overscrollEffect = null
                ) {
                    if (!loginState.value) {
                        item {
                            SuggestChip(
                                onClick = {
                                    scope.launch {
                                        viewModel.loadSecondClassSid()
                                        showSCLoginDialog.value = true
                                    }
                                },
                                text = "暂未登录第二课堂，或登录状态失效，请重新登录",
                                icon = Icons.AutoMirrored.Filled.ArrowForward,
                                type = SuggestChipType.ERROR
                            )
                        }
                    }
                    if (loginState.value || !uiState.hourList.isNullOrEmpty()) {
                        if (uiState.hourList == null) {
                            item {
                                CircularProgressIndicator()
                            }
                        } else {
                            item {
                                Card {
                                    uiState.hourList?.lastOrNull().let {
                                        val difference = 600 - (it?.convertedTotalScore ?: 0.0)
                                        BasicComponent(
                                            title = "学时总计",
                                            summary = if (difference > 0) "距离毕业要求还差 $difference 学时" else "已满足毕业学时要求",
                                            titleColor = BasicComponentDefaults.titleColor(
                                                MiuixTheme.colorScheme.onBackground
                                            ),
                                            startAction = {
                                                Icon(
                                                    painter = painterResource(R.drawable.format_paint_24px_filled),
                                                    contentDescription = "学时总计",
                                                    modifier = Modifier.padding(end = 8.dp),
                                                    tint = Color(ApplicationEntity.ApplicationColor.ORANGE.color)
                                                )
                                            },
                                            endActions = {
                                                Text(
                                                    text = "${it?.totalScore?.toInt()} (${it?.convertedTotalScore?.toInt()})",
                                                    color = MiuixTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            item {
                                val xData = remember { mutableStateOf(viewModel.category) }
                                val yData = remember {
                                    mutableStateOf(
                                        uiState.hourList?.lastOrNull()?.let {
                                            listOf(
                                                it.classicScore,
                                                it.lectureScore,
                                                it.activityScore,
                                                it.practiceScore,
                                                it.subjectCompetitionScore,
                                                it.laborScore ?: 0.0
                                            )
                                        } ?: emptyList()
                                    )
                                }
                                Card {
                                    BasicComponent(title = "分项统计")
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 12.dp)
                                            .padding(horizontal = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BasicColumnChart(
                                            xData = xData,
                                            yData = yData,
                                            verticalAxisItemPlacerStep = 75.0,
                                            columnCollectionSpacing = 13.dp,
                                            columnWidth = 10.dp
                                        )
                                    }
                                }
                            }
                            item {
                                Column {
                                    Card {
                                        OverlayDropdownPreference(
                                            title = "学期学时统计",
                                            items = uiState.termList?.map { it.semester }
                                                ?: emptyList(),
                                            selectedIndex = selectedTermIndex.intValue,
                                            onSelectedIndexChange = {
                                                if (it != (uiState.termList?.get(it)?.termIndex ?: 0)) {
                                                    selectedTermIndex.intValue = it + 1
                                                } else {
                                                    selectedTermIndex.intValue = uiState.termList?.get(it)?.termIndex ?: 0
                                                }
                                            }
                                        )
                                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                                        (uiState.hourList ?: emptyList()).find {
                                            it.termIndex == selectedTermIndex.intValue.let { if (it == 0) 100 else it }
                                        }?.let { item ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(0.5f)
                                                ) {
                                                    BasicComponent(
                                                        title = item.classicScore.toInt()
                                                            .toString(),
                                                        summary = viewModel.category[0],
                                                    )
                                                    BasicComponent(
                                                        title = item.lectureScore.toInt()
                                                            .toString(),
                                                        summary = viewModel.category[1],
                                                    )
                                                    BasicComponent(
                                                        title = item.practiceScore.toInt()
                                                            .toString(),
                                                        summary = viewModel.category[3],
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.weight(0.5f)
                                                ) {
                                                    BasicComponent(
                                                        title = item.activityScore.toInt()
                                                            .toString(),
                                                        summary = viewModel.category[2],
                                                    )
                                                    BasicComponent(
                                                        title = item.subjectCompetitionScore.toInt()
                                                            .toString(),
                                                        summary = viewModel.category[4],
                                                    )
                                                    BasicComponent(
                                                        title = (item.laborScore ?: 0.0).toInt()
                                                            .toString(),
                                                        summary = viewModel.category[5],
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "注意：1. 学时以 75 为单位，即学时每满 75 加 75，不足 75 按 0 计算。\n2. 每 75 学时可转换为 0.5 学分，毕业要求为 4 学分，因此至少需要 600 学时。\n3. 每一类别至多 300 学时。",
                                        fontSize = 14.sp,
                                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            EmptyContent(
                                image = emptyData(),
                                text = "请点击右上角按钮进行登录",
                            )
                        }
                    }
                }
            }
        }

        var verifyCodeRefreshKey by remember { mutableIntStateOf(0) }
        val verifyCodeModel = remember(verifyCodeRefreshKey, uiState.cookie) {
            val headers = NetworkHeaders.Builder()
                .set("Cookie", "sid=${uiState.cookie}")
                .build()
            ImageRequest.Builder(context)
                .data("http://dekt.htu.edu.cn/img/resources-code.jpg?${System.currentTimeMillis()}")
                .httpHeaders(headers)
                .crossfade(true)
                .build()
        }

        LoginDialog(
            showDialog = showSCLoginDialog.value,
            title = "登录到第二课堂",
            summary = "登录后可获取第二课堂的部分数据",
            isNeedVerifyCode = true,
            verifyCodeModel = verifyCodeModel,
            onRefreshVerifyCode = {
                verifyCodeRefreshKey++
            },
            initAccount = uiState.studentID,
            initPassword = uiState.password,
            onLogin = { studentID, password, verifyCode ->
                scope.launch {
                    viewModel.secondClassLogin(
                        studentID = studentID,
                        password = password,
                        verifyCode = verifyCode,
                        onSuccess = {
                            showSCLoginDialog.value = false
                            isRefreshing = true
                            showToast(context, "登录成功!")
                        },
                        onFailure = {
                            showToast(context, "登录失败！$it")
                            verifyCodeRefreshKey++
                        }
                    )
                }
            },
            loginState = uiState.scLoginState,
            onDismissRequest = {
                showSCLoginDialog.value = false
            }
        )
    }
}