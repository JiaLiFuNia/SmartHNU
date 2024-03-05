package com.xhand.hnu2.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsListService {
    @GET("{type}/list{page}.htm")
    suspend fun getNewsList(@Path("page") page: String, @Path("type") type: String): Response<ResponseBody>
    companion object {
        fun instance(): NewsListService {
            return Network.newsService(NewsListService::class.java)
        }
    }
}