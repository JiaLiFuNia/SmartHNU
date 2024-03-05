package com.xhand.hnu2.model.entity

data class LoginEntity(
    val code: Int,
    val msg: String,
    val user: UserEntity? = null
)

data class UserEntity(
    val token: String,
    val userxm: String,
    val userdwmc: String,
    val userAccount: String
)

data class LoginPostEntity(
    val username: String,
    val password: String,
    val code: String,
    val appid: String?
)