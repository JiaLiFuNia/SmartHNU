package com.smart.htu.screens.application.physicalTest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.R
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.EmptyContent
import com.smart.htu.component.card.MessageCardDisplay
import com.smart.htu.component.card.SingleInfo
import com.smart.htu.component.imageVectors.emptyData
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.utils.PhysicalTestUtil
import com.smart.htu.utils.PhysicalTestUtil.YearLevel
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.FloatingActionButton
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun PhysicalTest(
    viewModel: PhysicalTestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    val scrollBehavior = MiuixScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val showAddSheet = remember { mutableStateOf(false) }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(R.string.physical_test),
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.push(Route.AddPhysicalTestScore()) }
            ) {
                Icon(
                    MiuixIcons.Add,
                    contentDescription = "添加",
                    tint = MiuixTheme.colorScheme.onPrimary
                )
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
                    .scrollEndHaptic(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.scoreList.isEmpty()) {
                    item {
                        EmptyContent(text = "无数据", image = emptyData())
                    }
                } else {
                    val semsterList = uiState.scoreList.keys.toList()
                    itemsIndexed(uiState.scoreList.values.toList()) { index, score ->
                        PhysicalTestScoreCard(
                            score = score,
                            index = index,
                            semster = semsterList[index],
                            yearLevel = when (semsterList[index] - uiState.grade) {
                                0, 1 -> YearLevel.FRESHMAN_SOPHOMORE
                                else -> YearLevel.JUNIOR_SENIOR
                            },
                            onDelete = {
                                viewModel.deletePhysicalTestScore(semsterList[index])
                            },
                            onModifier = {
                                navigator.push(Route.AddPhysicalTestScore(semsterList[index]))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhysicalTestScoreCard(
    semster: Int,
    score: PhysicalTestScore,
    index: Int,
    yearLevel: YearLevel,
    onModifier: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    val holdDownIndex = remember { mutableIntStateOf(0) }
    val showDetail = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    val result = remember(score, yearLevel) {
        PhysicalTestUtil.calculate(score, yearLevel = yearLevel)
    }
    val totalColor = if (result.total >= 60f) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicComponent(
            title = "$semster 学年",
            summary = "身高 ${score.height}cm · 体重 ${score.weight}kg",
            endActions = {
                Text(
                    text = result.total.formatScore(),
                    fontSize = MiuixTheme.textStyles.title3.fontSize,
                    color = totalColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            holdDownState = (showDetail.value || showDeleteDialog.value) && holdDownIndex.intValue == index,
            onClick = {
                showDetail.value = true
                holdDownIndex.intValue = index
            }
        )
    }

    PhysicalTestDetailDialog(
        semster = semster,
        score = score,
        result = result,
        show = showDetail.value,
        showDeleteDialog = showDeleteDialog,
        onDismiss = { showDetail.value = false },
        onModifier = { onModifier(semster) },
        onDelete = {
            onDelete(semster)
        }
    )
}

@Composable
private fun PhysicalTestDetailDialog(
    semster: Int,
    score: PhysicalTestScore,
    result: PhysicalTestUtil.ScoreResult,
    show: Boolean,
    showDeleteDialog: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onModifier: () -> Unit,
    onDelete: () -> Unit
) {
    OverlayBottomSheet(
        title = "$semster 学年体测详情",
        show = show,
        onDismissRequest = onDismiss,
        enableNestedScroll = false,
        backgroundColor = MiuixTheme.colorScheme.surface
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    labelOnTop = false,
                    message = listOf(
                        SingleInfo(
                            label = "总成绩",
                            content = "${result.total.formatScore()}（${result.level}）",
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = "BMI",
                            content = "${result.bmi.score}分（${result.bmi.level}）",
                            rowIndex = 1
                        )
                    )
                )
            }
            item {
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    labelOnTop = false,
                    message = listOf(
                        SingleInfo(
                            label = "肺活量",
                            content = "${score.vitalCapacity} ML",
                            endContent = { Text("${result.vitalCapacity.score}分") },
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = "50米跑",
                            content = "${score.fiftyMeterRun} 秒",
                            endContent = { Text("${result.fiftyMeterRun.score}分") },
                            rowIndex = 2
                        ),
                        SingleInfo(
                            label = "坐位体前屈",
                            content = "${score.sitAndReach} CM",
                            endContent = { Text("${result.sitAndReach.score}分") },
                            rowIndex = 3
                        )
                    )
                )
            }
            item {
                MessageCardDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    labelOnTop = false,
                    message = listOf(
                        SingleInfo(
                            label = "立定跳远",
                            content = "${score.standingLongJump} 米",
                            endContent = { Text("${result.standingLongJump.score}分") },
                            rowIndex = 1
                        ),
                        SingleInfo(
                            label = "耐力跑",
                            content = "${score.enduranceRun} 分",
                            endContent = { Text("${result.enduranceRun.score}分") },
                            rowIndex = 2
                        ),
                        SingleInfo(
                            label = "力量练习",
                            content = "${score.strengthExercise} 个",
                            endContent = { Text("${result.strengthExercise.score}分") },
                            rowIndex = 3
                        )
                    )
                )
            }
            item {
                Card {
                    BasicComponent(
                        title = "修改成绩", titleColor = BasicComponentDefaults.titleColor(
                            MiuixTheme.colorScheme.primary
                        ),
                        onClick = {
                            onDismiss()
                            onModifier()
                        }
                    )
                    BasicComponent(
                        title = "删除", titleColor = BasicComponentDefaults.titleColor(
                            MiuixTheme.colorScheme.error
                        ),
                        onClick = {
                            onDismiss()
                            showDeleteDialog.value = true
                        }
                    )
                }
            }
        }
    }
    DeleteDialog(
        show = showDeleteDialog.value,
        onDelete = {
            onDelete()
            showDeleteDialog.value = false
        },
        onDismiss = { showDeleteDialog.value = false }
    )
}

@Composable
fun DeleteDialog(
    show: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    OverlayDialog(show = show, title = "确定删除该学年成绩？") {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "删除",
                onClick = {
                    onDelete()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(textColor = MiuixTheme.colorScheme.error)
            )
        }
    }
}

private fun Float.formatScore(): String {
    return if (this % 1f == 0f) this.toInt().toString() else "%.1f".format(this)
}
