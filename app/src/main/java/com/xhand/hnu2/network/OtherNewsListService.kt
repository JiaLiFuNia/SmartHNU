package com.xhand.hnu2.network

import com.xhand.hnu2.model.entity.SearchResultEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OtherNewsListService {
    @GET("teaching/{type}/list{page}.htm")
    suspend fun getNewsList(@Path("page") page: String, @Path("type") type: String): Response<ResponseBody>
    companion object {
        fun instance(): OtherNewsListService {
            return Network.newsService(OtherNewsListService::class.java)
        }
    }
}