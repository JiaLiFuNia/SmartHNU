package com.smart.htu.screens.application.classroom

data class ClassroomNameEntity(
    val buildingCode: String,
    val buildingName: String,
)

data class ClassroomPostEntity(
    val buildingCode: String,
    val buildingName: String,
    val date: String
)

data class SingleBuilding(
    val msg: String,
    val code: Long,
    val buildingName: String,
    val buildingCode: String,
    val freeRoomList: List<FreeRoomEntity>,
    val busyRoomList: List<BusyRoomEntity>
)

data class BusyRoomEntity(
    val teacherName: String,
    val className: String,
    val courseType: String,
    val courseName: String,
    val courseEnglishName: String,
    val courseNumber: String, // 节次代码
    val courseNumberList: String, // 节次代码 列表
    val roomName: String,
    val roomCode: String,
    val shortTermCode: String,
    val dm: String,
    val zc: String,
    val xq: String,
    val kxh: Long,
    val rq: String,
)

data class FreeRoomEntity(
    val floorNumber: Int,
    val roomCode: String,
    val roomName: String,
)