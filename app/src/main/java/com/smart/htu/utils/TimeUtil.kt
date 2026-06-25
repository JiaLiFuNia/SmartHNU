package com.smart.htu.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TimeUtil {

    // 获取当前时间
    fun getCurrentTime(pattern: String = "HH:mm:ss"): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return currentTime.format(formatter)
    }

    fun convertLocalTimeToStringTime(time: LocalTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return time.format(formatter)
    }

    fun convertStringTimeToLocalTime(timeString: String, pattern: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalTime.parse(timeString, formatter)
    }

    fun timeFormatter(timeString: String, fromPattern: String, toPattern: String): String {
        return try {
            val time = convertStringTimeToLocalTime(timeString, fromPattern)
            val toFormatter = DateTimeFormatter.ofPattern(toPattern)
            time.format(toFormatter)
        } catch (_: Exception) {
            timeString
        }
    }
}