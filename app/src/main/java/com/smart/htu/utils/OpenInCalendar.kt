package com.smart.htu.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openCalendar(context: Context) {
    try {
        val calendarIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("content://com.android.calendar/time")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(calendarIntent)
    } catch (e: Exception) {
        sendToast(context, "无法启动应用")
    }
}

fun openApp(context: Context, packageName: String) {
    try {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            sendToast(context, "应用未安装")
        }
    } catch (e: Exception) {
        sendToast(context, "无法启动应用")
    }
}