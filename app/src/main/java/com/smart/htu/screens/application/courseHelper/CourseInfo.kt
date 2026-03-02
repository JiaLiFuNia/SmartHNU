package com.smart.htu.screens.application.courseHelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.LocalNavigator
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseInfo(
    viewModel: CourseHelperViewModel = hiltViewModel(),
    courseCode: String
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()

    LaunchedEffect(courseCode) {
        viewModel.getCourseInfo(courseCode = courseCode)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = "课程安排",
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                ) {
                    blurRadius = 30.dp
                    noiseFactor = 0f
                    blurEnabled = true
                }
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .hazeSource(hazeState)
                .overScrollVertical(),
            overscrollEffect = null,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding(),
                end = 16.dp,
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.courseInfo == null) {
                item {
                    CircularProgressIndicator()
                }
            } else {
                if (uiState.courseInfo!!.isEmpty()) {
                    item {
                        EmptyContent(
                            text = "没有数据",
                            image = emptyData()
                        )
                    }
                } else {
                    items(uiState.courseInfo ?: emptyList()) {
                        MessageCardDisplay(
                            modifier = Modifier.fillMaxWidth(),
                            labelOnTop = true,
                            message = listOf(
                                SingleInfo(
                                    label = "周次",
                                    content = "第 ${it.weekIndexString} 周 星期${it.day}",
                                    rowIndex = 0
                                ),
                                SingleInfo(
                                    label = "节次",
                                    content = it.sectionListString ?: "",
                                    rowIndex = 0
                                ),
                                SingleInfo(
                                    label = "教师",
                                    content = it.teacherNames,
                                    rowIndex = 1
                                ),
                                SingleInfo(
                                    label = "教室",
                                    content = it.classroomName,
                                    rowIndex = 1
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}