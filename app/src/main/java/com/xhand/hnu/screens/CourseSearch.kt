package com.xhand.hnu.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.xhand.hnu.components.CourseSearchListItem
import com.xhand.hnu.viewmodel.CourseSearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
            FloatingActionButton(onClick = {
                courseSearchViewModel.courseSearch(
                    courseSearchViewModel.searchContent.value
                )
                ifShow = false
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "搜索")
            }
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
            HorizontalDivider()
            if (ifShow) {
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.xsnj,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(xsnj = it)
                    },
                    label = { Text(text = "xsnj") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.gnqdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(gnqdm = it)
                    },
                    label = { Text(text = "gnqdm") },
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
                    label = { Text(text = "jcdm") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.jhlxdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(jhlxdm = it)
                    },
                    label = { Text(text = "jhlxdm") },
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
                    label = { Text(text = "jxbmc") },
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
                    label = { Text(text = "jzwdm") },
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
                    label = { Text(text = "教学楼名称") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.rq,
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(rq = it)
                    },
                    label = { Text(text = "日期") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.zc.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(zc = it)
                    },
                    label = { Text(text = "周数") },
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
                    label = { Text(text = "kkyxdm") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                OutlinedTextField(
                    value = courseSearchViewModel.searchContent.value.kkjysdm.toString(),
                    onValueChange = {
                        courseSearchViewModel.searchContent.value =
                            courseSearchViewModel.searchContent.value.copy(kkjysdm = it)
                    },
                    label = { Text(text = "kkjysdm") },
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
                    label = { Text(text = "xsyxdm") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
            }
            if (courseSearchViewModel.isGettingCourse) {
                CircularProgressIndicator()
            } else {
                courseSearchViewModel.searchResult.forEach { room ->
                    CourseSearchListItem(course = room)
                }
            }
        }
    }
}
