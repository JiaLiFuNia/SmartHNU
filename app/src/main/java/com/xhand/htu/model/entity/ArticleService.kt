package com.xhand.htu.model.entity

import retrofit2.http.GET

interface ArticleService {

    @GET("newslist.json")
    suspend fun list():ArticleListResponse

    companion object{
        fun instance(): ArticleService{
            return Network.createService(ArticleService::class.java)
        }
    }
}