package com.smart.htu.utils

import android.util.Log
import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.screens.news.entity.NewsType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ParseNewsUtil {

    private val PARSE_RULE = ParseRule(
        SingleParseRule("ul.list2 li.news", ""),
        SingleParseRule("div.wz div.news_title a", "title"), // title
        SingleParseRule("div.wz div.news_title a", "href"), // url
        SingleParseRule("div.wz div.news_time", "text"),// time
        SingleParseRule("div.imgs a img", "src") // img_url
    )

    private val NOTICE_PARSE_RULE = ParseRule(
        SingleParseRule("ul.list2 li.news", ""),
        SingleParseRule("div.wz div.news_title", "text"), // title
        SingleParseRule("a", "href"), // url
        SingleParseRule("div.news_meta", "date"),// time
        SingleParseRule("a", "href") // img_url
    )


    fun parseBannerImg(html: String, label: NewsType): List<NewsItemEntity> {
        // Log.i("TAG666 parseHtml", html)
        val resultList = mutableListOf<NewsItemEntity>()
        val rules = when (label) {
            NewsType.NOTICE -> NOTICE_PARSE_RULE
            else -> PARSE_RULE
        }
        val document = Jsoup.parse(html)
        val newsListSize = document.select(rules.allElementPath.path)
        // Log.i("TAG666 parseHtml", newsListSize.toString())
        newsListSize.forEach {
            val newsListElement = NewsItemEntity(
                label = label,
                title = selectElement(it, rules.titlePath.path, rules.titlePath.element),
                _url = selectElement(it, rules.urlPath.path, rules.urlPath.element),
                _imgUrl = selectElement(it, rules.imgUrlPath.path, rules.imgUrlPath.element),
                time = selectElement(it, rules.timePath.path, rules.timePath.element)
            )
            resultList.add(newsListElement)
            Log.i("TAG666 parseHtml", newsListElement.toString())
        }
        return resultList
    }

    private fun selectElement(element: Element, path: String, target: String): String {
        val elements = element.select(path)
        return if (target == "text") {
            elements.text()
        } else
            elements.attr(target)
    }

}

data class ParseRule(
    val allElementPath: SingleParseRule = SingleParseRule(),
    val titlePath: SingleParseRule = SingleParseRule(),
    val urlPath: SingleParseRule = SingleParseRule(),
    val timePath: SingleParseRule = SingleParseRule(),
    val imgUrlPath: SingleParseRule = SingleParseRule()
)

data class SingleParseRule(
    val path: String = "",
    val element: String = "",
)