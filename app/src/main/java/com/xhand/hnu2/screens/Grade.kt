package com.xhand.hnu2.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.xhand.hnu2.R
import com.xhand.hnu2.components.GradeListItem
import com.xhand.hnu2.components.ModalBottomSheet
import com.xhand.hnu2.components.showAlert
import com.xhand.hnu2.model.entity.KccjList
import com.xhand.hnu2.viewmodel.GradeViewModel
import com.xhand.hnu2.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GradeScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel,
    gradeViewModel: GradeViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = settingsViewModel.isRefreshing,
        onRefresh = {
            scope.launch {
                settingsViewModel.gradeService()
            }
        }
    )

    if (settingsViewModel.isLoginSuccess) {
        LaunchedEffect(Unit) {
            settingsViewModel.gradeService()
        }
    }
    Log.i("TAG666", "5656${settingsViewModel.gradeList}")
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
            LazyColumn(modifier = Modifier.padding(paddingValues = it)) {
                Log.i("TAG666", "3")
                items(settingsViewModel.gradeList) { grade ->
                    GradeListItem(grade = grade, modifier = Modifier.clickable {
                        showAlert = grade
                        settingsViewModel.showPersonAlert = true
                    })
                }
            }
            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                refreshing = settingsViewModel.isRefreshing,
                state = pullRefreshState
            )
        }
    }
    if (settingsViewModel.showPersonAlert) {
        Log.i("TAG666", "${settingsViewModel.showPersonAlert}")
        showAlert(grade = showAlert, settingsViewModel = settingsViewModel)
    }
    ModalBottomSheet(showModalBottomSheet = showBottomSheet, text = "筛选成绩") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}
