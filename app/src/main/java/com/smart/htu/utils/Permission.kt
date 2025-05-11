package com.smart.htu.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class Permission {

    companion object {
        internal val CALENDAR_PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )

        fun hasCalendarPermissions(context: Context): Boolean {
            return CALENDAR_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }

        fun checkRequestCalendarPermissions(
            context: Context,
            calendarPermissionLauncher: ActivityResultLauncher<Array<String>>
        ) {
            if (!hasCalendarPermissions(context)) {
                calendarPermissionLauncher.launch(CALENDAR_PERMISSIONS)
            }
        }
    }

}