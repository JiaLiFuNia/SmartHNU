package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.R
import com.smart.htu.api.network.SecondClassService
import com.smart.htu.utils.RSAUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject

class SecondClassRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secondClassService: SecondClassService,
    private val dataStoreRepo: DataStoreRepo
) {

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
                sid = sid,
                tk = tk
            )
            val document: Document = Jsoup.parse(res.body()?.string().toString())
            val states = document.select("div.content div.formRow")
            val userInfo = document.select("div.navbox div.ab_box h3")
            Log.i("TAG666 sc", "userInfo: $userInfo")
            Log.i("TAG666 sc", "scLoginState: $states")
            val state = states.last()?.text()
            return if (userInfo.isNotEmpty())
                Result.success("")
            else
                Result.failure(Exception(state))
        } catch (e: Exception) {
            Log.e("TAG666 sc", "${e.message}")
            return Result.failure(e)
        }
    }

    private var tk: String = ""
    suspend fun getSCLoginPage(): String {
        try {
            val res = secondClassService.scLogin("", "", "", "", "", "")
            val document = Jsoup.parse(res.body()?.string().toString())
            tk = document.select("input[name=tk]").attr("value")
            val sid = res.headers().get("Set-Cookie")?.substringBefore(";") ?: ""
            Log.i("TAG666 sc", "tk: $tk $sid")
            return sid
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
            return ""
        }
    }
}