package com.smart.htu.screens.application

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.smart.htu.R

@kotlinx.serialization.Serializable
data class ApplicationEntity(
    val enabled: Boolean = true,
    val guestMode: Boolean = true,
    val loginMode: LoginMode = LoginMode.NONE,
    val label: Int,
    val description: String? = null,
    @DrawableRes val icon: Int,
    @DrawableRes val trailingIcon: Int? = null,
    val routeType: RouteType?,
    val route: String?,
    val category: ApplicationCategory
) {

    enum class ApplicationCategory(@StringRes val category: Int) {
        STUDY(R.string.study),
        CAMPUS(R.string.campus),
        ACADEMIC_AFFAIRS(R.string.academic_affairs),
        TOOLS(R.string.tool)
    }

    enum class RouteType {
        Url, // 网页
        Screen, // 页面
        ALIPAY, // 支付宝
        ExternalApp, // 应用,
        BottomSheet,
        Dialog
    }

    enum class LoginMode {
        NONE, // 不需要登录
        COMMON, // 通用登录
        AUTH_SERVER, // 统一认证登录
        SECOND_CLASS, // 二课登录
        LIBRARY // 图书馆登录
    }

}