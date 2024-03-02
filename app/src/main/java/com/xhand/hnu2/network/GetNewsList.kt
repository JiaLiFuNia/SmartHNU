package com.xhand.hnu2.network

import android.util.Log
import androidx.compose.runtime.MutableState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.xhand.hnu2.model.entity.ArticleListEntity
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.log

var firstList = mutableListOf<ArticleListEntity>()
var num = 0
fun getNewsList(str: Response<ResponseBody>, type: String): MutableList<ArticleListEntity> {
    var ruleDate = "ul.news_list > li.news > div.wz > div.news_time"
    var ruleTime = "ul.news_list > li.news > div.wz > div.news_title > a"
    val ruleDate2 = "ul.news_list li.news span.news_meta"
    val ruleTime2 = "ul.news_list li.news span.news_title a"
    if (type[0] == 't') {
        ruleDate = ruleDate2
        ruleTime = ruleTime2
    }
    val document: Document = Jsoup.parse(str.body()?.string())
    val dateElements =
        document.select(ruleDate)
    val liElements =
        document.select(ruleTime)
    for (index in 0 until dateElements.size) {
        var url = liElements[index].attr("href")
        if (url[0] != 'h') {
            url = "https://www.htu.edu.cn${liElements[index].attr("href")}"
        }
        num += 1
        firstList.add(
            ArticleListEntity(
                title = liElements[index].attr("title"),
                time = dateElements[index].text(),
                id = num,
                url = url,
                type = type
            )
        )
    }
    return firstList
}

