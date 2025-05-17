package com.smart.htu.api.module

import com.smart.htu.utils.convertDateToDouble
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ACCookie(
    val shiroJID: String = "",
    val ymId: String = ""
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
    val mdtype: String,
    val roomStatus: String
)

data class BillRecords(
    val statusCode: Int,
    val success: Boolean,
    val message: String,
    val rows: List<BillRecordsData>,
    val total: Int
)

data class BillRecordsData(
    val datetime: String,
    val used: String
) {
    val dateTimeDouble: String
        get() {
            return convertDateToDouble(datetime, "yyyy-MM-dd")
        }
}

data class BuyRecords(
    val statusCode: Int,
    val success: Boolean,
    val message: String,
    val rows: List<BuyRecordsData>
)

data class BuyRecordsData(
    private val datetime: String,
    val money: String,
) {
    val dateTime: String
        get() {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val parsedDateTime = LocalDateTime.parse(datetime, inputFormatter)
            return parsedDateTime.format(outputFormatter)
        }

    val dateTimeDouble: String
        get() {
            return convertDateToDouble(dateTime, "yyyy-MM-dd HH:mm")
        }
}
