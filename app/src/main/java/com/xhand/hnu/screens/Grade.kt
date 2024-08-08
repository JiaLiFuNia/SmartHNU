package com.xhand.hnu.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel,
    gradeViewModel: GradeViewModel
) {
    val checkboxes = gradeViewModel.checkboxes
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val gradeList = viewModel.gradeList
    Log.i("TAG666", gradeList.toString())
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("默认", "成绩")
    var cjdm by remember {
        mutableStateOf("")
    }
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
                order = 0
            )
        )
    }
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val showBottomSheetData = remember {
        mutableStateOf(false)
    }
    /*val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = {
            scope.launch {
                delay(1000)
                viewModel.gradeService()
            }
        }
    )*/
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh = {
        isRefreshing = true
        coroutineScope.launch {
            delay(timeMillis = 1000)
                viewModel.gradeService()
                isRefreshing = false
        }
    }
    LaunchedEffect(Unit) {
        viewModel.gradeService()
        delay(500)
        viewModel.isGettingGrade = false
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
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
    ) { values ->
        if (viewModel.isGettingGrade)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() },
                state = state,
                modifier = Modifier
                    .padding(paddingValues = values),
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    val matchedElements = checkboxes.filter { it.isChecked }
                    Log.i("TAG62", matchedElements.toString())
                    gradeList.sortByDescending {
                        when (selectedIndex) {
                            0 -> it.order.toString()
                            1 -> it.zcjfs.toString()
                            else -> null
                        }
                    }
                    gradeList.forEach { grade ->
                        Log.i("TAG62", grade.xnxqdm)
                        if (grade.xnxqmc in matchedElements.map { it.term })
                            GradeListItem(
                                grade = grade,
                                modifier = Modifier
                                    .clickable {
                                        showAlert = grade
                                        viewModel.showPersonAlert = true
                                        cjdm = grade.cjdm
                                    }
                            )
                    }
                }
            }
        }
    }
    if (viewModel.showPersonAlert) {
        ShowAlert(grade = showAlert, viewModel = viewModel, cjdm = cjdm)
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

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(start = 32.dp)
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex
                    ) {
                        Text(label)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
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
