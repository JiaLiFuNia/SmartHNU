package com.smart.htu.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smart.htu.screens.application.entity.SmallCardContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SmallMediumCardDisplay(
    content: SmallCardContent,
    modifier: Modifier,
    onLongClick: () -> Unit,
    onCLick: () -> Unit,
    isCommon: Boolean
) {
    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        ),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onLongClick = { showDropDownMenu = true },
                onClick = {
                    onCLick()
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            supportingContent = {
                if (content.description != null)
                    Text(
                        text = content.description,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            },
            headlineContent = {
                Text(
                    text = stringResource(id = content.label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            repeatDelayMillis = 2_000,
                        ),
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        DropdownMenu(
            expanded = showDropDownMenu,
            onDismissRequest = { showDropDownMenu = false },
            shape = RoundedCornerShape(15.dp),
        ) {
            DropdownMenuItem(
                enabled = isCommon,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "add"
                    )
                },
                text = { Text(text = "添加到主页") },
                onClick = {
                    onLongClick()
                    showDropDownMenu = false
                }
            )
        }
    }
}