package com.smart.htu.api.network

import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayEntity
import com.smart.htu.api.module.NoticeEntity
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.api.module.VersionEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {

    @GET("notice")
    fun getNotice(): Call<NoticeEntity>

    @POST("latest")
    fun getUpdate(
        @Body versionCode: VersionEntity = VersionEntity()
    ): Call<UpdateEntity>

    @GET("holiday")
    fun getHoliday(
        @Query("date") date: String,
    ): Call<HolidayEntity>

    @POST("feedback")
    fun feedback(@Body feedback: FeedbackEntity): Call<FeedbackRes>

}