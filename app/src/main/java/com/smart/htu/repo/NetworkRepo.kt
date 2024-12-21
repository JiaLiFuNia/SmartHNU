package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.NetworkService
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.AuthLoginNetworkService
import com.smart.htu.di.AuthMessageNetworkService
import com.smart.htu.utils.AESUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject


class NetworkRepo @Inject constructor(
    @AuthLoginNetworkService private val networkService: NetworkService,
    @AuthMessageNetworkService private val eHallNetworkService: NetworkService,
    private val dataStoreRepo: DataStoreRepo,
) {

    suspend fun getStudentInfo(): PersonalMessage? {
        try {
            val res = eHallNetworkService.getStudentInfo()
            Log.i("TAG666", res.body().toString())
            return res.body()?.data?.first()
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            e.printStackTrace()
            throw IOException("error")
        }
    }

    private var pwdEncryptSalt: String = ""
    private var execution: String = ""

    private suspend fun getLoginPage() {
        withContext(Dispatchers.IO) {
            dataStoreRepo.clearCookies()
            try {
                val loginPage = networkService.authServer()
                parseLoginPage(loginPage.body()?.string() ?: "")
                Log.i("TAG666", "repo ${pwdEncryptSalt}\n${execution}")
            } catch (e: IOException) {
                Log.e("TAG666", "${e.message}")
                throw e
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                throw IOException("error")
            }
        }
    }

    private fun parseLoginPage(html: String) {
        val document = Jsoup.parse(html)
        pwdEncryptSalt = document.getElementById("pwdEncryptSalt")?.attr("value") ?: ""
        execution = document.getElementById("execution")?.attr("value") ?: ""
    }

    suspend fun authLogin(
        username: String,
        password: String,
        captcha: String? = ""
    ): Int {
        dataStoreRepo.clearCookies()
        try {
            getLoginPage()
            val response = networkService.authLogin(
                username = username,
                password = AESUtils.encryptPassword(password, pwdEncryptSalt),
                captcha = captcha ?: "",
                execution = execution
            )
            val document = Jsoup.parse(response.body()?.string() ?: "")
            val errorTip = document.getElementById("showErrorTip")?.text() ?: ""
            Log.d("TAG666 tip", errorTip)

            val cookies = dataStoreRepo.observeCookies().first()
            val isLoggedIn = cookies.any { it.name == "MOD_AUTH_CAS" }

            return when {
                response.code() == 401 -> -1 // Account or password error
                isLoggedIn -> 1 // Login successful if MOD_AUTH_CAS cookie is present
                "必须录入" in errorTip -> 2 // Account or password is empty
                else -> -1 // Other cases
            }
        } catch (e: IOException) {
            Log.e("TAG666 IOException while logging in", "${e.message}")
            return -1
        } catch (e: Exception) {
            Log.e("TAG666 Unexpected error while logging in", "${e.message}")
            return -1
        }
    }
}