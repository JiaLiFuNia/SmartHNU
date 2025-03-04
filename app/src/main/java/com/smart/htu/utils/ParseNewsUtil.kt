package com.smart.htu.utils

import android.util.Log
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.screens.news.entity.NewsType
import org.jsoup.Jsoup

object ParseNewsUtil {

    private val PARSE_RULE = mapOf(
        NewsType.BANNER to listOf(
            ParseRule("div.imgs a", "title"), // title
            ParseRule("div.imgs a", "href"), // url
            ParseRule("div.imgs a img","src"),// img_url
            ParseRule("div.imgs a","title"),// time
        )
    )

    fun parseBannerImg(html: String, label: NewsType): List<NewsItemEntity> {
        // Log.i("TAG666 parseHtml", html)
        val resultList = mutableListOf<NewsItemEntity>()
        val rules = PARSE_RULE[label] ?: emptyList()
        val document = Jsoup.parse(html)
        val titleElements = document.select(rules[0].path)
        Log.i("TAG666", "parseBannerImg: $titleElements")
        val urlElements = document.select(rules[1].path)
        val imgElements = document.select(rules[2].path)
        val timeElements = document.select(rules[3].path)
        for (index in 0 until urlElements.size) {
            val title = titleElements[index].attr(rules[0].element)
            val url = urlElements[index].attr(rules[1].element)
            val imgUrl = imgElements[index].attr(rules[2].element)
            val time = timeElements[index].attr(rules[3].element)
            resultList.add(NewsItemEntity(label, title, url, imgUrl, time))
        }
        return resultList
    }

}

data class ParseRule(
    val path: String,
    val element: String,
)