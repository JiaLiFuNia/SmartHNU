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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.smart.htu.api.module.Term
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.chart.ColumnChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.updateWebViewCookies
import com.smart.htu.di.NetworkModule.ApiConstants.SECOND_CLASS_BASE_URL
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.login.LoginDialog
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Cookie
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.extra.SuperListPopup
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SecondClass(
    viewModel: SecondClassViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val hazeState = rememberHazeState()

    val selectedTermIndex = remember(uiState.termList) {
        mutableIntStateOf(100)
    }
    val showSCLoginDialog = remember { mutableStateOf(false) }
    val loginState = remember(uiState.scLoginState) { mutableStateOf(uiState.scLoginState == 1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            viewModel.getHourList()
            isRefreshing = false
        }
    }

    /*LaunchedEffect(uiState.scLoginState) {
        Log.d("TAG66", "LoginDialog: $showSCLoginDialog")
        showSCLoginDialog.value = uiState.scLoginState != 1
    }*/

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(id = R.string.second_class),
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
                    if (uiState.scLoginState != 1) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.loadSecondClassSid()
                                    showSCLoginDialog.value = true
                                }
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = "Login",
                                tint = MaterialTheme.colorScheme.error
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
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.format_paint_24px),
                                contentDescription = "paint"
                            )
                        }
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = true
                },
            )
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackBarHostState)
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
                    .padding(top = 16.dp)
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                                                painter = painterResource(R.drawable.format_paint_24px),
                                                contentDescription = "学时总计",
                                                modifier = Modifier.padding(end = 12.dp),
                                                tint = MiuixTheme.colorScheme.onBackground
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
                            SmallTitle(
                                text = "分项统计",
                                insideMargin = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            )
                            Card {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ColumnChart(
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                SmallTitle(
                                    text = "学期学时统计",
                                    insideMargin = PaddingValues(horizontal = 12.dp)
                                )
                                SelectTerm(
                                    termList = uiState.termList ?: emptyList(),
                                    selectedIndex = selectedTermIndex
                                )
                            }
                            Column {
                                (uiState.hourList ?: emptyList()).find {
                                    it.termIndex == selectedTermIndex.intValue
                                }?.let { item ->
                                    Card(modifier = Modifier.fillMaxSize()) {
                                        Row {
                                            Column(
                                                modifier = Modifier.weight(0.5f)
                                            ) {
                                                BasicComponent(
                                                    title = item.classicScore.toInt().toString(),
                                                    summary = viewModel.category[0],
                                                )
                                                BasicComponent(
                                                    title = item.lectureScore.toInt().toString(),
                                                    summary = viewModel.category[1],
                                                )
                                                BasicComponent(
                                                    title = item.practiceScore.toInt().toString(),
                                                    summary = viewModel.category[3],
                                                )
                                            }
                                            Column(
                                                modifier = Modifier.weight(0.5f)
                                            ) {
                                                BasicComponent(
                                                    title = item.activityScore.toInt().toString(),
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
        showDialog = showSCLoginDialog,
        summary = "第二课堂登录",
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
        loginState = uiState.scLoginState
    )
}

@Composable
fun SelectTerm(
    modifier: Modifier = Modifier,
    termList: List<Term>,
    selectedIndex: MutableState<Int>
) {
    val expanded = remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current
    Surface(
        onClick = {
            expanded.value = true
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = termList.find { it.termIndex == selectedIndex.value }?.semester
                    ?: "选择学期",
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
            Icon(
                painter = painterResource(R.drawable.expand_all_24px),
                contentDescription = "Select Term",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(18.dp),
                tint = MiuixTheme.colorScheme.onSurface
            )
        }

        SuperListPopup(
            show = expanded,
            popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
            alignment = PopupPositionProvider.Align.TopEnd,
            onDismissRequest = {
                expanded.value = false
            },
            enableWindowDim = false
        ) {
            ListPopupColumn {
                termList.forEachIndexed { index, term ->
                    DropdownImpl(
                        text = term.semester,
                        optionSize = termList.size,
                        isSelected = termList[index].termIndex == selectedIndex.value,
                        onSelectedIndexChange = {
                            selectedIndex.value = termList[index].termIndex
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                            expanded.value = false
                        },
                        index = index
                    )
                }
            }
        }
    }
}