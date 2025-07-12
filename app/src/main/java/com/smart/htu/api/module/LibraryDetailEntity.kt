package com.smart.htu.api.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable

@Serializable
data class LibraryDetailEntity(
    val imageUrl: String? = "",
    val title: String? = "",
    val author: String? = "",
    val bookId: String? = "",
    val isbn: String? = "",
    val tags: List<String>? = emptyList(),
    val abstract: String? = "",
)

data class LibraryBookDetailRes(
    val code: Long,
    val msg: Any? = null,
    val data: Data
) {
    data class Data(
        val map: BookDataMap,
        val empty: Boolean
    )
}

data class BookDataMap(
    val baseInfo: BaseInfo,
    val detailInfo: DetailInfo
) {
    data class BaseInfo(
        val map: BaseInfoMap,
        val empty: Boolean
    ) {
        data class BaseInfoMap(
            val author: String,
            val docType: String,
            val isbn: String,
            val title: String,
            val tags: List<String>
        )
    }

    data class DetailInfo(
        val map: DetailInfoMap,
        val empty: Boolean
    ) {
        data class DetailInfoMap(
            @SerializedName("题名/责任者") val author: String,
            @SerializedName("ISBN及定价") val isbn: String,
            @SerializedName("学科主题") val topic: String,
            @SerializedName("提要文摘附注") val abstract: String
        )
    }
}

data class BookBorrowingDetailRes(
    val code: Long,
    val msg: Any? = null,
    val data: BookBorrowingDetailData
) {
    data class BookBorrowingDetailData(
        val holdings: String
    ) {
        val bookBorrowingDetails: List<BookBorrowingDetails>
            get() = try {
                val gson = Gson()
                val type = object : TypeToken<List<BookBorrowingDetails>>() {}.type
                gson.fromJson(holdings, type)
            } catch (e: Exception) {
                Log.e("TAG666 borrow", "解析失败: ${e.message}")
                ArrayList()
            }
    }
}