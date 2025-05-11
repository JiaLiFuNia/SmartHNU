package com.smart.htu.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

object CourseColorUtil {

    val courseColors = listOf(
        Color(0xFFE1F5FE), // 浅蓝
        Color(0xFFE8F5E9), // 浅绿
        Color(0xFFFFF3E0), // 浅橙
        Color(0xFFF3E5F5), // 浅紫
        Color(0xFFE0F7FA), // 青色
        Color(0xFFFCE4EC), // 粉红
        Color(0xFFE8EAF6), // 靛蓝
        Color(0xFFF1F8E9), // 浅黄绿
        Color(0xFFEFEBE9), // 棕色
        Color(0xFFE0F2F1), // 蓝绿
        Color(0xFFEDE7F6), // 深紫
        Color(0xFFE3F2FD), // 深蓝
        Color(0xFFE0F2F1), // 青绿
        Color(0xFFF9FBE7), // 青黄
        Color(0xFFFFF8E1), // 琥珀
        Color(0xFFFFEBEE)  // 红粉
    )

    private val courseColorMap = mutableMapOf<String, Color>()

    private val colorUsageCount = mutableMapOf<Color, Int>()

    fun getColorByCourseName(courseName: String): Color {
        courseColorMap[courseName]?.let { return it }

        val selectedColor = colorUsageCount.entries
            .minByOrNull { it.value }?.key
            ?: courseColors[abs(courseName.hashCode()) % courseColors.size]

        courseColorMap[courseName] = selectedColor
        colorUsageCount[selectedColor] = (colorUsageCount[selectedColor] ?: 0) + 1

        return selectedColor
    }

    init {
        courseColors.forEach { colorUsageCount[it] = 0 }
    }

}