package com.xhand.hnu.network

import com.xhand.hnu.model.entity.HolidayEntity
import retrofit2.http.GET

interface HolidayService {
    @GET()
    suspend fun holiday(): HolidayEntity

    /*companion object {
        fun instance(): HolidayService {
            return Network.holidayService(HolidayService::class.java)
        }
    }*/
}