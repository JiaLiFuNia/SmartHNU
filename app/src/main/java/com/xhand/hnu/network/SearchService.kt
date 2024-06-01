package com.xhand.hnu.network

import com.xhand.hnu.model.entity.SearchResultEntity
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SearchService {
    @Headers("referer: https://www.htu.edu.cn/_web/_search/api/search/new.rst?locale=zh_CN&request_locale=zh_CN&_p=YXM9MyZ0PTQ0NDQmZD0xMjk3MCZwPTEmbT1TTiY_")
    @POST("_web/_search/api/searchCon/create.rst")
    @FormUrlEncoded
    suspend fun pushPost(
        @Field("searchInfo") searchInfo: String,
        @Field("_p") _p: String = "YXM9MyZ0PTQ0NDQmZD0xMjk3MCZwPTEmbT1TTiY_",
        @Field("t") t: Double = 0.8995560840992034
    ): Response<SearchResultEntity>

    companion object {
        fun instance(): SearchService {
            return Network.createService(SearchService::class.java)
        }
    }
}