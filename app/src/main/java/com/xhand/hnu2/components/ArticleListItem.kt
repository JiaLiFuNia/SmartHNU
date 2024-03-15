package com.xhand.hnu2.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu2.model.entity.ArticleListEntity

@Composable
fun ArticleListItem(
    article: ArticleListEntity,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = article.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = "时间：${article.time}"
            )
        },
        modifier = modifier
    )
}
