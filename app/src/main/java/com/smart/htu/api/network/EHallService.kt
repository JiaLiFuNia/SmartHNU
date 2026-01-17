package com.smart.htu.api.network

import com.smart.htu.api.module.PersonalMessageRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface EHallService {

    @GET("psfw/sys/pubbiinfaapphtu/api/select_xsjbxx.do")
    suspend fun getStudentInfo(): Response<PersonalMessageRes>

    @GET("qljfwapp/sys/lwPsPortalAnnualBill/modules/annualBill/getAnnualBillData.do")
    suspend fun getPersonalMessage(): Response<ResponseBody>
}