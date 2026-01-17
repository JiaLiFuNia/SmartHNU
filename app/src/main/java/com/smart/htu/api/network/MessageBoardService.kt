package com.smart.htu.api.network

import com.smart.htu.api.module.MessageBoardPostDetailRes
import com.smart.htu.api.module.MessageBoardPostsRes
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface MessageBoardService {

    @GET("service/app/posts/list")
    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 Edg/132.0.0.0")
    suspend fun getMessageBoardPosts(
        @Header("signature") signature: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("cate_type") cateType: Int,
        @Query("_time") time: String
    ): MessageBoardPostsRes

    @GET("service/app/posts/list_detail")
    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 Edg/132.0.0.0")
    suspend fun getMessageBoardPostDetail(
        @Header("signature") signature: String,
        @Query("id") id: String,
        @Query("_time") time: String
    ): MessageBoardPostDetailRes

}