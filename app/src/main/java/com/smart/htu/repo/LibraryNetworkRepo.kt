package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.BookDataMap
import com.smart.htu.api.module.BookImageData
import com.smart.htu.api.module.LibrarySearchImgPost
import com.smart.htu.api.module.LibrarySearchPost
import com.smart.htu.api.module.LibrarySearchPost.QueryFieldList
import com.smart.htu.api.module.SearchResultData
import com.smart.htu.api.network.LibraryService
import javax.inject.Inject

class LibraryNetworkRepo @Inject constructor(
    val libraryService: LibraryService, val dataStoreRepo: DataStoreRepo
) {

    // 图书搜索
    suspend fun librarySearchService(
        keyword: String, page: Int
    ): Result<SearchResultData> {
        try {
            val res = libraryService.librarySearch(
                LibrarySearchPost(
                    page = page, queryFieldList = listOf(
                        QueryFieldList(values = listOf(keyword))
                    )
                )
            )
            return Result.success(res.data)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

    // 封面
    suspend fun libraryBookImgService(
        isbnList: List<String>, bookIdList: List<String>
    ): Result<Map<String, List<BookImageData>?>> {
        try {
            val res = libraryService.libraryBookImg(LibrarySearchImgPost(isbnList, bookIdList))
            return if (res.data.isEmpty()) {
                Result.failure(Exception("No data found"))
            } else {
                Result.success(res.data)
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

    suspend fun libraryBookDetailService(
        bookId: String
    ): Result<BookDataMap> {
        try {
            val res = libraryService.libraryBookDetail(bookId)
            return Result.success(res.data.map)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

    suspend fun libraryBookBorrowingDetailService(
        bookId: String
    ): Result<List<BookBorrowingDetails>> {
        try {
            val res = libraryService.libraryBookBorrowingDetail(bookId)
            return Result.success(res.data.bookBorrowingDetails)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

}