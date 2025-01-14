package com.smart.htu.api.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LibraryService {

    @GET("m/weixin/wsearch.action")
    suspend fun librarySearch(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("t") type: String = "any"
    ): Response<ResponseBody>

    @GET("m/weixin/wdetail.action")
    suspend fun libraryBookDetails(
        @Query("id") id: String
    ): Response<ResponseBody>

}