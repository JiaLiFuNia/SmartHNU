package com.smart.htu.screens.application.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.smart.htu.R
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationEntity(
    val guestEnable: Boolean = true,
    val label: Int,
    val description: String? = null,
    @DrawableRes val icon: Int,
    @DrawableRes val trailingIcon: Int? = null,
    val routeType: RouteType?,
    val route: String?,
    val category: ApplicationCategory
) {

    enum class ApplicationCategory(@StringRes val category: Int) {
        AI(R.string.hnu_ai),
        CAMPUS(R.string.campus),
        ACADEMIC_AFFAIRS(R.string.academic_affairs),
        OTHERS(R.string.other)
    }

    enum class RouteType {
        URL, // 网页
        SCREEN, // 页面
        ALIPAY, // 支付宝
        APP // 应用
    }

}