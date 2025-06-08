package com.smart.htu.api.network

import com.smart.htu.api.module.BookBorrowingDetailRes
import com.smart.htu.api.module.LibraryBookDetailRes
import com.smart.htu.api.module.LibrarySearchImgPost
import com.smart.htu.api.module.LibrarySearchImgRes
import com.smart.htu.api.module.LibrarySearchPost
import com.smart.htu.api.module.LibrarySearchRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryService {

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
    suspend fun libraryBookBorrowingDetail(
        @Path("bibId") bookId: String,
        @Query("isMobile") isMobile: Int = 0,
        @Query("relateStat") relateStat: Int = 1
    ): BookBorrowingDetailRes

}