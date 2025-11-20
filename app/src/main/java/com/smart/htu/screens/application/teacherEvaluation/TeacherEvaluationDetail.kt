package com.smart.htu.screens.application.teacherEvaluation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.EvaluationQuestion
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherEvaluationDetail(
    viewModel: TEViewModel = hiltViewModel(),
    navController: NavController,
    syllabusEvaluateCode: String,
    teacherCode: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing, uiState.loginJWCState) {
        if (isRefreshing) {
            delay(500)
            viewModel.getTEDetailService(syllabusEvaluateCode, teacherCode)
            isRefreshing = false
        }
    }

    LaunchedEffect(syllabusEvaluateCode, teacherCode) {
        viewModel.getTEDetailService(syllabusEvaluateCode, teacherCode)
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
                title = { Text(text = stringResource(id = R.string.teacher_evaluation)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
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
                if (uiState.evaluationQuestionList == null) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    items(uiState.evaluationQuestionList ?: emptyList()) {
                        QuestionItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: EvaluationQuestion
) {
    Surface(
        modifier = Modifier
            .semantics { role = androidx.compose.ui.semantics.Role.Button }
            .fillMaxWidth(),
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = question.questionType,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MiuixTheme.colorScheme.onSurfaceContainerVariant
                )
            )
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MiuixTheme.colorScheme.onSurface
                )
            )
            Column {

            }
        }
    }
}