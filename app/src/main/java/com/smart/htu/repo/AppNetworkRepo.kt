package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.ConfigEntity
import com.smart.htu.api.module.CourseEntity
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayEntity
import com.smart.htu.api.module.NoticeRes
import com.smart.htu.api.module.ShareCourseEntity
import com.smart.htu.api.module.UpdateRes
import com.smart.htu.api.network.AppService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNetworkRepo @Inject constructor(
    private val appService: AppService
) {

    suspend fun shareCourseService(courseData: String): Result<String> {
        try {
            val res = appService.shareCourse(ShareCourseEntity(courseData))
            return when (res.code()) {
                200 -> {
                    val body = res.body()
                    if (body != null) {
                        if (body.code == 200) {
                            Result.success(body.data.shareCode)
                        } else {
                            Result.failure(Exception(body.message))
                        }
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

    suspend fun importSharedCourseService(shareCode: String): Result<Pair<String, List<List<List<CourseEntity>>>>> {
        try {
            val res = appService.importSharedCourse(shareCode)
            val body = res.body()
            return when (res.code()) {
                200 -> {
                    if (body != null) {
                        if (body.code == 200) {
                            Result.success(shareCode to body.courseScheduleData)
                        } else {
                            Result.failure(Exception(body.message))
                        }
                    } else {
                        Result.failure(Exception("请稍后重试，错误码：${res.code()}"))
                    }
                }

                else -> {
                    Result.failure(Exception("请稍后重试，错误码：${res.code()}"))
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

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

    suspend fun getNotice(): Result<NoticeRes> {
        try {
            val res = appService.getNotice()
            return when (res.code()) {
                200 -> {
                    val noticeRes = res.body()
                    if (noticeRes != null) {
                        Result.success(noticeRes)
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
