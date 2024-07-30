package com.xhand.hnu.network

import android.content.Context
import coil.ImageLoader
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient

class CustomCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: listOf()
    }
}

fun createImageLoader(context: Context,sid: String): ImageLoader {
    val cookieJar = CustomCookieJar()

    val customCookies = listOf(
        Cookie.Builder()
            .domain("dekt.htu.edu.cn")
            .name("sid")
            .value(sid)
            .build()
    )
    cookieJar.saveFromResponse("http://dekt.htu.edu.cn/img/resources-code.jpg".toHttpUrl(), customCookies)

    val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build()

    return ImageLoader.Builder(context)
        .okHttpClient(client)
        .build()
}