package com.smart.htu.component.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MessageCardDisplay(
    modifier: Modifier,
    message: List<SingleInfo>,
) {
    top.yukonga.miuix.kmp.basic.Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                message.forEachIndexed { index, it ->
                    if (index < message.size / 2)
                        FocusCardItem(
                            title = it.label,
                            content = it.content,
                            leadingContent = {
                                if (it.leadingIcon != null) {
                                    Icon(
                                        painter = painterResource(it.leadingIcon),
                                        contentDescription = null
                                    )
                                }
                            },
                            onClick = {},
                            modifier = Modifier
                        )
                }
            }
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                message.forEachIndexed { index, it ->
                    if (index >= message.size / 2)
                        FocusCardItem(
                            title = it.label,
                            content = it.content,
                            leadingContent = {
                                if (it.leadingIcon != null) {
                                    Icon(
                                        painter = painterResource(it.leadingIcon),
                                        contentDescription = null
                                    )
                                }
                            },
                            onClick = {},
                            modifier = Modifier
                        )
                }
            }
        }
    }
}

data class SingleInfo(
    val label: String,
    val content: String,
    @DrawableRes val leadingIcon: Int? = null
)

@Composable
fun FocusCardItem(
    containerColor: Color = Color.Transparent,
    leadingContent: @Composable () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
    title: String,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = containerColor),
        leadingContent = {
            leadingContent()
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        },
        trailingContent = {
            if (trailingContent != null) {
                trailingContent()
            }
        },
        supportingContent = {
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
        },
        modifier = modifier
            .clickable {
                onClick()
            }
    )
}