package com.xhand.hnu2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private val retrofitNews = Retrofit.Builder()
        .baseUrl("https://www.htu.edu.cn/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> createService(clazz: Class<T>): T {
        return retrofitNews.create(clazz)
    }


    private val retrofitLogin = Retrofit.Builder()
        .baseUrl("https://jwc.htu.edu.cn/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> LoginService(clazz: Class<T>): T {
        return retrofitLogin.create(clazz)
    }



    private val retrofitUpdate = Retrofit.Builder()
        .baseUrl("https://gitee.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> updateService(clazz: Class<T>): T {
        return retrofitUpdate.create(clazz)
    }



    private val retrofitNewsList = Retrofit.Builder()
        .baseUrl("https://www.htu.edu.cn/")
        .build()
    fun <T> newsService(clazz: Class<T>): T {
        return retrofitNewsList.create(clazz)
    }

}