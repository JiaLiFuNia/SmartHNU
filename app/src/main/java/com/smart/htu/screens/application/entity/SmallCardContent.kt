package com.smart.htu.screens.application.entity

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class SmallCardContent(
    val label: Int,
    val description: String? = null,
    @DrawableRes val icon: Int,
    val route: String? = null,
    val url: String? = null
)
