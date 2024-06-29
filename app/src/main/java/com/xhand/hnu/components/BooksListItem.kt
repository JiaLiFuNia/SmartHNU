package com.xhand.hnu.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xhand.hnu.model.entity.Xdjcdata

@Composable
fun BooksListItem(book: Xdjcdata, modifier: Modifier) {
    ListItem(
        headlineContent = { Text(text = book.kcmc) },
        supportingContent = { Text(text = book.kcdlmc) },
        trailingContent = { Text(text = book.xnxqmc) },
        modifier = modifier
    )
}