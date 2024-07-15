package com.xhand.hnu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
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
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                label = { Text(text = "jxcdmc") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            TextButton(onClick = { courseSearchViewModel.courseSearch() }) {
                Text(text = "搜索")
            }

            Text(text = courseSearchViewModel.searchResult.toString())
        }
    }
}
