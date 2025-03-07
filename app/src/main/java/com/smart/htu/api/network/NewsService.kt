package com.smart.htu.api.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface NewsService {

    @GET("{academic}/{type}/list{page}.psp")
    fun getNewsList(
        @Path("academic") academic: String = "",
        @Path("page") page: String,
        @Path("type") type: String
    ): Call<ResponseBody>

}