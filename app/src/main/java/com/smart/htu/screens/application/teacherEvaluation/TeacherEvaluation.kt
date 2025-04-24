package com.smart.htu.screens.application.teacherEvaluation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.EvaluationInfo
import com.smart.htu.api.module.Status
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.ScaffoldWithHazeLazyColumn
import com.smart.htu.component.svgVector.DrawableVectors
import com.smart.htu.component.svgVector.drawablevectors.emptyData
import com.smart.htu.screens.application.grade.SelectTermBottomSheet
import com.smart.htu.utils.Term.termConverter
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.LazyColumn
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TeacherEvaluation(
    viewModel: TEViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val pullToRefreshState = top.yukonga.miuix.kmp.basic.rememberPullToRefreshState()
    val isBottomSheetShow = remember {
        mutableStateOf(false)
    }
    val onRefresh: () -> Unit = {
        viewModel.refreshTermIndex()
        viewModel.getTeacherListService(uiState.termCode)
    }

    ScaffoldWithHazeLazyColumn(
        isMediumTopAppBar = true,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        blurEnabledState = uiState.blurEffect,
        title = { Text(text = stringResource(id = R.string.teacher_evaluation)) },
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
        },
        refreshState = pullToRefreshState,
        onRefresh = { onRefresh() },
        itemSpacePadding = 12.dp
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (uiState.evaluationInfo.status == Status.LOADING || !uiState.isTokenValid) {
                true ->
                    item {
                        CircularProgressIndicator()
                    }

                false -> {
                    item {
                        if (uiState.evaluationInfo.data?.evaluationInfoList?.isEmpty() == true)
                            EmptyContent(
                                text = "学期 ${termConverter(uiState.termCode)}\n评价时间 ${uiState.evaluationInfo.data?.msg}",
                                image = DrawableVectors.emptyData()
                            )
                        else
                            EmptyContent(
                                text = "学期 ${termConverter(uiState.termCode)}\n评价时间 ${uiState.evaluationInfo.data?.msg}",
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(bottom = 12.dp)
                            )
                    }
                    items(uiState.evaluationInfo.data?.evaluationInfoList ?: emptyList()) {
                        SingleTeacher(it)
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
                pullToRefreshState.completeRefreshing {
                    onRefresh()
                }
            }
        }
    )

}

@Composable
fun SingleTeacher(
    teacher: EvaluationInfo
) {
    Surface(
        onClick = {
        },
        modifier = Modifier
            .semantics { role = androidx.compose.ui.semantics.Role.Button }
            .fillMaxWidth(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = MiuixTheme.colorScheme.surface
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    text = teacher.teacherName,
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
                    InfoBadge(teacher.courseType)
                    InfoBadge(text = teacher.courseName)
                }
            },
            trailingContent = {
                Text(
                    text = "已评价",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        )
    }
}