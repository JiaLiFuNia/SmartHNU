package com.xhand.hnu2.network

import com.xhand.hnu2.model.entity.LoginEntity
import com.xhand.hnu2.model.entity.LoginPostEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers(
        "Host: jwc.htu.edu.cn",
        "Accept: application/json, text/plain, */*",
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309092b) XWEB/8555 Flue",
        "Referer: http://jwc.htu.edu.cn/app/?code",
        "Accept-Encoding: gzip, deflate",
        "Accept-Language: zh-CN,zh;q=0.9",
        "Cookie: language=zh-CN"
    )
    @POST("dev-api/appapi/applogin")
    suspend fun loginPost(
        @Body body: LoginPostEntity
    ): Response<LoginEntity>

    companion object {
        fun instance(): LoginService {
            return Network.LoginService(LoginService::class.java)
        }
    }

}