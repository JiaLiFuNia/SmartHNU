package com.xhand.htu.model.entity

import android.icu.text.CaseMap.Title
import java.sql.Timestamp
//文章数据类
data class ArticleEntity(
    val title: String,
    var timestamp: String
)
