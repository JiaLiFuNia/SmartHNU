package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.R
import com.smart.htu.api.module.SCHourEntity
import com.smart.htu.api.network.SecondClassService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.SECOND_CLASS_BASE_URL
import com.smart.htu.utils.RSAUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class SecondClassNetworkRepo @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val secondClassService: SecondClassService,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) {

    suspend fun getHourList(sid: String): Result<SCHourEntity> {
        try {
            val res = secondClassService.getHourScoreList(sid)
            // Log.d("TAG666 sc", "getHourList: ${res.body()} ${res.code()}")
            return if (res.code() == 200) {
                Result.success(res.body() ?: SCHourEntity())
            } else {
                Result.failure(Exception("获取数据失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666 sc", "getHourList: ${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun scLogin(
        studentID: String,
        password: String,
        verifyCode: String,
        sid: String,
    ): Result<String> {
        try {
            val publicKey = RSAUtil.getPublicKeyFromRaw(context, R.raw.sc_public_key)
            val passwordEncrypt = publicKey?.let { RSAUtil.encryptText(password, it) }
            val res = secondClassService.scLogin(
                username = studentID,
                password2 = passwordEncrypt,
                verifyCode = verifyCode,
                sid = "sid=${sid}",
                tk = tk
            )
            // Log.i("TAG666 sc", "scLogin: ${res.body()}")
            val loggedPage = Jsoup.parse(res.body()?.string().toString())
            val states = loggedPage.select("div.content div.formRow")
            // val userInfo = loggedPage.select("div.navbox div.ab_box h3")
            /*Log.i("TAG666 sc", "userInfo: $userInfo")
            Log.i("TAG666 sc", "scLoginState: $states")*/
            val state = states.last()?.text()
            if (res.body() == null) {
                dataStoreRepo.saveSecondClassSid(sid)
                saveCookie(sid)
                return Result.success("success")
            } else return Result.failure(Exception(state))
        } catch (e: Exception) {
            Log.e("TAG666 sc", "${e.message}")
            return Result.failure(e)
        }
    }

    private var tk: String = ""
    suspend fun getSCLoginPage(): String {
        try {
            val res = secondClassService.scLogin()
            val document = Jsoup.parse(res.body()?.string().toString())
            tk = document.select("input[name=tk]").attr("value")
            // sid=4b473860-5b10-4a68-ba2a-bbb659d012c6; Path=/; HttpOnly; SameSite=lax
            val cookie = res.headers().get("Set-Cookie").toString()
            val matchResult = Regex("""sid=([^;]+)""").find(cookie)
            if (matchResult != null) {
                val sid = matchResult.groupValues[1]
                Log.i("TAG666 sc", "$tk $sid")
                return sid
            } else {
                return ""
            }
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
            return ""
        }
    }

    fun saveCookie(sid: String) {
        val cookie = Cookie.Builder()
            .name("sid")
            .value(sid)
            .domain(
                SECOND_CLASS_BASE_URL
                    .replace("/", "")
                    .substringAfter("http:")
            )
            .build()
        networkCookieJar.saveFromResponse("https://dekt.htu.edu.cn".toHttpUrl(), listOf(cookie))
    }
}