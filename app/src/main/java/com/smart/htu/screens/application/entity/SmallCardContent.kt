package com.smart.htu.screens.application.entity

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class SmallCardContent(
    val guestEnable: Boolean = true,
    val label: Int,
    val description: String? = null,
    @DrawableRes val icon: Int,
    val routeType: RouteType?,
    val route: String?,
    val category: SmallCardCategory
)

enum class SmallCardCategory(val category: String) {
    CAMPUS("校园"),
    ACADEMIC_AFFAIRS("教务"),
    OTHERS("其他")
}

enum class RouteType {
    URL,
    SCREEN,
    APP
}