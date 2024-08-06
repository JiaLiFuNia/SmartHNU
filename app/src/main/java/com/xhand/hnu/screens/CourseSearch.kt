package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.xhand.hnu.components.CourseSearchListItem
import com.xhand.hnu.viewmodel.CourseSearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSearchScreen(
    onBack: () -> Unit,
    courseSearchViewModel: CourseSearchViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    var ifShow by remember {
        mutableStateOf(true)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        if (!ifShow) courseSearchViewModel.getCourseIndex()
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
            ExtendedFloatingActionButton(
                onClick = {
                    courseSearchViewModel.courseSearch(
                        courseSearchViewModel.searchContent.value
                    )
                    ifShow = false
                },
                text = {
                    Text(text = "搜索")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "搜索")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
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
                        onClick = { ifShow = !ifShow }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.clickable { ifShow = !ifShow }
            )
            var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
            var showDropDownMenu by remember { mutableStateOf(false) }
            HorizontalDivider()
            if (ifShow) {
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.xsnj,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(xsnj = it)
                    },
                    label = { Text(text = "年级") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        IconButton(onClick = { showDropDownMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "年级"
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.zydm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(zydm = it)
                    },
                    label = { Text(text = "专业") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.kcmc,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(kcmc = it)
                    },
                    label = { Text(text = "课程") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.teaxm,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(teaxm = it)
                    },
                    label = { Text(text = "教师") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.jcdm,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(jcdm = it)
                    },
                    label = { Text(text = "节次") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.jxbmc,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(jxbmc = it)
                    },
                    label = { Text(text = "教学班") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.jzwdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(jzwdm = it)
                    },
                    label = { Text(text = "教学楼") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.jxcdmc,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(jxcdmc = it)
                    },
                    label = { Text(text = "教学场地") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.rq,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(text = "日期") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { showDatePicker = true },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "日期")
                        }
                    },
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.zc.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(zc = it)
                    },
                    label = { Text(text = "周次") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.xq.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(xq = it)
                    },
                    label = { Text(text = "星期") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.kkyxdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(kkyxdm = it)
                    },
                    label = { Text(text = "开课单位") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.xsyxdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(xsyxdm = it)
                    },
                    label = { Text(text = "学生院系") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (courseSearchViewModel.isGettingCourse) {
                CircularProgressIndicator()
            } else {
                if (courseSearchViewModel.searchResult.isEmpty()) {
                    Text(text = "无结果")
                }
                courseSearchViewModel.searchResult.forEach { room ->
                    CourseSearchListItem(course = room)
                }
            }
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
            Log.i("TAG63", confirmEnabled.value.toString())
            if (showDatePicker)
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
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
                                showDatePicker = false
                            }
                        ) {
                            Text("取消")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = { showDropDownMenu = false },
                modifier = Modifier
            ) {
                courseSearchViewModel.searchCourseIndex.xsnjList.forEach { aXsnjList ->
                    DropdownMenuItem(text = { Text(text = aXsnjList.title) }, onClick = {})
                }
            }
        }
    }
}
