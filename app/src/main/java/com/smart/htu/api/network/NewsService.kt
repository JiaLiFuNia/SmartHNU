package com.smart.htu.api.network

import com.smart.htu.api.module.SearchResultEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import kotlin.random.Random

interface NewsService {

    @GET("{academic}/{type}/list{page}.psp")
    suspend fun getNewsList(
        @Path("academic") academic: String = "",
        @Path("page") page: String,
        @Path("type") type: String
    ): Response<ResponseBody>

    @GET
    suspend fun getNewsDetail(@Url url: String): ResponseBody

    @POST
    suspend fun getVisitNumbers(
        @Url url: String,
        @Header("referer") referer: String
    ): ResponseBody

    @Headers("referer: https://www.htu.edu.cn/_web/_search/api/search/new.rst?locale=zh_CN&request_locale=zh_CN&_p=YXM9MyZ0PTQ0NDQmZD0xMjk3MCZwPTEmbT1TTiY_")
    @POST("_web/_search/api/searchCon/create.rst?_p=YXM9MyZ0PTQ0NDQmZD0xMjk3MCZwPTEmbT1TTiY_")
    @FormUrlEncoded
    suspend fun searchService(
        @Field("searchInfo") searchInfo: String,
        @Field("tt") t: Double = Random.nextDouble()
    ): Response<SearchResultEntity>

}