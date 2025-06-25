package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class CourseGradeRes(
    val msg: String,
    val code: Int,
    @SerializedName("kccjList") val gradeData: List<CourseGradeEntity>,
) {
    data class CourseGradeEntity(
        @SerializedName("zcjfs") val gradeDouble: Double, // 总成绩
        @SerializedName("zcj") val gradeString: String, // 成绩
        @SerializedName("cjjd") val gradePoint: Double, // 绩点
        @SerializedName("xf") val gradeCredits: Int, // 学分
        @SerializedName("cjdm") val gradeCode: String, // 成绩代码
        @SerializedName("cjfsmc") val gradeTypeCode: String, // 成绩类型
        @SerializedName("kcmc") val courseName: String, // 课程名称
        @SerializedName("kcrwdm") val courseCode: String, // 课程代码
        @SerializedName("kcdlmc") val courseCategory: String, // 课程类型 大类
        @SerializedName("kcflmc") val courseClassification: String, // 博约 小类
        @SerializedName("xnxqmc") val termString: String, // 学期 2024-2025-1
        @SerializedName("xnxqdm") val termCode: String, // 学期 202401
    )
}

data class CourseGradeDetailPost(
    val cjdm: String
)

data class CourseGradeDetailRes(
    val msg: String,
    val code: Int,
    @SerializedName("xscj") val gradeData: CourseGradeDetailEntity,
) {
    data class CourseGradeDetailEntity(
        @SerializedName("zcj") val totalGrade: String,
        @SerializedName("bl1") val usualGradePercentage: Any,
        @SerializedName("cj1") val usualGrade: Any,
        @SerializedName("bl4") val finalGradePercentage: Any,
        @SerializedName("cj4") val finalGrade: Any
    )
}