package com.xhand.hnu.network

import android.content.Context
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 获取Retrofit对象
 * */
fun <T> scRetrofitCreater(clazz: Class<T>): T {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://dekt.htu.edu.cn")
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
    Log.i("TAG666", "userInfo: $userInfo")
    Log.i("TAG666", "scLoginState: $states")
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