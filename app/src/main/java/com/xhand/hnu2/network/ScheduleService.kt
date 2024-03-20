package com.xhand.hnu2.network

import com.xhand.hnu2.model.entity.GradeDetailPost
import com.xhand.hnu2.model.entity.ScheduleEntity
import com.xhand.hnu2.model.entity.SchedulePost
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ScheduleService {

    @POST("dev-api/appapi/appqxkb/datagrkb")
    suspend fun scheduleDetail(
        @Body body: SchedulePost,
        @Header("Token") token: String
    ): ScheduleEntity

    companion object {
        fun instance(): ScheduleService {
            return Network.loginService(ScheduleService::class.java)
        }
    }

}