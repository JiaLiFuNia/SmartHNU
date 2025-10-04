package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class GPAPost(
    val ckfs: String,
    val ckjh: String
)

data class GPAEntity(
    val code: Int,
    val msg: String,
    val list: List<GPAData>
)

data class GPAData(
    @SerializedName("mc") val label: String,
    @SerializedName("pjxfjd") val gpa: String,
)