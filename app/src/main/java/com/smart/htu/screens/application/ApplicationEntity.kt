package com.smart.htu.screens.application

@kotlinx.serialization.Serializable
data class ApplicationEntity(
    val guestMode: Boolean = true,
    val label: Int,
    val description: String? = null,
    @androidx.annotation.DrawableRes val icon: Int,
    @androidx.annotation.DrawableRes val trailingIcon: Int? = null,
    val routeType: RouteType?,
    val route: String?,
    val category: ApplicationCategory
) {

    enum class ApplicationCategory(@androidx.annotation.StringRes val category: Int) {
        AI(com.smart.htu.R.string.hnu_ai),
        CAMPUS(com.smart.htu.R.string.campus),
        ACADEMIC_AFFAIRS(com.smart.htu.R.string.academic_affairs),
        OTHERS(com.smart.htu.R.string.other)
    }

    enum class RouteType {
        URL, // 网页
        SCREEN, // 页面
        ALIPAY, // 支付宝
        APP // 应用
    }

}