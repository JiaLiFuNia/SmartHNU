package com.smart.htu.screens.application.courseHelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.smart.htu.App.Companion.context
import com.smart.htu.api.module.CourseItemEntity
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.SuggestChip
import com.smart.htu.component.SuggestChipType
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.ToastUtil.showToast
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.miuixShape
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseRepo(
    courseTypeName: String,
    courseTypeId: String,
    viewModel: CourseHelperViewModel = hiltViewModel(),
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()
    val textValue = rememberSaveable { mutableStateOf("") }

    val hasRequestedLoad = rememberSaveable(courseTypeId) { mutableStateOf(false) }

    LaunchedEffect(courseTypeId) {
        val shouldLoad = !hasRequestedLoad.value || uiState.courseRepo == null
        if (shouldLoad) {
            if (uiState.courseRepo == null) {
                viewModel.getCourseTypeInfo(courseTypeId)
                viewModel.getCourseRepo(courseTypeId)
                hasRequestedLoad.value = true
            }
        }
    }

    LaunchedEffect(textValue.value) {
        viewModel.searchCourse(textValue.value)
    }

    BackHandler {
        if (textValue.value.isNotEmpty()) {
            textValue.value = ""
        } else {
            navigator.pop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = courseTypeName,
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                        
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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .zIndex(1f)
                    .background(Color.Transparent)
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    }
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding())
                        .padding(vertical = 8.dp),
                    insideMargin = DpSize(16.dp, 0.dp),
                    inputField = {
                        InputField(
                            query = textValue.value,
                            onQueryChange = { textValue.value = it },
                            onSearch = {
                                scope.launch {
                                    viewModel.searchCourse(textValue.value)
                                }
                            },
                            expanded = false,
                            onExpandedChange = { },
                            label = "搜索课程、教师、课程类别...",
                        )
                    },
                    expanded = false,
                    onExpandedChange = { }
                ) { }
            }
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
                    top = it.calculateTopPadding() + 48.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding() + 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (textValue.value.isNotEmpty()) {
                    if (uiState.searchCourseRepo.isNotEmpty()) {
                        items(uiState.searchCourseRepo) {
                            CourseRepoItem(
                                course = it,
                                onClick = { courseCode ->
                                    navigator.push(Route.CourseInfo(courseCode))
                                },
                                onAddClick = { course ->
                                    scope.launch {
                                        viewModel.addTargetCourse(course)
                                    }
                                },
                                targetCourseListSize = uiState.targetCourseList.size,
                                isInTargetCourseList = uiState.targetCourseList.contains(it)
                            )
                        }
                    }
                } else {
                    if (uiState.courseRepo == null || uiState.termCode == null) {
                        item {
                            CircularProgressIndicator()
                        }
                    } else {
                        item {
                            SuggestChip(
                                text = "现在是 ${uiState.selectionPhase} 阶段（${uiState.startTime}-${uiState.endTime}），${uiState.isCancelable.takeIf { it } ?: "不"}可退选课程。",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {},
                                type = SuggestChipType.INFO
                            )
                        }
                        if (uiState.courseRepo.isNullOrEmpty()) {
                            item {
                                EmptyContent(
                                    text = "没有课程",
                                    image = emptyData()
                                )
                            }
                        } else {
                            items((uiState.courseRepo ?: emptyList())) {
                                CourseRepoItem(
                                    course = it,
                                    onClick = { courseCode ->
                                        navigator.push(Route.CourseInfo(courseCode))
                                    },
                                    onAddClick = { course ->
                                        scope.launch {
                                            viewModel.addTargetCourse(course)
                                        }
                                    },
                                    targetCourseListSize = uiState.targetCourseList.size,
                                    isInTargetCourseList = uiState.targetCourseList.contains(it)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseRepoItem(
    course: CourseItemEntity,
    onClick: (String) -> Unit,
    onAddClick: (CourseItemEntity) -> Unit,
    targetCourseListSize: Int,
    isInTargetCourseList: Boolean,
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick(course.courseTaskCode)
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
                        text = course.courseName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight(550)
                    )
                    if (course.category.isNotEmpty()) {
                        Text(
                            text = course.courseCategoryName.ifEmpty { course.category },
                            fontSize = 12.sp,
                            color = MiuixTheme.colorScheme.onTertiaryContainer.copy(
                                0.8f
                            ),
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .clip(miuixShape(6.dp))
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
                        text = "${course.credit}学分",
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onTertiaryContainer.copy(
                            0.8f
                        ),
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(miuixShape(6.dp))
                            .background(
                                MiuixTheme.colorScheme.tertiaryContainer.copy(
                                    0.6f
                                )
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight(750),
                        maxLines = 1
                    )
                }
                Text(
                    text = "教师：${course.teacherName?.ifEmpty { "无" }}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "人数：${course.enrolledCount} / ${course.totalCapacity}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "教学班：${course.className.replace("复制", "").replace(",", "、")}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight(550),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
            }
            IconButton(
                backgroundColor = MiuixTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.8f
                ),
                minHeight = 35.dp,
                minWidth = 35.dp,
                onClick = {
                    scope.launch {
                        onAddClick(course)
                        if (targetCourseListSize >= 3) {
                            if (isInTargetCourseList)
                                showToast(context, "课程已在待选列表中")
                            else
                                showToast(
                                    context,
                                    "最多只能添加3门课程"
                                )
                        } else {
                            if (isInTargetCourseList)
                                showToast(context, "课程已在待选列表中")
                            else
                                showToast(context, "已添加到待选课程")
                        }
                    }
                },
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Outlined.Add,
                    tint = MiuixTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}