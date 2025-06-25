package com.smart.htu.screens.application.teacherEvaluation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.EvaluationQuestion
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TeacherEvaluationDetail(
    viewModel: TEViewModel = hiltViewModel(),
    navController: NavController,
    syllabusEvaluateCode: String,
    teacherCode: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    val onRefresh: () -> Unit = {
        scope.launch {
            pullToRefreshState.completeRefreshing {
                viewModel.getTEDetailService(syllabusEvaluateCode, teacherCode)
            }
        }
    }

    LaunchedEffect(syllabusEvaluateCode, teacherCode) {
        viewModel.getTEDetailService(syllabusEvaluateCode, teacherCode)
    }

    ScaffoldWithHazeLazyColumn(
        isMediumTopAppBar = true,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        blurEnabledState = uiState.blurEffect,
        title = { Text(text = stringResource(id = R.string.teacher_evaluation)) },
        actions = { },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        refreshState = pullToRefreshState,
        onRefresh = { onRefresh() }
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
            when (uiState.evaluationQuestionList.status == Status.LOADING || !uiState.isTokenValid) {
                true ->
                    item {
                        CircularProgressIndicator()
                    }

                false -> {
                    items(uiState.evaluationQuestionList.data ?: emptyList()) {
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
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
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