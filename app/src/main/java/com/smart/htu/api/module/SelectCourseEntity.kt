package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import com.smart.htu.utils.DateUtil.convertStringDateToLocalDate
import com.smart.htu.utils.DateUtil.getCurrentDate
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class SelectableCourseTypeEntity(
    val courseTypeId: String,
    val courseTypeName: String,
    val courseTermString: String? = null,
    val description: String,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null
)

data class CourseRepoRes(
    @SerializedName("rows") val courseRepo: List<CourseItemEntity>
)

@Serializable
data class CourseItemEntity(
    @SerializedName("teaxm") val teacherName: String? = null, // 教师
    @SerializedName("jxbmc") val className: String, // 上课班级
    @SerializedName("kcmc") val courseName: String, // 课程名称
    @SerializedName("xmmc") val projectName: String? = null, // 项目名称
    @SerializedName("zxs") val totalHour: Int, // 总学时
    @SerializedName("xf") val credit: String, // 学分
    @SerializedName("kcdlmc") val category: String, // 课程大类
    @SerializedName("sksj") val classTime: String, // 课程类别
    @SerializedName("pkrs") val totalCapacity: Int, // 计划人数
    @SerializedName("jxbrs") val enrolledCount: String, // 已选人数
    @SerializedName("kcrwdm") val courseTaskCode: String,
    @SerializedName("kcflmc") val courseCategoryName: String,
    var courseTypeId: String
)

data class CourseTypeInfoRes(
    val code: Int,
    val data: CourseTypeInfoEntity,
    val message: String
) {
    data class CourseTypeInfoEntity(
        @SerializedName("xklxdm") val courseTypeId: String,
        @SerializedName("xkkz") val courseTypeInfoData: CourseTypeInfoData
    )

    data class CourseTypeInfoData(
        @SerializedName("xklxmc") val courseTypeName: String,
        @SerializedName("xnxqmc") val termString: String,
        @SerializedName("xnxqdm") val termCode: String,
        @SerializedName("bz") val description: String,
        @SerializedName("xkjd") val selectionPhaseIndex: Int, // "一选", "二选", "退选", "补选"
        @SerializedName("qssj") val startTime: String,
        @SerializedName("jssj") val endTime: String,
        @SerializedName("qssj1") val startTime1: String?, // 一选
        @SerializedName("jssj1") val endTime1: String?,
        @SerializedName("qssj2") val startTime2: String, // 二选
        @SerializedName("jssj2") val endTime2: String,
        @SerializedName("qssj3") val startTime3: String?, // 退选
        @SerializedName("jssj3") val endTime3: String?,
        @SerializedName("qssj4") val startTime4: String, // 补选
        @SerializedName("jssj4") val endTime4: String,
        @SerializedName("iscancel") val isCancelable: Boolean, // 能否退课
    ) {

        val selectionPhase: String
            get() = when (selectionPhaseIndex) {
                1 -> "一选"
                2 -> "二选"
                3 -> "退选"
                4 -> "补选"
                else -> "未知"
            }

    }
}


data class CourseInfoEntity(
    @SerializedName("kcmc") val courseName: String? = null,
    @SerializedName("kcywmc") val courseEnglishName: String? = null,
    @SerializedName("jzwmc") val buildingName: String? = null,
    @SerializedName("jxcdmc") val teachingVenueName: String? = null,
    @SerializedName("xqmc") val campusName: String? = null,
    @SerializedName("pkrq") private val dateString: String? = null,
    @SerializedName("lch") val buildingFloor: String? = null,
    @SerializedName("jxbrs") val enrolledCount: Int? = null,
    @SerializedName("xmmc") val projectName: String? = null,

    @SerializedName("jxbmc") val className: String,
    @SerializedName("teaxms") val teacherNames: String,
    @SerializedName("zdjxcdmc") val classroomName: String,
    @SerializedName("flfzmc") val categoryName: String,
    @SerializedName("jxhjmc") val teachingType: String,

    @SerializedName("zc") val weekIndexString: String,
    @SerializedName("xq") val dayOfWeekString: String,
    @SerializedName("jcdm2") val sectionListString: String? = null,
    @SerializedName("jcdm") val sectionCode: String? = null,
    @SerializedName("xs") val courseHours: Int,
    @SerializedName("qssj") val startTime: String,
    @SerializedName("jssj") val endTime: String,
    @SerializedName("xnxqmc") val semesterName: String,
    @SerializedName("ps") val startSection: String? = null,
    @SerializedName("pe") val endSection: String? = null,

    @SerializedName("kcrwdm") val taskId: String,
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("kkbmdm") val departmentId: String,
    @SerializedName("pkrs") val studentCount: Int,

    @SerializedName("dgksdm") val examCode: String
) {
    val dayOfWeek: Int
        get() = dayOfWeekString.toIntOrNull() ?: 1

    val day: String
        get() = when (dayOfWeek) {
            1 -> "一"
            2 -> "二"
            3 -> "三"
            4 -> "四"
            5 -> "五"
            6 -> "六"
            7 -> "日"
            else -> ""
        }

    val date: LocalDate
        get() = convertStringDateToLocalDate(
            dateString ?: getCurrentDate("yyyy-MM-dd"),
            "yyyy-MM-dd"
        )

    val sectionList: List<Int>
        get() = startSection?.toIntOrNull()?.let { start ->
            endSection?.toIntOrNull()?.let { end ->
                (start..end).toList()
            }
        } ?: emptyList()

}


data class SelectCourseEntity(
    val code: Int,
    val data: String,
    val message: String
)