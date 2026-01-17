package com.smart.htu.screens.application.teacherEvaluation

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.EvaluationInfo
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.application.grade.SelectTermBottomSheet
import com.smart.htu.screens.application.grade.UpFloatingActionButton
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.TermUtil.termConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherEvaluation(
    viewModel: TEViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = MiuixScrollBehavior()

    val isBottomSheetShow = remember { mutableStateOf(false) }
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(500)
            viewModel.refreshTermIndex()
            viewModel.getTeacherListService()
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.teacher_evaluation),
                actions = {
                    IconButton(
                        onClick = { isBottomSheetShow.value = true },
                        modifier = Modifier.padding(end = 16.dp),
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
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
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
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = it
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 8.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .overScrollVertical(),
                overscrollEffect = null
            ) {
                if (uiState.evaluationInfo == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    item {
                        if (uiState.evaluationInfo?.evaluationInfoList?.isEmpty() == true)
                            EmptyContent(
                                text = "学期 ${termConverter(uiState.termCode)}\n评价时间 ${uiState.evaluationInfo?.msg}",
                                image = emptyData()
                            )
                        else
                            EmptyContent(
                                text = "学期 ${termConverter(uiState.termCode)}\n评价时间 ${uiState.evaluationInfo?.msg}",
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                    }
                    items(
                        (uiState.evaluationInfo?.evaluationInfoList
                            ?: emptyList()).sortedBy { it.evaluationCode }
                    ) {
                        TeacherItem(
                            teacher = it,
                            onClick = { syllabusEvaluateCode, teacherCode ->
                                if (it.evaluationCode.isEmpty())
                                    navController.navigate("${Destinations.TeacherEvaluationDetail.route}/${syllabusEvaluateCode}/${teacherCode}")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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
            }
        }
    )

}

@Composable
fun TeacherItem(
    teacher: EvaluationInfo,
    onClick: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onClick(teacher.syllabusEvaluateCode, teacher.teacherCode) },
        insideMargin = PaddingValues(16.dp),
        pressFeedbackType = PressFeedbackType.Sink,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    top.yukonga.miuix.kmp.basic.Text(
                        text = teacher.teacherName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight(550),
                    )
                }
                top.yukonga.miuix.kmp.basic.Text(
                    text = "${teacher.courseType} | ${teacher.courseName}",
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 4
                )
            }
            top.yukonga.miuix.kmp.basic.Text(
                text = if (teacher.evaluationCode.isNotEmpty()) "已评价" else "未评价",
                fontSize = 12.sp,
                color = if (teacher.evaluationCode.isNotEmpty())
                    MiuixTheme.colorScheme.onTertiaryContainer.copy(0.8f)
                else MiuixTheme.colorScheme.error.copy(0.8f),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(ContinuousRoundedRectangle(6.dp))
                    .background(
                        if (teacher.evaluationCode.isNotEmpty())
                            MiuixTheme.colorScheme.tertiaryContainer.copy(0.6f)
                        else MiuixTheme.colorScheme.errorContainer.copy(0.6f)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight(750),
                maxLines = 1
            )
            if (teacher.evaluationCode.isEmpty()) {
                top.yukonga.miuix.kmp.basic.Icon(
                    modifier = Modifier
                        .size(width = 10.dp, height = 16.dp),
                    imageVector = MiuixIcons.Basic.ArrowRight,
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurfaceVariantActions,
                )
            }
        }
    }
}