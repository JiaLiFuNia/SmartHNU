package com.smart.htu.screens.application

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.smart.htu.R
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationEntity(
    val enabled: Boolean = true,
    val guestMode: Boolean = true,
    val loginMode: LoginMode = LoginMode.NONE,
    val label: Int,
    @param:DrawableRes val outlinedIcon: Int,
    @param:DrawableRes val icon: Int? = null,
    val iconColor: ApplicationColor? = ApplicationColor.GRAY,
    val routeType: RouteType,
    val url: String? = null,
    val screenRoute: NavKey? = null,
    val category: ApplicationCategory
) {

    enum class ApplicationColor(val color: Long) {
        RED(0xFFFA321B),
        ORANGE(0xFFFF8615),
        BLUE(0xFF3482FF),
        PURPLE(0xFF8482FF),
        YELLOW(0xFFFFB21E),
        GREEN(0xFF36D267),
        GRAY(0xFF8C93B0)
    }

    enum class ApplicationCategory(@param:StringRes val category: Int) {
        STUDY(R.string.study),
        CAMPUS(R.string.campus),
        ACADEMIC_AFFAIRS(R.string.academic_affairs),
        TOOLS(R.string.tool)
    }

    enum class RouteType {
        Url, // 网页
        Screen, // 页面
        ExternalApp, // 应用
    }

    enum class LoginMode {
        NONE, // 不需要登录
        COMMON, // 通用登录
        AUTH_SERVER, // 统一认证登录
        SECOND_CLASS, // 二课登录
    }

}