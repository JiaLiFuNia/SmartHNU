package com.smart.htu.screens.application.grade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.CourseGradeDetailRes.CourseGradeDetailEntity
import com.smart.htu.api.module.CourseGradeRes.CourseGradeEntity
import com.smart.htu.component.BasicDialog
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.TabRow
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.chart.LineChart
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.GradeDivideUtil.divideGrade
import com.smart.htu.utils.TermUtil.termConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.G2RoundedCornerShape
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = { Text(text = stringResource(id = R.string.course_grade)) },
                actions = {
                    IconButton(onClick = { isBottomSheetShow.value = true }) {
                        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "more")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                modifier = Modifier,
                visible = !fabVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = { scope.launch { lazyListState.scrollToItem(0) } }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                        contentDescription = "up"
                    )
                }
            }
        }
    ) {
        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = { isRefreshing = true },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = it
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
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
                                        SingleCourseGrade(
                                            course = it,
                                            onClick = {
                                                scope.launch {
                                                    viewModel.getCourseGradeDetail(it)
                                                    isGradeDetailBottomSheetShow.value = true
                                                }
                                            }
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
                                        shape = G2RoundedCornerShape(CardDefaults.CornerRadius),
                                        color = MiuixTheme.colorScheme.surface
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
                                                    containerColor = MiuixTheme.colorScheme.surface,
                                                    contentColor = MiuixTheme.colorScheme.onSurface
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
                                                    .size(width = 96.dp, height = 36.dp)
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
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = G2RoundedCornerShape(CardDefaults.CornerRadius),
                                        color = MiuixTheme.colorScheme.surface
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            uiState.allCredits?.forEach {
                                                BasicComponent(
                                                    title = it.label,
                                                    rightActions = {
                                                        Text(it.credit)
                                                    }
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

        CourseGradeDetailDialog(
            message = uiState.courseGradeDetail,
            showGradeDetailBottomSheet = isGradeDetailBottomSheetShow
        )
    }
}


@Composable
fun SingleCourseGrade(
    course: CourseGradeEntity,
    onClick: (String) -> Unit
) {
    Surface(
        onClick = { onClick(course.gradeCode) },
        modifier = Modifier
            .semantics { role = Role.Button }
            .fillMaxWidth()
            .animateContentSize(),
        shape = G2RoundedCornerShape(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoBadge(course.courseCategory)
                    InfoBadge(course.courseClassification)
                }
            },
            trailingContent = {
                Text(
                    text = course.gradeString,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = divideGrade(course.gradeDouble),
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@Composable
fun CourseGradeDetailDialog(
    message: CourseGradeDetailEntity?,
    showGradeDetailBottomSheet: MutableState<Boolean>
) {
    BasicDialog(
        showDialog = showGradeDetailBottomSheet,
        title = "成绩详情",
        insideMargin = DpSize(16.dp, 24.dp)
    ) {
        if (message == null) {
            top.yukonga.miuix.kmp.basic.CircularProgressIndicator()
        } else {
            MessageCardDisplay(
                modifier = Modifier.fillMaxWidth(),
                message = listOf(
                    SingleInfo(
                        label = "平时成绩",
                        content = message.usualGrade.toString()
                    ),
                    SingleInfo(
                        label = "期末成绩",
                        content = message.finalGrade.toString()
                    )
                )
            )
        }
    }
}