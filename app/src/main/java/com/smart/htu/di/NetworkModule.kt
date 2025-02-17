package com.smart.htu.di

import android.util.Log
import com.smart.htu.api.network.AirConditionService
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.EHallService
import com.smart.htu.api.network.GiteeService
import com.smart.htu.api.network.JWCService
import com.smart.htu.api.network.LibraryService
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
        const val AUTH_SERVER_BASE_URL = "https://authserver2.htu.edu.cn/"
        const val JWC_BASE_URL = "https://jwc.htu.edu.cn/"
        const val E_HALL_BASE_URL = "https://ehall2.htu.edu.cn/"
        const val QQ_BASE_URL = "https://q1.qlogo.cn/"
        const val LIBRARY_BASE_URL = "http://libmsg.htu.cn/"
        const val AIR_CONDITION_BASE_URL = "https://application.xiaofubao.com/"
        const val GITEE_BASE_URL = "https://gitee.com/"
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
            .baseUrl(ApiConstants.AUTH_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AuthLoginService::class.java)
    }


    @Provides
    @Singleton
    fun provideEHallService(
        okHttpClient: OkHttpClient
    ): EHallService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.E_HALL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(EHallService::class.java)
    }

    @Provides
    @Singleton
    fun provideLibraryService(): LibraryService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.LIBRARY_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(LibraryService::class.java)
    }

    @Provides
    @Singleton
    fun provideJWCService(
        okHttpClient: OkHttpClient,
        dataStoreRepo: DataStoreRepo
    ): JWCService {
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
    fun provideGiteeConfig(): GiteeService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.GITEE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GiteeService::class.java)
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
            try {
                val cookies = dataStoreRepo.observeCookies().first()
                cookies.forEach { cookie ->
                    cookie.toHttpCookie()?.let { httpCookie ->
                        cookieManager.cookieStore.add(URI.create(cookie.domain), httpCookie)
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkCookieJar", "Error initializing cookies: ${e.message}")
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return try {
            val cookies = cookieManager.cookieStore.get(url.toUri())
            cookies.mapNotNull { it.toOkHttpCookie() }
        } catch (e: Exception) {
            Log.e("NetworkCookieJar", "Error loading cookies: ${e.message}")
            emptyList()
        }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        try {
            cookies.forEach { cookie ->
                cookie.toHttpCookie()?.let { httpCookie ->
                    cookieManager.cookieStore.add(url.toUri(), httpCookie)
                }
            }
            scope.launch {
                val allCookies = cookieManager.cookieStore.cookies
                    .mapNotNull { it.toOkHttpCookie() }
                dataStoreRepo.saveCookies(allCookies)
            }
        } catch (e: Exception) {
            Log.e("NetworkCookieJar", "Error saving cookies: ${e.message}")
        }
    }

    private fun Cookie.toHttpCookie(): HttpCookie? {
        return try {
            HttpCookie(name, value).apply {
                domain = this@toHttpCookie.domain
                path = this@toHttpCookie.path
                secure = this@toHttpCookie.secure
                isHttpOnly = this@toHttpCookie.httpOnly
            }
        } catch (e: Exception) {
            Log.e("NetworkCookieJar", "Error converting to HttpCookie: ${e.message}")
            null
        }
    }

    private fun HttpCookie.toOkHttpCookie(): Cookie? {
        return try {
            Cookie.Builder()
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
        } catch (e: Exception) {
            Log.e("NetworkCookieJar", "Error converting to OkHttpCookie: ${e.message}")
            null
        }
    }

    suspend fun clearCookies() {
        cookieManager.cookieStore.removeAll()
        dataStoreRepo.saveCookies(emptyList())
    }

}

