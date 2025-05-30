package com.smart.htu.utils

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun saveTextToFile(fileName: String, content: String) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        FileOutputStream(file).use { output ->
            output.write(content.toByteArray())
        }
    }

    fun downloadFile(
        context: Context,
        url: String,
        fileName: String,
        targetDirectory: String = Environment.DIRECTORY_DOWNLOADS
    ) {
        try {
            val request = DownloadManager.Request(url.toUri())
                .setTitle(fileName)
                .setDescription("正在下载文件")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(targetDirectory, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            Log.e("TAG666 downloadFile", "${e.message}")
        }
    }

}