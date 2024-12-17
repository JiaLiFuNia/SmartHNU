package com.smart.htu.utils

import java.time.LocalDate
import java.time.LocalTime

fun checkTimeInterval(): Int {
    val currentTime = LocalTime.now()
    val currentDate = LocalDate.now()

    // 设置5月1日到10月1日的日期范围
    val startDate = LocalDate.of(currentDate.year, 5, 1)
    val endDate = LocalDate.of(currentDate.year, 10, 1)

    val dateCheckResult =
        if (currentDate.isAfter(startDate.minusDays(1)) && currentDate.isBefore(endDate.plusDays(1))) {
            1
        } else {
            0
        }

    val intervals = listOf(
        listOf(
            Pair(LocalTime.of(8, 0), LocalTime.of(9, 40)),  // 08:00 - 09:40
            Pair(LocalTime.of(10, 10), LocalTime.of(11, 50)), // 10:10 - 11:50
            Pair(LocalTime.of(14, 30), LocalTime.of(16, 10)),  // 14:30 - 16:10
            Pair(LocalTime.of(16, 40), LocalTime.of(18, 20)), // 16:40 - 18:20
            Pair(LocalTime.of(19, 30), LocalTime.of(21, 10)),  // 19:30 - 21:10
            Pair(LocalTime.of(9, 40), LocalTime.of(10, 10)),  // 09:40 - 10:10
            Pair(LocalTime.of(11, 50), LocalTime.of(14, 30)), // 11:50 - 14:30
            Pair(LocalTime.of(16, 10), LocalTime.of(16, 40)),  // 16:10 - 16:40
            Pair(LocalTime.of(18, 20), LocalTime.of(19, 30))   // 18:20 - 19:30
        ),
        listOf(
            Pair(LocalTime.of(8, 0), LocalTime.of(9, 40)),  // 08:00 - 09:40
            Pair(LocalTime.of(10, 10), LocalTime.of(11, 50)), // 10:10 - 11:50
            Pair(LocalTime.of(15, 0), LocalTime.of(16, 40)),  // 15:00 - 16:40
            Pair(LocalTime.of(17, 10), LocalTime.of(18, 50)), // 17:10 - 18:50
            Pair(LocalTime.of(20, 0), LocalTime.of(21, 40)),  // 20:00 - 21:40
            Pair(LocalTime.of(9, 40), LocalTime.of(10, 10)),  // 09:40 - 10:10
            Pair(LocalTime.of(11, 50), LocalTime.of(15, 0)), // 11:50 - 15:00
            Pair(LocalTime.of(16, 40), LocalTime.of(17, 10)),  // 16:40 - 17:10
            Pair(LocalTime.of(18, 50), LocalTime.of(20, 0))   // 18:50 - 20:00
        )
    )

    var timeIndex = intervals[dateCheckResult].indexOfFirst {
        currentTime.isAfter(it.first) && currentTime.isBefore(it.second)
    }
    if (timeIndex >= 5)
        timeIndex -= 4
    else
        if (timeIndex == -1)
            timeIndex = 0
    return timeIndex
}
