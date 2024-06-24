package com.xhand.hnu.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xhand.hnu.components.ClassroomEmptyListItem
import com.xhand.hnu.components.ClassroomListItem
import com.xhand.hnu.components.MessageDetailDialog
import com.xhand.hnu.components.MessageListItem
import com.xhand.hnu.components.TeacherListItem
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.viewmodel.SettingsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val haveClassroom = viewModel.haveClassRoom
    val allClassroom = viewModel.allClassRoom
    val buildingsSave = viewModel.buildingsSave
    LaunchedEffect(Unit) {
        // viewModel.buildingService()
        viewModel.classroomService()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(
                            MaterialTheme.colorScheme.surface.copy()
                        )
                ),
                title = {
                    Text(text = "教室查询")
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
        if (viewModel.isGettingRoom)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        else {
            val isShowSubList = remember {
                mutableStateListOf(
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            }
            val isShowSubSubList = remember {
                mutableStateListOf(
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            }
            val timeList = mapOf(
                "8:00-9:40" to "0102",
                "10:10-11:50" to "0304",
                "15:00-16:40" to "0506",
                "17:10-18:50" to "0708",
                "20:00-21:40" to "0910"
            )
            Column(
                modifier = Modifier
                    .padding(paddingValues = it)
                    .verticalScroll(scrollState)
            ) {
                // val buildings = buildingsList.filter { it.jzwmc in buildingsSave }
                Log.i("TAG654", buildingsSave.toString())
                Log.i("TAG654", haveClassroom.size.toString())
                Log.i("TAG654", allClassroom.toString())
                buildingsSave.forEachIndexed { index, building ->
                    val arrowRotateDegrees: Float by animateFloatAsState(if (isShowSubList[index]) 0f else -90f,
                        label = ""
                    )
                    ClassroomListItem(
                        text = building.jzwmc,
                        modifier = Modifier.clickable {
                            isShowSubList[index] = !isShowSubList[index]
                        },
                        IconModifier = Modifier
                            .rotate(arrowRotateDegrees)
                    ) {
                        if (isShowSubList[index]) {
                            Column(
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                val subArrowRotateDegrees: Float by animateFloatAsState(if (isShowSubSubList[index]) 0f else -90f,
                                    label = ""
                                )
                                timeList.keys.forEachIndexed { timeIndex, timeDivide ->
                                    ClassroomListItem(
                                        text = timeDivide,
                                        modifier = Modifier
                                            .clickable {
                                                isShowSubSubList[timeIndex] =
                                                    !isShowSubSubList[timeIndex]
                                            },
                                        IconModifier = Modifier
                                            .rotate(subArrowRotateDegrees)
                                    ) {
                                        if (isShowSubSubList[timeIndex])
                                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                                allClassroom.forEach { classroom ->
                                                    if (classroom.jzwdm == building.jzwdm)
                                                        ClassroomEmptyListItem(
                                                            text = classroom.jxcdmc,
                                                            modifier = Modifier.clickable { }
                                                        )
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun pp() {
    ClassroomScreen(onBack = { }, viewModel = SettingsViewModel())
}