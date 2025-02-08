package com.smart.htu.utils

import com.smart.htu.screens.application.librarySearch.LibraryBookDetail
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


fun parseLibrarySearchResult(html: String): Pair<String, MutableList<LibraryBookListEntity>> {
    val document = Jsoup.parse(html)
    val books = mutableListOf<LibraryBookListEntity>()
    val bookElements = document.select("a.weui_media_box")
    for (element: Element in bookElements) {
        val title = element.select("h4.weui_media_title").text()
        val description = element.select("p.weui_media_desc").text()
        var imageUrl = element.select("img.weui_media_appmsg_thumb").attr("src")
        if (imageUrl.first() != 'h') imageUrl = "http://libmsg.htu.cn${imageUrl}"
        val availability = element.select("ul.weui_media_info li.weui_media_info_meta").text()
        val id = element.select("a.weui_media_box").attr("href")
        books.add(LibraryBookListEntity(title, description, imageUrl, availability, id))
    }
    val count = document.select("div.center").text().split("/").last()
    return count to books
}

fun parseLibraryBookDetail(html: String): MutableList<LibraryBookDetail> {
    val document = Jsoup.parse(html)
    val books = mutableListOf<LibraryBookDetail>()
    val bookElements = document.select("div.weui_media_box")
    for (element: Element in bookElements) {
        val library = element.select("p.weui_media_desc").text()
        val bookPosition = element.select("h4.weui_media_title").text()
        val description = element.select("ul.weui_media_info li.weui_media_info_meta").text()
        books.add(LibraryBookDetail(library, bookPosition, description))
    }
    return books
}