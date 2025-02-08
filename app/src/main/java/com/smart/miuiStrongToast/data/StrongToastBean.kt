package com.smart.miuiStrongToast.data

import kotlinx.serialization.Serializable

@Serializable
data class StrongToastBean(
    var left: Left? = null,
    var right: Right? = null
)