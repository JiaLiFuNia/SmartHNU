package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class TextbookEntity(
    val msg: String,
    val code: Int,
    @SerializedName("xdjcdatas") val courseTextbookList: List<CourseTextbook>
)

data class CourseTextbook(
    @SerializedName("kcflmc") val courseClassification: String, // 博约 小类
    @SerializedName("kcdlmc") val courseCategory: String, // 课程类型 大类
    @SerializedName("zdlxmc") val type: String,
    @SerializedName("kcdm") val courseCode: String,
    @SerializedName("xmmc") val description: String,
    @SerializedName("kcrwdm") val courseTaskCode: String,
    @SerializedName("kcmc") val courseName: String
) {
    val isNeedTextbook: Boolean
        get() = "不" !in type
}

data class TextbookSelectPost(
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("kcrwdm") val courseTaskCode: String,
)

data class SelectEntity(
    val msg: String,
    val code: Int,
    @SerializedName("kxjcdatas") val selectableList: List<Textbook>,
    @SerializedName("yxjcdatas") val selectedList: List<Textbook>
)

data class Textbook(
    @SerializedName("isbn") val isbn: String,
    @SerializedName("zb") val editor: String,
    @SerializedName("jcbc") val textbookVersion: String,
    @SerializedName("jcdj") val price: Double,
    @SerializedName("cbs") val publisher: String,
    @SerializedName("jcmc") val textbookName: String,
    @SerializedName("xsbh") val studentId: String? = null,
    // val tbdm: String,
    // val pcdm: String,
    // val jcdm: String,
    // val kcrwdm: String
) {
    val isSelected: Boolean
        get() = studentId.isNullOrEmpty()
}