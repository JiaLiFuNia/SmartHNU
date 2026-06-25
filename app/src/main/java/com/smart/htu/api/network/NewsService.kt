package com.smart.htu.api.network

import com.smart.htu.api.module.SearchResultEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
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

    @POST("_web/_search/api/searchCon/create.rst")
    @FormUrlEncoded
    suspend fun searchService(
        @Query("_p") type: String = "YXM9MyZ0PTQ0NDQmZD0xMjk3MCZwPTEmbT1TTiY_",
        @Field("searchInfo") searchInfo: String,
        @Query("tt") t: Double = Random.nextDouble(),
        @Header("referer") referer: String = "https://www.htu.edu.cn/_web/_search/api/search/new.rst?locale=zh_CN&request_locale=zh_CN&context=&_p=${type}"
    ): Response<SearchResultEntity>

}