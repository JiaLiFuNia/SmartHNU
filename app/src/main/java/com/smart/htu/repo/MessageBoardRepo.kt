package com.smart.htu.repo

import com.smart.htu.api.module.PostDetailData
import com.smart.htu.api.module.PostsListData
import com.smart.htu.api.network.AuthLoginService
import com.smart.htu.api.network.MessageBoardService
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.MD5Util.md5
import javax.inject.Inject

class MessageBoardRepo @Inject constructor(
    val messageBoardService: MessageBoardService,
    val authLoginService: AuthLoginService
) {

    suspend fun getMessageBoardPostsService(
        page: Int = 1,
        perPage: Int = 20,
        cateType: Int = -1
    ): Result<PostsListData> {
        try {
            val timeStamp = System.currentTimeMillis().toString()
            val res = messageBoardService.getMessageBoardPosts(
                signature = generateSignature(
                    listOf(
                        timeStamp,
                        cateType.toString(),
                        page.toString(),
                        perPage.toString()
                    )
                ),
                page = page,
                perPage = perPage,
                cateType = cateType,
                time = timeStamp
            )
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
            val timeStamp = System.currentTimeMillis().toString()
            val res = messageBoardService.getMessageBoardPostDetail(
                signature = generateSignature(
                    listOf(timeStamp, postID)
                ),
                id = postID,
                time = timeStamp
            )
            return if (res.code == 0) {
                Result.success(res.data.detail)
            } else {
                Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun authLogin(): Result<String> {
        try {
            val res = authLoginService.authServer("http://yjfk.htu.edu.cn/service/cas?h5=1")
            return if (res.code() == 200) {
                val url = res.raw().request.url.toString()
                if (url.contains("token=")) {
                    Result.success(url.substringAfter("token="))
                } else {
                    Result.failure(Exception("认证失败，未获取到token"))
                }
            } else {
                Result.failure(Exception("认证失败，状态码：${res.code()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun generateSignature(d: List<String>): String {
        val data = md5(d.joinToString(""))
        val date = getCurrentDate("yyyyMMdd")
        val signature = md5("${data}YjYj${date}")
        // Log.i("TAG666", "generateSignature: d=$data, ${d.joinToString("")}, $date")
        return signature
    }

}