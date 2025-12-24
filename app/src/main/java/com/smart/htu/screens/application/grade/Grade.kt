package com.smart.htu.screens.application.grade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.CourseGradeDetailRes.CourseGradeDetailEntity
import com.smart.htu.api.module.CourseGradeRes.CourseGradeEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.TabRow
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.chart.LineChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.GradeDivideUtil.divideGrade
import com.smart.htu.utils.TermUtil.termConverter
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.extra.SuperBottomSheet
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.icon.icons.useful.ImmersionMore
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Grade(
    viewModel: GradeViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    val isBottomSheetShow = remember { mutableStateOf(false) }
    val isGradeDetailBottomSheetShow = remember { mutableStateOf(false) }
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectIndex by remember { derivedStateOf { pagerState.currentPage } }
    val tabItem = listOf("成绩", "统计")

    val selectedIndex = remember { mutableIntStateOf(1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(500)
            viewModel.refreshTermList()
            viewModel.getCourseGrade()
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(id = R.string.course_grade),
                actions = {
                    IconButton(
                        onClick = { isBottomSheetShow.value = true },
                        modifier = Modifier.padding(end = 16.dp),
                        holdDownState = isBottomSheetShow.value
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.ImmersionMore,
                            contentDescription = "more"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        top.yukonga.miuix.kmp.basic.Icon(
                            imageVector = MiuixIcons.Useful.Back,
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            UpFloatingActionButton(
                fabVisible = fabVisible,
                onClick = { scope.launch { lazyListState.scrollToItem(0) } }
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
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding() + 16.dp)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TabRow(
                    tabs = tabItem,
                    selectedTabIndex = selectIndex,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                    pageSpacing = 12.dp
                ) {
                    if (it == 0) {
                        LazyColumn(
                            state = lazyListState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .overScrollVertical(),
                            overscrollEffect = null
                        ) {
                            if (uiState.courseGrade == null) {
                                item {
                                    CircularProgressIndicator()
                                }
                            } else {
                                if (uiState.courseGrade?.isEmpty() == true) {
                                    item {
                                        EmptyContent(
                                            text = "学期 ${termConverter(uiState.termCode)}\n暂无数据",
                                            image = emptyData()
                                        )
                                    }
                                } else {
                                    items(uiState.courseGrade ?: emptyList()) {
                                        CourseGradeItem(
                                            grade = it,
                                            onClick = {
                                                scope.launch {
                                                    viewModel.getCourseGradeDetail(it)
                                                }
                                            },
                                            gradeDetail = uiState.courseGradeDetail
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            state = lazyListState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .overScrollVertical(),
                            overscrollEffect = null
                        ) {
                            if (uiState.courseGPA.values.isEmpty() || uiState.allCredits == null) {
                                item {
                                    CircularProgressIndicator()
                                }
                            } else {
                                item {
                                    SmallTitle(
                                        text = "绩点",
                                        insideMargin = PaddingValues(start = 12.dp, bottom = 8.dp)
                                    )
                                    val data = remember(uiState.courseGPA) {
                                        mutableStateOf(uiState.courseGPA)
                                    }
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
                                        color = MiuixTheme.colorScheme.surfaceContainer
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LineChart(data = data)
                                            Button(
                                                onClick = {
                                                    scope.launch {
                                                        selectedIndex.intValue =
                                                            (selectedIndex.intValue + 1) % 2
                                                        viewModel.getCourseGPA(selectedIndex.intValue)
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MiuixTheme.colorScheme.background,
                                                    contentColor = MiuixTheme.colorScheme.onBackground
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    MiuixTheme.colorScheme.outline
                                                ),
                                                contentPadding = PaddingValues(
                                                    horizontal = 4.dp,
                                                    vertical = 2.dp
                                                ),
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .size(width = 96.dp, height = 36.dp),
                                                enabled = !uiState.isLoadingGPA
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                    contentDescription = null
                                                )
                                                Text(viewModel.statisticalMethod.keys.toList()[selectedIndex.intValue])
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    SmallTitle(
                                        text = "课程大类学分",
                                        insideMargin = PaddingValues(start = 12.dp, bottom = 8.dp)
                                    )
                                    Card(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row {
                                            Column(
                                                modifier = Modifier.weight(0.5f)
                                            ) {
                                                uiState.allCredits?.filterIndexed { index, entity ->
                                                    index % 2 == 0
                                                }?.forEach {
                                                    BasicComponent(
                                                        title = it.credit,
                                                        summary = it.label,
                                                        onClick = { copyContent("${it.label} ${it.credit}") }
                                                    )
                                                }
                                            }
                                            Column(
                                                modifier = Modifier.weight(0.5f)
                                            ) {
                                                uiState.allCredits?.filterIndexed { index, entity ->
                                                    index % 2 == 1
                                                }?.forEach {
                                                    BasicComponent(
                                                        title = it.credit,
                                                        summary = it.label,
                                                        onClick = { copyContent("${it.label} ${it.credit}") }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        SelectTermBottomSheet(
            globalTermCode = uiState.globalTermCode,
            termSelectedCode = uiState.termCode,
            termList = uiState.termList,
            isBottomSheetShow = isBottomSheetShow,
            onClick = {
                scope.launch {
                    viewModel.changeTermCode(it)
                    isRefreshing = true
                }
            }
        )
    }
}


@Composable
fun CourseGradeItem(
    gradeDetail: CourseGradeDetailEntity?,
    grade: CourseGradeEntity,
    onClick: (String) -> Unit
) {
    val isGradeDetailBottomSheetShow = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick(grade.gradeCode)
            isGradeDetailBottomSheetShow.value = true
        },
        insideMargin = PaddingValues(16.dp),
        pressFeedbackType = PressFeedbackType.Sink,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = grade.courseName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(550),
                )
                Text(
                    text = buildAnnotatedString {
                        append(grade.courseCategory)
                        if (grade.courseClassification.isNotEmpty())
                            append(" | ")
                        append(grade.courseClassification)
                    },
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 4
                )
            }
            Text(
                text = grade.gradeString,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = divideGrade(grade.gradeDouble),
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }

    CourseGradeDetailDialog(
        gradeDetail = gradeDetail,
        grade = grade,
        showGradeDetailBottomSheet = isGradeDetailBottomSheetShow
    )
}

@Composable
fun CourseGradeDetailDialog(
    gradeDetail: CourseGradeDetailEntity?,
    grade: CourseGradeEntity,
    showGradeDetailBottomSheet: MutableState<Boolean>
) {
    SuperBottomSheet(
        title = "成绩详情",
        show = showGradeDetailBottomSheet,
        onDismissRequest = {
            showGradeDetailBottomSheet.value = false
        },
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        if (gradeDetail == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                top.yukonga.miuix.kmp.basic.CircularProgressIndicator()
            }
        } else {
            MessageCardDisplay(
                modifier = Modifier.fillMaxWidth(),
                labelOnTop = false,
                message = listOf(
                    SingleInfo(
                        label = "绩点",
                        content = grade.gradePoint.toString(),
                        rowIndex = 1
                    ),
                    SingleInfo(
                        label = "学分",
                        content = grade.gradeCredits.toString(),
                        rowIndex = 1
                    ),
                    SingleInfo(
                        label = gradeDetail.percentageFirstLabel,
                        content = gradeDetail.gradeFirst.toString().ifEmpty { "无" },
                        rightContent = {
                            gradeDetail.percentageFirst.toString().let {
                                if (it.isNotEmpty())
                                    Text("${it}%")
                            }
                        },
                        rowIndex = 2
                    ),
                    SingleInfo(
                        label = gradeDetail.percentageSecondLabel,
                        content = gradeDetail.gradeSecond.toString().ifEmpty { "无" },
                        rightContent = {
                            gradeDetail.percentageSecond.toString().let {
                                if (it.isNotEmpty())
                                    Text("${it}%")
                            }
                        },
                        rowIndex = 2
                    ),
                    SingleInfo(
                        label = gradeDetail.percentageThirdLabel,
                        content = gradeDetail.gradeThird.toString().ifEmpty { "无" },
                        rightContent = {
                            gradeDetail.percentageThird.toString().let {
                                if (it.isNotEmpty())
                                    Text("${it}%")
                            }
                        },
                        rowIndex = 3
                    ),
                    SingleInfo(
                        label = gradeDetail.percentageFourthLabel,
                        content = gradeDetail.gradeFourth.toString().ifEmpty { "无" },
                        rightContent = {
                            gradeDetail.percentageFourth.toString().let {
                                if (it.isNotEmpty())
                                    Text("${it}%")
                            }
                        },
                        rowIndex = 3
                    )
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun UpFloatingActionButton(
    fabVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = !fabVisible,
        enter = slideInVertically(initialOffsetY = { it * 2 }),
        exit = slideOutVertically(targetOffsetY = { it * 2 }),
    ) {
        top.yukonga.miuix.kmp.basic.FloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowUpward,
                contentDescription = "up",
                tint = MiuixTheme.colorScheme.onPrimary
            )
        }
    }
}