package com.smart.htu.api.module

import com.smart.htu.utils.APPVersion.getVersionCode

data class UpdateRes(
    val code: Int,
    val message: String,
    val data: UpdateEntity
)

data class UpdateEntity(
    val versionName: String = "",
    val versionCode: Int = 0,
    val isNeedUpdate: Boolean = false,
    val update: UpdateData? = null,
    val isForceUpdate: Boolean = true,
)

data class UpdateData(
    val downloadUrl: String,
    val content: String,
)

data class VersionEntity(
    val versionCode: Int = getVersionCode(),
)