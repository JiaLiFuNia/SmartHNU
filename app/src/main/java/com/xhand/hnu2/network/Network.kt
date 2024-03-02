package com.xhand.hnu2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val baseurl = "https://www.htu.edu.cn/"

    private val retrofitNews = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(clazz: Class<T>): T {
        return retrofitNews.create(clazz)
    }

    private val retrofitUpdate = Retrofit.Builder()
        .baseUrl("https://gitee.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> updateService(clazz: Class<T>): T {
        return retrofitUpdate.create(clazz)
    }

}