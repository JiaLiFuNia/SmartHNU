package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class MessageBoardPostsRes(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: PostsListData
)

data class PostsListData(
    val list: List<PostsEntity>,
    val page: PageData
) {
    data class PostsEntity(
        @SerializedName("id") val postID: String,
        @SerializedName("cate_name") val cateName: String,
        val title: String,
        val content: String,
        @SerializedName("user_name") val userName: String,
        @SerializedName("create_time") val createTime: String,
        @SerializedName("status_name") val statusName: String
    )

    data class PageData(
        val currentPage: Int,
        val perPage: String,
        val lastPage: Int,
        val total: Int
    )
}


data class MessageBoardPostDetailRes(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: DetailData
)

data class DetailData(
    val detail: PostDetailData
)

data class PostDetailData(
    @SerializedName("id") val postID: String,
    @SerializedName("cate_name") val cateName: String,
    val title: String,
    val content: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("create_time") val createTime: String,
    @SerializedName("status_name") val statusName: String,
    @SerializedName("org_name") val organizationName: String,
    @SerializedName("team_name") val teamName: String,
    @SerializedName("project_name") val projectName: String,
    @SerializedName("sat") val comment: CommentData,
    @SerializedName("comment_list") val replyList: List<ReplyData>,
    val pics: List<Pic>
) {
    data class CommentData(
        val score: Long,
        val content: String,
        @SerializedName("create_time") val createTime: String
    )

    data class ReplyData(
        val label: String,
        @SerializedName("user_name") val userName: String,
        @SerializedName("create_time") val createTime: String,
        val content: String,
        val pics: List<Pic>
    )
}

data class Pic(
    val url: String
)