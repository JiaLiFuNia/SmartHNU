package com.xhand.hnu.network

import com.xhand.hnu.model.entity.Update
import retrofit2.http.GET

interface UpdateService {
    @GET("xhand_xbh/hnu/raw/master/version.json")
    suspend fun update(): Update

    companion object {
        fun instance(): UpdateService {
            return Network.updateService(UpdateService::class.java)
        }
    }
}