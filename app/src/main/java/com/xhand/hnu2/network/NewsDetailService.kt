package com.xhand.hnu2.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsDetailService {

    @GET()
    suspend fun getNewsDetail(@Url url: String): Response<ResponseBody>

    companion object {
        fun instance(): NewsDetailService {
            return Network.detailService(NewsDetailService::class.java)
        }
    }

}