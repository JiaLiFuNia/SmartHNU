package com.xhand.hnu2.network

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.xhand.hnu2.model.entity.ArticleListEntity

var num = 0
fun getNewsList(str: String?, type: String, rule: Int): MutableList<ArticleListEntity> {
    val firstList = mutableListOf<ArticleListEntity>()
    var ruleDate = "ul.news_list > li.news > div.wz > div.news_time"
    var ruleTime = "ul.news_list > li.news > div.wz > div.news_title > a"
    val ruleDate2 = "ul.news_list li.news span.news_meta"
    val ruleTime2 = "ul.news_list li.news span.news_title a"
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
    for (index in 0 until dateElements.size) {
        if (rule == 3) {
            num += 1
            firstList.add(
                ArticleListEntity(
                    title = dateElements[index].text(),
                    time = liElements[index].text(),
                    id = num,
                    url = dateElements[index].attr("href"),
                    type = type
                )
            )
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
                    type = type
                )
            )
        }
    }
    return firstList
}

