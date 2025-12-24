package com.smart.htu.screens.application.courseHelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.R
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.navigation.Destinations
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.useful.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.format.DateTimeFormatter

@Composable
fun CourseHelperNavHost(
    navHostController: NavHostController
) {
    val navController = rememberNavController()
    val courseHelperViewModel: CourseHelperViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Destinations.CourseHelper.route
    ) {
        animatedComposable(Destinations.CourseHelper.route) {
            CourseHelper(navController, { navHostController.popBackStack() }, courseHelperViewModel)
        }
        animatedComposable(
            route = "${Destinations.CourseRepo.route}/{courseTypeName}/{courseTypeId}",
            arguments = listOf(
                navArgument(name = "courseTypeName") {
                    type = NavType.StringType
                },
                navArgument(name = "courseTypeId") {
                    type = NavType.StringType
                }
            )
        ) {
            val courseTypeName = it.arguments?.getString("courseTypeName") ?: ""
            val courseTypeId = it.arguments?.getString("courseTypeId") ?: ""
            CourseRepo(
                navController = navController,
                viewModel = courseHelperViewModel,
                courseTypeName = courseTypeName,
                courseTypeId = courseTypeId
            )
        }
        animatedComposable(
            route = "${Destinations.CourseInfo.route}/{courseCode}",
            arguments = listOf(
                navArgument(name = "courseCode") {
                    type = NavType.StringType
                }
            )
        ) {
            val courseCode = it.arguments?.getString("courseCode") ?: ""
            CourseInfo(
                navController = navController,
                viewModel = courseHelperViewModel,
                courseCode = courseCode
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseHelper(
    navController: NavController,
    onBack: () -> Unit,
    viewModel: CourseHelperViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(R.string.select_course_assistance),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Useful.Back,
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
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    if (uiState.targetCourseList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("暂无待选课程")
                        }
                    } else {
                        Column {
                            uiState.targetCourseList.forEach {
                                BasicComponent(
                                    title = it.courseName,
                                    summary = "${it.category} | ${it.teacherName} | ${it.credit}学分",
                                    rightActions = {
                                        top.yukonga.miuix.kmp.basic.IconButton(
                                            onClick = {
                                                viewModel.removeTargetCourse(it)
                                            }
                                        ) {
                                            top.yukonga.miuix.kmp.basic.Icon(
                                                imageVector = Icons.Outlined.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    onClick = {
                                        scope.launch {
                                            navController.navigate("${Destinations.CourseInfo.route}/${it.kcrwdm}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        top.yukonga.miuix.kmp.basic.IconButton(
                            minHeight = 35.dp,
                            minWidth = 35.dp,
                            onClick = { },
                            enabled = uiState.targetCourseList.isNotEmpty(),
                            backgroundColor = MiuixTheme.colorScheme.secondaryContainer,
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Outlined.TouchApp,
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp, end = 3.dp),
                                    text = "确定",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
            item { HorizontalDivider() }
            items(uiState.allCourseType) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            viewModel.changeCourseRepo(null)
                            delay(100)
                            navController.navigate("${Destinations.CourseRepo.route}/${it.courseTypeName}/${it.courseTypeId}")
                        }
                    },
                    insideMargin = PaddingValues(16.dp),
                    pressFeedbackType = PressFeedbackType.Sink,
                ) {
                    Text(
                        text = it.courseTypeName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight(550),
                    )
                    Text(
                        text = "选课学期: ${it.courseTermString}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        fontWeight = FontWeight(550),
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    Text(
                        text = "选课时间: ${it.startTime.format(pattern)} - ${
                            it.endTime.format(pattern)
                        }",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        fontWeight = FontWeight(550),
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                    Text(
                        text = it.description,
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        modifier = Modifier.padding(top = 2.dp),
                        maxLines = 4
                    )
                }
            }
        }
    }
}