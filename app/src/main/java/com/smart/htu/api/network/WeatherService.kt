package com.smart.htu.api.network

import com.smart.htu.api.module.WeatherResponse
import com.smart.htu.utils.GenerateQWeatherJWT
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherService {

    //实时天气
    @GET("weather/now")
    suspend fun getWeather(
        @Query("location") locationID: String = "113.91,35.33",
        @Header("Authorization") authorization: String = GenerateQWeatherJWT.getQWeatherJWT()
    ): WeatherResponse

    //天气预警
    @GET("warning/now")
    suspend fun getWeatherWarn(
        @Query("location") locationID: String = "113.91,35.33",
        @Header("Authorization") authorization: String = GenerateQWeatherJWT.getQWeatherJWT()
    ): ResponseBody

}