package com.smart.htu.utils

import android.content.ContentValues
import android.content.Context
import androidx.core.net.toUri
import com.smart.htu.utils.DeviceUtil.getSystem
import java.time.LocalDate
import java.time.ZoneId

object MIStepsUtil {

    private val URI = "content://com.miui.providers.steps/item".toUri()

    fun queryTodaySteps(context: Context): Result<Map<Long, Int>> {
        try {
            if (getSystem() != "Xiaomi") return Result.failure(Exception("非小米/红米设备"))
            val todayStartTime = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val projection = arrayOf("_steps", "_begin_time")

            val selection = "_begin_time > ?"
            val selectionArgs = arrayOf(todayStartTime.toString())

            val cursor = context.contentResolver.query(
                URI,
                projection,
                selection,
                selectionArgs,
                "_begin_time DESC"
            )
            val steps = mutableMapOf<Long, Int>()
            cursor?.use {
                while (it.moveToNext()) {
                    val step = it.getString(it.getColumnIndexOrThrow("_steps"))
                    val beginTime = it.getLong(it.getColumnIndexOrThrow("_begin_time"))
                    steps[beginTime] = step.toIntOrNull() ?: 0
                }
            }
            return Result.success(steps)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun insertSteps(
        context: Context,
        beginTime: Long,
        endTime: Long,
        mode: Int,
        steps: Int
    ): Result<Boolean> {
        try {
            if (getSystem() != "Xiaomi") return Result.failure(Exception("非小米/红米设备"))
            val values = ContentValues().apply {
                put("_begin_time", beginTime)
                put("_end_time", endTime)
                put("_mode", mode)
                put("_steps", steps)
            }

            val resultUri = context.contentResolver.insert(URI, values)

            return if (resultUri != null) {
                Result.success(true)
            } else {
                Result.failure(Exception("数据插入失败"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun insertStepsWithSystemUser(
        beginTime: Long,
        endTime: Long,
        steps: Int,
        mode: Int
    ): Result<Boolean> {
        val command = "su 1000 -c \"content insert --user 0 " +
                "--uri content://com.miui.providers.steps/item " +
                "--bind _begin_time:s:$beginTime " +
                "--bind _end_time:s:$endTime " +
                "--bind _mode:i:$mode " +
                "--bind _steps:i:$steps\""

        try {
            if (getSystem() != "Xiaomi") return Result.failure(Exception("非小米/红米设备"))
            val process = Runtime.getRuntime().exec("su")
            val os = process.outputStream.bufferedWriter()
            os.write(command + "\n")
            os.write("exit\n")
            os.flush()
            os.close()

            val resultText = process.inputStream.bufferedReader().use { it.readText() }
            val errorText = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            return if (exitCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("插入失败 (Code $exitCode): $errorText"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}