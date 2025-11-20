package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.ConfigEntity
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayEntity
import com.smart.htu.api.module.UpdateRes
import com.smart.htu.api.network.AppService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNetworkRepo @Inject constructor(
    private val appService: AppService
) {

    suspend fun configService(): Result<ConfigEntity> {
        try {
            val res = appService.getConfig()
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        Result.success(body.data)
                    } else {
                        Result.failure(Exception("获取失败，请稍后重试，错误码：${res.code()}"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请稍后重试，错误码：${res.code()}"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun feedbackService(content: FeedbackEntity): Result<FeedbackRes> {
        try {
            val res = appService.feedback(content)
            Log.e("TAG666 ", "feedbackService: $res")
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        if (body.data) Result.success(body)
                        else Result.failure(Exception(body.message))
                    } else {
                        Result.failure(Exception("提交失败，请稍后重试，错误码：${res.code()}"))
                    }
                }

                else -> {
                    Result.failure(Exception("提交失败，请稍后重试，错误码：${res.code()}"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun holidayService(date: String): Result<HolidayEntity> {
        try {
            val res = appService.getHoliday(date)
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        Result.success(body.data)
                    } else {
                        Result.failure(Exception("获取失败，请稍后重试，错误码：${res.code()}"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请稍后重试，错误码：${res.code()}"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateService(): Result<UpdateRes> {
        try {
            val res = appService.getUpdate()
            return when (res.code()) {
                200 -> {
                    val updateRes = res.body()
                    if (updateRes != null) {
                        Result.success(updateRes)
                    } else {
                        Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请稍后重试，错误码：${res.code()}"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}
