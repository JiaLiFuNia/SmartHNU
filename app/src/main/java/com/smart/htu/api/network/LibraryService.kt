package com.smart.htu.api.network

import com.smart.htu.api.module.BookBorrowingDetailRes
import com.smart.htu.api.module.LibraryBookDetailRes
import com.smart.htu.api.module.LibraryBorrowedBookRes
import com.smart.htu.api.module.LibraryLoginPost
import com.smart.htu.api.module.LibraryLoginRes
import com.smart.htu.api.module.LibrarySearchImgPost
import com.smart.htu.api.module.LibrarySearchImgRes
import com.smart.htu.api.module.LibrarySearchPost
import com.smart.htu.api.module.LibrarySearchRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryService {

    @POST("meta-local/opac/sys/login")
    suspend fun libraryLogin(
        @Header("Cookie") session: String = "",
        @Body body: LibraryLoginPost
    ): Response<LibraryLoginRes>

    @GET("meta-local/opac/sys/pic_check")
    suspend fun libraryLoginPage(): Response<ResponseBody>

    @POST("meta-local/opac/search")
    suspend fun librarySearch(
        @Body body: LibrarySearchPost
    ): LibrarySearchRes

    @POST("meta-local/opac/search/extend3")
    suspend fun libraryBookImg(
        @Body body: LibrarySearchImgPost
    ): LibrarySearchImgRes

    @GET("meta-local/opac/bibs/{bibId}/infos")
    suspend fun libraryBookDetail(
        @Path("bibId") bookId: String,
        @Query("detail") detail: Boolean = true,
        @Query("isMobile") isMobile: Int = 0
    ): LibraryBookDetailRes

    @GET("meta-local/opac/bibs/{bibId}/holdings")
    suspend fun libraryBookBorrowingStatus(
        @Path("bibId") bookId: String,
        @Query("isMobile") isMobile: Int = 0,
        @Query("relateStat") relateStat: Int = 1
    ): BookBorrowingDetailRes

    @GET("meta-local/opac/users/loan_hists")
    suspend fun libraryBorrowedBook(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<LibraryBorrowedBookRes>

    @GET("meta-local/opac/users/loans")
    suspend fun libraryCurrentBorrowingBook(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<LibraryBorrowedBookRes>

}