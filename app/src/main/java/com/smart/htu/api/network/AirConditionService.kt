package com.smart.htu.api.network

import com.smart.htu.api.module.Area
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuyRecords
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AirConditionService {

    @POST("app/electric/queryArea")
    @FormUrlEncoded
    suspend fun getQueryArea(
        @Header("Cookie") shiroJID: String,
        @Field("ymId") ymId: String,
        @Field("type") customType: String = "3",
        @Field("platform") platform: String = "WECHAT_H5"
    ): Area

    @POST("app/electric/queryISIMSRoomSurplus")
    @FormUrlEncoded
    suspend fun getElectricityBillDetails(
        @Header("Cookie") shiroJID: String,
        @Field("ymId") ymId: String,
        @Field("areaId") areaId: String,
        @Field("buildingCode") buildingCode: String,
        @Field("floorCode") floorCode: String,
        @Field("roomCode") roomCode: String,
        @Field("platform") platform: String = "WECHAT_H5"
    ): BillDetail

    @POST("app/electric/getISIMSRecords")
    @FormUrlEncoded
    suspend fun getBillRecordsData(
        @Header("Cookie") shiroJID: String,
        @Field("ymId") ymId: String,
        @Field("areaId") areaId: String,
        @Field("buildingCode") buildingCode: String,
        @Field("floorCode") floorCode: String,
        @Field("roomCode") roomCode: String,
        @Field("platform") platform: String = "WECHAT_H5",
        @Field("mdtype") mdtype: String,
    ): BillRecords

    @POST("app/electric/queryISIMSRoomBuyRecord")
    @FormUrlEncoded
    suspend fun getBuyRecord(
        @Header("Cookie") shiroJID: String,
        @Field("ymId") ymId: String,
        @Field("areaId") areaId: String,
        @Field("buildingCode") buildingCode: String,
        @Field("floorCode") floorCode: String,
        @Field("roomCode") roomCode: String,
        @Field("mdtype") mdtype: String = "room",
        @Field("platform") platform: String = "WECHAT_H5"
    ): BuyRecords
}