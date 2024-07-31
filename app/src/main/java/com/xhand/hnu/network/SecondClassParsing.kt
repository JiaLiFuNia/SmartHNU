package com.xhand.hnu.network

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 获取Retrofit对象
 * @param context Context
 * */
fun <T> scRetrofitCreater(clazz: Class<T>, context: Context): T {
    val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    val client = OkHttpClient.Builder().cookieJar(cookieJar).build()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://dekt.htu.edu.cn")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(clazz)
}

fun secondClassParsing(str: String): String {
    val document: Document = Jsoup.parse(str)
    val token = document.select("input[name=tk]").attr("value")
    return token
}

fun secondClassLoginState(str: String): Int {
    val document: Document = Jsoup.parse(str)
    val states = document.select("div.content div.formRow")
    val userInfo = document.select("div.navbox div.ab_box h3")
    Log.i("TAG666", userInfo.toString())
    Log.i("TAG666", states.toString())
    val state = states.last()?.text()
    var stateCode = when (state) {
        "token已失效，请刷新页面重试！" -> -3
        "验证码错误" -> -2
        "用户名/密码错误" -> -1
        else -> 0
    }
    if (userInfo.isNotEmpty())
        stateCode = 1
    return stateCode
}