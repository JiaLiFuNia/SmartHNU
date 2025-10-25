package com.smart.htu.utils

import com.smart.htu.R
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.application.ApplicationEntity.LoginMode
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.navigation.Destinations

class Constants {
    companion object {
        const val GITHUB_PERSON_URL = "https://github.com/JiaLiFuNia"
        const val GITHUB_PROJECT_URL = "https://github.com/JiaLiFuNia/SmartHNU"
        const val GITHUB_PROJECT_URL_CN = "https://gitee.com/JiaLiFuNia/SmartHNU"

        const val AUTH_BASE_URL = "https://authserver2.htu.edu.cn/"
        const val AUTH_LOGIN_URL = AUTH_BASE_URL + "authserver/login?service="
        const val RETRIEVE_PASSWORD = AUTH_BASE_URL + "retrieve-password/passwordMobile/index.html"
        const val EHALL_APP_BASE_URL = "https://ehall2.htu.edu.cn/appShow?appId="

        const val HENAN_NORMAL_UNIVERSITY = "https://www.htu.edu.cn/"
        const val ACADEMIC_URL = "https://jwc.htu.edu.cn/"
        const val ACADEMIC_APP_URL = "https://jwc.htu.edu.cn/app/"
        const val SECOND_CLASS_URL = "http://dekt.htu.edu.cn/"

        const val SMH_URL = "https://xhand.edu.deal/"
        // "https://shtu.xubohan04.tk/"

        const val SHOWER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_3Zz793BHBpUQsAf6r8hLXZ"
        const val HOT_WATER_WASHER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_5vgVGlXsaUEmCUjnaEvLJL"
        const val PINDUODUO_URL =
            "pinduoduo://com.xunmeng.pinduoduo/mdkd/package?tab=ID_CODE&entry_source=11&refer_page_name=login&refer_page_id=10169_1751901995470_3gdprcfjhr&refer_page_sn=10169"
        const val TAOBAO_URL =
            "https://pages-fast.m.taobao.com/wow/z/uniapp/1011717/last-mile-fe/end-collect-platform/identity-code?x-ssr=true"

        val PULL_TO_REFRESH_TEXT = listOf("下拉刷新", "松开刷新", "正在刷新...", "刷新成功")

        val COURSE_PERIOD = mapOf(
            R.string.period_1_2 to "0102",
            R.string.period_3_4 to "0304",
            R.string.period_5_6 to "0506",
            R.string.period_7_8 to "0708",
            R.string.period_9_10 to "0910",
        )

        val ALL_APP_LIST = listOf(
            ApplicationEntity(
                guestMode = false,
                label = R.string.dorm_air_conditioner,
                icon = R.drawable.bolt_24px,
                routeType = RouteType.SCREEN,
                route = Destinations.AirCondition.route,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                label = R.string.classroom_search,
                icon = R.drawable.apartment_24px,
                routeType = RouteType.SCREEN,
                route = Destinations.ClassroomSearch.route,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = true,
                label = R.string.book_search,
                icon = R.drawable.book_4_24px,
                routeType = RouteType.SCREEN,
                route = Destinations.LibrarySearch.route,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                icon = R.drawable.finance_24px,
                label = R.string.course_grade,
                routeType = RouteType.SCREEN,
                route = Destinations.Grade.route,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = true,
                icon = R.drawable.near_me_24px,
                label = R.string.campus_life,
                routeType = RouteType.SCREEN,
                route = Destinations.CampusLife.route,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.SECOND_CLASS,
                icon = R.drawable.format_paint_24px,
                label = R.string.second_class,
                routeType = RouteType.URL,
                route = SECOND_CLASS_URL,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                icon = R.drawable.book_4_24px,
                label = R.string.textbook_select,
                routeType = RouteType.SCREEN,
                route = "textbook",
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                icon = R.drawable.person_check_24px,
                label = R.string.teacher_evaluation,
                routeType = RouteType.SCREEN,
                route = Destinations.TeacherEvaluation.route,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.credit_card_24px,
                label = R.string.school_card,
                routeType = RouteType.URL,
                route = "https://ehall2.htu.edu.cn/appShow?appId=6548421524823376",
                category = ApplicationCategory.TOOLS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.school_24px,
                label = R.string.academic_affairs_system,
                routeType = RouteType.URL,
                route = AUTH_LOGIN_URL + "https://jwc.htu.edu.cn/new/ssoLogin",
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.id_card_24px,
                label = R.string.smart_work,
                routeType = RouteType.URL,
                route = AUTH_LOGIN_URL + "http://ehall2.htu.edu.cn/login?service=http://ehall2.htu.edu.cn/appShow?appId=6689155909292538",
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.psychology_alt_24px,
                label = R.string.htu_helper,
                routeType = RouteType.URL,
                route = AUTH_LOGIN_URL + "https://ai.htu.edu.cn/api/cas",
                category = ApplicationCategory.TOOLS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.captive_portal_24px,
                label = R.string.one_stop_service,
                routeType = RouteType.URL,
                route = AUTH_LOGIN_URL + "https://ehall2.htu.edu.cn/ywtb-mobile/index.html#/OfficeHall",
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                icon = R.drawable.calendar_month_24px,
                label = R.string.course_table,
                routeType = RouteType.SCREEN,
                route = Destinations.CourseTable.route,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = true,
                icon = R.drawable.globe_book_24px,
                label = R.string.website_navigation,
                routeType = RouteType.SCREEN,
                route = Destinations.WebsiteNavigation.route,
                category = ApplicationCategory.TOOLS
            ),
            ApplicationEntity(
                guestMode = true,
                icon = R.drawable.speaker_notes_24px,
                label = R.string.message_board,
                routeType = RouteType.SCREEN,
                route = Destinations.MessageBoard.route,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                icon = R.drawable.lab_profile_24px,
                label = R.string.exam,
                routeType = RouteType.SCREEN,
                route = Destinations.ExamSchedule.route,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                icon = R.drawable.construction_24px,
                label = R.string.repair,
                routeType = RouteType.URL,
                route = AUTH_LOGIN_URL + "https://houqin.htu.edu.cn/authserver/caslogin?service=https://houqin.htu.edu.cn/h5/pages/rsp/myDeclare/repairDeclares?plat=weChat&plat=h5",
                category = ApplicationCategory.CAMPUS
            ),
            /*
                        ApplicationEntity(
                            guestMode = false,
                            icon = R.drawable.sports_handball_24px,
                            label = R.string.physical_test,
                            routeType = RouteType.APP,
                            route = Destinations.PhysicalTest.route,
                            category = ApplicationCategory.CAMPUS
                        )*/
        )
        val INIT_COMMON_APP_LIST = listOf(
            ALL_APP_LIST[1],
            ALL_APP_LIST[2],
            ALL_APP_LIST[3],
            ALL_APP_LIST[4],
        )
    }


}
