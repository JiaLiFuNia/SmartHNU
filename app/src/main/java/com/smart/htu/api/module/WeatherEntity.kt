package com.smart.htu.api.module

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.annotations.SerializedName
import com.smart.htu.R

data class NowWeatherResponse(
    @SerializedName("now") val now: NowWeatherData
)

data class NowWeatherData(
    @SerializedName("temp") val temperature: String, //温度
    @SerializedName("feelsLike") val feelsLike: String, //体感温度
    @SerializedName("text") val weather: String,//多云
    @SerializedName("windDir") val windDir: String, //西北风
    @SerializedName("windScale") val windScale: String, //风级数
    @SerializedName("humidity") val humidity: String, //湿度
    @SerializedName("icon") val icon: String //图标代码
) {
    val iconResId: Int
        get() = when (icon) {
            "100" -> R.drawable.icon_sunny
            "150" -> R.drawable.icon_sunny_night
            "101" -> R.drawable.icon_cloudy
            "153" -> R.drawable.icon_cloudy_night
            "104" -> R.drawable.icon_overcast
            "305" -> R.drawable.icon_light_rain
            "306" -> R.drawable.icon_moderate_rain
            "307" -> R.drawable.icon_heavy_rain
            "400" -> R.drawable.icon_light_snow
            "401" -> R.drawable.icon_moderate_snow
            "402" -> R.drawable.icon_heavy_snow
            "404" -> R.drawable.icon_rain_snow
            "456" -> R.drawable.icon_ice_rain
            "302" -> R.drawable.icon_t_storm
            "501" -> R.drawable.icon_fog
            "407" -> R.drawable.icon_sand
            else -> R.drawable.icon_sunny
        }
}

data class WarningWeatherResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("warning") val warning: List<WarningWeatherData>
)

/*"id": "10102010020230403103000500681616",
"sender": "上海中心气象台",
"pubTime": "2023-04-03T10:30+08:00",
"title": "上海中心气象台发布大风蓝色预警[Ⅳ级/一般]",
"startTime": "2023-04-03T10:30+08:00",
"endTime": "2023-04-04T10:30+08:00",
"status": "active",
"level": "",
"severity": "Minor",
"severityColor": "Blue",
"type": "1006",
"typeName": "大风",
"urgency": "",
"certainty": "",
"text": "上海中心气象台2023年04月03日10时30分发布大风蓝色预警[Ⅳ级/一般]：受江淮气旋影响，预计明天傍晚以前本市大部地区将出现6级阵风7-8级的东南大风，沿江沿海地区7级阵风8-9级，请注意防范大风对高空作业、交通出行、设施农业等的不利影响。",
"related": ""*/
data class WarningWeatherData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("text") val content: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("severity") val severity: String,
    @SerializedName("severityColor") val severityColor: String,
    @SerializedName("typeName") val typeName: String
)