package com.xhand.hnu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.xhand.hnu.model.entity.Jxllist

@Composable
fun ClassroomListItem(
    building: Jxllist,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = building.jzwmc
            )
        },
        trailingContent = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        },
        modifier = modifier
    )
}