package com.xhand.hnu.model.entity

data class HolidayEntity(
    val code: Int,
    val type: Type,
    val holiday: Holiday,
)

data class Type(
    val type: Int,
    val name: String,
    val week: Int,
)

data class Holiday(
    val holiday: String,
    val name: String,
    val wage: Int,
    val after: String,
    val target: String,
)
