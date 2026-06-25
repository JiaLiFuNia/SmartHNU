package com.smart.htu.api.module

data class HolidayRes(
    val code: Int,
    val message: String,
    val data: HolidayEntity
)

// {'date': '2025-02-01', 'holiday': {'isLieu': False, 'holiday': '春节', 'message': ''}, 'isOffDay': True, 'name': '星期六', 'weekDay': 6}
data class HolidayEntity(
    val date: String,
    val holiday: HolidayData? = null,
    val isOffDay: Boolean,
    val name: String,
    val weekDay: Int,
)

data class HolidayData(
    val isLieu: Boolean,
    val holiday: String,
    val message: String,
)