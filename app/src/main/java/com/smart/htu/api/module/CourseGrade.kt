package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class CourseGrade(
    val msg: String,
    val code: Int,
    @SerializedName("kccjList") val gradeData: List<GradeData>,
)

data class GradeData(
    @SerializedName("zcjfs") val gradeDouble: Double, // 总成绩
    @SerializedName("zcj") val gradeString: String, // 成绩
    @SerializedName("cjjd") val gradePoint: Double, // 绩点
    @SerializedName("xf") val gradeCredits: Int, // 学分
    @SerializedName("cjdm") val gradeCode: String, // 成绩代码
    @SerializedName("cjfsdm") val gradeTypeCode: String, // 成绩类型
    @SerializedName("kcmc") val courseName: String, // 课程名称
    @SerializedName("kcrwdm") val courseCode: String, // 课程代码
    @SerializedName("kcdlmc") val courseCategory: String, // 课程类型 大类
    @SerializedName("kcflmc") val courseClassification: String, // 博约 小类
    @SerializedName("xnxqmc") val longTerm: String, // 学期 2024-2025-1
    @SerializedName("xnxqdm") val shortTermCode: String, // 学期 202401
)
