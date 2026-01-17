package com.smart.htu.screens.application.courseSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smart.htu.R
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.MiuixHintTextField
import com.smart.htu.component.animation.animatedComposable
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.DateUtil.getCurrentDate
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@Composable
fun CourseSearchNavHost(
    navHostController: NavHostController
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.CourseSearch.route
    ) {
        animatedComposable(Destinations.CourseSearch.route) {
            CourseSearch(navController, { navHostController.popBackStack() })
        }
        animatedComposable(
            route = "${Destinations.CourseSearchRepo.route}/{searchInfo}",
            arguments = listOf(
                navArgument(name = "searchInfo") {
                    type = NavType.StringType
                }
            )
        ) {
            val searchInfoString = it.arguments?.getString("searchInfo") ?: ""
            val searchInfo = Json.decodeFromString<CourseSearchPostEntity>(searchInfoString)
            CourseSearchRepo(navController, searchInfo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CourseSearch(
    navController: NavController,
    onBack: () -> Unit,
    viewModel: CourseSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val hazeState = rememberHazeState()

    val isLoadingIndex = remember { derivedStateOf { mutableStateOf(uiState.isLoadingIndex) } }

    val dateOrWeek = rememberSaveable { mutableIntStateOf(0) }
    val selectedTermIndex = rememberSaveable(uiState.currentTermCode) {
        mutableIntStateOf(
            uiState.termList.indexOfFirst { it.termCode == uiState.currentTermCode }.let {
                if (it == -1) 0 else it
            }
        )
    }
    val selectedDepartmentIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedCampusIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedBuildingIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedStudentGradeIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedStudentDepartmentIndex = rememberSaveable { mutableIntStateOf(0) }
    val selectedMajorIndex = rememberSaveable { mutableIntStateOf(0) }

    var searchInfo by rememberSaveable(
        uiState.currentTermCode,
        stateSaver = Saver(
            save = { Json.encodeToString(it) },
            restore = { Json.decodeFromString(it) }
        )
    ) {
        mutableStateOf(
            CourseSearchPostEntity(
                date = getCurrentDate(),
                termCode = uiState.currentTermCode
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                color = Color.Transparent,
                title = stringResource(R.string.course_search),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Regular.Back,
                            contentDescription = "back"
                        )
                    }
                },
                modifier =
                    Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(MiuixTheme.colorScheme.surface)
                    ) {
                        blurRadius = 30.dp
                        noiseFactor = 0f
                        blurEnabled = true
                    }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextButton(
                        text = "搜索",
                        onClick = {
                            scope.launch {
                                val searchInfoString = Json.encodeToString(searchInfo)
                                navController.navigate("${Destinations.CourseSearchRepo.route}/${searchInfoString}")
                            }
                        },
                        minHeight = 32.dp,
                        colors = ButtonDefaults.textButtonColorsPrimary(),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
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
            item {
                Card {
                    SuperDropdown(
                        title = "学年学期",
                        items = uiState.termList.map { it.termString },
                        selectedIndex = selectedTermIndex.intValue,
                        onSelectedIndexChange = {
                            selectedTermIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                termCode = uiState.termList[it].termCode
                            )
                        }
                    )
                    Row {
                        SuperCheckbox(
                            title = "日期",
                            checked = dateOrWeek.intValue == 0,
                            onCheckedChange = {
                                searchInfo = searchInfo.copy(dayOfWeek = null)
                                searchInfo = searchInfo.copy(week = null)
                                dateOrWeek.intValue = 0
                            },
                            modifier = Modifier.weight(0.5f)
                        )
                        SuperCheckbox(
                            title = "周次星期",
                            checked = dateOrWeek.intValue == 1,
                            onCheckedChange = {
                                searchInfo = searchInfo.copy(date = null)
                                dateOrWeek.intValue = 1
                            },
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    if (dateOrWeek.intValue == 0) {
                        MiuixHintTextField(
                            value = searchInfo.date ?: "",
                            onValueChange = {
                                searchInfo = searchInfo.copy(date = it)
                            },
                            label = "日期"
                        )
                    } else {
                        MiuixHintTextField(
                            value = searchInfo.week ?: "",
                            onValueChange = {
                                searchInfo = searchInfo.copy(week = it)
                            },
                            label = "周次"
                        )
                        MiuixHintTextField(
                            value = searchInfo.dayOfWeek ?: "",
                            onValueChange = {
                                searchInfo = searchInfo.copy(dayOfWeek = it)
                            },
                            label = "星期"
                        )
                    }
                    MiuixHintTextField(
                        value = searchInfo.sessionCode ?: "",
                        onValueChange = {
                            searchInfo = searchInfo.copy(sessionCode = it)
                        },
                        label = "节次"
                    )
                }
            }
            item {
                Card {
                    MiuixHintTextField(
                        value = searchInfo.courseName ?: "",
                        onValueChange = {
                            searchInfo = searchInfo.copy(courseName = it)
                        },
                        label = "课程名称"
                    )
                    MiuixHintTextField(
                        value = searchInfo.teacherName ?: "",
                        onValueChange = {
                            searchInfo = searchInfo.copy(teacherName = it)
                        },
                        label = "教师姓名"
                    )
                    SuperDropdown(
                        title = "开课单位",
                        items = uiState.departmentList.map { it.title },
                        selectedIndex = selectedDepartmentIndex.intValue,
                        onSelectedIndexChange = {
                            selectedDepartmentIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                departmentCode = uiState.departmentList[it].value
                            )
                        }
                    )
                }
            }
            item {
                Card {
                    SuperDropdown(
                        title = "校区",
                        items = uiState.campusList.map { it.title },
                        selectedIndex = selectedCampusIndex.intValue,
                        onSelectedIndexChange = {
                            selectedCampusIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                campusCode = uiState.campusList[it].value
                            )
                        }
                    )
                    SuperDropdown(
                        title = "教学楼",
                        items = uiState.buildingList.map { it.title },
                        selectedIndex = selectedBuildingIndex.intValue,
                        onSelectedIndexChange = {
                            selectedBuildingIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                buildingCode = uiState.buildingList[it].value
                            )
                        }
                    )
                    MiuixHintTextField(
                        value = searchInfo.teachingVenueName ?: "",
                        onValueChange = {
                            searchInfo = searchInfo.copy(teachingVenueName = it)
                        },
                        label = "教学场地"
                    )
                }
            }
            item {
                Card {
                    SuperDropdown(
                        title = "年级",
                        items = uiState.studentGradeList.map { it.title },
                        selectedIndex = selectedStudentGradeIndex.intValue,
                        onSelectedIndexChange = {
                            selectedStudentGradeIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                studentGrade = uiState.studentGradeList[it].title
                            )
                        }
                    )
                    SuperDropdown(
                        title = "院系",
                        items = uiState.studentDepartmentList.map { it.title },
                        selectedIndex = selectedStudentDepartmentIndex.intValue,
                        onSelectedIndexChange = {
                            selectedStudentDepartmentIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                studentDepartmentCode = uiState.studentDepartmentList[it].value
                            )
                        }
                    )
                    SuperDropdown(
                        title = "专业",
                        items = uiState.majorList.map { it.title },
                        selectedIndex = selectedMajorIndex.intValue,
                        onSelectedIndexChange = {
                            selectedMajorIndex.intValue = it
                            searchInfo = searchInfo.copy(
                                majorCode = uiState.majorList[it].value
                            )
                        }
                    )
                }
            }
        }
        BottomCircularProgressIndicator(isLoadingIndex.value)
    }
}
