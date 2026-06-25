package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SCHourEntity(
    val data: List<HourScoreEntity> = emptyList(),
    val totalSize: Int = 0
) {
    val termIndex: List<Term>
        get() {
            val terms = data.filter { it.semester != null }
                .map { Term("${it.semester}-${it.termIndexPerYear}", it.termIndex) }
            return terms
        }
}

@Serializable
data class HourScoreEntity(
    @SerializedName("activity_type1_score") val classicScore: Double, // 经典 实际
    @SerializedName("activity_type2_score") val lectureScore: Double, // 报告
    @SerializedName("activity_type3_score") val activityScore: Double, // 活动
    @SerializedName("activity_type4_score") val practiceScore: Double, // 创新创业
    @SerializedName("activity_type5_score") val subjectCompetitionScore: Double, // 学科竞赛
    @SerializedName("activity_type6_score") val laborScore: Double? = 0.0, // 劳动
    @SerializedName("score_value") val totalScore: Double, // 每一学期合计
    @SerializedName("score_value_last") val convertedTotalScore: Double, // 每一学期合计
    @SerializedName("score_value_convert") val pointPerTerm: Double, // 学分转换后
    @SerializedName("score_value_convert_last") val convertedPointPerTerm: Double, // /75
    @SerializedName("school_year") val semester: String?, // 学年
    @SerializedName("school_term") val termIndexPerYear: String?, // 学期
    @SerializedName("school_term_index") val termIndex: Int, // 学期索引
    @SerializedName("student_name") val studentName: String?, // 姓名
    @SerializedName("student_code") val studentId: String?, // 学号
    @SerializedName("class_name") val className: String?, // 班级名称
)

@Serializable
data class Term(
    val semester: String,
    val termIndex: Int
)