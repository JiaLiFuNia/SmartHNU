package com.smart.htu.utils

import android.annotation.SuppressLint
import java.io.File
import kotlin.math.log10
import kotlin.math.pow

object CoilUtil {

    // 递归计算目录大小
    fun getDirectorySize(directory: File): Long {
        var size: Long = 0
        try {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    size += if (file.isDirectory) {
                        getDirectorySize(file)
                    } else {
                        file.length()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    @SuppressLint("DefaultLocale")
    fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return String.format(
            "%.1f %s",
            size / 1024.0.pow(digitGroups.toDouble()),
            units[digitGroups]
        )
    }

}