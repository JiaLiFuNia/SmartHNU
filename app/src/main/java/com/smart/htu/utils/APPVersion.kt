package com.smart.htu.utils

import android.content.pm.PackageManager
import com.smart.htu.App

object APPVersion {
    fun getVersionCode(): Int {
        var versionCode = 0
        try {
            versionCode = App.context.packageManager.getPackageInfo(
                App.context.packageName,
                0
            ).longVersionCode.toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getVersionName(): String {
        var versionName = ""
        try {
            versionName = App.context.packageManager.getPackageInfo(
                App.context.packageName,
                0
            ).versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }
}