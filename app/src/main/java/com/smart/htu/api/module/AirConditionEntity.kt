package com.smart.htu.api.module

data class LoginCookie(
    val shiroJID: String,
    val ymId: String
)

data class Area(
    val statusCode: Int,
    val success: Boolean,
    val message: String? = null,
    val rows: List<AreaData>? = null,
)

data class AreaData(
    val schoolCode: String,
    val id: String,
    val areaName: String,
)

data class BillDetail(
    val statusCode: Int,
    val message: String,
    val data: BillData? = null,
    val success: Boolean
)

data class BillData(
    val displayRoomName: String,
    val surplusList: List<SurplusList>,
    val soc: Double,
)

data class SurplusList(
    val roomStatus: String
)