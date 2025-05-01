package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import java.time.LocalTime

data class TodayCoursePost(
    val todaykb: String = "1"
)

data class TodayCourseResponse(
    @SerializedName("msg") val message: String,
    //  @SerializedName("issj") val isDataValid: Boolean,
    @SerializedName("code") val code: Int,
    //  @SerializedName("isOpen") val isOpen: Boolean,
    @SerializedName("kbList") val courseList: List<Course>
)

data class Course(
    @SerializedName("ps") val sortString: String, // 排序
    @SerializedName("khfsmc") val assessmentMethod: String, // 考核方式
    @SerializedName("pkrs") val totalStudents: Int, // 上课人数
    @SerializedName("jxhjmc") val teachingEnvironment: String, // 理论 实验
    @SerializedName("qssj") private val startTimeString: String, // 开始时间
    @SerializedName("jssj") private val endTimeString: String, // 结束时间
    @SerializedName("kcmc") val courseName: String, // 课程名称
    @SerializedName("teaxms") val teacherNames: String, // 教师
    @SerializedName("jxbmc") val className: String, // 上课班级
    @SerializedName("jxcdmc") val classroomName: String, // 上课地点
    @SerializedName("jcdm") val classTimeCode: String, // 节次代码
    @SerializedName("jzwmc") val buildingName: String, // 教学楼
    @SerializedName("xmmc") val projectName: String, // 项目名称
    @SerializedName("jcdm2") val classTimeCodeDetailed: String, // 节次代码2
) {
    val startTime: LocalTime
        get() = LocalTime.parse(startTimeString)

    val endTime: LocalTime
        get() = LocalTime.parse(endTimeString)

    val sortNumber: Int
        get() = sortString.toInt()
}

data class CourseSchedulePost(
    val zc: String = "",
    val jc: String = ""
)

data class CourseScheduleEntity(
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("maxzc") val totalWeeks: String,
    @SerializedName("zc") val week: String,
    @SerializedName("curDay") val weekday: String,
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("kbList") val kbList: String,
)
