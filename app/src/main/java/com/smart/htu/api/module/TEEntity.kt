package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class TEEntity(
    val msg: String,
    val code: Int,
    @SerializedName("allPjxxList") val evaluationInfoList: List<EvaluationInfo>,
    @SerializedName("allowPj") val allowEvaluation: Boolean
)

data class EvaluationInfo(
    @SerializedName("teadm") val teacherCode: String, // 用于请求评价信息
    @SerializedName("jxhjmc") val courseType: String,
    @SerializedName("dgksdm") val syllabusEvaluateCode: String, // 用于请求评价信息
    @SerializedName("pjdm") val evaluationCode: String, // 用于请求已评价的信息
    @SerializedName("teaxm") val teacherName: String,
    @SerializedName("kcmc") val courseName: String
)

data class TEDetailPost(
    val dgksdm: String,
    val teadm: String
)

data class EvaluationDetail(
    val msg: String,
    val code: Int,
    // @SerializedName("skInfo") val skInfo: SkInfo,
    @SerializedName("wtList") val evaluationQuestionList: List<EvaluationQuestion>
)

data class SkInfo(
    val ldpj: String,
    val qmpj: String,
    val teabh: String,
    val zcmc: String,
    val txpath: String,
    val jxhjmc: String,
    val teadm: String,
    val bmywmc: String,
    val yxmc: String,
    val kcywmc: String,
    val yxdm: String,
    val ktpj: String,
    val fjpath: String,
    val kcdm: String,
    val xnxqdm: String,
    val zjpj: String,
    val jxhjdm: String,
    val ddpj: String,
    val dgksdm: String,
    val teaxm: String,
    val thpj: String,
    val kcrwdm: String,
    val kcptdm: String,
    val kcmc: String
)

data class EvaluationQuestion(
    @SerializedName("maxzbfz") val maximumScore: Any,
    @SerializedName("minzbfz") val minimumScore: Any,
    @SerializedName("txdm") val txdm: String,
    @SerializedName("zbbh") val zbbh: String,
    @SerializedName("zbdm") val zbdm: String,
    @SerializedName("zbfz") val scorePercentage: Any,
    @SerializedName("zblxdm") val questionNum: String,
    @SerializedName("zblxmc") val questionType: String,
    @SerializedName("zbmc") val question: String,
    val sectionList: List<Selection>
)

data class Selection(
    @SerializedName("fzbl") val score: Any,
    @SerializedName("zbxmbh") val selectionNum: String,
    @SerializedName("zbxmdm") val selectionCode: String,
    @SerializedName("zbxmmc") val selectionLabel: String
)