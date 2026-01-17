package com.smart.htu.screens.application.courseSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.api.module.CourseInfoEntity
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.main.TaskEntity
import com.smart.htu.screens.main.TaskType
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.MD5Util.md5
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDateTime

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseSearchRepo(
    navController: NavController,
    searchInfo: CourseSearchPostEntity,
    viewModel: CourseSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()

    LaunchedEffect(searchInfo) {
        viewModel.courseSearch(searchInfo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(R.string.course_search_result),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
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
                .overScrollVertical()
                .imePadding(),
            overscrollEffect = null,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = it.calculateTopPadding(),
                end = 16.dp,
                bottom = it.calculateBottomPadding() + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.searchRes == null) {
                item {
                    CircularProgressIndicator()
                }
            } else {
                if (uiState.searchRes.isNullOrEmpty()) {
                    item {
                        EmptyContent(
                            text = "没有搜索结果",
                            image = emptyData()
                        )
                    }
                } else {
                    items(uiState.searchRes ?: emptyList()) {
                        CourseSearchResItem(
                            course = it,
                            isInTaskList = false,
                            onAddClick = { task ->
                                scope.launch {
                                    viewModel.addTaskList(task)
                                }
                            },
                            onRemoveClick = { task ->
                                scope.launch {
                                    viewModel.removeTaskList(task)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseSearchResItem(
    course: CourseInfoEntity,
    isInTaskList: Boolean,
    onAddClick: (TaskEntity) -> Unit,
    onRemoveClick: (TaskEntity) -> Unit
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
        },
        insideMargin = PaddingValues(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = course.courseName.toString(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight(550),
                    )
                    Text(
                        text = course.teachingType,
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onTertiaryContainer.copy(
                            0.8f
                        ),
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(ContinuousRoundedRectangle(6.dp))
                            .background(
                                MiuixTheme.colorScheme.tertiaryContainer.copy(
                                    0.6f
                                )
                            )
                            .padding(
                                horizontal = 6.dp,
                                vertical = 2.dp
                            ),
                        fontWeight = FontWeight(750),
                        maxLines = 1
                    )
                }
                Text(
                    text = "教师：${course.teacherNames.ifEmpty { "无" }}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "教学场地：${course.campusName}-${course.teachingVenueName?.ifEmpty { "无" }}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "教学班：${course.className.replace(" 复制 ", "").replace(", ", "、")}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "${
                        convertLocalDateToStringDate(
                            course.date,
                            "yyyy年M月d日"
                        )
                    } 第 ${course.weeks} 周 周${course.day} ${course.sectionCode} 节",
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 4
                )
            }
            IconButton(
                backgroundColor = MiuixTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.8f
                ),
                minHeight = 35.dp,
                minWidth = 35.dp,
                onClick = {
                    val task = TaskEntity(
                        id = md5(course.courseName + course.teacherNames + course.sectionCode),
                        type = TaskType.Course,
                        title = course.courseName ?: "课程",
                        content = "",
                        location = course.teachingVenueName ?: "无",
                        startDateTime = LocalDateTime.of(2026, 1, 5, 10, 0, 0),
                        endDateTime = LocalDateTime.of(2026, 1, 5, 11, 0, 0)
                    )
                    if (isInTaskList) onRemoveClick(task)
                    else onAddClick(task)
                },
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (isInTaskList) Icons.Outlined.Remove else Icons.Outlined.Add,
                    tint = MiuixTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}
