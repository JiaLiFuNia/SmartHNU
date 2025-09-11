package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent
import network.chaintech.kmp_date_time_picker.utils.MAX
import network.chaintech.kmp_date_time_picker.utils.MIN
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults.selectorProperties
import network.chaintech.kmp_date_time_picker.utils.now
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun DatePickerDialog(
    showDatePicker: MutableState<Boolean>,
    onConfirmClick: (LocalDate) -> Unit,
) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    SuperDialog(
        show = showDatePicker,
        title = "选择日期",
        summary = "${selectedDate.value} ${selectedDate.value.dayOfWeek}",
        onDismissRequest = {
            showDatePicker.value = false
        }
    ) {
        Column {
            WheelDatePickerComponent.WheelDatePicker(
                modifier = Modifier,
                titleStyle = LocalTextStyle.current,
                doneLabelStyle = LocalTextStyle.current,
                startDate = LocalDate.now(),
                minDate = LocalDate.MIN(),
                maxDate = LocalDate.MAX(),
                yearsRange = IntRange(LocalDate.now().year - 1, LocalDate.now().year + 3),
                rowCount = 3,
                showShortMonths = false,
                showMonthAsNumber = true,
                selectedDateTextStyle = MiuixTheme.textStyles.title2.copy(MiuixTheme.colorScheme.primary),
                height = 150.dp,
                hideHeader = true,
                customMonthNames = (1..12).map { it.toString() + "月" },
                selectorProperties = selectorProperties(),
                onDoneClick = {
                    selectedDate.value = it
                },
                onDateChangeListener = {
                    selectedDate.value = it
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                text = "确定",
                onClick = {
                    onConfirmClick(selectedDate.value)
                    showDatePicker.value = false
                },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}