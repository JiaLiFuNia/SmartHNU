package com.smart.htu.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object GradeDivideUtil {

    @Composable
    fun divideGrade(grade: Double): Color {
        return when {
            grade >= 90 -> Color(0xFF43A047)
            grade >= 80 -> Color(0xFF1E88E5)
            grade >= 60 -> Color(0xFFFFA726)
            else -> Color(0xFFE53935)
        }
    }

}