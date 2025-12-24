package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class SelectableCourseTypeEntity(
    val courseTypeId: String,
    val courseTypeName: String,
    val courseTermString: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

data class CourseRepoRes(
    @SerializedName("rows") val courseRepo: List<CourseItemEntity>
)

data class CourseItemEntity(
    @SerializedName("teaxm") val teacherName: String? = null, // 教师
    @SerializedName("jxbmc") val className: String, // 上课班级
    @SerializedName("kcmc") val courseName: String, // 课程名称
    @SerializedName("kcywmc") val courseEnglishName: String? = null, // 课程英文名称
    @SerializedName("xmmc") val projectName: String? = null, // 项目名称
    @SerializedName("zxs") val totalHour: Int, // 总学时
    @SerializedName("xf") val credit: String, // 学分
    @SerializedName("kcdlmc") val category: String, // 课程大类
    @SerializedName("sksj") val classTime: String, // 课程类别
    @SerializedName("pkrs") val totalCapacity: Int, // 计划人数
    @SerializedName("jxbrs") val enrolledCount: String, // 已选人数
    val kcrwdm: String,
    val kcdm: String
)

data class CourseTimeEntity(
    @SerializedName("jxbmc") val className: String,
    @SerializedName("teaxms") val teacherNames: String,
    @SerializedName("zdjxcdmc") val classroomName: String,
    @SerializedName("sknrjj") val contentSummary: String,
    @SerializedName("flfzmc") val categoryName: String,
    @SerializedName("jxhjmc") val teachingType: String,

    @SerializedName("zc") val weeks: String,
    @SerializedName("xq") val dayOfWeek: String,
    @SerializedName("jcdm2") val sectionCode: String,
    @SerializedName("xs") val courseHours: Long,
    @SerializedName("qssj") val startTime: String,
    @SerializedName("jssj") val endTime: String,
    @SerializedName("xnxqmc") val semesterName: String,

    @SerializedName("kcrwdm") val taskId: String,
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("kkbmdm") val departmentId: String,
    @SerializedName("kxh") val courseSequenceNum: Int,
    @SerializedName("pkrs") val studentCount: Int,

    @SerializedName("dgksdm") val examCode: String
) {
    val day: String
        get() = when (dayOfWeek) {
            "1" -> "一"
            "2" -> "二"
            "3" -> "三"
            "4" -> "四"
            "5" -> "五"
            "6" -> "六"
            "7" -> "日"
            else -> dayOfWeek
        }
}
