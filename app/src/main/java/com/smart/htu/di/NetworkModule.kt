package com.smart.htu.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.smart.htu.api.NetworkService
import com.smart.htu.repo.DataStoreRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.URI
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthLoginNetworkService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthMessageNetworkService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CleanMessageNetworkService

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @AuthLoginNetworkService
    fun provideAuthLoginNetworkService(
        dataStoreRepo: DataStoreRepo
    ): NetworkService {
        return createRetrofitService(
            NetworkService.BASE_URL,
            dataStoreRepo,
            createOkHttpClient(dataStoreRepo)
        )
    }

    @Singleton
    @Provides
    @AuthMessageNetworkService
    fun provideAuthMessageNetworkService(
        dataStoreRepo: DataStoreRepo
    ): NetworkService {
        return createRetrofitService(
            NetworkService.E_HALL_BASE_URL,
            dataStoreRepo,
            createOkHttpClient(dataStoreRepo)
        )
    }

    @Singleton
    @Provides
    @CleanMessageNetworkService
    fun provideLibraryMessageNetworkService(
        dataStoreRepo: DataStoreRepo
    ): NetworkService {
        val retrofit = Retrofit.Builder().baseUrl(NetworkService.LIBRARY_URL).build()
        return retrofit.create(NetworkService::class.java)
    }

    private fun createOkHttpClient(dataStoreRepo: DataStoreRepo): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(NetworkCookieJar(dataStoreRepo))
            .addInterceptor { chain ->
                try {
                    chain.proceed(chain.request())
                } catch (e: Exception) {
                    okhttp3.Response.Builder()
                        .protocol(Protocol.HTTP_1_1)
                        .request(chain.request())
                        .message("Network connection error")
                        .body("Network connection error".toResponseBody())
                        .build()
                }
            }
            .build()
    }

    private fun createRetrofitService(
        baseUrl: String,
        dataStoreRepo: DataStoreRepo,
        client: OkHttpClient
    ): NetworkService {
        val retrofitBuilder = Retrofit.Builder()
            .client(client)

        configureRetrofitBuilder(baseUrl, retrofitBuilder)

        val retrofit = retrofitBuilder.build()
        return retrofit.create(NetworkService::class.java)
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun configureRetrofitBuilder(baseUrl: String, retrofitBuilder: Retrofit.Builder) {
        when (baseUrl) {
            NetworkService.BASE_URL -> {
                retrofitBuilder.baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                // .addConverterFactory(GsonConverterFactory.create())
            }

            NetworkService.E_HALL_BASE_URL -> {
                retrofitBuilder.baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                // .addConverterFactory(ScalarsConverterFactory.create())
            }

            else -> {
                retrofitBuilder.baseUrl(baseUrl)
            }
        }
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
                try {
                    val allCookies =
                        cookieManager.cookieStore.cookies.mapNotNull { it.toOkHttpCookie() }
                    dataStoreRepo.changeCookies(allCookies)
                } catch (e: Exception) {
                    Log.e("NetworkCookieJar", "Error saving cookies to DataStore: ${e.message}")
                }
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
                // 设置过期时间
                if (this@toHttpCookie.expiresAt != Long.MIN_VALUE) {
                    maxAge = (this@toHttpCookie.expiresAt - System.currentTimeMillis()) / 1000
                }
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

    private fun HttpUrl.toUri(): URI {
        return try {
            URI(toString())
        } catch (e: Exception) {
            Log.e("NetworkCookieJar", "Error converting HttpUrl to URI: ${e.message}")
            URI.create(host)
        }
    }

    // 清理资源
    fun clear() {
        scope.cancel()
        cookieManager.cookieStore.removeAll()
    }
}
