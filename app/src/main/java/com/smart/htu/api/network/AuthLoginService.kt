package com.smart.htu.api.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthLoginService {

    @GET("authserver/login")
    suspend fun authServer(
        @Query("service") service: String = "http://authserver2.htu.edu.cn/authserver/mobile/callback?appId=537288889"
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("authserver/login")
    @Headers(
        "Connection: keep-alive",
        "User-Agent: Mozilla/5.0 (Linux; Android 12; DCO-AL00 Build/V417IR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/101.0.4951.61 Mobile Safari/537.36 AgentWeb/5.0.0  UCBrowser/11.6.4.950",
        "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
        "X-Requested-With: com.autewifi.sd.enroll",
        "Referer: http://authserver2.htu.edu.cn/authserver/customTheme/static/mobile/css/index.css?v=20240829.084741",
        "Accept-Encoding: gzip, deflate",
        "Accept-Language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "Cookie: org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh_CN"
    )
    suspend fun authLogin(
        @Query("service") service: String = "http://authserver2.htu.edu.cn/authserver/mobile/callback?appId=537288889",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("execution") execution: String,
        @Field("captcha") captcha: String = "",
        @Field("_eventId") eventId: String = "submit",
        @Field("cllt") cllt: String = "userNameLogin",
        @Field("dllt") dllt: String = "mobileLogin",
        @Field("lt") lt: String = ""
    ): Response<ResponseBody>

}