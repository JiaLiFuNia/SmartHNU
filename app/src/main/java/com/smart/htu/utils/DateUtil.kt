package com.smart.htu.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtil {

    // 获取当前日期
    fun getCurrentDate(pattern: String = "yyyy-MM-dd"): String {
        val currentDate = LocalDate.now()
        val date = convertLocalDateToStringDate(currentDate, pattern)
        return date
    }

    fun convertLocalDateToStringDate(date: LocalDate, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }

    fun convertStringDateToLocalDate(dateString: String, pattern: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val date = LocalDate.parse(dateString, formatter)
        return date
    }

    fun dateFormatter(dateString: String, fromPattern: String, toPattern: String): String {
        return try {
            val date = convertStringDateToLocalDate(dateString, fromPattern)
            val toFormatter = DateTimeFormatter.ofPattern(toPattern)
            date.format(toFormatter)
        } catch (_: Exception) {
            dateString
        }
    }

    /**
     * 从字符串中提取日期
     * @param dateString 可能包含日期的字符串
     * @return 提取的日期字符串，格式为 "yyyy-MM-dd"；如果无法提取则返回原始字符串
     */
    fun extractDateFromString(dateString: String): String? {
        val dateRegex = """(\d{4}-\d{2}-\d{2})""".toRegex()
        val matchResult = dateRegex.find(dateString)
        return matchResult?.value
    }

    /**
     * 将日期转换为更友好的显示格式
     * - 最近三天：今天、昨天、前天
     * - 3-5天内：n天前
     * - 今年内：MM月DD日
     * - 其他年份：YYYY年MM月DD日
     *
     * @param dateString 日期字符串，格式为 "yyyy-MM-dd"
     * @return 格式化后的日期字符串
     */
    fun convertToFriendlyDate(dateString: String): String {
        try {
            val date = convertStringDateToLocalDate(dateString, "yyyy-MM-dd")
            val today = LocalDate.now()
            val daysDiff = ChronoUnit.DAYS.between(date, today)
            return when {
                daysDiff == 0L -> "今天"
                daysDiff == 1L -> "昨天"
                daysDiff == 2L -> "前天"
                daysDiff in 3L..5L -> "${daysDiff}天前"
                date.year == today.year -> "${date.monthValue}月${date.dayOfMonth}日"
                else -> "${date.year}年${date.monthValue}月${date.dayOfMonth}日"
            }
        } catch (_: Exception) {
            return dateString
        }
    }

    /**
     * 将日期转换为更友好的显示格式
     * @param dateString 可能包含日期的字符串
     * @return 格式化后的日期字符串
     */
    fun formatDateToFriendly(dateString: String): String {
        val extractedDate = extractDateFromString(dateString)
        return convertToFriendlyDate(extractedDate.toString())
    }

}