package com.smart.htu.di

import com.smart.htu.api.NetworkService
import com.smart.htu.repo.DataStoreRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @AuthLoginNetworkService
    fun provideFirstNetworkService(
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
    fun provideSecondNetworkService(
        dataStoreRepo: DataStoreRepo
    ): NetworkService {
        return createRetrofitService(
            NetworkService.E_HALL_BASE_URL,
            dataStoreRepo,
            createOkHttpClient(dataStoreRepo)
        )
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

    private fun configureRetrofitBuilder(baseUrl: String, retrofitBuilder: Retrofit.Builder) {
        when (baseUrl) {
            NetworkService.BASE_URL -> {
                retrofitBuilder.baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
            }

            NetworkService.E_HALL_BASE_URL -> {
                retrofitBuilder.baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
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

    init {
        val cookies = runBlocking { dataStoreRepo.observeCookies().first() }
        cookies.forEach { cookie ->
            cookieManager.cookieStore.add(URI.create(cookie.domain), cookie.toHttpCookie())
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = cookieManager.cookieStore.get(url.toUri())
        return cookies.map { it.toOkHttpCookie() }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            cookieManager.cookieStore.add(url.toUri(), cookie.toHttpCookie())
        }
        runBlocking {
            dataStoreRepo.changeCookies(cookieManager.cookieStore.cookies.map { it.toOkHttpCookie() })
        }
    }

    private fun Cookie.toHttpCookie(): HttpCookie {
        val httpCookie = HttpCookie(name, value)
        httpCookie.domain = domain
        httpCookie.path = path
        httpCookie.secure = secure
        httpCookie.isHttpOnly = httpOnly
        return httpCookie
    }

    private fun HttpCookie.toOkHttpCookie(): Cookie {
        return Cookie.Builder()
            .name(name)
            .value(value)
            .domain(domain)
            .path(path)
            .expiresAt(maxAge)
            .build()
    }
}
