package com.smart.htu.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentColors
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SliderColors
import top.yukonga.miuix.kmp.basic.SliderDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SuperSlider(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    endText: String? = null,
    startAction: @Composable (() -> Unit)? = null,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    interactionSource: MutableInteractionSource? = null,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    reverseDirection: Boolean = false,
    height: Dp = SliderDefaults.MinHeight,
    colors: SliderColors = SliderDefaults.sliderColors(),
    hapticEffect: SliderDefaults.SliderHapticEffect = SliderDefaults.DefaultHapticEffect,
    showKeyPoints: Boolean = false,
    keyPoints: List<Float>? = null,
    magnetThreshold: Float = 0.02f,
    isShowValueDialog: Boolean = false,
) {
    val showValueDialog = remember { mutableStateOf(false) }
    BasicComponent(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        startAction = startAction,
        endActions = {
            if (endText != null) {
                Text(
                    text = endText,
                    color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp),
                )
            }
            if (isShowValueDialog) {
                Icon(
                    modifier = Modifier
                        .size(width = 10.dp, height = 16.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = MiuixIcons.Basic.ArrowRight,
                    contentDescription = null,
                    tint = if (enabled) MiuixTheme.colorScheme.onSurfaceVariantActions
                    else MiuixTheme.colorScheme.disabledOnSecondaryVariant,
                )
            }
        },
        insideMargin = insideMargin,
        onClick = {
            if (isShowValueDialog) showValueDialog.value = true
        },
        holdDownState = showValueDialog.value,
        interactionSource = interactionSource,
        enabled = enabled,
        bottomAction = {
            Slider(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = onValueChangeFinished,
                reverseDirection = reverseDirection,
                height = height,
                colors = colors,
                hapticEffect = hapticEffect,
                showKeyPoints = showKeyPoints,
                keyPoints = keyPoints,
                magnetThreshold = magnetThreshold,
            )
        }
    )
    SliderDialog(
        title = title,
        showDialog = showValueDialog,
        valueState = { value },
        onValueChange = { onValueChange(it) },
        valueRange = valueRange
    )
}

@Composable
fun SliderDialog(
    showDialog: MutableState<Boolean>,
    valueState: () -> Float,
    onValueChange: (Float) -> Unit,
    title: String? = null,
    summary: String? = null,
    valueRange: ClosedFloatingPointRange<Float>,
) {
    val isPercentageMode = valueRange.start == 0f && valueRange.endInclusive == 1f

    SuperDialog(
        title = title,
        summary = summary,
        show = showDialog,
        onDismissRequest = { showDialog.value = false },
    ) {
        var text by remember { mutableStateOf("") }

        LaunchedEffect(showDialog.value) {
            if (showDialog.value) {
                val currentVal = valueState()
                text = if (isPercentageMode) {
                    (currentVal * 100).toInt().toString()
                } else {
                    if (currentVal % 1.0f == 0f) currentVal.toInt()
                        .toString() else currentVal.toString()
                }
            }
        }

        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = text,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    if (newValue.length <= 8) {
                        text = newValue
                    }
                }
            },
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                text = "取消",
                onClick = { showDialog.value = false },
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(20.dp))
            TextButton(
                text = "确定",
                onClick = {
                    val parsed = text.toFloatOrNull()
                    if (parsed != null) {
                        val finalValue = if (isPercentageMode) parsed / 100f else parsed
                        val clamped = finalValue.coerceIn(valueRange)
                        onValueChange(clamped)
                    }
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary(),
            )
        }
    }
}