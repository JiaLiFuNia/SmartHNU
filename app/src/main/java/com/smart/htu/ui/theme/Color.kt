package com.smart.htu.ui.theme

import androidx.compose.ui.graphics.Color

val KeyColors: List<Pair<String, Color>> = listOf(
    "Blue" to Color(0xFF3482FF),
    "Green" to Color(0xFF36D167),
    "Purple" to Color(0xFF7C4DFF),
    "Yellow" to Color(0xFFFFB21D),
    "Orange" to Color(0xFFFF5722),
    "Pink" to Color(0xFFE91E63),
    "Teal" to Color(0xFF00BCD4)
)

fun keyColorFor(index: Int): Color? =
    if (index <= 0) null else KeyColors.getOrNull(index - 1)?.second