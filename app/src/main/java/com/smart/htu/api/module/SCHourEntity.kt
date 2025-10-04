package com.smart.htu.api.module

typealias SCHourEntity = List<SingleItemHourEntity>

data class SingleItemHourEntity (
    val value: Int,
    val name: String
)