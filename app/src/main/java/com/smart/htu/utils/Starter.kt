package com.smart.htu.utils

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.net.toUri
import com.smart.htu.App.Companion.context

// 打开日历
fun startCalendar() {
    try {
        val calendarIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "content://com.android.calendar/time".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(calendarIntent)
    } catch (e: Exception) {
        ToastUtil.showToast(context, "$e")
    }
}

// 传入网页URL打开
fun startWebUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        ToastUtil.showToast(context, "$e")
    }
}

//通过包名启动第三方应用
@SuppressLint("QueryPermissionsNeeded")
fun startLaunchAPK(
    appName: String,
    packageName: String
) {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
    } catch (e: Exception) {
        if (e is android.content.ActivityNotFoundException) {
            ToastUtil.showToast(context, "${appName}未安装或版本过低")
        } else {
            ToastUtil.showToast(context, "$e")
        }
    }
}

// 通过包名和Activity名称启动指定应用的指定Activity，并可传入URI和额外参数
fun startActivityWithUri(
    appName: String,
    packageName: String = "",
    activityName: String = "",
    uri: String? = null,
    extra: Map<String, String>? = null
) {
    try {
        val intent = Intent().apply {
            if (packageName.isNotEmpty() && activityName.isNotEmpty()) {
                setClassName(packageName, activityName)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            uri?.let { data = it.toUri() }
            extra?.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        if (e is android.content.ActivityNotFoundException) {
            ToastUtil.showToast(context, "${appName}未安装或版本过低")
        } else {
            ToastUtil.showToast(context, "$e")
        }
    }
}