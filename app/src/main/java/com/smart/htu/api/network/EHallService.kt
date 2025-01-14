package com.smart.htu.api.network

import com.smart.htu.api.module.PersonalMessageRes
import retrofit2.Response
import retrofit2.http.GET

interface EHallService {

    @GET("psfw/sys/pubbiinfaapphtu/api/select_xsjbxx.do")
    suspend fun getStudentInfo(): Response<PersonalMessageRes>

}