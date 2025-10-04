package com.smart.htu.screens.application.examSchedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.api.module.ExamEntity
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.InfoBadge
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import kotlinx.serialization.json.Json
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamSchedule(
    navController: NavController,
    viewModel: ExamScheduleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background,
                ),
                title = { Text(text = "考试安排") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("${Destinations.AddExamSchedule.route}/null")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "up"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = it.calculateTopPadding() + 8.dp,
                bottom = 12.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            overscrollEffect = null
        ) {
            if (uiState.examScheduleList.isEmpty()) {
                item {
                    EmptyContent(text = "暂无考试安排", image = emptyData())
                }
            }
            val currentDate = getCurrentDate("yyyy年MM月dd日 E")
            val groupedExams =
                uiState.examScheduleList.sortedBy { exam -> exam.startTime }.groupBy { exam ->
                    convertLocalDateToStringDate(
                        date = exam.date,
                        pattern = "yyyy年MM月dd日 E"
                    )
                }.toSortedMap { d1, d2 -> d1.compareTo(d2) }
            groupedExams.forEach { (date, exams) ->
                item {
                    SmallTitle(
                        text = if (date == currentDate) "今天 ($date)" else date,
                        insideMargin = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                items(items = exams) { exam ->
                    ExamScheduleCard(
                        exam = exam,
                        isInProgress = LocalDateTime.now().isBefore(
                            LocalDateTime.of(exam.date, exam.endTime) // 结束之前
                        ) && LocalDateTime.now().isAfter(
                            LocalDateTime.of(exam.date, exam.startTime) // 开始之后
                        ),
                        isPassed = LocalDateTime.now().isAfter(
                            LocalDateTime.of(exam.date, exam.endTime) // 结束之后
                        ),
                        onClick = {
                            navController.navigate(
                                route = "${Destinations.AddExamSchedule.route}/${
                                    Json.encodeToString(exam)
                                }"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ExamScheduleCard(
    exam: ExamEntity,
    isInProgress: Boolean,
    isPassed: Boolean,
    onClick: (ExamEntity) -> Unit = {},
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.defaultColors(
            color = if (isPassed) MiuixTheme.colorScheme.surface
            else MiuixTheme.colorScheme.surface
        ),
        pressFeedbackType = PressFeedbackType.Sink,
        onClick = { onClick(exam) }
    ) {
        val textColor = if (isPassed) MiuixTheme.colorScheme.disabledOnSecondaryVariant
        else MiuixTheme.colorScheme.onSurface
        val iconColor = if (isPassed) MiuixTheme.colorScheme.primary.copy(0.6f)
        else MiuixTheme.colorScheme.primary
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = exam.examName,
                    style = MiuixTheme.textStyles.title3.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                InfoBadge(exam.examType.type, Color(exam.examType.color))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.schedule_24px),
                    contentDescription = "time",
                    modifier = Modifier
                        .size(23.dp)
                        .padding(end = 4.dp),
                    tint = iconColor
                )
                Text(
                    text = "${
                        convertLocalTimeToStringTime(
                            time = exam.startTime,
                            pattern = "HH:mm"
                        )
                    } - ${
                        convertLocalTimeToStringTime(
                            time = exam.endTime,
                            pattern = "HH:mm"
                        )
                    }  时长：${exam.duration.toInt()} min",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4.copy(color = textColor),
                    textAlign = TextAlign.Start
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location_on_24px),
                        contentDescription = "building",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 4.dp),
                        tint = iconColor
                    )
                    Text(
                        text = exam.examRoom,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        style = MiuixTheme.textStyles.body1.copy(color = textColor),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chair_24px),
                        contentDescription = "time",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 4.dp),
                        tint = iconColor
                    )
                    Text(
                        text = "座号：${exam.seatNumber}",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        style = MiuixTheme.textStyles.body1.copy(color = textColor),
                    )
                }
            }
        }
    }
}