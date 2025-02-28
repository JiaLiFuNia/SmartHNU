package com.smart.htu.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
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
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperSpinner
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SmallMediumCardDisplay(
    enabled: Boolean,
    themeMode: Int,
    content: SmallCardContent,
    modifier: Modifier,
    onLongClick: (() -> Unit)? = null,
    onCLick: () -> Unit,
    isCommon: Boolean
) {
    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onLongClick = { showDropDownMenu = true },
                onClick = {
                    onCLick()
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) if (themeMode == 0) MiuixTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
            else if (themeMode == 0) MiuixTheme.colorScheme.disabledSecondaryVariant else MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)
        )
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(24.dp),
                    tint = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(0.38f)
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
                    color = if (enabled) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(0.38f)
                )
            },
            trailingContent = {
                if (content.trailingIcon != null) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = content.trailingIcon),
                        contentDescription = "add"
                    )
                }
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
                    if (onLongClick != null) {
                        onLongClick()
                    }
                    showDropDownMenu = false
                }
            )
        }
    }
}