package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.BookBorrowingDetails
import com.smart.htu.api.module.BookDataMap
import com.smart.htu.api.module.BookImageData
import com.smart.htu.api.module.LibraryBorrowedBookRes
import com.smart.htu.api.module.LibraryLoginPost
import com.smart.htu.api.module.LibrarySearchImgPost
import com.smart.htu.api.module.LibrarySearchPost
import com.smart.htu.api.module.LibrarySearchPost.QueryFieldList
import com.smart.htu.api.module.SearchResultData
import com.smart.htu.api.network.LibraryService
import com.smart.htu.di.NetworkCookieJar
import com.smart.htu.di.NetworkModule.ApiConstants.LIBRARY_BASE_URL
import com.smart.htu.utils.AESUtils
import com.smart.htu.utils.MD5Util.md5
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class LibraryNetworkRepo @Inject constructor(
    private val libraryService: LibraryService,
    private val networkCookieJar: NetworkCookieJar
) {

    suspend fun libraryCurrentBorrowingBookService(
        page: Int,
        pageSize: Int
    ): Result<LibraryBorrowedBookRes?> {
        try {
            val res = libraryService.libraryCurrentBorrowingBook(page, pageSize)
            return when (res.code()) {
                200 -> Result.success(res.body())
                else -> Result.failure(Exception("获取借阅信息失败，错误代码：${res.code()}"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

    suspend fun libraryBorrowedBookService(
        page: Int,
        pageSize: Int
    ): Result<LibraryBorrowedBookRes?> {
        try {
            val res = libraryService.libraryBorrowedBook(page, pageSize)
            return when (res.code()) {
                200 -> Result.success(res.body())
                else -> Result.failure(Exception("获取借阅信息失败，错误代码：${res.code()}"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

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

    // 借阅情况
    suspend fun libraryBookBorrowingDetailService(
        bookId: String
    ): Result<List<BookBorrowingDetails>> {
        try {
            val res = libraryService.libraryBookBorrowingStatus(bookId)
            return Result.success(res.data.bookBorrowingDetails)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception(e))
        }
    }

    suspend fun libraryLogin(
        username: String,
        password: String,
        verifyCode: String
    ): Result<String> {
        try {
            if (username == "" || password == "") {
                return Result.failure(Exception("学号或密码不能为空"))
            }
            val passwordEncrypt = AESUtils.encryptPassword(
                password = password,
                key = md5(username),
                mode = "ECB"
            )
            val loginContext = "{\"userId\":\"${username}\",\"password\":\"${passwordEncrypt}\"}"
            val res =
                libraryService.libraryLogin(LibraryLoginPost(loginContext, verifyCode))
            when (res.body()?.code) {
                0 -> {
                    val session = extractSession(res.headers().get("Set-Cookie").toString())
                    return if (session.isNotEmpty()) {
                        Result.success(session)
                    } else {
                        Result.failure(Exception("未知错误，请稍后重试"))
                    }
                }

                1 -> return Result.failure(Exception(res.body()?.msg))

                else -> return Result.failure(Exception("未知错误，请稍后重试"))
            }
        } catch (e: Exception) {
            return Result.failure(Exception(e))
        }
    }

    suspend fun getLoginPage(): Result<String> {
        try {
            val res = libraryService.libraryLoginPage()
            val setCookie = res.headers()["Set-Cookie"] ?: ""
            val cookiesList = networkCookieJar.loadForRequest(LIBRARY_BASE_URL.toHttpUrl())
            val currentCookie = cookiesList.find { it.name == "meta-opac.session" }?.value ?: ""
            return if (setCookie.isNotEmpty()) {
                Result.success(extractSession(setCookie))
            } else {
                Result.success(currentCookie)
            }
        } catch (e: Exception) {
            return Result.failure(Exception(e))
        }
    }

    fun extractSession(cookie: String): String {
        val matchResult = Regex("""meta-opac.session=([^;]+)""").find(cookie)
        return if (matchResult != null) {
            val session = matchResult.groupValues[1]
            session
        } else {
            ""
        }
    }

}