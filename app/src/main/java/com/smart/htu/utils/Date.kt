package com.smart.htu.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// 时间戳转日期字符串
fun timeStamp2DateStr(timeStamp: Long): String {
    if (timeStamp == 0L) {
        return getCurrentDates()
    }
    val instant = Instant.ofEpochMilli(timeStamp)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())
    val formattedDateTime = formatter.format(instant)
    return formattedDateTime
}

// 获取当前日期
fun getCurrentDates(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = currentDate.format(formatter)
    return formattedDate
}