package com.smart.htu.utils

import com.smart.htu.api.module.NewsItemEntity
import com.smart.htu.screens.news.entity.NewsType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ParseNewsUtil {

    private val PARSE_RULE = ParseRule(
        SingleParseRule("all", "ul.list2 li.news", ""),
        SingleParseRule("title", "div.wz div.news_title a", "title"), // title
        SingleParseRule("url", "div.wz div.news_title a", "href"), // url
        SingleParseRule("time", "div.wz div.news_time", "text"),// time
        SingleParseRule("img_url", "div.imgs a img", "src") // img_url
    )

    private val NOTICE_PARSE_RULE = ParseRule(
        SingleParseRule("all", "ul.list2 li.news", ""),
        SingleParseRule("title", "div.wz div.news_title", "text"), // title
        SingleParseRule("url", "a", "href"), // url
        SingleParseRule("time", "div.news_meta", "date"),// time
        SingleParseRule(
            "top",
            "div.wz div.news_title",
            "font"
        ) // 由于通知公告有部分指定通知而且没有img 所以这里用font检测是否是置顶
    )

    private val MATH_PARSE_RULE = ParseRule(
        SingleParseRule("all", "ul.news_list li.news", ""),
        SingleParseRule("title", "div.news_title a", "title"), // title
        SingleParseRule("url", "div.news_title a", "href"), // url
        SingleParseRule("time", "div.news_time", "text"),// time
        SingleParseRule("img_url", "", "") // img_url
    )

    private val TEACHING_PARSE_RULE = ParseRule(
        SingleParseRule("all", "ul.news_list li.news", ""),
        SingleParseRule("title", "span.news_title a", "title"), // title
        SingleParseRule("url", "span.news_title a", "href"), // url
        SingleParseRule("time", "span.news_meta", "text"),// time
        SingleParseRule("img_url", "", "") // img_url
    )

    private val SEARCH_PARSE_RULE = ParseRule(
        SingleParseRule("all", "div.result_item", ""),
        SingleParseRule("title", "h3.item_title a", "text"), // title
        SingleParseRule("url", "h3.item_title a", "href"), // url
        SingleParseRule("time", "span.item_metas:nth-of-type(2)", "text"),// time
        SingleParseRule("img_url", "div.item_picture img", "src") // img_url
    )

    fun parseNewsHTML(html: String, label: NewsType): List<NewsItemEntity> {
        // Log.i("TAG666 parseHtml", html)
        val resultList = mutableListOf<NewsItemEntity>()
        val rules = when (label) {
            NewsType.NOTICE, NewsType.RESEARCH -> NOTICE_PARSE_RULE
            NewsType.MATH_NEWS, NewsType.MATH_NOTICE, NewsType.MATH_LECTURES -> MATH_PARSE_RULE
            NewsType.TEACHING_NEWS, NewsType.TEACHING_NOTICE, NewsType.TEACHING_ANNOUNCEMENT, NewsType.EXAMINATION_NOTICE -> TEACHING_PARSE_RULE
            NewsType.SEARCH -> SEARCH_PARSE_RULE
            else -> PARSE_RULE
        }
        val document = Jsoup.parse(html)
        val newsListSize = document.select(rules.elementPath.path)
        newsListSize.forEach {
            val newsListElement = NewsItemEntity(
                label = label,
                title = selectElement(it, rules.titlePath),
                _url = selectElement(it, rules.urlPath),
                imgUrlWithoutHttp = selectElement(it, rules.imgUrlPath),
                time = selectElement(it, rules.timePath)
            )
            if (newsListElement.imgUrlWithoutHttp != "top") resultList.add(newsListElement)
            // Log.i("TAG666 parseHtml Element", newsListElement.toString())
        }
        return resultList
    }

    private fun selectElement(element: Element, path: SingleParseRule): String {
        if (path.path.isEmpty() || path.element.isEmpty()) return ""
        val elements = element.select(path.path)
        return if (path.label == "top") {
            // Log.i("TAG666 selectElement", elements.toString())
            if (path.element in elements.toString()) "top" else ""
        } else {
            if (path.element == "text") {
                elements.text()
            } else {
                elements.attr(path.element)
            }
        }
    }

}

data class ParseRule(
    val elementPath: SingleParseRule = SingleParseRule(),
    val titlePath: SingleParseRule = SingleParseRule(),
    val urlPath: SingleParseRule = SingleParseRule(),
    val timePath: SingleParseRule = SingleParseRule(),
    val imgUrlPath: SingleParseRule = SingleParseRule()
)

data class SingleParseRule(
    val label: String = "",
    val path: String = "",
    val element: String = "",
)