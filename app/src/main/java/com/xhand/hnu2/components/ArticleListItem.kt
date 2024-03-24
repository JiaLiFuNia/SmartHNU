package com.xhand.hnu2.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu2.model.entity.ArticleListEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ArticleListItem(
    article: ArticleListEntity,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = if (article.isTop) "[置顶] ${article.title}" else article.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = if (article.time == "") "" else timeSwitch(dateString = article.time),
                color = Color.Gray
            )
        },
        modifier = modifier
    )
    // HorizontalDivider()
}

fun timeSwitch(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 格式转换
    val parsedDate = LocalDate.parse(dateString, formatter) // 新闻日期
    val currentDate = LocalDate.now() // 当前日期

    val result = when (currentDate.toEpochDay() - parsedDate.toEpochDay()) { // 日期差
        0L -> "今天"
        1L -> "昨天"
        2L -> "前天"
        else -> dateString
    }
    return result
}