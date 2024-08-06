package com.xhand.hnu.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu.model.entity.Jszylist

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
    classroom: Jszylist,
    modifier: Modifier,
    jcdm: String,
    roomAndCourseList: List<Jszylist>,
    isShowHadClassRoom: Boolean
) {
    val matchedElements =
        roomAndCourseList.filter { it.jxcdmc == classroom.jxcdmc && it.jcdm == jcdm }
    if (matchedElements.isNotEmpty() && isShowHadClassRoom)
        null
    else {
        ListItem(
            headlineContent = {
                Text(
                    text = classroom.jxcdmc,
                )
            },
            supportingContent = {
                if (matchedElements.isNotEmpty()) {
                    Text(
                        text = "${matchedElements[0].teaxms} ${matchedElements[0].kcmc}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            },
            trailingContent = {
                if (matchedElements.isNotEmpty())
                    Text(text = "有课")
                else
                    Text(text = "空闲")
            },
            modifier = modifier
        )
    }
}