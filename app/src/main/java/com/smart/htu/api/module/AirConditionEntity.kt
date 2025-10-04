package com.smart.htu.api.module

import com.smart.htu.utils.DateUtil.convertStringDateToLocalDate
import com.smart.htu.utils.DateUtil.dateFormatter
import kotlinx.serialization.Serializable

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
    val datetime: String, // 2025-9-10
    val used: String
) {
    val easyDateTime: String
        get() {
            val date = convertStringDateToLocalDate(datetime, "yyyy-M-d")
            return String.format("%d-%d", date.monthValue, date.dayOfMonth)
        }
}

data class BuyRecords(
    val statusCode: Int,
    val success: Boolean,
    val message: String,
    val rows: List<BuyRecordsData>
)

data class BuyRecordsData(
    private val datetime: String, // 2025-9-8 12:11:56
    val money: String,
) {
    val dateTime: String
        get() {
            return dateFormatter(datetime, "yyyy-M-d H:mm:ss", "yyyy-MM-dd HH:mm")
        }

    val easyDateTime: String
        get() {
            val date = convertStringDateToLocalDate(datetime, "yyyy-M-d H:mm:ss")
            return String.format("%d-%d", date.monthValue, date.dayOfMonth)
        }
}
