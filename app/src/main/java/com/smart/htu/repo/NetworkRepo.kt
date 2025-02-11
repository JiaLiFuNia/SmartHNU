package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.R
import com.smart.htu.api.module.Area
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.BuyRecords
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.module.GiteeEntity
import com.smart.htu.api.module.LoginJWCEntity
import com.smart.htu.api.module.LoginPost
import com.smart.htu.api.module.PersonalMessage
import com.smart.htu.api.network.AirConditionService
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.GiteeService
import com.smart.htu.api.network.JWCService
import com.smart.htu.api.network.LibraryService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.repo.DataStoreRepo.Companion.DEFAULT_MESSAGE
import com.smart.htu.screens.application.librarySearch.LibraryBookDetail
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.RSAUtil
import com.smart.htu.utils.parseLibraryBookDetail
import com.smart.htu.utils.parseLibrarySearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authServerService: AuthLoginService,
    private val eHallService: EHallService,
    private val libraryService: LibraryService,
    private val jwcService: JWCService,
    private val airConditionService: AirConditionService,
    private val giteeService: GiteeService,
    private val dataStoreRepo: DataStoreRepo,
    private val networkCookieJar: NetworkCookieJar
) {

    // 获取gitee配置
    suspend fun getGiteeConfig(): Result<GiteeEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val config = giteeService.getGiteeConfig()
                Result.success(config)
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(Exception("获取失败"))
            }
        }
    }

    // 获取空调区域
    suspend fun getAirConditionAreaService(shiroJID: String, ymID: String): Result<Area> {
        return withContext(Dispatchers.IO) {
            try {
                val config = airConditionService.getQueryArea(shiroJID, ymID)
                if (config.statusCode == 0) Result.success(config)
                else Result.failure(Exception(config.message))
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(Exception("获取失败"))
            }
        }
    }

    // 获取空调电量
    suspend fun getAirConditionBillService(
        shiroJID: String,
        ymId: String,
        areaId: String,
        buildingCode: String,
        floorCode: String,
        roomCode: String
    ): Result<BillDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val billRes = airConditionService.getElectricityBillDetails(
                    shiroJID = shiroJID,
                    ymId = ymId,
                    areaId = areaId,
                    buildingCode = buildingCode,
                    floorCode = floorCode,
                    roomCode = roomCode
                )
                Log.i("TAG666 air", billRes.data.toString())
                if (billRes.statusCode == 0) Result.success(billRes)
                else Result.failure(Exception(billRes.message))
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(Exception("请求失败"))
            }
        }
    }

    // 获取空调电量记录
    suspend fun getAirConditionBillRecords(
        shiroJID: String,
        ymId: String,
        areaId: String,
        buildingCode: String,
        floorCode: String,
        roomCode: String,
        mdType: String,
    ): Result<BillRecords> {
        return withContext(Dispatchers.IO) {
            try {
                val billRes = airConditionService.getBillRecordsData(
                    shiroJID = shiroJID,
                    ymId = ymId,
                    areaId = areaId,
                    buildingCode = buildingCode,
                    floorCode = floorCode,
                    roomCode = roomCode,
                    mdtype = mdType
                )
                if (billRes.statusCode == 0) Result.success(billRes)
                else Result.failure(Exception(billRes.message))
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(Exception("请求失败"))
            }
        }
    }

    // 获取空调充值记录
    suspend fun getAirConditionBuyRecords(
        shiroJID: String,
        ymId: String,
        areaId: String,
        buildingCode: String,
        floorCode: String,
        roomCode: String
    ): Result<BuyRecords> {
        return withContext(Dispatchers.IO) {
            try {
                val res = airConditionService.getBuyRecord(
                    shiroJID = shiroJID,
                    ymId = ymId,
                    areaId = areaId,
                    buildingCode = buildingCode,
                    floorCode = floorCode,
                    roomCode = roomCode
                )
                if (res.statusCode == 0) Result.success(res)
                else Result.failure(Exception(res.message))
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(Exception("请求失败"))
            }
        }
    }

    // 图书搜索
    suspend fun librarySearch(
        keyword: String,
        page: Int
    ): Pair<String, List<LibraryBookListEntity>> {
        try {
            val res = libraryService.librarySearch(keyword, page)
            val resParsed: Pair<String, MutableList<LibraryBookListEntity>> =
                if (res.code() == 200) {
                parseLibrarySearchResult(res.body()?.string() ?: "")
            } else {
                    "0" to mutableListOf()
            }
            return resParsed
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return "0" to mutableListOf()
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
            Log.i("TAG666", "errorTip: $errorTip")
            return when (response.code()) {
                401 -> Result.failure(Exception("状态码：${response.code()} $errorTip"))
                200 -> {
                    if (errorTip != "") {
                        Result.failure(Exception("状态码：${response.code()} $errorTip"))
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

    // 智慧教务登录
    suspend fun jwcLogin(
        username: String,
        password: String
    ): Result<LoginJWCEntity> {
        try {
            val publicKey = RSAUtil.getPublicKeyFromRaw(context, R.raw.public_key)
            val passwordEncrypt = publicKey?.let { RSAUtil.encryptText(password, it) }
            val logState = jwcService.login(LoginPost(username, passwordEncrypt ?: ""))
            return if (logState.code == 200) {
                Result.success(logState)
            } else {
                Result.failure(Exception("登录失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun checkJWCTokenService(
        token: String
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val res = jwcService.checkToken(token)
                if (res.code == 200) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("false"))
                }
            } catch (e: Exception) {
                Log.e("TAG666", "${e.message}")
                Result.failure(e)
            }
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