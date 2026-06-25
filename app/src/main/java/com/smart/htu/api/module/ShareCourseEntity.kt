package com.smart.htu.api.module

import android.util.Log
import kotlinx.serialization.json.Json

data class ShareCourseEntity(
    val courseData: String
)

data class ShareCourseRes(
    val code: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val shareCode: String,
        val expiresIn: Long
    )
}


data class ReceiveSharedData(
    val code: Int,
    val message: String,
    val data: String? = "",
) {

    val courseScheduleData: List<List<List<CourseEntity>>>
        get() = try {
            if (data.isNullOrEmpty()) {
                ArrayList()
            } else {
                Json.decodeFromString<List<List<List<CourseEntity>>>>(data)
            }
        } catch (e: Exception) {
            Log.e("TAG666 ShareCourseRes", "解析课程表失败: ${e.message}")
            ArrayList()
        }

}