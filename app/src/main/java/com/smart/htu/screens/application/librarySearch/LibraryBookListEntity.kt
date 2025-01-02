package com.smart.htu.screens.application.librarySearch

data class LibraryBookListEntity(
    val title: String,
    val description: String,
    val imageUrl: String,
    val availability: String,
    private val _id: String
) {
    private val regex = "^[a-zA-Z0-9-*:#@./]+".toRegex()
    private val bookPosition = regex.find(description)?.value ?: ""
    private val info = description.replace(" ", "").substring(bookPosition.length).split("/")
    val publishYear = info.last()
    val publisher = info[0]
    val publishPlace = info[1]

    private val regexId = "id=([0-9]+)".toRegex()
    private val matchResult = regexId.find(_id)
    val id = matchResult?.groupValues?.get(1) ?: ""
}

data class LibraryBookDetail(
    val library: String, // 书库
    val bookPosition: String, // 分类号
    val description: String // 可借几本
) {
    val bookName = bookPosition
    val publisher: String
        get() = library.substring(5)
    private val info: String
        get() = description.replace("　", "").replace(" ", "").replace(" ", "")

    private val isbnRegex = "ISBN：([0-9-]+)".toRegex()
    private val publisherRegex = "出版信息：([^/]+)".toRegex()
    private val publishYearRegex = "/(.*?)I".toRegex()
    private val isbnMatch = isbnRegex.find(info)
    private val publisherMatch = publisherRegex.find(info)
    private val publishYearMatch = publishYearRegex.find(info)
    val isbn = (isbnMatch?.groupValues?.get(1) ?: "").replace("-", "")
    val publishPlace = publisherMatch?.groupValues?.get(1) ?: ""
    val publishYear = publishYearMatch?.groupValues?.get(1) ?: ""
}


data class RentBookEntity(
    val bookName: String,
    val publisher: String,
    val id: String
)