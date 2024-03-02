package com.xhand.hnu2.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable

@Composable
fun SwitchListItem(
    value: Boolean,
    leadingImageVector: Int,
    headlineText: String,
    supportingText: String? = null,
    onValueChanged: (value: Boolean) -> Unit
) {
    BasicListItem(
        leadingImageVector = leadingImageVector,
        headlineText = headlineText,
        supportingText = supportingText,
        trailingContent = {
            Switch(
                checked = value,
                onCheckedChange = {
                    onValueChanged(it)
                },
            )
        }
    ) {
        onValueChanged(!value)
    }
}
