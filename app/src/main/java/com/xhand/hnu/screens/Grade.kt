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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.xhand.hnu.viewmodel.TermCheckBoxes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    onBack: () -> Unit,
    gradeViewModel: GradeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by gradeViewModel.uiState.collectAsState()

    val checkboxes = remember {
        mutableStateListOf<TermCheckBoxes>()
    }
    LaunchedEffect(Unit) {
        checkboxes.clear()
        checkboxes.addAll(
            gradeViewModel.longGradeTerm.map {
                TermCheckBoxes(
                    isChecked = it == gradeViewModel.currentLongTerm,
                    term = it
                )
            }
        )
        Log.i("TAG666", "cu: ${gradeViewModel.currentLongTerm}")
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
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
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh = {
        isRefreshing = true
        coroutineScope.launch {
            delay(timeMillis = 1000)
            gradeViewModel.gradeService()
                isRefreshing = false
        }
    }
    LaunchedEffect(Unit) {
        gradeViewModel.gradeService()
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
        if (uiState.isGettingGrade)
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
                val matchedElements = checkboxes.filter { it.isChecked }
                if (matchedElements.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("暂无数据")
                    }
                } else {
                    Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    gradeViewModel.gradeList.sortByDescending {
                        when (selectedIndex) {
                            0 -> it.order.toString()
                            1 -> it.zcjfs.toString()
                            else -> null
                        }
                    }
                    uiState.gradeList.forEach { grade ->
                        if (grade.xnxqmc in matchedElements.map { it.term }) {
                            GradeListItem(
                                grade = grade,
                                modifier = Modifier
                                    .clickable {
                                        showAlert = grade
                                        gradeViewModel.showPersonAlert = true
                                        cjdm = grade.cjdm
                                    }
                            )
                        }
                    }
                    }
                }

            }
        }
    }
    if (gradeViewModel.showPersonAlert) {
        ShowAlert(grade = showAlert, viewModel = gradeViewModel, cjdm = cjdm)
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
                        selected = index == selectedIndex,
                        label = { Text(text = label) },
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
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
}
