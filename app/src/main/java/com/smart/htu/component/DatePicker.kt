package com.smart.htu.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

val ITEM_HEIGHT = 56.dp
const val VISIBLE_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    date: LocalDate = LocalDate.now(),
    showDatePicker: MutableState<Boolean>,
    yearRange: IntRange = date.year - 1..date.year + 1,
    onConfirmClick: (LocalDate) -> Unit,
) {
    val startDate = remember(yearRange) { LocalDate.of(yearRange.first, 1, 1) }
    val endDate = remember(yearRange) { LocalDate.of(yearRange.last, 12, 31) }
    val dates = remember(yearRange) {
        buildList {
            var cursor = startDate
            while (!cursor.isAfter(endDate)) {
                add(cursor)
                cursor = cursor.plusDays(1)
            }
        }
    }
    val safeInitialDate = remember(date, dates) {
        dates.find { it == date } ?: dates.firstOrNull() ?: date
    }
    var currentSelection by remember { mutableStateOf(safeInitialDate) }

    SuperDialog(
        show = showDatePicker,
        title = "选择日期",
        summary = convertLocalDateToStringDate(currentSelection, "yyyy年MM月dd日 E"),
        onDismissRequest = {
            showDatePicker.value = false
        }
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
                val years = remember(yearRange) { yearRange.toList() }
                var selectedYear by remember {
                    mutableIntStateOf(
                        date.year.coerceIn(
                            yearRange.first,
                            yearRange.last
                        )
                    )
                }
                var selectedMonth by remember { mutableIntStateOf(date.monthValue) }
                var selectedDay by remember { mutableIntStateOf(date.dayOfMonth) }

                val currentMaxDay = remember(selectedYear, selectedMonth) {
                    LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth()
                }

                LaunchedEffect(currentMaxDay) {
                    if (selectedDay > currentMaxDay) {
                        selectedDay = currentMaxDay
                    }
                }

                currentSelection = LocalDate.of(selectedYear, selectedMonth, selectedDay)

                Box(modifier = Modifier.weight(10 / 3f)) {
                    WheelPicker(
                        items = years,
                        initialItem = selectedYear,
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { "$it 年" },
                        onSelectionChanged = { selectedYear = it }
                    )
                }

                Box(modifier = Modifier.weight(10 / 3f)) {
                    WheelPicker(
                        items = (1..12).toList(),
                        initialItem = selectedMonth,
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { "%02d 月".format(it) },
                        onSelectionChanged = { selectedMonth = it }
                    )
                }

                Box(modifier = Modifier.weight(10 / 3f)) {
                    WheelPicker(
                        items = (1..currentMaxDay).toList(),
                        initialItem = selectedDay.coerceIn(1, currentMaxDay),
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { "%02d 日".format(it) },
                        onSelectionChanged = { selectedDay = it }
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
                        showDatePicker.value = false
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确定",
                    onClick = {
                        onConfirmClick(currentSelection)
                        showDatePicker.value = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

@Composable
fun DateTimePicker(
    show: MutableState<Boolean>,
    initialTime: LocalDateTime = LocalDateTime.now(),
    title: String,
    onConfirm: (LocalDateTime) -> Unit
) {
    val startDate = LocalDate.now()
    val dates = remember {
        (0..365).map { startDate.plusDays(it.toLong()) }
    }
    val hours = remember { (0..23).toList() }
    val minutes = remember { (0..59).toList() }

    var selectedDate by remember { mutableStateOf(initialTime.toLocalDate()) }
    var selectedHour by remember { mutableIntStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }

    val titleFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日E HH:mm")

    // 当前选中的完整时间
    val currentSelection =
        LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, selectedMinute))


    SuperDialog(
        show = show,
        title = title,
        summary = currentSelection.format(titleFormatter)
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
                // 日期滚轮 (权重较大)
                Box(modifier = Modifier.weight(1.8f)) {
                    WheelPicker(
                        items = dates,
                        initialItem = selectedDate,
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { date ->
                            date.format(DateTimeFormatter.ofPattern("M月d日 E"))
                        },
                        onSelectionChanged = { selectedDate = it }
                    )
                }

                // 小时滚轮
                Box(modifier = Modifier.weight(1f)) {
                    WheelPicker(
                        items = hours,
                        initialItem = selectedHour,
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { "$it 时" },
                        onSelectionChanged = { selectedHour = it }
                    )
                }

                // 分钟滚轮
                Box(modifier = Modifier.weight(1f)) {
                    WheelPicker(
                        items = minutes,
                        initialItem = selectedMinute,
                        itemHeight = ITEM_HEIGHT,
                        visibleCount = VISIBLE_COUNT,
                        format = { "%02d 分".format(it) },
                        onSelectionChanged = { selectedMinute = it }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "取消",
                    onClick = {
                        show.value = false
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确定",
                    onClick = {
                        onConfirm(currentSelection)
                        show.value = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialItem: T,
    itemHeight: Dp,
    visibleCount: Int = 3,
    format: (T) -> String = { it.toString() },
    onSelectionChanged: (T) -> Unit
) {
    if (items.isEmpty()) return
    val initialIndex = items.indexOf(initialItem).coerceAtLeast(0)
    val middle = Int.MAX_VALUE / 2
    val startIndex = middle - (middle % items.size) + initialIndex
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val layoutInfo = listState.layoutInfo
            val centerOffset = layoutInfo.viewportEndOffset / 2
            val closestItem = layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - centerOffset)
            }
            closestItem?.let {
                val realIndex = ((it.index % items.size) + items.size) % items.size
                onSelectionChanged(items[realIndex])
            }
        }
    }

    Box(
        modifier = Modifier.height(itemHeight * visibleCount),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = items[((index % items.size) + items.size) % items.size]

                val scale by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                        if (itemInfo == null) return@derivedStateOf 0.85f
                        val centerOffset = layoutInfo.viewportEndOffset / 2f
                        val itemCenter = itemInfo.offset + itemInfo.size / 2f
                        val distance = abs(centerOffset - itemCenter)
                        val t = (distance / centerOffset).coerceIn(0f, 1f)
                        1.1f - 0.25f * t
                    }
                }

                val alpha by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                        if (itemInfo == null) return@derivedStateOf 0.4f
                        val centerOffset = layoutInfo.viewportEndOffset / 2f
                        val itemCenter = itemInfo.offset + itemInfo.size / 2f
                        val distance = abs(centerOffset - itemCenter)
                        val t = (distance / centerOffset).coerceIn(0f, 1f)
                        1f - 0.5f * t
                    }
                }

                val isSelected = scale >= 1.05f
                Text(
                    text = format(item),
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha.coerceIn(0.3f, 1f)
                        }
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    style = MiuixTheme.textStyles.title3.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MiuixTheme.colorScheme.primary else Color.Gray
                    )
                )
            }
        }
    }
}