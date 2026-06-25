package com.smart.htu.utils

import com.smart.htu.R
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.ApplicationEntity.ApplicationCategory
import com.smart.htu.screens.application.ApplicationEntity.LoginMode
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import com.smart.htu.screens.navigation.Route

class Constants {
    companion object {
        const val GITHUB_PERSON_URL = "https://github.com/JiaLiFuNia"
        const val GITHUB_PROJECT_URL = "https://github.com/JiaLiFuNia/SmartHNU"
        const val GITHUB_PROJECT_URL_CN = "https://gitee.com/JiaLiFuNia/SmartHNU"

        const val AUTH_BASE_URL = "https://authserver2.htu.edu.cn/"
        const val AUTH_LOGIN_URL = AUTH_BASE_URL + "authserver/login?service="
        const val RETRIEVE_PASSWORD = AUTH_BASE_URL + "retrieve-password/passwordMobile/index.html"

        const val HENAN_NORMAL_UNIVERSITY = "https://www.htu.edu.cn/"
        const val ACADEMIC_URL = "https://jwc.htu.edu.cn/"
        const val ACADEMIC_APP_URL = "https://jwc.htu.edu.cn/app/"
        const val SECOND_CLASS_URL = "http://dekt.htu.edu.cn/"

        const val SMH_URL = "https://smh.xubohan04.tk/"

        const val SHOWER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_3Zz793BHBpUQsAf6r8hLXZ"
        const val HOT_WATER_WASHER_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_5vgVGlXsaUEmCUjnaEvLJL"
        const val WAN_XIAO_ALIPAY_URL =
            "alipays://platformapi/startapp?appId=20000067&url=https://ur.alipay.com/_5ClmUoZYbBgQMkYRw4ew41"
        const val PINDUODUO_URL =
            "pinduoduo://com.xunmeng.pinduoduo/mdkd/package?tab=ID_CODE&entry_source=11&refer_page_name=login&refer_page_id=10169_1751901995470_3gdprcfjhr&refer_page_sn=10169"
        const val TAOBAO_URL =
            "https://pages-fast.m.taobao.com/wow/z/uniapp/1011717/last-mile-fe/end-collect-platform/identity-code?x-ssr=true"
        const val CAINIAO_URL = "guoguo://go/home_page"

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
                outlinedIcon = R.drawable.water_ec_24px,
                icon = R.drawable.water_ec_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.GREEN,
                routeType = RouteType.Screen,
                screenRoute = Route.AirCondition,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                label = R.string.classroom_search,
                outlinedIcon = R.drawable.apartment_24px,
                icon = R.drawable.apartment_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.PURPLE,
                routeType = RouteType.Screen,
                screenRoute = Route.ClassroomSearch,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                label = R.string.book_search,
                outlinedIcon = R.drawable.book_4_24px,
                icon = R.drawable.book_4_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.YELLOW,
                loginMode = LoginMode.AUTH_SERVER,
                routeType = RouteType.Screen,
                screenRoute = Route.LibrarySearch,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.finance_24px,
                icon = R.drawable.finance_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.ORANGE,
                label = R.string.course_grade,
                routeType = RouteType.Screen,
                screenRoute = Route.Grade,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = true,
                outlinedIcon = R.drawable.near_me_24px,
                icon = R.drawable.near_me_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.GREEN,
                label = R.string.campus_life,
                routeType = RouteType.Screen,
                screenRoute = Route.CampusLife,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.SECOND_CLASS,
                outlinedIcon = R.drawable.format_paint_24px,
                icon = R.drawable.format_paint_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.ORANGE,
                label = R.string.second_class,
                routeType = RouteType.Screen,
                screenRoute = Route.SecondClass,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.book_4_24px,
                icon = R.drawable.book_4_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.BLUE,
                label = R.string.textbook_select,
                routeType = RouteType.Screen,
                screenRoute = Route.Textbook,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.person_check_24px,
                icon = R.drawable.person_check_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.ORANGE,
                label = R.string.teacher_evaluation,
                routeType = RouteType.Screen,
                screenRoute = Route.TeacherEvaluation,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.credit_card_24px,
                icon = R.drawable.credit_card_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.RED,
                label = R.string.school_card,
                routeType = RouteType.Screen,
                screenRoute = Route.CampusCard,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                enabled = true,
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.add_task_24px,
                icon = R.drawable.add_task_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.BLUE,
                label = R.string.select_course_assistance,
                routeType = RouteType.Screen,
                screenRoute = Route.CourseHelper,
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.school_24px,
                icon = R.drawable.school_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.BLUE,
                label = R.string.academic_affairs_system,
                routeType = RouteType.Url,
                url = AUTH_LOGIN_URL + "https://jwc.htu.edu.cn/new/ssoLogin",
                category = ApplicationCategory.ACADEMIC_AFFAIRS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.id_card_24px,
                icon = R.drawable.id_card_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.GREEN,
                label = R.string.smart_work,
                routeType = RouteType.Url,
                url = AUTH_LOGIN_URL + "http://ydxg.htu.edu.cn/land/caslogin?ref=%2Fquickgo%2Fz8n58998",
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.psychology_alt_24px,
                icon = R.drawable.psychology_alt_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.PURPLE,
                label = R.string.htu_helper,
                routeType = RouteType.Url,
                url = AUTH_LOGIN_URL + "https://ai.htu.edu.cn/api/cas",
                category = ApplicationCategory.TOOLS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.captive_portal_24px,
                icon = R.drawable.captive_portal_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.PURPLE,
                label = R.string.one_stop_service,
                routeType = RouteType.Url,
                url = AUTH_LOGIN_URL + "http://ehall2.htu.edu.cn/login?service=http://ehall2.htu.edu.cn/ywtb-mobile/index.html",
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.calendar_month_24px,
                icon = R.drawable.calendar_month_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.BLUE,
                label = R.string.course_table,
                routeType = RouteType.Screen,
                screenRoute = Route.CourseTable,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = true,
                outlinedIcon = R.drawable.globe_book_24px,
                icon = R.drawable.globe_book_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.GRAY,
                label = R.string.website_navigation,
                routeType = RouteType.Screen,
                screenRoute = Route.WebsiteNavigation,
                category = ApplicationCategory.TOOLS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.speaker_notes_24px,
                icon = R.drawable.speaker_notes_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.PURPLE,
                label = R.string.message_board,
                routeType = RouteType.Screen,
                screenRoute = Route.MessageBoard,
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                enabled = true,
                guestMode = false,
                outlinedIcon = R.drawable.lab_profile_24px,
                icon = R.drawable.lab_profile_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.BLUE,
                label = R.string.task_manager,
                routeType = RouteType.Screen,
                screenRoute = Route.TaskManager,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.AUTH_SERVER,
                outlinedIcon = R.drawable.construction_24px,
                icon = R.drawable.construction_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.GRAY,
                label = R.string.repair,
                routeType = RouteType.Url,
                url = AUTH_LOGIN_URL + "https://houqin.htu.edu.cn/authserver/caslogin?service=https://houqin.htu.edu.cn/h5/pages/rsp/myDeclare/repairDeclares?plat=weChat&plat=h5",
                category = ApplicationCategory.CAMPUS
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.COMMON,
                outlinedIcon = R.drawable.quick_reference_all_24px,
                icon = R.drawable.quick_reference_all_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.PURPLE,
                label = R.string.course_search,
                routeType = RouteType.Screen,
                screenRoute = Route.CourseSearch,
                category = ApplicationCategory.STUDY
            ),
            ApplicationEntity(
                guestMode = false,
                loginMode = LoginMode.NONE,
                outlinedIcon = R.drawable.sports_handball_24px,
                icon = R.drawable.sports_handball_24px_filled,
                iconColor = ApplicationEntity.ApplicationColor.YELLOW,
                label = R.string.physical_test,
                routeType = RouteType.Screen,
                screenRoute = Route.PhysicalTest,
                category = ApplicationCategory.TOOLS
            )
        )
        val INIT_COMMON_APP_LIST = listOf(
            ALL_APP_LIST[1],
            ALL_APP_LIST[2],
            ALL_APP_LIST[3],
            ALL_APP_LIST[4],
        )
    }


}
