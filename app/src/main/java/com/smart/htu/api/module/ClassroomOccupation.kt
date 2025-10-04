package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BuildingEntity(
    @SerializedName("jzwdm") val buildingCode: String,
    @SerializedName("jzwmc") val buildingName: String,
    @SerializedName("rq") val date: String? = ""
)

@Serializable
data class ClassroomOccupationEntity(
    val code: Int,
    val msg: String,
    @SerializedName("jszylist") val busyRoomList: List<BusyRoom>,
    @SerializedName("jxcdxxList") val allRoomList: List<AllRoom>,
    @SerializedName("jzwmc") val buildingName: String,
    @SerializedName("jzwdm") val buildingCode: String
) {
    fun getBusyState(room: AllRoom): List<Boolean> {
        val busyTime = List(10) { false }.toMutableList()
        val busyRoomListGroupByRoomCode = busyRoomList.groupBy { it.roomCode }
        // Log.d("TAG666 ", "getBusyState: $busyRoomListGroupByRoomCode")
        busyRoomListGroupByRoomCode[room.roomCode]?.forEach {
            val busyPeriodList = if (it.busyPeriodListString.contains(","))
                it.busyPeriodListString.split(",").map { it.toInt() }
            else listOf(it.busyPeriodListString.toInt())
            busyPeriodList.forEach { busyTime[it - 1] = true }
        }
        return busyTime
    }
}

@Serializable
data class BusyRoom(
    @SerializedName("jxcdmc") val roomName: String,
    @SerializedName("jxcddm") val roomCode: String,
    @SerializedName("xnxqdm") val shortTermCode: String,
    @SerializedName("jcdm") val busyPeriodCode: String,
    @SerializedName("jcdm2") val busyPeriodListString: String,
    @SerializedName("xq") val week: String,
    @SerializedName("rq") val date: String
)

@Serializable
data class AllRoom(
    @SerializedName("lch") val floorNumber: Int,
    @SerializedName("jxcdmc") val roomName: String,
    @SerializedName("jxcddm") val roomCode: String
)