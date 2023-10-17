package com.xhand.htu.model.entity.HomeArticle

import com.xhand.htu.model.entity.Network
import retrofit2.http.GET

interface OtherArticleService {

    @GET("othernewslist.json")
    suspend fun otherlist(): OtherArticleListResponse

    companion object{
        fun instance(): OtherArticleService {
            return Network.createService(OtherArticleService::class.java)
        }
    }
}