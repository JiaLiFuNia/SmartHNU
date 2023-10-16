package com.xhand.htu.model.entity.ActiveArticle

import com.xhand.htu.model.entity.HomeArticle.ActiveArticleListResponse
import com.xhand.htu.model.entity.Network
import retrofit2.http.GET

interface ActiveArticleService {
    @GET("activenewslist.json")
    suspend fun activelist(): ActiveArticleListResponse

    companion object{
        fun instance(): ActiveArticleService {
            return Network.createService(ActiveArticleService::class.java)
        }
    }
}