package com.xhand.hnu.model.entity

data class SecondClassInfo (
    val username: String,
    val password: String,
    val cookie: String,
)


typealias HourEntity = List<HourListEntity>

data class HourListEntity (
    val value: Long,
    val name: String
)
