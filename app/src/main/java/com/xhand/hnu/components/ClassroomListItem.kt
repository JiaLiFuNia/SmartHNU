package com.xhand.hnu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.model.entity.Jszylist
import com.xhand.hnu.model.entity.JxcdxxList
import com.xhand.hnu.model.entity.Jxllist

@Composable
fun ClassroomListItem(
    text: String,
    modifier: Modifier,
    iconModifier: Modifier,
    content: @Composable () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = text
            )
        },
        trailingContent = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = iconModifier)
        },
        modifier = modifier
    )
    content()
}


@Composable
fun ClassroomEmptyListItem(
    classroom: JxcdxxList,
    modifier: Modifier,
    ifHavingClass: Boolean,
    ifShowHadClass: Boolean
) {
    if (ifHavingClass and ifShowHadClass)
        null
    else
        ListItem(
            headlineContent = {
                Text(
                    text = classroom.jxcdmc,
                )
            },
            supportingContent = {
                if (ifHavingClass)
                    Text(
                        text = "${classroom.teaxms} ${classroom.kcmc}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
            },
            trailingContent = {
                if (ifHavingClass)
                    Text(text = "有课")
                else
                    Text(text = "空闲")
            },
            modifier = modifier
        )
}