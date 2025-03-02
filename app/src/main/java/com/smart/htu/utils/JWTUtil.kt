package com.smart.htu.utils

object JWTUtil {

    //用于生成和风天气密钥
    fun getQWeatherAuth() : String {
        return "Bearer " + GenerateQWeather().generate(
            "MC4CAQAwBQYDK2VwBCIEILLPuzDVvJ0tIE1/2wiDwUSwZr2Lwt/BaOxJ7sYaIPrk",
            "2AKUWC7K78",
            "CHPN45DFAX"
        )
    }

}