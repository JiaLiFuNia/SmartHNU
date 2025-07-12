package com.smart.htu.repo

import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostsListData
import com.smart.htu.api.network.MessageBoardService
import javax.inject.Inject

class MessageBoardRepo @Inject constructor(
    val messageBoardService: MessageBoardService
) {

    suspend fun getMessageBoardPostsService(
        page: Int = 1,
        perPage: Int = 20,
        cateType: Int = -1
    ): Result<PostsListData> {
        try {
            val res = messageBoardService.getMessageBoardPosts(page, perPage, cateType)
            return if (res.code == 0) {
                Result.success(res.data)
            } else {
                Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getMessageBoardPostDetailService(
        postID: String
    ): Result<PostDetailData> {
        try {
            val res = messageBoardService.getMessageBoardPostDetail(postID)
            return if (res.code == 0) {
                Result.success(res.data.detail)
            } else {
                Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}