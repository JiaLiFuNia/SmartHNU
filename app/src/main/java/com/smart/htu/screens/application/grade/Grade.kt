package com.smart.htu.screens.application.grade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.api.module.CourseGradeDetailRes.CourseGradeDetailEntity
import com.smart.htu.api.module.CourseGradeRankRes
import com.smart.htu.api.module.CourseGradeRes.CourseGradeEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.chart.BasicLineChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.GradeDivideUtil.divideGrade
import com.smart.htu.utils.TermUtil.termConverter
import com.smart.htu.utils.copyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.TabRowDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlin.time.Duration.Companion.milliseconds

data class CourseGradeDialogData(
    val gradeDetail: CourseGradeDetailEntity? = null,
    val gradeRankInClass: CourseGradeRankRes.CourseGradeRankEntity? = null,
    val gradeRankInCourse: CourseGradeRankRes.CourseGradeRankEntity? = null
) {
    val isReady: Boolean
        get() = gradeDetail != null && gradeRankInClass != null && gradeRankInCourse != null
}

@Composable
fun Grade(
    viewModel: GradeViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    val isBottomSheetShow = remember { mutableStateOf(false) }
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectIndex by remember { derivedStateOf { pagerState.currentPage } }
    val tabItem = listOf("成绩", "统计")

    val selectedIndex = remember { mutableIntStateOf(1) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(500.milliseconds)
            viewModel.refreshTermList()
            viewModel.getCourseGrade()
            isRefreshing = false
        }
    }
    var collapsedFraction by remember { mutableFloatStateOf(scrollBehavior.state.collapsedFraction) }
    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        snapshotFlow { scrollBehavior.state.collapsedFraction }.collectLatest {
            collapsedFraction = it
        }
    }
    val dynamicTopPadding by remember { derivedStateOf { 12.dp * (1f - collapsedFraction) } }

    val backdrop = rememberBlurBackdrop(uiState.blurEffect)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = stringResource(id = R.string.course_grade),
                    actions = {
                        IconButton(
                            onClick = { isBottomSheetShow.value = true },
                            holdDownState = isBottomSheetShow.value
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.More,
                                contentDescription = "more"
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                    },
                    bottomContent = {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = dynamicTopPadding, bottom = 6.dp)
                        ) {
                            TabRow(
                                tabs = tabItem,
                                selectedTabIndex = selectIndex,
                                onTabSelected = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(it)
                                    }
                                },
                                colors = TabRowDefaults.tabRowColors(
                                    backgroundColor = barColor
                                )
                            )
                        }
                    }
                )
            }
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
                .fillMaxSize()
                .let {
                    if (backdrop != null) {
                        it.layerBackdrop(backdrop)
                    } else {
                        it
                    }
                },
            contentPadding = PaddingValues(
                top = it.calculateTopPadding() + 12.dp,
                bottom = it.calculateBottomPadding() + 16.dp
            )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                pageSpacing = 12.dp
            ) { index ->
                when (index) {
                    0 -> LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .overScrollVertical()
                            .scrollEndHaptic(),
                        overscrollEffect = null,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = it.calculateTopPadding() + 12.dp,
                            bottom = it.calculateBottomPadding() + 16.dp
                        )
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
                                itemsIndexed(uiState.courseGrade ?: emptyList()) { index, course ->
                                    CourseGradeItem(
                                        index = index,
                                        grade = course,
                                        gradeInfo = CourseGradeDialogData(
                                            gradeDetail = uiState.courseGradeDetail,
                                            gradeRankInClass = uiState.courseGradeRankInClass,
                                            gradeRankInCourse = uiState.courseGradeRankInCourse
                                        ),
                                        onClick = {
                                            scope.launch {
                                                viewModel.getCourseGradeDetail(it)
                                                viewModel.getCourseGradeRank(it)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    1 -> LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .overScrollVertical()
                            .scrollEndHaptic(),
                        overscrollEffect = null,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = it.calculateTopPadding() + 12.dp,
                            bottom = it.calculateBottomPadding() + 16.dp
                        )
                    ) {
                        if (uiState.courseGPA.values.isEmpty() || uiState.allCredits == null) {
                            item {
                                CircularProgressIndicator()
                            }
                        } else {
                            item {
                                SmallTitle(
                                    text = "绩点",
                                    insideMargin = PaddingValues(
                                        start = 12.dp,
                                        bottom = 8.dp
                                    )
                                )
                                val data = remember(uiState.courseGPA) {
                                    mutableStateOf(uiState.courseGPA)
                                }
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(CardDefaults.CornerRadius),
                                    color = MiuixTheme.colorScheme.surfaceContainer
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BasicLineChart(data = data)
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    selectedIndex.intValue =
                                                        (selectedIndex.intValue + 1) % 2
                                                    viewModel.getCourseGPA(selectedIndex.intValue)
                                                }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .size(width = 96.dp, height = 36.dp),
                                            enabled = !uiState.isLoadingGPA,
                                            insideMargin = PaddingValues(0.dp, 0.dp)
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
                                    insideMargin = PaddingValues(
                                        start = 12.dp,
                                        bottom = 8.dp
                                    )
                                )
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row {
                                        Column(
                                            modifier = Modifier.weight(0.5f)
                                        ) {
                                            uiState.allCredits?.filterIndexed { index, _ ->
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
                                            uiState.allCredits?.filterIndexed { index, _ ->
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

        SelectTermDialog(
            globalTermCode = uiState.globalTermCode,
            termSelectedCode = uiState.termCode,
            termList = uiState.termList,
            show = isBottomSheetShow.value,
            onClick = {
                scope.launch {
                    viewModel.changeTermCode(it)
                }
            },
            onDismissRequest = { isBottomSheetShow.value = false }
        )
    }
}


@Composable
fun CourseGradeItem(
    index: Int,
    grade: CourseGradeEntity,
    gradeInfo: CourseGradeDialogData?,
    onClick: (String) -> Unit
) {
    val holdDownIndex = remember { mutableIntStateOf(0) }
    val isGradeDetailBottomSheetShow = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BasicComponent(
            title = if (grade.examType != "初修") "[${grade.examType}]" else "" + grade.courseName,
            summary = buildString {
                append(grade.courseMode)
                append(" | ")
                append(grade.courseCategory)
                if (grade.courseClassification.isNotEmpty())
                    append("-")
                append(grade.courseClassification)
            },
            endActions = {
                Text(
                    text = grade.gradeString,
                    fontSize = MiuixTheme.textStyles.body1.fontSize,
                    color = divideGrade(grade.gradeDouble),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            onClick = {
                onClick(grade.gradeCode)
                holdDownIndex.intValue = index
                isGradeDetailBottomSheetShow.value = true
            },
            holdDownState = holdDownIndex.intValue == index && isGradeDetailBottomSheetShow.value
        )
    }

    CourseGradeDetailDialog(
        gradeInfo = gradeInfo,
        grade = grade,
        showGradeDetailBottomSheet = isGradeDetailBottomSheetShow.value,
        onDismissRequest = { isGradeDetailBottomSheetShow.value = false }
    )
}

@Composable
fun CourseGradeDetailDialog(
    grade: CourseGradeEntity,
    gradeInfo: CourseGradeDialogData?,
    showGradeDetailBottomSheet: Boolean,
    onDismissRequest: () -> Unit
) {
    OverlayBottomSheet(
        title = "${grade.courseName} 的成绩详情",
        show = showGradeDetailBottomSheet,
        onDismissRequest = {
            onDismissRequest()
        },
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        if (gradeInfo?.isReady != true) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                top.yukonga.miuix.kmp.basic.CircularProgressIndicator()
            }
        } else {
            val gradeDetail = gradeInfo.gradeDetail!!
            val gradeRankInClass = gradeInfo.gradeRankInClass!!
            val gradeRankInCourse = gradeInfo.gradeRankInCourse!!
            Column {
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
                            label = "总成绩",
                            content = gradeRankInClass.totalGrade.toString(),
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = gradeDetail.percentageFirstLabel,
                            content = gradeDetail.gradeFirst.toString().ifEmpty { "无" },
                            endContent = {
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
                            endContent = {
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
                            endContent = {
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
                            endContent = {
                                gradeDetail.percentageFourth.toString().let {
                                    if (it.isNotEmpty())
                                        Text("${it}%")
                                }
                            },
                            rowIndex = 3
                        )
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    labelOnTop = false,
                    message = listOf(
                        SingleInfo(
                            label = "学时",
                            content = grade.totalHours,
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = "学分",
                            content = grade.gradeCredits.toString(),
                            rowIndex = 1
                        )
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    labelOnTop = false,
                    message = listOf(
                        SingleInfo(
                            label = "${gradeRankInClass.courseType}排名-${gradeRankInClass.className}",
                            content = "${gradeRankInClass.ranking} / ${gradeRankInClass.totalStudents}",
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = "${gradeRankInCourse.courseType}排名-${gradeRankInCourse.className}",
                            content = "${gradeRankInCourse.ranking} / ${gradeRankInCourse.totalStudents}",
                            rowIndex = 1
                        )
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun UpFloatingActionButton(
    modifier: Modifier = Modifier,
    fabVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
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