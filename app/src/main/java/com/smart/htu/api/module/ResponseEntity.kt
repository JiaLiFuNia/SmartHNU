package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class PersonalMessageRes(
    val message: String,
    val data: List<PersonalMessages>,
    val code: String,
    val success: Boolean
)

data class PersonalMessages(
    val zydm: String,
    val zzmmdm: String,
    val xbdm: String,
    val wid: String,
    val xjzt: Any? = null,
    val sfzx: Long,
    val xh: String,
    val rxrq: String,
    val dw: String,
    val sfzjh: String,
    val mzdm: String,
    val dwdm: String,
    val jgdm: Any? = null,
    val pyfs: Any? = null,
    val csrq: String,
    val gjdqdm: Any? = null,
    val zymc: String,
    val xm: String,
    val pyccdm: Any? = null,
    val xb: String,
    val bh: String,
    val jtdz: Any? = null,
    val xslb: Any? = null,
    val sfzs: Any? = null,
    val rxnj: String,
    val fdy: Any? = null,
    val cym: Any? = null,
    val mz: String,
    val xx: Any? = null,
    val sj: String,
    val pyfsdm: Any? = null,
    val jkzk: Any? = null,
    val xjztdm: String,
    val xxdm: Any? = null,
    val zzmm: String,
    val bjmc: String,
    val xzdm: Any? = null,
    val pycc: Any? = null,
    val gj: Any? = null,
    val jg: Any? = null,
    val xslbdm: Any? = null,
    val jtdh: String,
    val xznj: String
)

data class PersonalMessage(
    @SerializedName("xh") val studentId: String? = "", // 学号
    @SerializedName("dw") val academic: String? = "", // 学院
    @SerializedName("csrq") val birthday: String? = "2000-00-00", // 出生日期
    @SerializedName("zymc") val majorName: String? = "", // 专业
    @SerializedName("xm") val username: String? = "未命名", // 姓名
    @SerializedName("xb") val gender: String? = "男", // 性别
    @SerializedName("mz") val nationality: String? = "", //  民族
    @SerializedName("sj") val phoneNumber: String? = "", // 手机
    @SerializedName("zzmm") val politicalProfile: String? = "群众", // 政治面貌
    @SerializedName("bjmc") val className: String? = "", // 班级
    @SerializedName("xznj") val gradeNumber: String? = "2022", // 年级
    val emailNumber: String? = "" // 邮件地址
    // @SerializedName("sfzjh") val sfzjh: String,
)
