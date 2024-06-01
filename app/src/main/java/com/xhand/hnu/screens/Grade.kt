package com.xhand.hnu.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xhand.hnu.R
import com.xhand.hnu.components.GradeListItem
import com.xhand.hnu.components.ModalBottomSheet
import com.xhand.hnu.components.ShowAlert
import com.xhand.hnu.model.entity.KccjList
import com.xhand.hnu.viewmodel.GradeViewModel
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GradeScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel,
    gradeViewModel: GradeViewModel
) {
    val checkboxes = gradeViewModel.checkboxes
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    var showAlert by remember {
        mutableStateOf(
            KccjList(
                xsxm = "",
                zcjfs = 100.0,
                kcflmc = "",
                xnxqmc = "",
                kcdlmc = "",
                cjjd = 5.0,
                kcrwdm = "",
                kcmc = "",
                cjdm = "",
                zcj = "",
                xf = 3,
                xnxqdm = "",
                xdfsmc = "",
                cjfsmc = "",
            )
        )
    }
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val showBottomSheetData = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = {
            scope.launch {
                viewModel.gradeService()
            }
        }
    )

    if (viewModel.isLoginSuccess) {
        LaunchedEffect(Unit) {
            viewModel.gradeService()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(
                            MaterialTheme.colorScheme.surface.copy()
                        )
                ),
                title = {
                    Text(text = "成绩")
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { showBottomSheetData.value = true }) {
                        Icon(
                            painterResource(id = R.drawable.ic_baseline_show_chart_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_filter),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            Column(
                modifier = Modifier
                    .padding(paddingValues = it)
                    .verticalScroll(scrollState)
                    .pullRefresh(pullRefreshState)
            ) {
                viewModel.gradeList.forEach { grade ->
                    GradeListItem(
                        grade = grade,
                        modifier = Modifier
                            .clickable {
                                showAlert = grade
                                viewModel.showPersonAlert = true
                                viewModel.cjdm = grade.cjdm
                            }
                    )
                }
            }
            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                refreshing = viewModel.isRefreshing,
                state = pullRefreshState
            )
        }
    }
    if (viewModel.showPersonAlert) {
        ShowAlert(grade = showAlert, viewModel = viewModel)
    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "筛选成绩") {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "排序方式",
                    fontWeight = FontWeight.W900,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "学年学期",
                    fontWeight = FontWeight.W900,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            checkboxes.forEachIndexed { index, info ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp)
                        .clickable {
                            checkboxes[index] = info.copy(
                                isChecked = !info.isChecked
                            )
                        }
                ) {
                    Checkbox(
                        checked = info.isChecked,
                        onCheckedChange = { isChecked ->
                            checkboxes[index] = info.copy(
                                isChecked = isChecked
                            )
                        }
                    )
                    Text(text = info.term)
                }
            }

        }

    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheetData, text = "数据统计") {

    }
}
