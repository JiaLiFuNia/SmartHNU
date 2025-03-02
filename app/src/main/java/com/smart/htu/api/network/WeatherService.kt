package com.smart.htu.api.network

import com.smart.htu.api.module.WeatherResponse
import com.smart.htu.utils.JWTUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherService {

    //实时天气
    @GET("weather/now")
    fun getWeather(
        @Query("location") locationID : String = "113.91,35.33",
        @Header("Authorization") authorization : String = JWTUtil.getQWeatherAuth()
    ) : Call<WeatherResponse>

}