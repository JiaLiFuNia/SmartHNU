package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.smart.htu.utils.DateUtil.convertLocalDateToStringDate
import com.smart.htu.utils.TimeUtil.convertLocalTimeToStringTime
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.CjkSuffixConfig
import dev.darkokoa.datetimewheelpicker.core.format.MonthDisplayStyle
import dev.darkokoa.datetimewheelpicker.core.format.dateFormatter
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DatePickerDialog(
    date: LocalDate = LocalDate.now(),
    showDatePicker: MutableState<Boolean>,
    onConfirmClick: (LocalDate) -> Unit,
) {
    val selectedDate = remember { mutableStateOf(date) }
    SuperDialog(
        show = showDatePicker,
        title = "选择日期",
        summary = convertLocalDateToStringDate(selectedDate.value, "yyyy年MM月dd日 E"),
        onDismissRequest = {
            showDatePicker.value = false
        }
    ) {
        Column {
            WheelDatePicker(
                startDate = kotlinx.datetime.LocalDate(date.year, date.monthValue, date.dayOfMonth),
                yearsRange = date.year - 1..date.year + 1,
                dateFormatter = dateFormatter(
                    locale = Locale.current,
                    monthDisplayStyle = MonthDisplayStyle.NUMERIC,
                    cjkSuffixConfig = CjkSuffixConfig.HideAll
                ),
                size = DpSize(height = 180.dp, width = 500.dp),
                rowCount = 3,
                textStyle = MiuixTheme.textStyles.title2,
                textColor = MiuixTheme.colorScheme.primary,
                selectorProperties = WheelPickerDefaults.selectorProperties(false),
                modifier = Modifier.fillMaxWidth(),
                onSnappedDate = {
                    selectedDate.value = it.toJavaLocalDate()
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                WheelPicker(
                    items = (2024..2026).toList(),
                    selectedItem = selectedDate.value.year - 1,
                    onItemSelected = { year ->
                        val month = selectedDate.value.monthValue
                        val day = selectedDate.value.dayOfMonth
                        val maxDay = LocalDate.of(year, month, 1).lengthOfMonth()
                        selectedDate.value = LocalDate.of(
                            year,
                            month,
                            day.coerceAtMost(maxDay)
                        )
                    },
                    displayText = { it.toString() },
                    modifier = Modifier.weight(1f)
                )
                WheelPicker(
                    items = (1..12).toList(),
                    selectedItem = selectedDate.value.monthValue - 1,
                    onItemSelected = { month ->
                        val year = selectedDate.value.year
                        val day = selectedDate.value.dayOfMonth
                        val maxDay = LocalDate.of(year, month, 1).lengthOfMonth()
                        selectedDate.value = LocalDate.of(
                            year,
                            month,
                            day.coerceAtMost(maxDay)
                        )
                    },
                    displayText = { it.toString() },
                    modifier = Modifier.weight(1f)
                )
                WheelPicker(
                    items = (1..selectedDate.value.lengthOfMonth()).toList(),
                    selectedItem = selectedDate.value.dayOfMonth - 1,
                    onItemSelected = { day ->
                        val year = selectedDate.value.year
                        val month = selectedDate.value.monthValue
                        selectedDate.value = LocalDate.of(year, month, day)
                    },
                    displayText = { it.toString() },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                text = "确定",
                onClick = {
                    showDatePicker.value = false
                    onConfirmClick(selectedDate.value)
                },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    time: LocalTime = LocalTime.now(),
    showDatePicker: MutableState<Boolean>,
    onConfirmClick: (LocalTime) -> Unit,
) {
    val selectedTime = remember { mutableStateOf(time) }
    SuperDialog(
        show = showDatePicker,
        title = "选择时间",
        summary = convertLocalTimeToStringTime(selectedTime.value, "HH:mm"),
        onDismissRequest = {
            showDatePicker.value = false
        }
    ) {
        Column {
            WheelTimePicker(
                startTime = kotlinx.datetime.LocalTime(time.hour, time.minute, time.second),
                size = DpSize(height = 180.dp, width = 500.dp),
                rowCount = 3,
                textStyle = MiuixTheme.textStyles.title2,
                textColor = MiuixTheme.colorScheme.primary,
                selectorProperties = WheelPickerDefaults.selectorProperties(false),
                modifier = Modifier.fillMaxWidth(),
                onSnappedTime = {
                    selectedTime.value = it.toJavaLocalTime()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                text = "确定",
                onClick = {
                    showDatePicker.value = false
                    onConfirmClick(selectedTime.value)
                },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun <T> WheelPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    displayText: (T) -> String,
    modifier: Modifier = Modifier
) {
}

