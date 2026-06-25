package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// 请求参数类
data class LibrarySearchPost(
    val page: Int = 1,
    val pageSize: Int = 20,
    val indexName: String = "idx.opac",
    val sortField: String = "relevance",
    val sortType: String = "desc",
    val collapseField: String = "groupId",
    val queryFieldList: List<QueryFieldList>,
    val filterFieldList: List<QueryFieldList> = emptyList()
) {
    data class QueryFieldList(
        val logic: Int = 0,
        val field: String = "all",
        val values: List<String>,
        val operator: String = "*"
    )
}

data class LibrarySearchImgPost(
    val isbns: List<String>,
    val bibIds: List<String>
)


// 响应结果类
data class LibrarySearchRes(
    val code: Int,
    val msg: String? = null,
    val data: SearchResultData
)

data class SearchResultData(
    val total: Int,
    val actualTotal: Int,
    val dataList: List<SearchBookData>? = emptyList(),
)

@Serializable
data class SearchBookData(
    @SerializedName("bibId") val bookId: String = "",
    @SerializedName("author") val author: String? = "",
    @SerializedName("isbn") val isbn: String = "",
    @SerializedName("title") val title: String? = "",
    @SerializedName("pub_year") val publishYear: String? = "",
    @SerializedName("publisher") val publisher: String? = "",
    @SerializedName("circCount") val borrowableCount: Int? = 0,
    @SerializedName("itemCount") val totalCount: Int? = 0,
    var imageUrl: String? = null,
)

@Serializable
data class BookBorrowingDetails(
    val callNo: String,
    val location: String,
    val status: String,
    val locationName: String
)

data class LibrarySearchImgRes(
    val code: Int,
    val msg: String? = null,
    val data: Map<String, List<BookImageData>?>
)

data class BookImageData(
    val imageUrl: String
) {
    val coverImageUrl: String
        get() = if (imageUrl.startsWith("//")) {
            "https:$imageUrl"
        } else imageUrl
}