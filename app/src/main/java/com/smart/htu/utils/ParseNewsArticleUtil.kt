package com.smart.htu.utils

import android.util.Log
import androidx.core.net.toUri
import com.smart.htu.api.module.AttachmentEntity
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.utils.DateUtil.extractDateFromString
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object ParseNewsArticleUtil {

    private val PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div.article", ""),
        SingleParseRule("title", "h1.arti_title", "text"),
        SingleParseRule("publishDate", "span.arti_update", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.wp_articlecontent", "all")
    )

    private val OIP_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div#content_1", ""),
        SingleParseRule("title", "h3.title1", "text"),
        SingleParseRule("publishDate", "div.intro", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.wp_articlecontent", "all")
    )

    private val XTW_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div#content_1", ""),
        SingleParseRule("title", "span.Article_Title", "text"),
        SingleParseRule("publishDate", "span.Article_PublishDate", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.Article_Content", "all")
    )

    private val RSC_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div.article", ""),
        SingleParseRule("title", "h1.arti-title", "text"),
        SingleParseRule("publishDate", "span.arti-update", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.wp_articlecontent", "all")
    )

    private val XGH_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div.article", ""),
        SingleParseRule("title", "h1.arti-title", "text"),
        SingleParseRule("publishDate", "span.Article_PublishDate", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.wp_articlecontent", "all")
    )

    private val LIB_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div.article", ""),
        SingleParseRule("title", "h3", "text"),
        SingleParseRule("publishDate", "div.art-info", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.wp_articlecontent", "all")
    )

    private val BWC_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div#content_1", ""),
        SingleParseRule("title", "span.Article_Title", "text"),
        SingleParseRule("publishDate", "span.Article_PublishDate", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.Article_Content", "all")
    )

    private val XYY_PARSE_RULE = ArticleParseRule(
        SingleParseRule("all", "div#container_page", ""),
        SingleParseRule("title", "span.Article_Title", "text"),
        SingleParseRule("publishDate", "span.Article_PublishDate", "text"),
        SingleParseRule("visitCount", "span.WP_VisitCount", "text"),
        SingleParseRule("articleContent", "div.Article_Content", "all")
    )

    fun parseHTMLToNewsArticle(url: String, html: String): NewsArticleEntity {
        val document = Jsoup.parse(html)
        val rule = selectParseRule(url)
        val newsArticleElement = document.select(rule.elementPath.path)
        val articleEntity = NewsArticleEntity(
            title = selectElement(
                newsArticleElement.firstOrNull() ?: Element("<div></div>"),
                rule.titlePath
            ),
            publishDate = extractDateFromString(
                selectElement(
                    newsArticleElement.firstOrNull() ?: Element("<div></div>"),
                    rule.publishDatePath
                ).toString()
            ),
            visitCount = selectElement(
                newsArticleElement.firstOrNull() ?: Element("<div></div>"),
                rule.visitCountPath
            ),
            attachment = extractAttachment(newsArticleElement),
            articleContent = dealArticleContent(newsArticleElement.select(rule.articleContentPath.path))
        )
        return articleEntity
    }

    fun dealArticleContent(rawArticleHtml: Elements): String {
        // 段落
        val pElements = rawArticleHtml.select("p")
        pElements.forEach {
            // 处理 img
            val imgElement = it.select("img")
            if (imgElement.isNotEmpty()) {
                imgElement.forEach { img ->
                    img.removeAttr("width")
                    img.removeAttr("height")
                    img.removeAttr("style")
                    img.removeAttr("data-layer")
                    img.removeAttr("sudyfile-attr")
                    img.removeAttr("border")
                    img.removeAttr("hspace")
                    img.removeAttr("vspace")
                    img.removeAttr("align")
                    if (img.hasAttr("isupload")) {
                        img.remove()
                    }
                }
            }

            // 处理 p
            if (it.attr("style").contains("text-align:right")) {
                it.addClass("Signature")
            }

            it.removeAttr("style")
            if (it.text().isEmpty() && imgElement.isEmpty()) it.remove()

            // 处理 a
            val aElement = it.select("a")
            if (aElement.isNotEmpty()) {
                aElement.forEach { a ->
                    a.removeAttr("style")
                }
            }

            // 处理 span
            val spanElement = it.select("span")
            if (spanElement.isNotEmpty()) {
                spanElement.forEach { span ->
                    val spanText = span.text()
                    if (spanText.isNotEmpty()) {
                        span.unwrap()
                    } else {
                        span.remove()
                    }
                }
            }

            // 处理 strong
            val strongElement = it.select("strong")
            strongElement.removeAttr("style")
        }

        // 表格
        val tableElements = rawArticleHtml.select("table")
        tableElements.removeAttr("style")
        tableElements.removeAttr("border")
        tableElements.removeAttr("cellspacing")
        tableElements.removeAttr("cellpadding")


        val tdElements = rawArticleHtml.select("td")
        tdElements.removeAttr("style")
        tdElements.removeAttr("bgcolor")
        tdElements.removeAttr("height")
        tdElements.removeAttr("width")
        // tdElements.removeAttr("rowspan")

        val trElements = rawArticleHtml.select("tr")
        trElements.removeAttr("style")

        val colGroupElements = rawArticleHtml.select("colgroup")
        colGroupElements.remove()

        // 删除带有附件的元素
        /*val sudyFileElements = rawArticleHtml.select("[sudyfile-attr]")
        sudyFileElements.forEach {
            it.remove()
        }*/

        // 原始文本
        val rawArticleHtml = rawArticleHtml.toString()
            .replace("article>", "div>")
            .replace("(&nbsp;){2,}", "")
            .replace("&nbsp;", " ")
            .replace("  ", "")
            .replace("\u2003", "")
            .replace("\u3000", " ")

        return rawArticleHtml
    }

    // 提取附件
    private fun extractAttachment(rawArticleHtml: Elements): List<AttachmentEntity> {
        val attachment = mutableListOf<AttachmentEntity>()
        val sudyFileElements = rawArticleHtml.select("[sudyfile-attr]")
        if (sudyFileElements.isNotEmpty()) {
            sudyFileElements.forEach { element ->
                if (element.tagName() != "img") {
                    val url = element.attr("href").ifEmpty { element.attr("pdfsrc") }
                    val fileName = element.text().ifEmpty { element.attr("sudyfile-attr") }
                    val pattern = "'title':\\s*'([^']*)'".toRegex()
                    val matchResult = pattern.find(fileName)
                    val dealFileName = matchResult?.groupValues?.getOrNull(1) ?: fileName
                    attachment.add(
                        AttachmentEntity(
                            fileName = dealFileName,
                            url = "https://www.htu.edu.cn$url",
                            fileType = url.split('.').lastOrNull() ?: "",
                            isNeedOnlineView = element.attr("pdfsrc").isNotEmpty()
                        )
                    )
                }
            }
        }
        return attachment
    }

    private fun selectElement(element: Element, path: SingleParseRule): String? {
        try {
            if (path.path.isEmpty() || path.element.isEmpty()) return null
            val elements = element.select(path.path)
            // Log.i("TAG666", "elements:${element.html()}")
            return when (path.element) {
                "text" -> elements.text().ifEmpty { null }
                "all" -> elements.html().ifEmpty { null }
                else -> elements.attr(path.element).ifEmpty { null }
            }
        } catch (e: Exception) {
            Log.e("TAG666 ParseNewsArticleUtil", "${e.message}")
            return null
        }
    }

    private fun selectParseRule(url: String): ArticleParseRule {
        val type = url.toUri().path.toString().split('/')[1]
        Log.i("TAG666", "selectParseRule: $type ${url.toUri().path}")
        return when (type) {
            "oip" -> OIP_PARSE_RULE
            "xtw" -> XTW_PARSE_RULE
            "rsc" -> RSC_PARSE_RULE
            "xgh" -> XGH_PARSE_RULE
            "skc" -> XTW_PARSE_RULE
            "kyc" -> OIP_PARSE_RULE
            "mhec" -> RSC_PARSE_RULE
            "lib" -> LIB_PARSE_RULE
            "bwc" -> BWC_PARSE_RULE
            // "xyy" -> XYY_PARSE_RULE
            else -> PARSE_RULE
        }
    }

}

data class ArticleParseRule(
    val elementPath: SingleParseRule = SingleParseRule(),
    val titlePath: SingleParseRule = SingleParseRule(),
    val publishDatePath: SingleParseRule = SingleParseRule(),
    val visitCountPath: SingleParseRule = SingleParseRule(),
    val articleContentPath: SingleParseRule = SingleParseRule()
)