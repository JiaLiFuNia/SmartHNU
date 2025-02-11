package com.smart.htu.utils

import com.smart.htu.R
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.navigation.Destinations

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
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_5vgVGlXsaUEmCUjnaEvLJL"
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

        val ALL_APP_LIST = listOf(
            SmallCardContent(
                guestEnable = false,
                icon = R.drawable.today_24px,
                label = R.string.today_course,
            ),
            SmallCardContent(
                guestEnable = false,
                label = R.string.dorm_air_conditioner,
                icon = R.drawable.bolt_24px,
                route = Destinations.AirCondition.route
            ),
            SmallCardContent(
                guestEnable = false,
                label = R.string.classroom_search,
                icon = R.drawable.apartment_24px,
                route = Destinations.ClassroomSearch.route
            ),
            SmallCardContent(
                guestEnable = true,
                label = R.string.book_search,
                icon = R.drawable.book_4_24px,
                route = Destinations.LibrarySearch.route
            ),
            SmallCardContent(
                guestEnable = false,
                icon = R.drawable.finance_24px,
                label = R.string.course_grade,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.near_me_24px,
                label = R.string.live_service,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.bathtub_24px,
                description = "支付宝-卡博士",
                label = R.string.shower_water,
                appUrl = SHOWER_ALIPAY_URL
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.water_voc_24px,
                description = "支付宝-胖乖生活",
                label = R.string.water_washer,
                appUrl = HOT_WATER_WASHER_ALIPAY_URL
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.format_paint_24px,
                label = R.string.second_class,
                route = null
            ),
            SmallCardContent(
                guestEnable = true,
                icon = R.drawable.book_4_24px,
                label = R.string.textbook_select,
                route = null
            )
        )
        val INIT_COMMON_APP_LIST = listOf(
            ALL_APP_LIST[1],
            ALL_APP_LIST[2],
            ALL_APP_LIST[3]
        )

    }


}
