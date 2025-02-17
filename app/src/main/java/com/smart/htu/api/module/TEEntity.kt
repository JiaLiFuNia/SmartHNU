package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class TEEntity(
    val msg: String,
    val code: Int,
    @SerializedName("allPjxxList") val evaluationInfoList: List<EvaluationInfo>,
    @SerializedName("allowPj") val allowEvaluation: Boolean
)

data class EvaluationInfo(
    @SerializedName("teadm") val teacherCode: String,
    @SerializedName("jxhjmc") val courseType: String,
    @SerializedName("dgksdm") val dgksdm: String,
    @SerializedName("pjdm") val evaluationCode: String,
    @SerializedName("teaxm") val teacherName: String,
    @SerializedName("kcmc") val courseName: String
)