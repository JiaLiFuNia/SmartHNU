package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.NetworkService
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.di.AuthLoginNetworkService
import com.smart.htu.di.AuthMessageNetworkService
import com.smart.htu.di.CleanMessageNetworkService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.screens.application.librarySearch.LibraryBookDetail
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.parseLibraryBookDetail
import com.smart.htu.utils.parseLibrarySearchResult
import kotlinx.coroutines.flow.first
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject


class NetworkRepo @Inject constructor(
    @AuthLoginNetworkService private val authLoginNetworkService: NetworkService,
    @AuthMessageNetworkService private val authNetworkService: NetworkService,
    @CleanMessageNetworkService private val libraryNetworkService: NetworkService,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) {

    suspend fun librarySearch(keyword: String): List<LibraryBookListEntity> {
        val bookList: List<LibraryBookListEntity>
        try {
            val res = libraryNetworkService.librarySearch(keyword, 1)
            bookList = if (res.code() == 200) {
                parseLibrarySearchResult(res.body()?.string() ?: "")
            } else {
                emptyList()
            }
            Log.i("TAG666", bookList.toString())
            return bookList
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            throw IOException("error")
        }
    }

    suspend fun libraryBookDetails(id: String): List<LibraryBookDetail> {
        val bookList: List<LibraryBookDetail>
        try {
            val res = libraryNetworkService.libraryBookDetails(id)
            bookList = if (res.code() == 200) {
                parseLibraryBookDetail(res.body()?.string() ?: "")
            } else {
                emptyList()
            }
            return bookList
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            throw IOException("error")
        }
    }

    suspend fun getStudentInfo(): PersonalMessage? {
        try {
            val res = authNetworkService.getStudentInfo()
            Log.i("TAG666", res.headers().toString())
            if (res.code() == 200) {
                Log.i("TAG666", res.body()?.data.toString())
                return res.body()?.data?.first()
            } else
                return DEFAULT_MESSAGE
        } catch (e: IOException) {
            Log.i("TAG666 message", "${e.message}")
            throw e
        } catch (e: Exception) {
            Log.i("TAG666 message", "${e.message}")
            e.printStackTrace()
            throw IOException("error")
        }
    }

    private suspend fun getLoginPage() {
        networkCookieJar.clear()
        try {
            val loginPage = authLoginNetworkService.authServer()
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

    suspend fun authLogin(
        username: String,
        password: String,
        captcha: String? = ""
    ): Int {
        networkCookieJar.clear()
        try {
            getLoginPage()
            val response = authLoginNetworkService.authLogin(
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

    private var pwdEncryptSalt: String = ""
    private var execution: String = ""
    private fun parseLoginPage(html: String) {
        val document = Jsoup.parse(html)
        pwdEncryptSalt = document.getElementById("pwdEncryptSalt")?.attr("value") ?: ""
        execution = document.getElementById("execution")?.attr("value") ?: ""
    }
}