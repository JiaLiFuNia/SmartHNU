package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import com.smart.htu.utils.TermUtil.termConverter

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
    val mc: String,
    @SerializedName("pjxfjd") val gpa: String,
) {
    val label: String
        get() = if (mc.length == 11) termConverter(mc) else mc
}