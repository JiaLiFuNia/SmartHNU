package com.smart.htu.screens.application.webview

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
import okhttp3.Cookie
import javax.inject.Inject

@HiltViewModel
class AppWebViewViewModel @Inject constructor(
    private val networkCookieJar: NetworkCookieJar
) : ViewModel() {
    private val _cookies = MutableStateFlow<List<Cookie>>(emptyList())
    val cookies: StateFlow<List<Cookie>> = _cookies.asStateFlow()

    fun loadCookiesForUrl(url: String) {
        viewModelScope.launch {
            val cookie = networkCookieJar.loadAllCookies()
            Log.d("TAG666 WebViewViewModel", "$cookie $url")
            _cookies.update { cookie }
        }
    }

}