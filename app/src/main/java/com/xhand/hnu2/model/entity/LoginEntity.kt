package com.xhand.hnu2.model.entity

// 返回参数
data class LoginEntity(
    val msg: String,
    val code: Long,
    val user: UserEntity? = null,
)

// 返回token
data class UserEntity(
    val token: String,
    val userxm: String,
    val userdwmc: String,
    val userAccount: String
)

// 登录请求
data class LoginPostEntity(
    val username: String,
    val password: String,
    val code: String,
    val appid: Any?
)

// 成绩请求
data class GradePost(
    val xnxqdm: String
)

// 成绩回传
data class GradeEntity(
    val msg: String,
    val code: Long,
    val kccjList: List<KccjList>,
)

data class KccjList(
    val xsxm: String, // 姓名
    val zcjfs: Double, // 总成绩
    val xnxqmc: String, // 学期
    val kcdlmc: String, // 课程类型
    val cjjd: Double, // 绩点
    val kcrwdm: String, // 课程代码
    val kcmc: String, // 课程名称
    val cjdm: String, // 成绩代码
    val xf: Long, // 学分
    val xnxqdm: String, // 学期
    val xdfsmc: String, // 类型
    val cjfsmc: String, // 成绩类型
)
