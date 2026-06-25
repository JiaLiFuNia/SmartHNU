package com.smart.htu.api.network

import com.smart.htu.api.module.CampusCardInfoEntity
import com.smart.htu.api.module.CardUserInfoEntity
import com.smart.htu.api.module.ConsumptionRecordEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface CampusCardService {

    @POST("https://hub.17wanxiao.com/bsacs/redirect.action")
    @FormUrlEncoded
    suspend fun getUserInfo(
        @Field("userData") userData: String,
    ): Response<CardUserInfoEntity>

    @POST("ecardh5/bootcallback")
    @FormUrlEncoded
    suspend fun getCampusCardInfo(
        @Field("gotowhere") des: String
    ): Response<CampusCardInfoEntity>

    @POST("ecardh5/bootcallback")
    @FormUrlEncoded
    suspend fun getConsumptionRecord(
        @Field("beginIndex") beginIndex: Int,
        @Field("pageSize") pageSize: Int,
        @Field("type") type: Int,
        @Field("beginDate") beginDate: String,
        @Field("endDate") endDate: String,
        @Field("gotowhere") des: String = "XYK_TRADE_DETAIL"
    ): Response<ConsumptionRecordEntity>

    @GET
    suspend fun getExternalUrl(@Url url: String): Response<ResponseBody>

}