package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class PersonalMessageRes(
    val message: String,
    val data: List<PersonalMessage>,
    val code: String,
    val success: Boolean
)

data class PersonalMessage(
    @SerializedName("XH") val studentId: String? = "", // 学号
    @SerializedName("DW") val academic: String? = "", // 学院
    @SerializedName("CSRQ") val birthday: String? = "2000-00-00", // 出生日期
    @SerializedName("ZYMC") val majorName: String? = "", // 专业
    @SerializedName("XM") val username: String? = "未命名", // 姓名
    @SerializedName("XB") val gender: String? = "男", // 性别
    @SerializedName("MZ") val nationality: String? = "", //  民族
    @SerializedName("SJ") val phoneNumber: String? = "", // 手机
    @SerializedName("ZZMM") val politicalProfile: String? = "群众", // 政治面貌
    @SerializedName("BJMC") val className: String? = "", // 班级
    @SerializedName("XZNJ") val gradeNumber: String? = "2022", // 年级
    // @SerializedName("sfzjh") val sfzjh: String,
) {
    val emailNumber: String
        get() = "$studentId@stu.htu.edu.cn"// 邮件地址
}
