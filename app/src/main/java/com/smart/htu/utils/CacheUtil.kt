package com.smart.htu.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object CacheUtil {

    /**
     * Calculates the total size of the application's cache (internal + external).
     */
    suspend fun getCacheSize(context: Context): Long = withContext(Dispatchers.IO) {
        var totalSize = 0L
        try {
            context.cacheDir?.let { totalSize += getDirSize(it) }
            context.externalCacheDir?.let { totalSize += getDirSize(it) }
        } catch (e: Exception) {
            Log.e("TAG666 cache", "Error calculating cache size", e)
        }
        totalSize
    }

    /**
     * Clears the application's cache directory.
     */
    suspend fun clear(context: Context): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            var allDeleted = true

            // 清理内部缓存
            context.cacheDir?.let {
               if (!deleteDirContent(it)) allDeleted = false
            }

            // 清理外部缓存
            context.externalCacheDir?.let {
                if (!deleteDirContent(it)) allDeleted = false
            }

            if (allDeleted) Result.success(Unit) else Result.success(Unit) // 即使部分失败也算完成
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDirSize(dir: File): Long {
        if (!dir.exists()) return 0
        return dir.walkTopDown().sumOf { it.length() }
    }

    private fun deleteDirContent(dir: File): Boolean {
        if (!dir.exists()) return true
        var result = true
        dir.listFiles()?.forEach { file ->
            if (!file.deleteRecursively()) {
                result = false
            }
        }
        return result
    }
}