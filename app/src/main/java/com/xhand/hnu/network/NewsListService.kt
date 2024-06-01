package com.xhand.hnu.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url

interface NewsListService {
    @GET("{type}/list{page}.htm")
    suspend fun getNewsList(
        @Path("page") page: String,
        @Path("type") type: String
    ): Response<ResponseBody>

    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309092b) XWEB/8555 Flue",
        "Referer: https://www.htu.edu.cn/teaching/main.htm"
    )
    @GET("teaching/{type}/list{page}.htm")
    suspend fun getTeacherNewsList(
        @Path("page") page: String,
        @Path("type") type: String
    ): Response<ResponseBody>


    @GET()
    suspend fun getPicList(@Url url: String): Response<ResponseBody>

    companion object {
        fun instance(): NewsListService {
            return Network.newsService(NewsListService::class.java)
        }
    }
}