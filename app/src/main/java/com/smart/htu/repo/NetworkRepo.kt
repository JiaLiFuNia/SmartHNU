package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.AppToken
import com.smart.htu.api.module.Area
import com.smart.htu.api.module.BillDetail
import com.smart.htu.api.module.BillRecords
import com.smart.htu.api.module.BuyRecords
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.api.module.NowWeatherData
import com.smart.htu.api.module.WarningWeatherData
import com.smart.htu.api.network.AirConditionService
import com.smart.htu.api.network.AppLoginService
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.NewsService
import com.smart.htu.api.network.WeatherService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.screens.news.entity.NewsCategoryEntity
import com.smart.htu.screens.news.entity.NewsType
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.AESUtils.randomString
import com.smart.htu.utils.DateUtil.convertStringDateToLocalDate
import com.smart.htu.utils.ParseNewsArticleUtil.dealArticleContent
import com.smart.htu.utils.ParseNewsArticleUtil.extractAttachment
import com.smart.htu.utils.ParseNewsArticleUtil.parseHTMLToNewsArticle
import com.smart.htu.utils.ParseNewsListUtil.parseHTMLToNewsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dankito.readability4j.Readability4J
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val authServerService: AuthLoginService,
    private val appLoginService: AppLoginService,
    private val eHallService: EHallService,
    private val airConditionService: AirConditionService,
    private val weatherService: WeatherService,
    private val newsService: NewsService,
    private val networkCookieJar: NetworkCookieJar,
    private val dataStoreRepo: DataStoreRepo
) {

    // 搜索新闻
    suspend fun searchNewsService(searchInfo: String, type: String = ""): List<NewsItemEntity> {
        try {
            val res = newsService.searchService(
                searchInfo = searchInfo,
                // type = type
            )
            return res.body()?.dataList ?: emptyList()
        } catch (e: Exception) {
            Log.e("TAG666", "searchNewsService $e")
            return emptyList()
        }
    }

    val newsOptionItems = listOf(
        NewsCategoryEntity(NewsType.RESEARCH, "河南师范大学主页", "", "xsygcs"),
        NewsCategoryEntity(NewsType.NOTICE, "河南师范大学主页", "", "8955"),
        NewsCategoryEntity(NewsType.FAST_NEWS, "河南师范大学主页", "", "8957"),
        NewsCategoryEntity(NewsType.HEADLINES, "河南师范大学主页", "", "8954"),
        NewsCategoryEntity(NewsType.MATH_LECTURES, "数学与信息科学学院", "math", "xsyg"),
        NewsCategoryEntity(NewsType.MATH_NEWS, "数学与信息科学学院", "math", "xinwen"),
        NewsCategoryEntity(NewsType.MATH_NOTICE, "数学与信息科学学院", "math", "1143"),
        NewsCategoryEntity(NewsType.TEACHING_NEWS, "河南师范大学教务处", "teaching", "3257"),
        NewsCategoryEntity(NewsType.TEACHING_NOTICE, "河南师范大学教务处", "teaching", "3251"),
        NewsCategoryEntity(
            NewsType.TEACHING_ANNOUNCEMENT,
            "河南师范大学教务处",
            "teaching",
            "3258"
        ),
        NewsCategoryEntity(
            NewsType.EXAMINATION_NOTICE,
            "河南师范大学教务处",
            "teaching",
            "kwgl"
        )
    )

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
            Log.e("TAG666", "getNewsService $e")
            return emptyList()
        }
    }

    // 获取重要新闻
    suspend fun getImportantNewsService(): List<NewsItemEntity> {
        try {
            val importantOptionsIndex = listOf(8, 10, 1)
            val resultList = mutableListOf<NewsItemEntity>()
            val date = LocalDate.now().minusMonths(2)
            importantOptionsIndex.forEach {
                val option = newsOptionItems[it]
                val res = newsService.getNewsList(
                    academic = option.academic,
                    page = "1",
                    type = option.type
                )
                val newsList = parseHTMLToNewsList(res.body()?.string().toString(), option.label)
                newsList.forEach {
                    if (
                        (it.title.contains("考试") ||
                                it.title.contains("测试") ||
                                it.title.contains("选课") ||
                                it.title.contains("补选") ||
                                it.title.contains("补重修") ||
                                it.title.contains("教材退订") ||
                                it.title.contains("放假安排") ||
                                it.title.contains("报名")) &&
                        convertStringDateToLocalDate(it.time).isAfter(date)
                    ) {
                        resultList.add(it)
                    }
                }
            }
            resultList.apply {
                this.sortByDescending { it.time }
            }
            return resultList
        } catch (e: Exception) {
            Log.e("TAG666", "getNewsService $e")
            return emptyList()
        }
    }

    // 新闻详情
    suspend fun getNewsDetailService(url: String): NewsArticleEntity? {
        try {
            val res = newsService.getNewsDetail(url).string()
            val article = parseHTMLToNewsArticle(url, res)
            if (article.articleContent == null || article.title == null) {
                Log.i("TAG666 getNewsDetailService article", "解析失败，采用 Readability4J")
                val readability4J = Readability4J(url, res)
                val articleContent = readability4J.parse()
                val document = Jsoup.parse(articleContent.content ?: "")
                return NewsArticleEntity(
                    title = articleContent.title,
                    publishDate = article.publishDate,
                    articleContent = dealArticleContent(document.body()),
                    attachment = extractAttachment(document.body()),
                    visitCount = article.visitCount
                )
            }
            return article
        } catch (e: Exception) {
            Log.e("TAG666", "getNewsDetailService $e")
            return null
        }
    }

    // 获取实时天气
    suspend fun getWeatherService(): NowWeatherData? {
        try {
            val res = weatherService.getNowWeather()
            return res.now
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return null
        }
    }

    // 天气预警
    suspend fun getWarningWeatherService(): Result<List<WarningWeatherData>> {
        return try {
            val res = weatherService.getWarningWeather()
            if (res.code == 200) Result.success(res.warning)
            // Log.e("TAG666", "getWarningWeatherService ${res.body()?.string()}")
            else Result.failure(Exception("获取失败"))
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            Result.failure(Exception("获取失败"))
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
    suspend fun getAirConditionCurrentBillDataService(
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
    suspend fun getAirConditionBillRecordsService(
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
    suspend fun getAirConditionBuyRecordsService(
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

    suspend fun getPersonalMessageService(): Result<Any> {
        return try {
            val res = eHallService.getPersonalMessage()
            when (res.code()) {
                200 -> Result.success(res)
                else -> Result.failure(Exception("状态码：${res.code()} 获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            Result.failure(Exception("获取失败"))
        }
    }

    // 解析登录参数
    private suspend fun getAuthLoginPage() {
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
            getAuthLoginPage()
            val response = authServerService.authLogin(
                username = studentId,
                password = AESUtils.encryptPassword(
                    randomString(64) + password,
                    pwdEncryptSalt,
                    "CBC"
                ),
                execution = execution,
                captcha = captcha ?: ""
            )
            val loggedPage = Jsoup.parse(response.body()?.string() ?: "")
            val errorTip = loggedPage.getElementById("showErrorTip")?.text() ?: ""
            Log.i("TAG666", "errorTip: $errorTip")
            // 获取 mobile_code
            /*val mobileCode = extractMobileCode(response)
            if (mobileCode.isNotEmpty()) {
                Log.i("TAG666", "获取到 mobile_code: $mobileCode")
                dataStoreRepo.saveMobileCode(mobileCode)
            }*/
            // getAppTokenService(mobileCode)
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