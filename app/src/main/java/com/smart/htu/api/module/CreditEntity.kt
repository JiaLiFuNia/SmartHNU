package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class CreditEntity(
    val code: Int,
    val msg: String,
    val list: List<CreditItemEntity>
)

data class CreditItemEntity(
    @SerializedName("kcdlmc") val label: String,
    @SerializedName("xf") val credit: String
)
