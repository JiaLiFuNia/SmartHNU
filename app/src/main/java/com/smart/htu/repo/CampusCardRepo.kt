package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.CampusCardInfoEntity
import com.smart.htu.api.module.ConsumptionRecordEntity
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.CampusCardService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.CAMPUS_CARD_BASE_URL
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CampusCardRepo @Inject constructor(
    private val campusCardService: CampusCardService,
    private val authLoginService: AuthLoginService,
    private val networkCookieJar: NetworkCookieJar
) {

    // 获取余额
    suspend fun getCardBalance(): Result<CampusCardInfoEntity> {
        return try {
            val res = campusCardService.getCampusCardInfo("XYK_BASE_INFO")
            when (res.isSuccessful) {
                true -> {
                    val body = res.body()
                    when (body?.result) {
                        true -> Result.success(body)
                        false -> Result.failure(Exception(body.message))
                        null -> Result.failure(Exception("获取校园卡信息失败"))
                    }
                }

                false -> Result.failure(Exception("获取校园卡信息失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 记录
    suspend fun getConsumptionRecord(
        beginIndex: Int,
        pageSize: Int,
        type: Int = -1,
        beginDate: String,
        endDate: String
    ): Result<ConsumptionRecordEntity> {
        return try {
            val res = campusCardService.getConsumptionRecord(
                beginIndex = beginIndex,
                pageSize = pageSize,
                type = type,
                beginDate = beginDate,
                endDate = endDate
            )
            when (res.isSuccessful) {
                true -> {
                    val body = res.body()
                    when (body?.result) {
                        true -> Result.success(body)
                        false -> Result.failure(Exception(body.message))
                        null -> Result.failure(Exception("获取消费记录失败"))
                    }
                }

                false -> Result.failure(Exception("获取消费记录失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun login(): Result<Boolean> {
        // networkCookieJar.editCookie(CAMPUS_CARD_BASE_URL, "SESSION", "")
        networkCookieJar.clearCookieForUrl("ecardh5.17wanxiao.com")
        networkCookieJar.clearCookieForUrl("open.17wanxiao.com")
        networkCookieJar.clearCookieForUrl("hub.17wanxiao.com")
        try {
            val res =
                authLoginService.authServer("http://ehall2.htu.edu.cn/qljfwapp/sys/lwHtuSchoolCard/index.do")
                    .raw().request.url
            val userData = res.queryParameterNames.associateWith { res.queryParameter(it) }
            val userDataString = Json.encodeToString(userData)
            Log.i("TAG666 res", userDataString)
            val res2 = campusCardService.getUserInfo(userDataString)
            val url = res2.body()?.url
            if (url != null) {
                val res3 = campusCardService.getExternalUrl(url)
                val setCookie = res3.raw().priorResponse?.headers?.get("set-cookie") ?: ""
                Log.i("TAG666 res3", setCookie)
                if (res3.isSuccessful && setCookie.contains("SESSION=")) {
                    networkCookieJar.editCookie(
                        url = CAMPUS_CARD_BASE_URL,
                        name = "SESSION",
                        value = extractSession(setCookie)
                    )
                }
            }
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun extractSession(setCookie: String): String {
        val parts = setCookie.split(';')
        for (part in parts) {
            val trimmed = part.trim()
            if (trimmed.startsWith("SESSION=", ignoreCase = true)) {
                return trimmed.substringAfter('=')
            }
        }
        return ""
    }

}