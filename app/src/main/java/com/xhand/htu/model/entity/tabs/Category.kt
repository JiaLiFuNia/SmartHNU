package com.xhand.htu.model.entity.tabs

import com.xhand.htu.model.entity.BaseResponse

//首页标签数据类
data class Category(
    val title: String,
    val id: String
)

data class CategoryResponse(var data:List<Category>): BaseResponse() {}
