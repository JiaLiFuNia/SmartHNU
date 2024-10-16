package com.xhand.hnu.network

import android.util.Log
import com.xhand.hnu.model.entity.ArticleListEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getNewsList(str: String?, type: String, rule: Int): MutableList<ArticleListEntity> {
    val firstList: MutableList<ArticleListEntity> = when (rule) {
        2 -> getNewsList2(str, type) // 搜索
        3 -> getNewsList3(str, type) // 教务
        4 -> getNewsList4(str, type) // 通知公告
        else -> getNewsList1(str, type) // 主页
    }
    return firstList
}

// 搜索
fun getNewsList2(str: String?, type: String): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    // 搜索用
    val titleAndUrlRule = "div.result_item h3.item_title a"
    val timeRule = "div.result_item span.item_metas:nth-of-type(2)"
    val timeRule2 = "div.result_item span.item_metas:nth-of-type(3)"
    val document: Document = Jsoup.parse(str.toString())
    val titleAndUrlElements =
        document.select(titleAndUrlRule) // 链接和标题
    val timeElements =
        document.select(timeRule)
    val timeElements2 =
        document.select(timeRule2)
    for (index in 0 until titleAndUrlElements.size) {
        firstList.add(
            ArticleListEntity(
                title = titleAndUrlElements[index].text(),
                time = try {
                    if (timeElements[index].text()[0] == '发') timeElements[index].text()
                        .substring(5, 15) else timeElements2[index].text().substring(5, 15)
                } catch (e: Exception) {
                    timeElements[index].text()
                },
                url = titleAndUrlElements[index].attr("href"),
                type = type,
                isTop = false
            )
        )
    }
    return firstList
}

// 教务
fun getNewsList3(str: String?, type: String): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    val timeRule = "ul.news_list li.news span.news_meta" // time.text
    val titleAndUrlRule =
        "ul.news_list li.news span.news_title a" // title.title url.href
    val document: Document = Jsoup.parse(str.toString())
    val timeElements =
        document.select(timeRule)
    val titleAndUrlElements =
        document.select(titleAndUrlRule)
    for (index in 0 until timeElements.size) {
        var url = titleAndUrlElements[index].attr("href")
        if (url[0] != 'h') {
            url = "https://www.htu.edu.cn${titleAndUrlElements[index].attr("href")}"
        }
        firstList.add(
            ArticleListEntity(
                title = titleAndUrlElements[index].attr("title"),
                time = timeElements[index].text(),
                url = url,
                type = type,
                isTop = false
            )
        )
    }
    return firstList
}

// 通用
fun getNewsList1(str: String?, type: String): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    val timeRule = "ul.news_list > li.news > div.wz > div.news_time" // time.text
    val titleAndUrlRule =
        "ul.news_list > li.news > div.wz > div.news_title > a" // title.title url.href
    val document: Document = Jsoup.parse(str.toString())
    val timeElements =
        document.select(timeRule)
    val titleAndUrlElements =
        document.select(titleAndUrlRule)
    for (index in 0 until timeElements.size) {
        var url = titleAndUrlElements[index].attr("href")
        if (url[0] != 'h') {
            url = "https://www.htu.edu.cn${titleAndUrlElements[index].attr("href")}"
        }
        firstList.add(
            ArticleListEntity(
                title = titleAndUrlElements[index].attr("title"),
                time = timeElements[index].text(),
                url = url,
                type = type,
                isTop = false
            )
        )
    }
    return firstList
}

// 通知公告
fun getNewsList4(str: String?, type: String): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    val timeRule = "ul.news_list > li.news > a > div.news_box > div.news_meta" // time .data
    val urlRule = "ul.news_list > li.news > a" // url .href
    val titleRule =
        "ul.news_list > li.news > a > div.news_box > div.wz > div.news_con" // title .text
    val ifTop =
        "ul.news_list > li.news > a > div.news_box > div.wz > div.news_con > div.news_title > font" // top .text
    val document: Document = Jsoup.parse(str.toString())
    val timeElements =
        document.select(timeRule) // 时间
    val urlElements =
        document.select(urlRule)
    val titleElements =
        document.select(titleRule)
    val topsElements = document.select(ifTop)
    Log.i("TAG666", "topSize: ${topsElements.size}")
    val ifTopNum = topsElements.size
    for (index in 0 until timeElements.size) {
        var url = urlElements[index].attr("href")
        if (url[0] != 'h') {
            url = "https://www.htu.edu.cn${urlElements[index].attr("href")}"
        }
        firstList.add(
            ArticleListEntity(
                title = titleElements[index].text(),
                time = timeElements[index].attr("date"),
                url = url,
                type = type,
                isTop = ifTopNum - index - 1 >= 0
            )
        )
    }
    return firstList
}

data class PictureListItem(
    val url: String,
    val title: String,
    val newsUrl: String
)


// 主页图片
fun getPicList(str: String?): MutableList<PictureListItem> {
    val firstList = mutableListOf<PictureListItem>()
    val document: Document = Jsoup.parse(str.toString())
    // val picRule = "div#banner div.inner ul.news_list li.news div.imgs a img"
    val picRule = "div.bodycon1 div.tab-con1 div.c ul.news_list li.news div.imgs a img"
    val picTitle = "div.bodycon1 div.tab-con1 div.c ul.news_list li.news div.imgs a"
    val picElements = document.select(picRule)
    val picTitleElements = document.select(picTitle)
    for (index in 0 until picElements.size) {
        firstList.add(
            PictureListItem(
                url = "https://www.htu.edu.cn" + picElements[index].attr("src"),
                title = picTitleElements[index].attr("title"),
                newsUrl = "https://www.htu.edu.cn" + picTitleElements[index].attr("href")
            )
        )
    }
    return firstList
}


fun preProcessNewsDetail(str: String?): String {
    val document: Document = Jsoup.parse(str.toString())
    document.select("h1").remove()
    document.select("[class=arti_metas]").remove()
    return document.toString()
}