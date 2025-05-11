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
    } catch (_: Exception) {
        sendToast(context, "无法启动应用")
    }
}

// 传入网页URL打开
fun startWebUrl(url: String) {
    try {
        val it = Intent(Intent.ACTION_VIEW, url.toUri())
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    } catch (_: Exception) {
        sendToast(context, "启动浏览器失败")
    }
}

//通过包名启动第三方应用
@SuppressLint("QueryPermissionsNeeded")
fun startLaunchAPK(packageName: String, appName: String = "应用") {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) sendToast(context, "未安装${appName}")
        else context.startActivity(intent)
    } catch (_: Exception) {
        sendToast(context, "启动外部应用失败")
    }
}

//传入应用URL打开
fun startAppUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_DEFAULT, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {
        sendToast(context, "打开支付宝失败")
    }
}