package com.smart.htu.utils

import com.smart.htu.R
import com.smart.htu.api.module.BuildingEntity

class Constants {
    companion object {
        const val HENAN_NORMAL_UNIVERSITY = "https://www.htu.edu.cn/"
        const val RETRIEVE_PASSWORD =
            "https://authserver2.htu.edu.cn/retrieve-password/retrievePassword/index.html"
        const val ACADEMIC_URL = "https://jwc.htu.edu.cn/"
        const val ACADEMIC_APP_URL = "https://jwc.htu.edu.cn/app/"
        const val SHOWER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_3Zz793BHBpUQsAf6r8hLXZ"
        const val HOT_WATER_WASHER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_5a64m9UUCdhd1jOgkBVIHm"
        val BUILDING_LIST = listOf(
            BuildingEntity("104", "启智楼"),
            BuildingEntity("107", "新五五四楼"),
            BuildingEntity("102", "文渊楼"),
            BuildingEntity("310", "文昌楼（东综）")
        )

        val COURSE_PERIOD = mapOf(
            R.string.period_1_2 to "0102",
            R.string.period_3_4 to "0304",
            R.string.period_5_6 to "0506",
            R.string.period_7_8 to "0708",
            R.string.period_9_10 to "0910",
        )
    }


}
