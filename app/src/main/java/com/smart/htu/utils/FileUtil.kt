package com.smart.htu.utils

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun saveTextToFile(fileName: String, content: String, targetDirectory: String) {
        val file = File(targetDirectory, fileName)
        FileOutputStream(file).use { output ->
            output.write(content.toByteArray())
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
    ): Boolean {
        return try {
            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs()
            }
            val targetFile = File(targetDirectory, targetFileName)
            if (!sourceFile.exists()) {
                false
            } else {
                sourceFile.copyTo(targetFile, overwrite = true)
                sourceFile.delete()
                true
            }
        } catch (e: Exception) {
            Log.e("TAG666 moveFile", "${e.message}")
            false
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