package com.smart.htu.repo

import com.smart.htu.api.module.ConfigEntity
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayEntity
import com.smart.htu.api.network.AppService
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNetworkRepo @Inject constructor(
    private val appService: AppService
) {

    suspend fun configService(): Result<ConfigEntity> {
        try {
            val call = appService.getConfig()
            val res = call.awaitResponse()
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        Result.success(body.data)
                    } else {
                        Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请切换至移动网络后重试"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun feedbackService(content: FeedbackEntity): Result<FeedbackRes> {
        try {
            val call = appService.feedback(content)
            val res = call.awaitResponse()
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        if (body.success) Result.success(body)
                        else Result.failure(Exception(body.message))
                    } else {
                        Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请切换至移动网络后重试"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun holidayService(date: String): Result<HolidayEntity> {
        try {
            val call = appService.getHoliday(date)
            val res = call.awaitResponse()
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        Result.success(body.data)
                    } else {
                        Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Result.failure(Exception("获取失败，请切换至移动网络后重试"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}
