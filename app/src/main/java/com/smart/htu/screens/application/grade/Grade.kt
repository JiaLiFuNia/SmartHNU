package com.smart.htu.screens.application.grade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.GradeDivideUtil.divideGrade
import com.smart.htu.utils.TermUtil.termConverter
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
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
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isBottomSheetShow = remember { mutableStateOf(false) }
    val isGradeDetailBottomSheetShow = remember { mutableStateOf(false) }
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.refreshTermList()
                viewModel.getCourseGrade()
            }
        }
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                viewModel.getCourseGrade()
            }
        }
    )

    CourseGradeDetailDialog(
        message = uiState.courseGradeDetail,
        showGradeDetailBottomSheet = isGradeDetailBottomSheetShow
    )
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
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
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