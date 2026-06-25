package com.smart.htu.api.module

import com.smart.htu.utils.APPVersion.getVersionCode
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRes(
    val code: Int,
    val message: String,
    val data: UpdateEntity,
    val captchaModelVersion: CaptchaVersionEntity
)

@Serializable
data class UpdateEntity(
    val versionName: String = "",
    val versionCode: Int = 0,
    val isNeedUpdate: Boolean = false,
    val update: UpdateData? = null,
    val isForceUpdate: Boolean = false,
)

@Serializable
data class UpdateData(
    val downloadUrl: String,
    val content: String,
)

@Serializable
data class CaptchaVersionEntity(
    val versionName: String = "0",
    val versionCode: Int = 0,
    val downloadUrl: String = ""
)

data class VersionEntity(
    val versionCode: Int = getVersionCode(),
)