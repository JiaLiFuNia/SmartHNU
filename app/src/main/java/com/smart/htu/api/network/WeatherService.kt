package com.smart.htu.api.network

import com.smart.htu.api.module.NowWeatherResponse
import com.smart.htu.api.module.WarningWeatherResponse
import com.smart.htu.utils.GenerateQWeatherJWT
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherService {

    //实时天气
    @GET("weather/now")
    suspend fun getNowWeather(
        @Query("location") locationID: String = "113.91,35.33",
        @Header("Authorization") authorization: String = GenerateQWeatherJWT.getQWeatherJWT()
    ): NowWeatherResponse

    //天气预警
    @GET("warning/now")
    suspend fun getWarningWeather(
        @Query("location") locationID: String = "113.91,35.33",
        @Header("Authorization") authorization: String = GenerateQWeatherJWT.getQWeatherJWT()
    ): WarningWeatherResponse

}