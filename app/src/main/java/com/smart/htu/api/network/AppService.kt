package com.smart.htu.api.network

import com.smart.htu.api.module.ConfigRes
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayRes
import com.smart.htu.api.module.NoticeRes
import com.smart.htu.api.module.UpdateRes
import com.smart.htu.api.module.VersionEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {

    @GET("notice")
    suspend fun getNotice(): Response<NoticeRes>

    @POST("latest")
    suspend fun getUpdate(
        @Body versionCode: VersionEntity = VersionEntity()
    ): Response<UpdateRes>

    @GET("holiday")
    suspend fun getHoliday(
        @Query("date") date: String,
    ): Response<HolidayRes>

    @POST("feedback")
    suspend fun feedback(@Body feedback: FeedbackEntity): Response<FeedbackRes>

    @GET("config")
    suspend fun getConfig(): Response<ConfigRes>

}