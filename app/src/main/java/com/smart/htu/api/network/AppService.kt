package com.smart.htu.api.network

import com.smart.htu.api.module.ConfigRes
import com.smart.htu.api.module.FeedbackEntity
import com.smart.htu.api.module.FeedbackRes
import com.smart.htu.api.module.HolidayRes
import com.smart.htu.api.module.NoticeRes
import com.smart.htu.api.module.UpdateRes
import com.smart.htu.api.module.VersionEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {

    @GET("notice")
    fun getNotice(): Call<NoticeRes>

    @POST("latest")
    fun getUpdate(
        @Body versionCode: VersionEntity = VersionEntity()
    ): Call<UpdateRes>

    @GET("holiday")
    fun getHoliday(
        @Query("date") date: String,
    ): Call<HolidayRes>

    @POST("feedback")
    fun feedback(@Body feedback: FeedbackEntity): Call<FeedbackRes>

    @GET("config")
    fun getConfig(): Call<ConfigRes>

}