package com.xhand.hnu2.network

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.xhand.hnu2.model.entity.ArticleListEntity

var num = 0
fun getNewsList(str: String?, type: String, rule: Int): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    // 主页用
    var ruleDate = "ul.news_list > li.news > div.wz > div.news_time"
    var ruleTime = "ul.news_list > li.news > div.wz > div.news_title > a"
    var ifTop = "ul.news_list > li.news > div.wz > div.news_title > a > font"
    // 教务用
    val ruleDate2 = "ul.news_list li.news span.news_meta"
    val ruleTime2 = "ul.news_list li.news span.news_title a"
    // 搜索用
    val ruleDate3 = "div.result_item h3.item_title a"
    val ruleTime3 = "div.result_item span.item_metas:nth-of-type(2)"

    when (rule) {
        1 -> {}
        2 -> {
            ruleDate = ruleDate2
            ruleTime = ruleTime2
        }

        3 -> {
            ruleDate = ruleDate3
            ruleTime = ruleTime3
        }
    }
    val document: Document = Jsoup.parse(str)
    val dateElements =
        document.select(ruleDate) // 链接和标题
    val liElements =
        document.select(ruleTime)
    val topsElements = document.select(ifTop)
    var ifTopNum = topsElements.size
    Log.i("TAG666", "${topsElements.size} ${liElements.size}")
    for (index in 0 until dateElements.size) {
        if (rule == 3) {
            num += 1
            firstList.add(
                ArticleListEntity(
                    title = dateElements[index].text(),
                    time = if (liElements[index].text()[0] == '发') liElements[index].text().substring(5,15) else "",
                    id = num,
                    url = dateElements[index].attr("href"),
                    type = type,
                    isTop = false
                )
            )
            Log.i("TAG666", "$firstList")
        } else {
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
                    type = type,
                    isTop = ifTopNum == 1
                )
            )
        }
        ifTopNum = 0
    }
    return firstList
}

