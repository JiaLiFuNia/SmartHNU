package com.smart.htu.utils

object DeviceUtil {

    // 获取安卓版本
    fun getAndroidVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    // 获取系统
    fun getSystem(): String {
        return android.os.Build.MANUFACTURER
    }

    // 获取设备型号
    fun getDeviceModel(): String {
        return android.os.Build.MODEL
    }

}