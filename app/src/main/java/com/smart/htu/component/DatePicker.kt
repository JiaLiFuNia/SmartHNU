package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.NumberPicker
import top.yukonga.miuix.kmp.basic.NumberPickerDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val VISIBLE_COUNT = 3
val ITEM_HEIGHT = 52.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    date: LocalDate = LocalDate.now(),
    showDatePicker: Boolean,
    yearRange: IntRange = date.year - 1..date.year + 1,
    onConfirmClick: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
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

    OverlayDialog(
        show = showDatePicker,
        title = "选择日期",
        summary = convertLocalDateToStringDate(currentSelection, "yyyy年MM月dd日 E"),
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
                val years = remember(yearRange) { yearRange.toList() }
                var selectedYear by remember {
                    mutableIntStateOf(date.year)
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
                    NumberPicker(
                        value = selectedYear,
                        onValueChange = { selectedYear = it },
                        range = yearRange,
                        label = { "$it 年" },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }

                Box(modifier = Modifier.weight(10 / 3f)) {
                    NumberPicker(
                        value = selectedMonth,
                        onValueChange = { selectedMonth = it },
                        range = 1..12,
                        label = { "%02d 月".format(it) },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }

                Box(modifier = Modifier.weight(10 / 3f)) {
                    NumberPicker(
                        value = selectedDay,
                        onValueChange = { selectedDay = it },
                        range = 1..currentMaxDay,
                        label = { "%02d 日".format(it) },
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
                        onConfirmClick(currentSelection)
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
    show: Boolean,
    initialTime: LocalDateTime = LocalDateTime.now(),
    title: String,
    onConfirm: (LocalDateTime) -> Unit,
    onDismissRequest: () -> Unit
) {
    val startDate = LocalDate.now()
    val dates = remember {
        (0..365).map { startDate.plusDays(it.toLong()) }
    }

    var selectedDate by remember { mutableStateOf(initialTime.toLocalDate()) }
    val selectedDateIndex = remember(selectedDate, dates) {
        dates.indexOfFirst { it == selectedDate }.takeIf { it >= 0 } ?: 0
    }
    var selectedHour by remember { mutableIntStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }

    val titleFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日E HH:mm")

    // 当前选中的完整时间
    val currentSelection =
        LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, selectedMinute))


    OverlayDialog(
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
                    NumberPicker(
                        value = selectedDateIndex,
                        onValueChange = { selectedDate = dates[it] },
                        range = 0 until dates.size,
                        label = {
                            dates[it].format(DateTimeFormatter.ofPattern("M月d日 E"))
                        },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }

                // 小时滚轮
                Box(modifier = Modifier.weight(1f)) {
                    NumberPicker(
                        value = selectedHour,
                        onValueChange = { selectedHour = it },
                        range = 0..23,
                        label = { "$it 时" },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
                    )
                }

                // 分钟滚轮
                Box(modifier = Modifier.weight(1f)) {
                    NumberPicker(
                        value = selectedMinute,
                        onValueChange = { selectedMinute = it },
                        range = 0..59,
                        label = { "%02d 分".format(it) },
                        visibleItemCount = VISIBLE_COUNT,
                        modifier = Modifier.fillMaxWidth(),
                        colors = NumberPickerDefaults.colors(selectedTextColor = MiuixTheme.colorScheme.primary),
                        textStyle = MiuixTheme.textStyles.title2.copy(fontWeight = FontWeight.Medium),
                        itemHeight = ITEM_HEIGHT,
                        wrapAround = true
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
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "确定",
                    onClick = {
                        onConfirm(currentSelection)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}