package com.xhand.hnu.components

import com.chimbori.crux.api.Extractor
import com.chimbori.crux.api.Fields.DURATION_MS
import com.chimbori.crux.api.Fields.TITLE
import com.chimbori.crux.api.Resource
import com.chimbori.crux.common.estimatedReadingTimeMs
import com.chimbori.crux.common.isLikelyArticle
import net.dankito.readability4j.extended.Readability4JExtended
import okhttp3.HttpUrl

class ArticleParsing : Extractor {
    override fun canExtract(url: HttpUrl) = url.isLikelyArticle()
    override suspend fun extract(request: Resource): Resource? =
        if (request.url != null && request.document != null) {
            val readability4J = Readability4JExtended(request.url.toString(), request.document!!)
            val article = readability4J.parse()
            Resource(
                article = article.articleContent,
                metadata = mapOf(
                    TITLE to article.title,
                    DURATION_MS to article.articleContent?.text()?.estimatedReadingTimeMs()
                ),
            )
        } else {
            null
        }
}