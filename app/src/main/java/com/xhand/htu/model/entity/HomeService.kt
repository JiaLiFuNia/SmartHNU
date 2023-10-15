package com.xhand.htu.model.entity

import retrofit2.http.GET

interface HomeService {

    @GET("tabslist.json")
    suspend fun category():CategoryResponse

    companion object{
        fun instance(): HomeService{
            return Network.createService(HomeService::class.java)
        }
    }
}