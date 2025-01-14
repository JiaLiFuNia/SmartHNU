package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class PersonalMessageRes(
    val message: String,
    val data: List<PersonalMessage>,
    val code: String,
    val success: Boolean
)

data class PersonalMessage(
    @SerializedName("XH") val studentId: String? = "-", // 学号
    @SerializedName("DW") val academic: String? = "-", // 学院
    @SerializedName("CSRQ") private val _birthday: String? = "0000-0-0", // 出生日期
    @SerializedName("ZYMC") val majorName: String? = "", // 专业
    @SerializedName("XM") val username: String? = "-", // 姓名
    @SerializedName("XB") val gender: String? = "-", // 性别
    @SerializedName("MZ") val nationality: String? = "-", //  民族
    @SerializedName("SJ") val phoneNumber: String? = "-", // 手机
    @SerializedName("ZZMM") val politicalProfile: String? = "-", // 政治面貌
    @SerializedName("BJMC") val className: String? = "-", // 班级
    @SerializedName("XZNJ") val gradeNumber: String? = "-", // 年级
    // @SerializedName("sfzjh") val sfzjh: String,
) {
    val emailNumber: String
        get() = "$studentId@stu.htu.edu.cn"// 邮件地址

    val birthday: String
        get() = _birthday?.let {
            val parts = it.split("-")
            "${parts[0]}年${parts[1]}月${parts[2]}日"
        } ?: "未知日期"
}

data class LoginPost(
    val username: String,
    val password: String,
    val code: String = "",
    val appid: Any? = null
)

data class LoginJWCEntity(
    val msg: String,
    val code: Int,
    val user: UserEntity? = null,
)

data class UserEntity(
    val token: String,
    @SerializedName("userxm") val username: String,
    @SerializedName("userdwmc") val academic: String,
    @SerializedName("userAccount") val studentId: String
)