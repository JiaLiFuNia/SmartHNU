package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class ConfigRes(
    val code: Int,
    val message: String,
    val data: ConfigEntity
)

data class ConfigEntity(
    @SerializedName("ac_cookie") private val acCookie: List<String>,
    @SerializedName("website_navigation") val websiteNavigation: List<WebsiteNavigation>,
) {
    val acCookieValue: ACCookie
        get() = ACCookie(acCookie.first(), acCookie.last())
}

data class WebsiteNavigation(
    val name: String,
    val url: String
)