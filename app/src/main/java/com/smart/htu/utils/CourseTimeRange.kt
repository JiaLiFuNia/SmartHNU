package com.smart.htu.utils

import java.time.LocalDate
import java.time.LocalTime

object CourseTimeRange {

    val courseTimeRange = listOf(
        listOf(
            Pair(LocalTime.of(8, 0), LocalTime.of(8, 45)),  // 08:00 - 08:45
            Pair(LocalTime.of(8, 55), LocalTime.of(9, 40)), // 8:55 - 09:40
            Pair(LocalTime.of(10, 10), LocalTime.of(10, 55)), // 10:10 - 10:55
            Pair(LocalTime.of(11, 5), LocalTime.of(11, 50)), // 11:05 - 11:50
            Pair(LocalTime.of(14, 30), LocalTime.of(15, 15)), // 14:30 - 15:15
            Pair(LocalTime.of(15, 25), LocalTime.of(16, 10)), // 15:25 - 16:10
            Pair(LocalTime.of(16, 40), LocalTime.of(17, 25)), // 16:40 - 17:25
            Pair(LocalTime.of(17, 35), LocalTime.of(18, 20)), // 17:35 - 18:20
            Pair(LocalTime.of(19, 30), LocalTime.of(20, 15)), // 19:30 - 20:15
            Pair(LocalTime.of(20, 25), LocalTime.of(21, 10)) // 20:25 - 21:10
        ),
        listOf(
            Pair(LocalTime.of(8, 0), LocalTime.of(8, 45)),  // 08:00 - 08:45
            Pair(LocalTime.of(8, 55), LocalTime.of(9, 40)), // 8:55 - 09:40
            Pair(LocalTime.of(10, 10), LocalTime.of(10, 55)), // 10:10 - 10:55
            Pair(LocalTime.of(11, 5), LocalTime.of(11, 50)), // 11:05 - 11:50
            Pair(LocalTime.of(15, 0), LocalTime.of(15, 45)), // 15:00 - 15:45
            Pair(LocalTime.of(15, 55), LocalTime.of(16, 40)), // 15:55 - 16:40
            Pair(LocalTime.of(17, 10), LocalTime.of(17, 55)), // 17:10 - 17:55
            Pair(LocalTime.of(18, 5), LocalTime.of(18, 50)), // 18:05 - 18:50
            Pair(LocalTime.of(20, 0), LocalTime.of(20, 45)), // 20:00 - 20:45
            Pair(LocalTime.of(20, 55), LocalTime.of(21, 40)) // 20:55 - 21:40
        )
    )

    private val currentTime = LocalTime.now()
    private val currentDate = LocalDate.now()

    fun summerOrWinterTimeInterval(): List<Pair<LocalTime, LocalTime>> {
        val startDate = LocalDate.of(currentDate.year, 5, 1)
        val endDate = LocalDate.of(currentDate.year, 10, 1)

        return if (currentDate.isAfter(startDate.minusDays(1)) && currentDate.isBefore(
                endDate.plusDays(1)
            )
        ) {
            courseTimeRange[1] // 秋季作息
        } else {
            courseTimeRange[0] // 春季作息
        }
    }

    fun checkTimeInterval(isDetailed: Boolean = false): Int {
        val result = summerOrWinterTimeInterval()

        var timeIndex = result.indexOfFirst { (start, end) ->
            currentTime.isAfter(start.minusMinutes(1)) && currentTime.isBefore(end.plusMinutes(1))
        }
        if (timeIndex == -1) {
            if (currentTime.isBefore(result.first().first)) {
                return 1
            }
            if (currentTime.isAfter(result.last().second)) {
                return 1
            }
            for (i in 0 until result.size - 1) {
                val currentEnd = result[i].second
                val nextStart = result[i + 1].first

                if (currentTime.isAfter(currentEnd) && currentTime.isBefore(nextStart)) {
                    timeIndex = i + 1
                    break
                }
            }
        }
        if (timeIndex >= 0 && !isDetailed) {
            timeIndex = timeIndex / 2
        }
        return timeIndex
    }

}