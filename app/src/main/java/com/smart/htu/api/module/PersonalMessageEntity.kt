package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class PersonalMessageRes(
    val msg: String,
    @SerializedName("xjxx") val personalMessage: PersonalMessageEntity? = PersonalMessageEntity(),
    val code: Int
)

data class PersonalMessageEntity(
    @SerializedName("account") val studentId: String? = "0000000000", // 学号
    @SerializedName("yxmc") val academic: String? = "--", // 学院
    @SerializedName("xqmc") val campusName: String? = "--", // 校区名称
    @SerializedName("csrq") val birthday: String? = "2000-01-01", // 出生日期
    @SerializedName("zymc") val majorName: String? = "--", // 专业
    @SerializedName("xsxm") val username: String? = "-", // 姓名
    @SerializedName("xbmc") val gender: String? = "-", // 性别
    @SerializedName("mzmc") val nationality: String? = "-", //  民族
    @SerializedName("dh") val phoneNumber: String? = "-", // 手机
    @SerializedName("zzmmmc") val politicalProfile: String? = "-", // 政治面貌
    @SerializedName("bjmc") val className: String? = "-", // 班级
    @SerializedName("sfzh") val idNumber: String? = "-", // 身份证号
    @SerializedName("nj") val gradeNumber: Int? = 2000
) {
    val emailNumber: String
        get() = "$studentId@stu.htu.edu.cn"// 邮件地址
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