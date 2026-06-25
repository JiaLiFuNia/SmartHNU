package com.smart.htu.utils

import android.content.Context
import android.net.Uri
import java.io.File

object CourseTableBackgroundUtil {
    private const val BACKGROUND_FILE_PREFIX = "background_"
    private const val BACKGROUND_FILE_EXTENSION = ".jpg"
    private const val LEGACY_BACKGROUND_FILE_NAME = "background.jpg"

    fun saveBackground(context: Context, uri: Uri): Uri? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "$BACKGROUND_FILE_PREFIX${System.currentTimeMillis()}$BACKGROUND_FILE_EXTENSION"
            val file = File(context.filesDir, fileName)
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
        val dir = context.filesDir
        val files = dir.listFiles()
            ?.filter { it.name.startsWith(BACKGROUND_FILE_PREFIX) && it.name.endsWith(BACKGROUND_FILE_EXTENSION) }
            ?: emptyList()

        if (files.isEmpty()) {
            val legacy = File(dir, LEGACY_BACKGROUND_FILE_NAME)
            return if (legacy.exists()) Uri.fromFile(legacy) else null
        }

        val latest = files.maxByOrNull { file ->
            val tsStr = file.name.removePrefix(BACKGROUND_FILE_PREFIX).removeSuffix(BACKGROUND_FILE_EXTENSION)
            tsStr.toLongOrNull() ?: 0L
        } ?: return null

        return Uri.fromFile(latest)
    }

    fun clearBackground(context: Context) {
        val dir = context.filesDir
        dir.listFiles()
            ?.filter { it.name.startsWith(BACKGROUND_FILE_PREFIX) && it.name.endsWith(BACKGROUND_FILE_EXTENSION) }
            ?.forEach { it.delete() }

        val legacy = File(dir, LEGACY_BACKGROUND_FILE_NAME)
        if (legacy.exists()) legacy.delete()
    }
}
