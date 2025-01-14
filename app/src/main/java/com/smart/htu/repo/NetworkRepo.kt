package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.JWCService
import com.smart.htu.api.network.LibraryService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.screens.application.librarySearch.LibraryBookDetail
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.RSAEncryptionHelper
import com.smart.htu.utils.parseLibraryBookDetail
import com.smart.htu.utils.parseLibrarySearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val authServerService: AuthLoginService,
    private val eHallService: EHallService,
    private val libraryService: LibraryService,
    private val jwcService: JWCService,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) {

    // 图书搜索
    suspend fun librarySearch(keyword: String): List<LibraryBookListEntity> {
        val bookList: List<LibraryBookListEntity>
        try {
            val res = libraryService.librarySearch(keyword, 1)
            bookList = if (res.code() == 200) {
                parseLibrarySearchResult(res.body()?.string() ?: "")
            } else {
                emptyList()
            }
            Log.i("TAG666", bookList.toString())
            return bookList
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return emptyList()
        }
    }

    // 图书详情
    suspend fun libraryBookDetails(id: String): List<LibraryBookDetail> {
        val bookList: List<LibraryBookDetail>
        try {
            val res = libraryService.libraryBookDetails(id)
            bookList = if (res.code() == 200) {
                parseLibraryBookDetail(res.body()?.string() ?: "")
            } else {
                emptyList()
            }
            return bookList
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return emptyList()
        }
    }

    // 获取个人信息
    suspend fun getStudentInfo(): PersonalMessage? {
        return withContext(Dispatchers.IO) {
            try {
                val res = eHallService.getStudentInfo()
                if (res.code() == 200) {
                    Log.i("TAG666", res.body()?.data.toString())
                    res.body()?.data?.first()
                } else {
                    DEFAULT_MESSAGE
                }
            } catch (e: Exception) {
                Log.i("TAG666 message", "${e.message}")
                throw IOException("error")
            }
        }
    }

    // 解析登录参数
    private suspend fun getLoginPage() {
        try {
            networkCookieJar.clearCookies()
            val loginPage = authServerService.authServer()
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

    // 登录
    suspend fun authLogin(
        studentId: String,
        password: String,
        captcha: String? = ""
    ): Result<String> {
        try {
            getLoginPage()
            val response = authServerService.authLogin(
                username = studentId,
                password = AESUtils.encryptPassword(password, pwdEncryptSalt),
                captcha = captcha ?: "",
                execution = execution
            )
            val loggedPage = Jsoup.parse(response.body()?.string() ?: "")
            val errorTip = loggedPage.getElementById("showErrorTip")?.text() ?: ""
            return when (response.code()) {
                401 -> Result.failure(Exception(response.code().toString() + errorTip))
                200 -> {
                    if (errorTip != "") {
                        Result.failure(Exception(response.code().toString() + errorTip))
                    } else {
                        Result.success("登录成功" + response.code())
                    }
                }

                else -> Result.failure(Exception("未知错误"))
            }
        } catch (e: Exception) {
            Log.e("TAG666 log", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun jwcLogin(
        username: String,
        password: String
    ): Result<LoginJWCEntity> {
        try {
            val publicKey = RSAEncryptionHelper.getPublicKeyFromString()
            val passwordEncrypt = RSAEncryptionHelper.encryptText(password, publicKey)
            val logState = jwcService.login(LoginPost(username, passwordEncrypt))
            return if (logState.code == 200) {
                Result.success(logState)
            } else {
                Result.failure(Exception("登录失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            throw e
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