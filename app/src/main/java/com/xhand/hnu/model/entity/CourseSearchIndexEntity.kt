package com.xhand.hnu.model.entity

import com.google.gson.annotations.SerializedName

data class CourseSearchIndexEntity(
    val msg: String,
    val zyList: List<GnqListElement>,
    val code: Long,
    val jhlxList: List<GnqListElement>,
    val xsyxList: List<GnqListElement>,
    val jxlList: List<GnqListElement>,
    val xnxqmc: String,
    val kkyxList: List<GnqListElement>,
    val xnxqList: List<XnxqList>,
    val xnxqdm: String,
    val xqList: List<GnqListElement>,
    val gnqList: List<GnqListElement>,
    val kkjysList: List<Any?>,
    val xsnjList: List<GnqListElement>
)

data class GnqListElement(
    val title: String,
    val value: String
)

data class XnxqList(
    @SerializedName("xnxqdm")
    val value: String,
    @SerializedName("xnxqmc")
    val title: String,
    val mrczxq: String,
    val xnd: String
)
