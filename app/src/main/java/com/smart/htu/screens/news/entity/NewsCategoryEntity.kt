package com.smart.htu.screens.news.entity

import androidx.annotation.StringRes
import com.smart.htu.R

data class NewsCategoryEntity(
    val label: NewsType,
    val source: String,
    val academic: String,
    val url: String
)

enum class NewsType(@StringRes val label: Int) {
    BANNER(R.string.banner_img),
    NOTICE(R.string.notice),
    FAST_NEWS(R.string.fast_news),
    HEADLINES(R.string.headlines),
    MEDIA(R.string.media),
    MATH_NEWS(R.string.math_news),
    MATH_NOTICE(R.string.math_notice),
    TEACHING_NEWS(R.string.teaching_news),
    TEACHING_NOTICE(R.string.teaching_notice),
    TEACHING_ANNOUNCEMENT(R.string.teaching_announcement),
    EXAMINATION_NOTICE(R.string.examination_notice)
}