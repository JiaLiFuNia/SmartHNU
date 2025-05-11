package com.smart.htu.utils

import android.os.Environment
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

}