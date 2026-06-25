package com.smart.htu.api.module

data class LibraryLoginPost(
    val loginContext: String,
    val verifyCode: String
)

data class LibraryLoginRes(
    val code: Int,
    val msg: String? = null,
    val data: LibraryLoginData? = null,
    val sign: String? = null
)

data class LibraryLoginData(
    val name: String,
    val uid: Int,
    val userId: String
)