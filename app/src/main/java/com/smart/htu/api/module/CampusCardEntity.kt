package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class CardUserInfoEntity(
    @SerializedName("registerSchId") val registerSchoolId: Int,
    @SerializedName("customerCode") val customerCode: String,
    @SerializedName("result_") val result: Boolean,
    @SerializedName("userSnWrapper") val userSnWrapper: String,
    @SerializedName("error") val isError: Boolean,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("message_") val statusMessage: String,
    @SerializedName("ecard_customerid") val eCardCustomerId: String,
    @SerializedName("afterBindLimitUse") val afterBindLimitUse: AfterBindLimitUse,
    @SerializedName("mobileWrapper") val mobileWrapper: String,
    @SerializedName("customPic") val customPic: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("customerId") val customerId: Int,
    @SerializedName("nickNameWrapper") val nicknameWrapper: String,
    @SerializedName("ecard_campus") val eCardCampus: String,
    @SerializedName("stuNo") val studentNo: String,
    @SerializedName("bindEcard") val isECardBound: Boolean,
    @SerializedName("openid") val openId: String,
    @SerializedName("sex") val gender: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("nameWrapper") val nameWrapper: String,
    @SerializedName("message") val detailMessage: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("customerName") val customerName: String,
    @SerializedName("serviceCompany") val serviceCompany: String,
    @SerializedName("url") val url: String,
    @SerializedName("token") val token: String,
    @SerializedName("bindMobile") val isMobileBound: Boolean,
    @SerializedName("code_") val code: Int,
    @SerializedName("lastModifyTime") val lastModifyTime: Long,
    @SerializedName("name") val name: String,
    @SerializedName("outid") val outId: String,
    @SerializedName("bindStu") val isStudentBound: Boolean,
)

data class AfterBindLimitUse(
    @SerializedName("msg") val message: String,
    @SerializedName("onOff") val isEnabled: Boolean,
)


data class CampusCardInfoEntity(
    val data: CampusCardData,
    @SerializedName("code_") val code: Int,
    @SerializedName("result_") val result: Boolean,
    @SerializedName("message_") val message: String,
)

data class CampusCardData(
    @SerializedName("fund_fare") val fundFare: Double,
    @SerializedName("main_fare") val mainFare: Double,
    @SerializedName("subsidy_fare") val subsidyFare: Double,
    val status: Int,
)

data class ConsumptionRecordEntity(
    val data: ConsumptionRecordData,
    @SerializedName("code_") val code: Int,
    @SerializedName("result_") val result: Boolean,
    val useTime: Long,
    @SerializedName("message_") val message: String,
)

data class ConsumptionRecordData(
    val size: Long,
    val data: List<ConsumptionRecordItemData>,
) {
    data class ConsumptionRecordItemData(
        @SerializedName("accdscrp") val payWay: String,
        val amount: Double,
        @SerializedName("term_name") val location: String, // 消费地点
        @SerializedName("orderno") val orderNo: String,
        @SerializedName("flag") val tradeFlag: String,// 交易标志（收入1/支出0）
        val description: String, // 支出/收入
        @SerializedName("businessopdt") val businessOpDate: String, // 日期
        val time: Long,
        val type: String,
    )
}

