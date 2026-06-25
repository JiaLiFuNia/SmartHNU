package com.smart.htu.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.CheckboxLocation
import top.yukonga.miuix.kmp.preference.CheckboxPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun HomeContentSettings(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()

    val tmp = remember { mutableIntStateOf(0) }

    val backdrop = rememberBlurBackdrop(uiState.blurEnabled)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                TopAppBar(
                    title = "主页内容",
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(
            WindowInsetsSides.Horizontal
        )
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
                item {
                    Card {
                        SwitchPreference(
                            title = "课程状态",
                            summary = "对于当前或下节课程的状态进行显著提示",
                            checked = uiState.homeCourseStateEnabled,
                            onCheckedChange = { viewModel.changeHomeCourseStateEnabled(it) }
                        )
                    }
                }
                item {
                    Card {
                        SwitchPreference(
                            title = "聚焦信息",
                            summary = "展示周次、电费、学时、待还书数目等信息",
                            checked = uiState.homeFocusEnabled,
                            onCheckedChange = { viewModel.changeHomeFocusEnabled(it) }
                        )
                        AnimatedVisibility(
                            visible = uiState.homeFocusEnabled,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CheckboxPreference(
                                    title = "周次",
                                    summary = "当前日期、周次、总周数等信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.WEEK.name]
                                        ?: true,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.WEEK.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.WEEK.name,
                                            it
                                        )
                                        tmp.intValue++
                                    }
                                )
                                CheckboxPreference(
                                    title = "天气",
                                    summary = "当前校园的天气信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.WEATHER.name]
                                        ?: true,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.WEATHER.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.WEATHER.name,
                                            it
                                        )
                                    }
                                )
                                CheckboxPreference(
                                    title = "电费",
                                    summary = "当前寝室空调电费余额信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.AIR_BOLT.name]
                                        ?: true,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.AIR_BOLT.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.AIR_BOLT.name,
                                            it
                                        )
                                    }
                                )
                                CheckboxPreference(
                                    title = "学时",
                                    summary = "当前总学时信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.STUDY_HOUR.name]
                                        ?: true,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.STUDY_HOUR.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.STUDY_HOUR.name,
                                            it
                                        )
                                    }
                                )
                                CheckboxPreference(
                                    title = "待还书数目",
                                    summary = "当前图书馆待还书数目信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.BOOK.name]
                                        ?: false,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.BOOK.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.BOOK.name,
                                            it
                                        )
                                    }
                                )
                                CheckboxPreference(
                                    title = "校园卡",
                                    summary = "当前校园卡余额信息",
                                    checkboxLocation = CheckboxLocation.End,
                                    checked = uiState.homeFocusItemStateMap[HomeFocusItem.SCHOOL_CARD.name]
                                        ?: false,
                                    enabled = !(uiState.homeFocusItemStateMap[HomeFocusItem.SCHOOL_CARD.name] == false && uiState.homeFocusItemStateMap.values.count { it } == 4),
                                    onCheckedChange = {
                                        viewModel.changeHomeFocusItemState(
                                            HomeFocusItem.SCHOOL_CARD.name,
                                            it
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    Card {
                        SwitchPreference(
                            title = "今日课程",
                            summary = "当天的所有课程和考试项目",
                            enabled = false,
                            checked = uiState.homeTodayCourseEnabled,
                            onCheckedChange = { viewModel.changeHomeTodayCourseEnabled(it) }
                        )
                        AnimatedVisibility(
                            visible = uiState.homeTodayCourseEnabled,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            SwitchPreference(
                                title = "仅显示未结束课程",
                                summary = "开启后仅显示当前时间未结束的课程",
                                checked = !uiState.homeShowAllTodayCourseEnabled,
                                onCheckedChange = { viewModel.changeHomeShowAllTodayCourseEnabled(!it) }
                            )
                        }
                    }
                }
                item {
                    Card {
                        SwitchPreference(
                            title = "今日任务",
                            summary = "通过手动创建的任务",
                            checked = uiState.homeTodayTaskEnabled,
                            enabled = tmp.intValue == 5,
                            onCheckedChange = { viewModel.changeHomeTodayTaskEnabled(it) }
                        )
                        AnimatedVisibility(
                            visible = uiState.homeTodayTaskEnabled,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            SwitchPreference(
                                title = "仅显示未结束任务",
                                summary = "开启后仅显示当前时间未结束的任务",
                                checked = !uiState.homeShowAllTodayTaskEnabled,
                                onCheckedChange = { viewModel.changeHomeShowAllTodayTaskEnabled(!it) }
                            )
                        }
                    }
                }
                item {
                    Card {
                        SwitchPreference(
                            title = "重要通知",
                            summary = "展示近两个月发布的重要通知",
                            checked = uiState.homeNewsEnabled,
                            onCheckedChange = { viewModel.changeHomeNewsEnabled(it) }
                        )
                    }
                }
            }
        }
    }
}