package com.smart.htu.screens.application.physicalTest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.MiuixHintTextField
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.utils.ToastUtil.showSnackbar
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate

@Composable
fun AddPhysicalTestScore(
    grade: Int? = null,
    viewModel: PhysicalTestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    val scrollBehavior = MiuixScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val year = LocalDate.now().year
    val selectedSemsterIndex = remember { mutableIntStateOf(0) }
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val vitalCapacity = remember { mutableStateOf("") }
    val fiftyMeterRun = remember { mutableStateOf("") }
    val sitAndReach = remember { mutableStateOf("") }
    val standingLongJump = remember { mutableStateOf("") }
    val enduranceRun = remember { mutableStateOf("") }
    val strengthExercise = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (grade != null) {
            val score = uiState.scoreList[grade]
            height.value = score?.height.toString()
            weight.value = score?.weight.toString()
            vitalCapacity.value = score?.vitalCapacity.toString()
            fiftyMeterRun.value = score?.fiftyMeterRun.toString()
            sitAndReach.value = score?.sitAndReach.toString()
            standingLongJump.value = score?.standingLongJump.toString()
            enduranceRun.value = score?.enduranceRun.toString()
            strengthExercise.value = score?.strengthExercise.toString()
        }
    }

    val backdrop = rememberBlurBackdrop(true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = if (grade == null) "添加成绩" else "修改成绩",
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            }
                        ) {
                            Icon(
                                imageVector = if (grade == null) MiuixIcons.Regular.Back else MiuixIcons.Close,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (height.value.isEmpty() ||
                                        weight.value.isEmpty() ||
                                        vitalCapacity.value.isEmpty() ||
                                        fiftyMeterRun.value.isEmpty() ||
                                        sitAndReach.value.isEmpty() ||
                                        standingLongJump.value.isEmpty() ||
                                        enduranceRun.value.isEmpty() ||
                                        strengthExercise.value.isEmpty()
                                    ) {
                                        showSnackbar(snackBarHostState, "各项内容均不能为空")
                                        return@launch
                                    } else {
                                        val score = PhysicalTestScore(
                                            height = height.value.toIntOrNull() ?: 0,
                                            weight = weight.value.toFloatOrNull() ?: 0f,
                                            vitalCapacity = vitalCapacity.value.toIntOrNull() ?: 0,
                                            fiftyMeterRun = fiftyMeterRun.value.toFloatOrNull()
                                                ?: 0f,
                                            sitAndReach = sitAndReach.value.toFloatOrNull() ?: 0f,
                                            standingLongJump = standingLongJump.value.toFloatOrNull()
                                                ?: 0f,
                                            enduranceRun = enduranceRun.value.toFloatOrNull() ?: 0f,
                                            strengthExercise = strengthExercise.value.toIntOrNull()
                                                ?: 0
                                        )
                                        viewModel.addPhysicalTestScore(
                                            grade = year - selectedSemsterIndex.intValue,
                                            score = score
                                        )
                                        navigator.pop()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Ok,
                                contentDescription = "ok"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
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
                    .scrollEndHaptic()
                    .imePadding(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding() + 12.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        OverlayDropdownPreference(
                            title = "选择年份",
                            items = (year - 3..year).sortedByDescending { it }.map { it.toString() }
                                .toList(),
                            selectedIndex = selectedSemsterIndex.intValue,
                            onSelectedIndexChange = {
                                selectedSemsterIndex.intValue = it
                            }
                        )
                    }
                }
                item {
                    Card {
                        MiuixHintTextField(
                            label = "身高 (CM)",
                            value = height.value,
                            onValueChange = { height.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiuixHintTextField(
                            label = "体重 (KG)",
                            value = weight.value,
                            onValueChange = { weight.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    Card {
                        MiuixHintTextField(
                            label = "肺活量 (ML)",
                            value = vitalCapacity.value,
                            onValueChange = { vitalCapacity.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiuixHintTextField(
                            label = "50米跑 (秒)",
                            value = fiftyMeterRun.value,
                            onValueChange = { fiftyMeterRun.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiuixHintTextField(
                            label = "坐位体前屈 (CM)",
                            value = sitAndReach.value,
                            onValueChange = { sitAndReach.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiuixHintTextField(
                            label = "立定跳远 (米)",
                            value = standingLongJump.value,
                            onValueChange = { standingLongJump.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    Card {
                        MiuixHintTextField(
                            label = "耐力跑 (分)",
                            value = enduranceRun.value,
                            onValueChange = { enduranceRun.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiuixHintTextField(
                            label = "力量练习 (个)",
                            value = strengthExercise.value,
                            onValueChange = { strengthExercise.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}