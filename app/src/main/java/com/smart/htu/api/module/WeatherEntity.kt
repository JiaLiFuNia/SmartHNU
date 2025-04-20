package com.smart.htu.api.module

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("now") val now: WeatherNowData
)

data class WeatherNowData(
    @SerializedName("temp") val temperature: String, //温度
    @SerializedName("feelsLike") val feelsLike: String, //体感温度
    @SerializedName("text") val weather: String,//多云
    @SerializedName("windDir") val windDir: String, //西北风
    @SerializedName("windScale") val windScale: String, //风级数
    @SerializedName("humidity") val humidity: String, //湿度
    @SerializedName("icon") val icon: String //图标代码
){
    @SuppressLint("DiscouragedApi")
    fun getIconResourceId(context: Context): Int {
        return context.resources.getIdentifier("qweather$icon", "drawable", context.packageName)
    }
}