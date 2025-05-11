package com.smart.htu.api.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
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
    @SerializedName("ps") private val sortString: String, // 排序
    @SerializedName("qssj") private val startTimeString: String, // 开始时间
    @SerializedName("jssj") private val endTimeString: String, // 结束时间
    @SerializedName("qsrq") private val startDateString: String, // 起始日期
    @SerializedName("jsrq") private val endDateString: String, // 结束日期
    @SerializedName("khfsmc") val assessmentMethod: String, // 考核方式
    @SerializedName("jxbrs") val totalStudents: Int, // 上课人数
    @SerializedName("jxhjmc") val teachingEnvironment: String, // 理论 实验
    @SerializedName("kcmc") val courseName: String, // 课程名称
    @SerializedName("kcywmc") val courseEnglishName: String? = null, // 课程英文名称
    @SerializedName("teaxms") val teacherName: String, // 教师
    @SerializedName("jxbmc") val className: String, // 上课班级
    @SerializedName("jxcdmc") val classroomName: String? = null, // 上课地点
    @SerializedName("jcdm") val classTimeCode: String, // 节次代码
    @SerializedName("jzwmc") val buildingName: String? = null, // 教学楼
    @SerializedName("xmmc") val projectName: String? = null, // 项目名称
    @SerializedName("jcdm2") val classTimeCodeDetailed: String, // 节次代码2
    @SerializedName("szxqmc") val campus: String? = null, // 所在校区
    @SerializedName("xq") val weekdayString: String, // 星期（1~7）
    @SerializedName("xnxqmc") val termString: String, // 学年学期名称
    @SerializedName("zc") val weekString: String, // 周次（表示在第几周的课）
) {
    val startTime: LocalTime
        get() = LocalTime.parse(startTimeString)

    val endTime: LocalTime
        get() = LocalTime.parse(endTimeString)

    val startDate: LocalDate
        get() = LocalDate.parse(startDateString)

    val endDate: LocalDate
        get() = LocalDate.parse(endDateString)

    val sortNumber: Int
        get() = sortString.toInt()

    val sectionList: List<Int>
        get() = classTimeCodeDetailed.split(",").map { it.toInt() }

    val weekday: Int
        get() = weekdayString.toIntOrNull() ?: 1

    val week: Int
        get() = weekString.toIntOrNull() ?: 1
}

data class CourseSchedulePost(
    val zc: String = "", // 周次（第几周 或者 all）
    val jc: String = ""  // 节次
)

data class CourseScheduleEntity(
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("zc") private val weekString: String,
    @SerializedName("minzc") val minWeek: String,
    @SerializedName("maxzc") val maxWeek: String,
    @SerializedName("curDay") private val weekday: String,
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("kbList") private val courseTableString: String,
    @SerializedName("rq") private val dateString: String,
) {
    val courseTable: CourseTable
        get() = try {
            val gson = Gson()
            val type = object : TypeToken<CourseTable>() {}.type
            gson.fromJson(courseTableString, type)
        } catch (e: Exception) {
            Log.e("TAG666 CourseEntity", "解析课程表失败: ${e.message}")
            ArrayList()
        }

    // 周次
    val week: Int
        get() = weekString.toIntOrNull() ?: 0

    // 若是本周的则为今天的日期，若非本周返回周一的日期
    val date: LocalDate
        get() = LocalDate.parse(dateString)

    // 今天周几，若非本周返回0
    val todayWeekday: Int
        get() = weekday.toIntOrNull() ?: 0
}

typealias CourseTable = ArrayList<Map<String, List<Course>>>


