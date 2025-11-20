package com.smart.htu.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SpinnerDefaults
import top.yukonga.miuix.kmp.extra.SpinnerEntry
import top.yukonga.miuix.kmp.extra.SpinnerItemImpl
import top.yukonga.miuix.kmp.extra.SuperDialog

@Composable
fun SuperSpinnerDialog(
    items: List<SpinnerEntry>,
    selectedIndex: Int,
    title: String,
    dialogButtonString: String,
    modifier: Modifier,
    isDropdownExpanded: MutableState<Boolean>,
    hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback,
    onSelectedIndexChange: ((Int) -> Unit)?
) {
    SuperDialog(
        modifier = modifier,
        title = title,
        show = isDropdownExpanded,
        onDismissRequest = {
            isDropdownExpanded.value = false
        },
        insideMargin = DpSize(0.dp, 24.dp),
        content = {
            Layout(
                content = {
                    LazyColumn {
                        items(items.size) { index ->
                            SpinnerItemImpl(
                                entry = items[index],
                                entryCount = items.size,
                                isSelected = selectedIndex == index,
                                index = index,
                                dialogMode = true,
                                spinnerColors = SpinnerDefaults.spinnerColors()
                            ) { selectedIdx ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                                onSelectedIndexChange?.invoke(selectedIdx)
                                isDropdownExpanded.value = false
                            }
                        }
                    }
                    TextButton(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 12.dp, end = 24.dp)
                            .fillMaxWidth(),
                        text = dialogButtonString,
                        minHeight = 50.dp,
                        onClick = {
                            isDropdownExpanded.value = false
                        }
                    )
                }
            ) { measurables, constraints ->
                if (measurables.size != 2) {
                    layout(0, 0) { }
                } else {
                    val button = measurables[1].measure(constraints)
                    val lazyList = measurables[0].measure(
                        constraints.copy(
                            maxHeight = constraints.maxHeight - button.height
                        )
                    )
                    layout(constraints.maxWidth, lazyList.height + button.height) {
                        lazyList.place(0, 0)
                        button.place(0, lazyList.height)
                    }
                }
            }
        }
    )
}