package com.xhand.htu.model.entity

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    private const val baseUrl = "http://xubohan04.tk/api/"

    private  val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        ).build()

    fun <T> createService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }
}