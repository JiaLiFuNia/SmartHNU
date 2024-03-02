package com.xhand.hnu2.components

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class SelectionItem<T>(val label: String, val value: T)

@Composable
fun <T> DropdownListItem(
    value: T?,
    leadingImageVector: Int? = null,
    headlineText: String,
    selections: List<SelectionItem<T>>,
    onValueChanged: (index: Int, value: T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    BasicListItem(
        headlineText = headlineText,
        supportingText = selections.find { it.value == value }?.label ?: "",
        onClick = { expanded.value = true },
        leadingImageVector = leadingImageVector,
        trailingContent = {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                selections.forEachIndexed { index, selection ->
                    DropdownMenuItem(
                        modifier = Modifier.background(
                            if (selection.value == value) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
                        ),
                        text = { Text(selection.label) },
                        onClick = {
                            expanded.value = false
                            onValueChanged(index, selection.value)
                        }
                    )
                }
            }
        }
    )
}
