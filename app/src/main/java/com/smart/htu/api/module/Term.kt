package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class OverallTerm(
    @SerializedName("xnxqdm") val termCode: String? = null,
)

data class TermIndex(
    val msg: String,
    val code: Int,
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("xnxqmc") val termString: String,
    @SerializedName("xqzcList") val termList: List<SingleTerm>
)

data class SingleTerm(
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("xnxqmc") val termString: String,
)