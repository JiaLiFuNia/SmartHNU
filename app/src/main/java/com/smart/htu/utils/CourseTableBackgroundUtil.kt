package com.smart.htu.utils

import android.content.Context
import android.net.Uri
import java.io.File

object CourseTableBackgroundUtil {
    private const val BACKGROUND_FILE_NAME = "background.jpg"

    fun saveBackground(context: Context, uri: Uri): Uri? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.filesDir, BACKGROUND_FILE_NAME)
            input.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getBackground(context: Context): Uri? {
        val file = File(context.filesDir, BACKGROUND_FILE_NAME)
        return if (file.exists()) Uri.fromFile(file) else null
    }

    fun clearBackground(context: Context) {
        val file = File(context.filesDir, BACKGROUND_FILE_NAME)
        if (file.exists()) file.delete()
    }
}
