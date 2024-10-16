package com.xhand.hnu.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.xhand.hnu.model.entity.ArticleListEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ArticleListItem(
    article: ArticleListEntity,
    modifier: Modifier,
    loaded: Boolean,
    color: ListItemColors = ListItemDefaults.colors(),
) {
    ListItem(
        headlineContent = {
            Text(
                text = if (article.isTop) "[置顶] ${article.title}" else article.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.placeholder(
                    visible = loaded,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
        },
        supportingContent = {
            Text(
                text =
                if (article.time == "")
                    ""
                else {
                    if (article.time.first() == '目')
                        article.time
                    else timeSwitch(
                        dateString = article.time
                    )
                },
                color = Color.Gray,
                modifier = Modifier.placeholder(
                    visible = loaded,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
        },
        modifier = modifier,
        colors = color
    )
    // HorizontalDivider()
}

fun timeSwitch(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 格式转换
    val parsedDate = LocalDate.parse(dateString, formatter) // 新闻日期
    val currentDate = LocalDate.now() // 当前日期
    val result =
        if (dateString.substring(2, 4) == currentDate.year.toString().substring(2, 4)) {
            when (currentDate.toEpochDay() - parsedDate.toEpochDay()) { // 日期差
                0L -> "今天"
                1L -> "昨天"
                2L -> "前天"
                3L -> "3天前"
                4L -> "4天前"
                5L -> "5天前"
                else -> datePattern(dateString.substring(5))
            }
        } else {
            datePattern(dateString)
        }
    return result
}


fun datePattern(date: String): String {
    val parts = date.split("-")
    return if (parts.size == 2) {
        val month = parts[0].toInt()
        val day = parts[1].toInt()
        "${month}月${day}日"
    } else {
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        "${year}年${month}月${day}日"
    }
}