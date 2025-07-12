package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.AIModulePostEntity
import com.smart.htu.api.module.AIResponseEntity
import com.smart.htu.api.module.AppToken
import com.smart.htu.api.module.Area
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuyRecords
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.Usage
import com.smart.htu.api.module.WeatherCurrentData
import com.smart.htu.api.network.AirConditionService
import com.smart.htu.api.network.AppLoginService
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.ChatService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.NewsService
import com.smart.htu.api.network.WeatherService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.ParseNewsArticleUtil.parseHTMLToNewsArticle
import com.smart.htu.utils.ParseNewsListUtil.parseHTMLToNewsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val authServerService: AuthLoginService,
    private val appLoginService: AppLoginService,
    private val eHallService: EHallService,
    private val airConditionService: AirConditionService,
    private val weatherService: WeatherService,
    private val newsService: NewsService,
    private val chatService: ChatService,
    private val networkCookieJar: NetworkCookieJar,
    private val dataStoreRepo: DataStoreRepo
) {

    // ai
    suspend fun chatService(
        url: String,
        key: String,
        data: AIModulePostEntity
    ): Result<AIResponseEntity> {
        return try {
            val res = chatService.chatService(authorization = "Bearer $key", data = data)
            when (res.code()) {
                200 -> Result.success(
                    res.body() ?: AIResponseEntity(
                        id = "",
                        exampleGenerateObject = "",
                        created = 0L,
                        model = "",
                        choices = emptyList(),
                        usage = Usage(0, 0, 0)
                    )
                )

                else -> Result.failure(Exception(res.body()?.detail))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "chatService error: ${e.message}")
            Result.failure(e)
        }
    }

    // 搜索新闻
    suspend fun searchNewsService(searchInfo: String): List<NewsItemEntity> {
        try {
            val res = newsService.searchService(
                searchInfo = searchInfo
            )
            Log.e("TAG666", "searchNewsService ${res.body()?.dataList}")
            return res.body()?.dataList ?: emptyList()
        } catch (e: Exception) {
            Log.e("TAG666", "searchNewsService $e")
            return emptyList()
        }
    }

    // 获取新闻
    suspend fun getNewsService(
        newsOptionItems: NewsCategoryEntity,
        page: Int? = 1
    ): List<NewsItemEntity> {
        try {
            val res = newsService.getNewsList(
                academic = newsOptionItems.academic,
                page = page.toString(),
                type = newsOptionItems.type
            )
            return parseHTMLToNewsList(res.body()?.string().toString(), newsOptionItems.label)
        } catch (e: Exception) {
            Log.e("TAG666", "getB $e")
            return emptyList()
        }
    }

    // 新闻详情
    suspend fun getNewsDetailService(url: String): NewsArticleEntity? {
        try {
            val res = newsService.getNewsDetail(url).string()
            return parseHTMLToNewsArticle(url, res)
        } catch (e: Exception) {
            Log.e("TAG666", "getNewsDetailService $e")
            return null
        }
    }

    // 获取实时天气
    suspend fun getWeatherService(): WeatherCurrentData? {
        val res = weatherService.getWeather()
        try {
            return res.now
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return null
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

    // 解析登录参数
    private suspend fun getLoginPage() {
        try {
            networkCookieJar.clearCookies()
            val loginPage = authServerService.authServer()
            parseLoginPage(loginPage.body()?.string() ?: "")
            Log.i("TAG666", "repo ${pwdEncryptSalt}\n${execution}")
        } catch (e: IOException) {
            Log.e("TAG666", "${e.message}")
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
                execution = execution,
                captcha = captcha ?: ""
            )
            val loggedPage = Jsoup.parse(response.body()?.string() ?: "")
            val errorTip = loggedPage.getElementById("showErrorTip")?.text() ?: ""
            Log.i("TAG666", "errorTip: $errorTip")
            // 获取 mobile_code
            val mobileCode = extractMobileCode(response)
            if (mobileCode.isNotEmpty()) {
                Log.i("TAG666", "获取到 mobile_code: $mobileCode")
                dataStoreRepo.saveMobileCode(mobileCode)
            }
            getAppTokenService(mobileCode)
            return when (response.code()) {
                401 -> Result.failure(Exception("状态码：${response.code()} $errorTip"))
                200 -> {
                    if (errorTip.isNotEmpty()) {
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

    suspend fun getAppTokenService(mobileCode: String): Result<AppToken?> {
        try {
            val appToken = appLoginService.appLogin(mobileCode)
            return when (appToken.code) {
                200 -> Result.success(appToken.data)
                else -> Result.failure(Exception(appToken.message))
            }
        } catch (e: Exception) {
            Log.e("TAG666 log", "${e.message}")
            return Result.failure(Exception(e.message))
        }
    }

    private var pwdEncryptSalt: String = ""
    private var execution: String = ""
    private fun parseLoginPage(html: String) {
        val document = Jsoup.parse(html)
        pwdEncryptSalt = document.getElementById("pwdEncryptSalt")?.attr("value") ?: ""
        execution = document.getElementById("execution")?.attr("value") ?: ""
    }

    private fun extractMobileCode(response: Response<ResponseBody>): String {
        // 从重定向的 URL 中获取 mobile_code
        val redirectUrl = response.raw().request.url.toString()
        Log.i("TAG666", "重定向 URL: $redirectUrl")
        val regex = "mobile_code=([^&#]+)".toRegex()
        val matchResult = regex.find(redirectUrl)
        return matchResult?.groupValues?.getOrNull(1) ?: ""
    }
}