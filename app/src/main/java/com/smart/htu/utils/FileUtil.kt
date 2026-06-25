package com.smart.htu.utils

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import java.io.File

object FileUtil {

    fun saveTextToFile(
        fileName: String,
        fileType: String,
        content: String,
        targetDirectory: String,
        context: Context
    ): Result<Boolean> {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, fileType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, targetDirectory)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                Result.success(true)
            } ?: Result.failure(Exception("导出失败"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun downloadFile(
        context: Context,
        url: String,
        fileName: String,
        targetDirectory: String
    ) {
        try {
            val request = DownloadManager.Request(url.toUri())
                .setTitle(fileName)
                .setDescription("正在下载文件")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(targetDirectory, fileName)
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
        }
    }


    fun moveFile(
        sourceFile: File,
        targetDirectory: File,
        targetFileName: String
    ): Result<Boolean> {
        return try {
            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs()
            }
            val targetFile = File(targetDirectory, targetFileName)
            if (!sourceFile.exists()) {
                Result.success(false)
            } else {
                sourceFile.copyTo(targetFile, overwrite = true)
                sourceFile.delete()
                Result.success(true)
            }
        } catch (e: Exception) {
            Log.e("TAG666 moveFile", "${e.message}")
            Result.failure(e)
        }
    }

    fun getFileContent(
        fileName: String,
        targetDirectory: File
    ): String? {
        return try {
            val file = File(targetDirectory, fileName)
            if (!file.exists()) {
                null
            } else {
                file.readText()
            }
        } catch (e: Exception) {
            Log.e("TAG666 getFileContent", "${e.message}")
            null
        }
    }

}