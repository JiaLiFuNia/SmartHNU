package com.xhand.hnu.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GuideService {
    @GET("xhand_xbh/hnu/raw/master/guide.txt")
    suspend fun guide(): Response<ResponseBody>

    companion object {
        fun instance(): GuideService {
            return Network.guideService(GuideService::class.java)
        }
    }
}