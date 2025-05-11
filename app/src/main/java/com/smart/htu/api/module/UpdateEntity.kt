package com.smart.htu.api.module

import com.smart.htu.utils.APPVersion.getVersionCode

data class UpdateEntity(
    val code: Int,
    val message: String,
    val data: UpdateData
)

data class UpdateData(
    val versionName: String = "",
    val versionCode: Int = 0,
    val isNeedUpdate: Boolean = false,
    val update: UpdateDetail? = null,
    val isForceUpdate: Boolean = true,
)

data class UpdateDetail(
    val url: String,
    val content: String,
)


data class VersionEntity(
    val versionCode: Int = getVersionCode(),
)