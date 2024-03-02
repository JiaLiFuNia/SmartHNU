package com.xhand.hnu2.network

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.xhand.hnu2.model.entity.ArticleListEntity

fun getNewsList(): List<ArticleListEntity> {
    val okHttpClient = OkHttpClient()
    val requestBuilder = Request.Builder()
    val firstList = mutableListOf<ArticleListEntity>()
    val ruleDate = "ul.news_list > li.news > div.wz > div.news_time"
    val ruleTime = "ul.news_list > li.news > div.wz > div.news_title > a"
    var num = 1
    Thread {
        for (i in 1..10) {
            val request: Request =
                requestBuilder.url("https://www.htu.edu.cn/8955/list${i}.htm").build()
            val response = okHttpClient.newCall(request).execute()
            val str = response.body!!.string()
            response.body!!.close()
            val document: Document = Jsoup.parse(str)
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
                        type = "通知公告"
                    )
                )
            }
        }
        for (i in 1..10) {
            val request: Request =
                requestBuilder.url("https://www.htu.edu.cn/8954/list${i}.htm").build()
            val response = okHttpClient.newCall(request).execute()
            val str = response.body!!.string()
            response.body!!.close()
            val document: Document = Jsoup.parse(str)
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
                        type = "师大要闻"
                    )
                )
            }
        }
        for (i in 1..10) {
            val request: Request =
                requestBuilder.url("https://www.htu.edu.cn/teaching/3251/list${i}.htm").build()
            val response = okHttpClient.newCall(request).execute()
            val str = response.body!!.string()
            response.body!!.close()
            val document: Document = Jsoup.parse(str)
            val dateElements =
                document.select("ul.news_list li.news span.news_meta")
            val liElements =
                document.select("ul.news_list li.news span.news_title a")
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
                        type = "教务通知"
                    )
                )
            }
        }
    }.start()

    firstList.add(
        ArticleListEntity(
            title = "获取失败",
            time = "获取失败",
            id = num,
            url = "https://www.htu.edu.cn/teaching/3258/list.htm",
            type = "公示公告"
        )
    )
    firstList.add(
        ArticleListEntity(
            title = "获取失败",
            time = "获取失败",
            id = num,
            url = "https://www.htu.edu.cn/teaching/kwgl/list.htm",
            type = "考务管理"
        )
    )
    return firstList
}

