package com.smart.htu.api.network

import com.smart.htu.api.module.MessageBoardPostDetailRes
import com.smart.htu.api.module.MessageBoardPostsRes
import retrofit2.http.GET
import retrofit2.http.Query

interface MessageBoardService {

    @GET("service/app/posts/list")
    suspend fun getMessageBoardPosts(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("cate_type") cateType: Int
    ): MessageBoardPostsRes

    @GET("service/app/posts/list_detail")
    suspend fun getMessageBoardPostDetail(
        @Query("id") id: String
    ): MessageBoardPostDetailRes

}