package com.xhand.hnu.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xhand.hnu.components.CourseSearchDropDownTextField
import com.xhand.hnu.components.CourseSearchListItem
import com.xhand.hnu.components.CourseSearchOutlineTextFiled
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
    "UseOfNonLambdaOffsetOverload"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSearchScreen(
    onBack: () -> Unit,
    courseSearchViewModel: CourseSearchViewModel = viewModel()
) {
    val uiState by courseSearchViewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    var ifShowTextField by remember {
        mutableStateOf(true)
    }
    val options = listOf("按日期", "按周数")
    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(uiState) {
        courseSearchViewModel.getCourseIndex()
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
                    Text(text = "课程查询")
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
            Column {
                ExtendedFloatingActionButton(
                    onClick = {
                        courseSearchViewModel.courseSearch(
                            courseSearchViewModel.searchContent.value
                        )
                        ifShowTextField = false
                    },
                    text = { Text(text = "搜索") },
                    expanded = scrollState.value == 0,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    },
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(10.dp))
                ExtendedFloatingActionButton(
                    onClick = {
                        courseSearchViewModel.searchContent.value = CourseSearchPost(
                            1,
                            50000,
                            uiState.searchCourseIndex.xnxqdm,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            rq = getCurrentDates(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        courseSearchViewModel.clearSearchResult()
                        ifShowTextField = true
                    },
                    text = { Text(text = "重置") },
                    expanded = scrollState.value == 0,
                    icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "重置") },
                    modifier = Modifier.align(Alignment.End)
                )
                /*AnimatedVisibility(
                    visible = scrollState.value > 0,
                    enter = slideInVertically(initialOffsetY = { it * 2 }),
                    exit = slideOutVertically(targetOffsetY = { it * 2 }),
                ) {*/
                // }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        val subArrowRotateDegrees: Float by animateFloatAsState(
            if (ifShowTextField) 180f else 0f,
            label = ""
        )
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ListItem(
                headlineContent = { Text(text = "搜索选项") },
                trailingContent = {
                    IconButton(
                        onClick = { ifShowTextField = !ifShowTextField }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(subArrowRotateDegrees),
                        )
                    }
                },
                modifier = Modifier.clickable { ifShowTextField = !ifShowTextField }
            )
            HorizontalDivider()

            if (ifShowTextField) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
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
                courseSearchViewModel.searchContentKeys.forEach { searchContentKey ->
                    if (searchContentKey.show && searchContentKey.name != "") {
                        if (searchContentKey.courseIndex != "") {
                            CourseSearchDropDownTextField(
                                content = searchContentKey,
                                value = getSearchContentValueByKey(
                                    courseSearchViewModel.searchContent.value,
                                    searchContentKey.key
                                ).toString(),
                                onValueChange = {
                                    courseSearchViewModel.searchContent.value =
                                        updateSearchContentValueByKey(
                                            courseSearchViewModel.searchContent.value,
                                            searchContentKey.key,
                                            it
                                        )
                                },
                                searchCourseIndex = uiState.searchCourseIndex
                            )
                        } else {
                            CourseSearchOutlineTextFiled(
                                content = searchContentKey,
                                value = getSearchContentValueByKey(
                                    courseSearchViewModel.searchContent.value,
                                    searchContentKey.key
                                ).toString(),
                                onValueChange = {
                                    courseSearchViewModel.searchContent.value =
                                        updateSearchContentValueByKey(
                                            courseSearchViewModel.searchContent.value,
                                            searchContentKey.key,
                                            it
                                        )
                                },
                                courseSearchViewModel = courseSearchViewModel,
                                enabled = selectedIndex
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (uiState.isGettingCourse) {
                CircularProgressIndicator()
            } else {
                if (uiState.searchResult!!.isEmpty()) {
                    Text(text = "无结果")
                } else {
                    uiState.searchResult!!.forEach { room ->
                        CourseSearchListItem(course = room)
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            val datePickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
            if (courseSearchViewModel.showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        courseSearchViewModel.showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                courseSearchViewModel.showDatePicker = false
                                courseSearchViewModel.searchContent.value.rq =
                                    timeStamp2DateStr(datePickerState.selectedDateMillis ?: 0)
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                courseSearchViewModel.showDatePicker = false
                            }
                        ) {
                            Text("取消")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

fun getSearchContentValueByKey(searchContent: CourseSearchPost, searchContentKey: String): Any? {
    val property = CourseSearchPost::class.memberProperties.find { it.name == searchContentKey }
    return property?.get(searchContent)
}

fun updateSearchContentValueByKey(
    searchContent: CourseSearchPost,
    key: String,
    value: Any
): CourseSearchPost {
    val constructor = CourseSearchPost::class.primaryConstructor!!
    val params = constructor.parameters.associateWith { param ->
        if (param.name == key) value else CourseSearchPost::class.memberProperties
            .find { it.name == param.name }
            ?.apply { isAccessible = true }
            ?.get(searchContent)
    }
    return constructor.callBy(params)
}