package com.smart.htu.screens.news.newsView

import android.webkit.JavascriptInterface


interface JavaScriptInterface {

    @JavascriptInterface
    fun onImgTagClick(imgUrl: String?)

    companion object {

        const val NAME = "JavaScriptInterface"
    }
}