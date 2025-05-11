package com.smart.htu.api.module

import com.smart.htu.utils.APPVersion.getVersionCode
import com.smart.htu.utils.DeviceUtil.getAndroidVersion
import com.smart.htu.utils.DeviceUtil.getDeviceModel
import com.smart.htu.utils.DeviceUtil.getSystem

data class FeedbackEntity(
    val type: String,
    val functionModule: String,
    val message: String,
    val email: String,
    val androidVersion: String = getAndroidVersion(),
    val system: String = getSystem(),
    val device: String = getDeviceModel(),
    val versionCode: Int = getVersionCode()
)

enum class FeedbackType(val type: String) {
    FEEDBACK("问题反馈"),
    SUGGESTION("使用建议")
}

data class FeedbackRes(
    val success: Boolean,
    val message: String
)