package com.smart.htu.screens.webview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.di.NetworkCookieJar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {
    private val _cookies = MutableStateFlow<Map<String, String>>(emptyMap())
    val cookies: StateFlow<Map<String, String>> = _cookies.asStateFlow()

    fun loadCookiesForUrl(url: String) {
        viewModelScope.launch {
            try {
                val httpUrl = url.toHttpUrlOrNull() ?: return@launch
                // val cookies = networkCookieJar.loadForRequest(httpUrl)
                val cookies = networkCookieJar.loadAllCookies()
                Log.d("TAG666 WebViewViewModel", "Loaded ${cookies.size} cookies for $url")
                cookies.forEach {
                    Log.d("TAG666 WebViewViewModel", "Cookie: ${it.name}=${it.value}")
                }
                val cookieMap = cookies.associate { it.name to it.value }
                _cookies.update { cookieMap }
            } catch (e: Exception) {
                Log.e("TAG666 WebViewViewModel", "加载Cookie失败", e)
            }
        }
    }

    fun getCookiesForUrl(url: String): Map<String, String> {
        val httpUrl = url.toHttpUrlOrNull() ?: return emptyMap()
        val cookies = networkCookieJar.loadForRequest(httpUrl)
        return cookies.associate { it.name to it.value }
    }

    fun clearCookies() {
        _cookies.update { emptyMap() }
    }
}