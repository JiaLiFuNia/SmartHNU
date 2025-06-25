package com.smart.htu.screens.application.entity

import androidx.annotation.DrawableRes
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
)

enum class ApplicationCategory(val category: String) {
    AI("师大 AI"),
    CAMPUS("校园"),
    ACADEMIC_AFFAIRS("教务"),
    OTHERS("其他")
}

enum class RouteType {
    URL, // 网页
    SCREEN, // 页面
    ALIPAY, // 支付宝
    APP // 应用
}