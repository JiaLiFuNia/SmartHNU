package com.xhand.hnu.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Network {
    private val retrofitNews = Retrofit.Builder()
        .baseUrl("https://www.htu.edu.cn/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val retrofitLogin = Retrofit.Builder()
        .baseUrl("https://jwc.htu.edu.cn/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val retrofitUpdate = Retrofit.Builder()
        .baseUrl("https://gitee.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val retrofitGuide = Retrofit.Builder()
        .baseUrl("https://gitee.com/")
        .build()
    /*private val retrofitHoliday = Retrofit.Builder()
        .baseUrl("https://timor.tech/api/holiday/info")
        .addConverterFactory(GsonConverterFactory.create())
        .build()*/

    private val retrofitNewsList = Retrofit.Builder()
        .baseUrl("https://www.htu.edu.cn/")
        .build()

    fun <T> createService(clazz: Class<T>): T {
        return retrofitNews.create(clazz)
    }

    fun <T> loginService(clazz: Class<T>): T {
        return retrofitLogin.create(clazz)
    }

    fun <T> updateService(clazz: Class<T>): T {
        return retrofitUpdate.create(clazz)
    }

    fun <T> guideService(clazz: Class<T>): T {
        return retrofitGuide.create(clazz)
    }
    fun <T> newsService(clazz: Class<T>): T {
        return retrofitNewsList.create(clazz)
    }

    fun <T> detailService(clazz: Class<T>): T {
        return retrofitNewsList.create(clazz)
    }


    /*fun <T> holidayService(clazz: Class<T>): T {
        return retrofitHoliday.create(clazz)
    }*/

}