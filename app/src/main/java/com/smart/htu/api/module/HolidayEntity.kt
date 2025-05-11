package com.smart.htu.api.module

data class HolidayEntity(
    val code: Int,
    val message: String,
    val data: HolidayData
)

// {'date': '2025-02-01', 'holiday': {'isLieu': False, 'holiday': '春节', 'message': ''}, 'isOffDay': True, 'name': '星期六', 'weekDay': 6}
data class HolidayData(
    val date: String,
    val holiday: Holiday? = null,
    val isOffDay: Boolean,
    val name: String,
    val weekDay: Int,
)

data class Holiday(
    val isLieu: Boolean,
    val holiday: String,
    val message: String,
)