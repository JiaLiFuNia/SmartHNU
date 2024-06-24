package com.xhand.hnu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.model.entity.Jszylist
import com.xhand.hnu.model.entity.JxcdxxList
import com.xhand.hnu.model.entity.Jxllist

@Composable
fun ClassroomListItem(
    text: String,
    modifier: Modifier,
    IconModifier: Modifier,
    content: @Composable () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = text
            )
        },
        trailingContent = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = IconModifier)
        },
        modifier = modifier
    )
    content()
}


@Composable
fun ClassroomEmptyListItem(
    text: String,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
            )
        },
        modifier = modifier
    )
}