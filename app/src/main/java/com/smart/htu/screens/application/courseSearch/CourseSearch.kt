package com.smart.htu.screens.application.courseSearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.api.module.CourseSearchPostEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.BottomCircularProgressIndicator
import com.smart.htu.component.DatePicker
import com.smart.htu.component.ITEM_HEIGHT
import com.smart.htu.component.MiuixHintTextField
import com.smart.htu.component.VISIBLE_COUNT
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.DateUtil.getCurrentDate
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.NumberPicker
import top.yukonga.miuix.kmp.basic.NumberPickerDefaults
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Clear
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.CheckboxPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDate

@Composable
fun CourseSearch(
    viewModel: CourseSearchViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    val isLoadingIndex = remember { derivedStateOf { mutableStateOf(uiState.isLoadingIndex) } }

    val dateOrWeek = rememberSaveable { mutableIntStateOf(0) }
    val selectedDate = rememberSaveable { mutableStateOf(LocalDate.now()) }
    val isDatePickerShow = remember { mutableStateOf(false) }
    val isWeekPickerShow = remember { mutableStateOf(false) }
    val isSessionPickerShow = remember { mutableStateOf(false) }
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
                date = if (dateOrWeek.intValue == 0) getCurrentDate() else null,
                week = if (dateOrWeek.intValue == 1) "" else null,
                dayOfWeek = if (dateOrWeek.intValue == 1) "" else null,
                termCode = uiState.currentTermCode
            )
        )
    }

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    title = stringResource(R.string.course_search),
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
                    actions = {
                        IconButton(
                            onClick = {
                                focusManager.clearFocus()
                                searchInfo = CourseSearchPostEntity(
                                    date = if (dateOrWeek.intValue == 0) getCurrentDate() else null,
                                    week = if (dateOrWeek.intValue == 1) "" else null,
                                    dayOfWeek = if (dateOrWeek.intValue == 1) "" else null,
                                    termCode = uiState.currentTermCode
                                )
                                selectedDepartmentIndex.intValue = 0
                                selectedCampusIndex.intValue = 0
                                selectedBuildingIndex.intValue = 0
                                selectedStudentGradeIndex.intValue = 0
                                selectedStudentDepartmentIndex.intValue = 0
                                selectedMajorIndex.intValue = 0
                            }
                        ) {
                            Icon(MiuixIcons.Clear, contentDescription = "clear")
                        }
                    }
                )
            }
        },
        bottomBar = {
            BlurredBar(
                backdrop = backdrop,
                blurEnabled = blurActive,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextButton(
                        text = "搜索",
                        onClick = {
                            scope.launch {
                                focusManager.clearFocus()
                                navigator.push(Route.CourseSearchRepo(searchInfo))
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
        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .overScrollVertical()
                    .imePadding(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        OverlayDropdownPreference(
                            title = "学年学期",
                            items = uiState.termList.map { it.termString },
                            selectedIndex = selectedTermIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
                                selectedTermIndex.intValue = it
                                searchInfo = searchInfo.copy(
                                    termCode = uiState.termList[it].termCode
                                )
                            }
                        )
                        Row {
                            CheckboxPreference(
                                title = "日期",
                                checked = dateOrWeek.intValue == 0,
                                onCheckedChange = {
                                    focusManager.clearFocus()
                                    searchInfo = searchInfo.copy(dayOfWeek = null)
                                    searchInfo = searchInfo.copy(week = null)
                                    dateOrWeek.intValue = 0
                                },
                                modifier = Modifier.weight(0.5f)
                            )
                            CheckboxPreference(
                                title = "周次星期",
                                checked = dateOrWeek.intValue == 1,
                                onCheckedChange = {
                                    focusManager.clearFocus()
                                    searchInfo = searchInfo.copy(date = null)
                                    dateOrWeek.intValue = 1
                                },
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                        if (dateOrWeek.intValue == 0) {
                            ArrowPreference(
                                title = "日期",
                                onClick = {
                                    focusManager.clearFocus()
                                    isDatePickerShow.value = true
                                },
                                endActions = {
                                    Text(
                                        text = convertLocalDateToStringDate(
                                            selectedDate.value,
                                            "YYYY年M月d日 E"
                                        ),
                                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                                        color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                    )
                                }
                            )
                        } else {
                            ArrowPreference(
                                title = "周次星期",
                                onClick = {
                                    focusManager.clearFocus()
                                    isWeekPickerShow.value = true
                                },
                                endActions = {
                                    Text(
                                        text = "第${searchInfo.week ?: "-"}周 星期${searchInfo.dayOfWeek ?: "-"}",
                                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                                        color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                    )
                                }
                            )
                        }
                        ArrowPreference(
                            title = "节次",
                            onClick = {
                                focusManager.clearFocus()
                                isSessionPickerShow.value = true
                            },
                            endActions = {
                                Text(
                                    text = searchInfo.sessionCode ?: "",
                                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantActions
                                )
                            }
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
                        OverlayDropdownPreference(
                            title = "开课单位",
                            items = uiState.departmentList.map { it.title },
                            selectedIndex = selectedDepartmentIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
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
                        OverlayDropdownPreference(
                            title = "校区",
                            items = uiState.campusList.map { it.title },
                            selectedIndex = selectedCampusIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
                                selectedCampusIndex.intValue = it
                                searchInfo = searchInfo.copy(
                                    campusCode = uiState.campusList[it].value
                                )
                            }
                        )
                        OverlayDropdownPreference(
                            title = "教学楼",
                            items = uiState.buildingList.map { it.title },
                            selectedIndex = selectedBuildingIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
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
                        OverlayDropdownPreference(
                            title = "年级",
                            items = uiState.studentGradeList.map { it.title },
                            selectedIndex = selectedStudentGradeIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
                                selectedStudentGradeIndex.intValue = it
                                searchInfo = searchInfo.copy(
                                    studentGrade = uiState.studentGradeList[it].title
                                )
                            }
                        )
                        OverlayDropdownPreference(
                            title = "院系",
                            items = uiState.studentDepartmentList.map { it.title },
                            selectedIndex = selectedStudentDepartmentIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
                                selectedStudentDepartmentIndex.intValue = it
                                searchInfo = searchInfo.copy(
                                    studentDepartmentCode = uiState.studentDepartmentList[it].value
                                )
                            }
                        )
                        OverlayDropdownPreference(
                            title = "专业",
                            items = uiState.majorList.map { it.title },
                            selectedIndex = selectedMajorIndex.intValue,
                            onSelectedIndexChange = {
                                focusManager.clearFocus()
                                selectedMajorIndex.intValue = it
                                searchInfo = searchInfo.copy(
                                    majorCode = uiState.majorList[it].value
                                )
                            }
                        )
                    }
                }
            }
            BottomCircularProgressIndicator(isLoadingIndex.value.value)

            DatePicker(
                date = selectedDate.value,
                showDatePicker = isDatePickerShow.value,
                onConfirmClick = {
                    selectedDate.value = it
                    searchInfo =
                        searchInfo.copy(date = convertLocalDateToStringDate(it, "yyyy-MM-dd"))
                    isDatePickerShow.value = false
                },
                onDismissRequest = { isDatePickerShow.value = false }
            )

            WeekPicker(
                week = searchInfo.week?.toIntOrNull() ?: 1,
                weekDay = searchInfo.dayOfWeek ?: "一",
                showWeekPicker = isWeekPickerShow.value,
                onConfirmClick = { week, weekDay ->
                    searchInfo = searchInfo.copy(week = week, dayOfWeek = weekDay)
                    isWeekPickerShow.value = false
                },
                onDismissRequest = { isWeekPickerShow.value = false }
            )

            SessionPicker(
                session = searchInfo.sessionCode ?: "",
                showSessionPicker = isSessionPickerShow.value,
                onConfirmClick = { session ->
                    searchInfo = searchInfo.copy(sessionCode = session)
                    isSessionPickerShow.value = false
                },
                onDismissRequest = { isSessionPickerShow.value = false }
            )
        }
    }

}

@Composable
fun WeekPicker(
    week: Int,
    weekDay: String,
    showWeekPicker: Boolean,
    onConfirmClick: (String, String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val weekDayList = mapOf(
        1 to "一",
        2 to "二",
        3 to "三",
        4 to "四",
        5 to "五",
        6 to "六",
        7 to "日"
    )
    val selectedWeekIndex = remember { mutableIntStateOf(week) }
    val selectedWeekDayIndex = remember {
        mutableIntStateOf(
            weekDayList.entries.firstOrNull { it.value == weekDay }?.key ?: 1
        )
    }

    OverlayDialog(
        show = showWeekPicker,
        title = "选择周次和星期",
        summary = "第 ${selectedWeekIndex.intValue} 周 星期${weekDayList[selectedWeekDayIndex.intValue]}",
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.weight(10 / 2f)) {
                    NumberPicker(
                        value = selectedWeekIndex.intValue,
                        onValueChange = { selectedWeekIndex.intValue = it },
                        range = 1..25,
                        label = { "$it" },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }

                Box(modifier = Modifier.weight(10 / 2f)) {
                    NumberPicker(
                        value = selectedWeekDayIndex.intValue,
                        onValueChange = { selectedWeekDayIndex.intValue = it },
                        range = 1..7,
                        label = { weekDayList[it] ?: "" },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "取消",
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确定",
                    onClick = {
                        onConfirmClick(
                            selectedWeekIndex.intValue.toString(),
                            weekDayList[selectedWeekDayIndex.intValue] ?: ""
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

@Composable
fun SessionPicker(
    session: String,
    showSessionPicker: Boolean,
    onConfirmClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selectedFirstSession = remember {
        mutableStateOf(
            if (session.length >= 2) session.substring(0, 2) else "0"
        )
    }
    val selectedLastSession = remember {
        mutableStateOf(
            if (session.length >= 4) session.substring(2, 4) else "1"
        )
    }
    OverlayDialog(
        show = showSessionPicker,
        title = "选择节次",
        summary =
            if (selectedFirstSession.value == "0") {
                "全部节次"
            } else {
                if (selectedFirstSession.value == selectedLastSession.value) {
                    "第 ${selectedFirstSession.value} 节"
                } else {
                    "第 ${selectedFirstSession.value}-${selectedLastSession.value} 节"
                }
            },
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.weight(10 / 2f)) {
                    NumberPicker(
                        value = selectedFirstSession.value.toIntOrNull() ?: 0,
                        onValueChange = {
                            selectedFirstSession.value = "$it"
                        },
                        range = 0..10,
                        label = { if (it == 0) "全部" else it.toString() },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }
                Text(text = "至")
                Box(modifier = Modifier.weight(10 / 2f)) {
                    NumberPicker(
                        value = selectedLastSession.value.toIntOrNull() ?: 1,
                        onValueChange = {
                            selectedLastSession.value = "$it"
                        },
                        range = 1..10,
                        label = { it.toString() },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true,
                        enabled = selectedFirstSession.value.toIntOrNull() != 0
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "取消",
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确定",
                    onClick = {
                        val first = selectedFirstSession.value.toIntOrNull() ?: 1
                        val last = selectedLastSession.value.toIntOrNull() ?: 1
                        if (first == 0) {
                            onConfirmClick("")
                            return@TextButton
                        }
                        var res = ""
                        for (i in minOf(first, last)..maxOf(first, last)) {
                            res += if (i < 10) "0$i" else "$i"
                        }
                        onConfirmClick(res)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}
