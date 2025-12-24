package com.smart.htu.di

import android.util.Log
import com.smart.htu.api.network.AIService
import com.smart.htu.api.network.AirConditionService
import com.smart.htu.api.network.AppLoginService
import com.smart.htu.api.network.AppService
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.JWCAppService
import com.smart.htu.api.network.JWCService
import com.smart.htu.api.network.LibraryService
import com.smart.htu.api.network.MessageBoardService
import com.smart.htu.api.network.NewsService
import com.smart.htu.api.network.SecondClassService
import com.smart.htu.api.network.WeatherService
import com.smart.htu.di.NetworkModule.ApiConstants.SILICON_BASE_URL
import com.smart.htu.repo.DataStoreRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    object ApiConstants {
        const val HTU_BASE_URL = "https://www.htu.edu.cn/"
        const val JWC_BASE_URL = "https://jwc.htu.edu.cn/"
        const val AUTH_BASE_URL = "https://authserver2.htu.edu.cn/"
        const val APP_BASE_URL = "http://app.htu.edu.cn/appapi/"
        const val EHALL_BASE_URL = "https://ehall2.htu.edu.cn/"
        const val LIBRARY_BASE_URL = "https://opac.htu.edu.cn/"
        const val MESSAGE_BOARD_BASE_URL = "https://yjfk.htu.edu.cn/"

        const val SECOND_CLASS_BASE_URL = "http://dekt.htu.edu.cn/"

        const val AIR_CONDITION_BASE_URL = "https://application.xiaofubao.com/"
        const val WEATHER_BASE_URL = "https://kq5g7ax26n.re.qweatherapi.com/v7/"

        const val SILICON_BASE_URL = "https://api.siliconflow.cn/"

        const val SMH_BASE_URL = "https://xhand.edu.deal/api/"
        //"https://shtu.xubohan04.tk/api/"
    }

    @Provides
    @Singleton
    fun provideNetworkCookieJar(
        dataStoreRepo: DataStoreRepo
    ): NetworkCookieJar {
        return NetworkCookieJar(dataStoreRepo)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cookieJar: NetworkCookieJar
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (response.isRedirect) {
                    val redirectUrl = response.header("Location") ?: ""
                    Log.i("TAG666", "重定向到: $redirectUrl")
                    if (redirectUrl.contains("mobile_code=")) {
                        Log.i("TAG666", "mobile_code: $redirectUrl")
                    }
                }
                response
            }
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthLoginService(
        okHttpClient: OkHttpClient
    ): AuthLoginService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AuthLoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppLoginService(): AppLoginService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AppLoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideEHallService(
        okHttpClient: OkHttpClient
    ): EHallService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.EHALL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(EHallService::class.java)
    }

    @Provides
    @Singleton
    fun provideLibraryService(
        dataStoreRepo: DataStoreRepo
    ): LibraryService {
        val clientWithInterceptor = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val session = runBlocking { dataStoreRepo.observeLibrarySession().first() }
                val newRequest = chain.request().newBuilder()
                    .addHeader("Cookie", "meta-opac.session=${session}")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.LIBRARY_BASE_URL)
            .client(clientWithInterceptor)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(LibraryService::class.java)
    }

    @Provides
    @Singleton
    fun provideJWCAppService(
        okHttpClient: OkHttpClient,
        dataStoreRepo: DataStoreRepo
    ): JWCAppService {
        val clientWithInterceptor = okHttpClient.newBuilder()
            .addInterceptor { chain ->
                val token = runBlocking { dataStoreRepo.observeJWCToken().first() }
                val newRequest = chain.request().newBuilder()
                    .addHeader("Token", token)
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.JWC_BASE_URL)
            .client(clientWithInterceptor)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(JWCAppService::class.java)
    }

    @Provides
    @Singleton
    fun provideJWCService(
        okHttpClient: OkHttpClient,
        cookieJar: NetworkCookieJar
    ): JWCService {
        val clientWithInterceptor = okHttpClient.newBuilder()
            .followRedirects(false)
            .cookieJar(cookieJar)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.JWC_BASE_URL)
            .client(clientWithInterceptor)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(JWCService::class.java)
    }

    @Provides
    @Singleton
    fun provideAirConditionService(): AirConditionService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.AIR_CONDITION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AirConditionService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherService(): WeatherService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsService(): NewsService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.HTU_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatService(): AIService {
        val retrofit = Retrofit.Builder()
            .baseUrl(SILICON_BASE_URL)
            // .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppService(): AppService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.SMH_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageBoardService(): MessageBoardService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.MESSAGE_BOARD_BASE_URL)
            // .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MessageBoardService::class.java)
    }

    @Provides
    @Singleton
    fun provideSecondClassService(): SecondClassService {
        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.SECOND_CLASS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SecondClassService::class.java)
    }

}

class NetworkCookieJar @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : CookieJar {
    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            val cookies = dataStoreRepo.observeAuthCookie().first()
            cookies.forEach { cookie ->
                cookie.toHttpCookie()?.let { httpCookie ->
                    cookieManager.cookieStore.add(URI.create(cookie.domain), httpCookie)
                }
            }
        }
    }

    fun loadAllCookies(): List<Cookie> {
        return cookieManager.cookieStore.cookies.mapNotNull { it.toOkHttpCookie() }
    }

    fun loadCookiesForUrl(url: String): List<Cookie> {
        val uri = URI.create(url)
        val cookies = cookieManager.cookieStore.get(uri)
        return cookies.mapNotNull { it.toOkHttpCookie() }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = cookieManager.cookieStore.get(url.toUri())
        return cookies.mapNotNull { it.toOkHttpCookie() }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            cookie.toHttpCookie()?.let { httpCookie ->
                cookieManager.cookieStore.add(url.toUri(), httpCookie)
            }
        }
        scope.launch {
            val allCookies = cookieManager.cookieStore.cookies
                .mapNotNull { it.toOkHttpCookie() }
            dataStoreRepo.saveAuthCookie(allCookies)
        }
    }

    private fun Cookie.toHttpCookie(): HttpCookie? {
        return HttpCookie(name, value).apply {
            domain = this@toHttpCookie.domain
            path = this@toHttpCookie.path
            secure = this@toHttpCookie.secure
            isHttpOnly = this@toHttpCookie.httpOnly
        }
    }

    private fun HttpCookie.toOkHttpCookie(): Cookie? {
        return Cookie.Builder()
            .name(name)
            .value(value)
            .domain(domain ?: return null)
            .path(path ?: "/")
            .apply {
                if (maxAge > 0) {
                    expiresAt(System.currentTimeMillis() + maxAge * 1000)
                }
                if (secure) {
                    secure()
                }
                if (isHttpOnly) {
                    httpOnly()
                }
            }
            .build()
    }

    fun clearCookies() {
        scope.launch {
            cookieManager.cookieStore.removeAll()
            dataStoreRepo.saveAuthCookie(emptyList())
        }
    }

}

