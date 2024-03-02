package com.xhand.hnu2.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BasicListItem(
    modifier: Modifier = Modifier,
    headlineText: String? = null,
    supportingText: String? = null,
    leadingImageVector: Int? = null,
    leadingPainter: Painter? = null,
    leadingText: String? = null,
    trailingContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = onClick?.let {
            modifier.clickable(
                onClick = onClick
            )
        } ?: modifier,
        headlineContent = {
            if (headlineText != null) {
                Text(headlineText)
            }
        },
        supportingContent = {
            if (supportingText != null) {
                Text(supportingText)
            }
        },
        leadingContent = {
            if (leadingText != null) {
                Text(
                    text = leadingText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            } else if (leadingImageVector != null) {
                Icon(painterResource(id = leadingImageVector), contentDescription = null)
            } else if (leadingPainter != null) {
                Image(
                    painter = leadingPainter, contentDescription = null, modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .size(24.dp)
                )
            }
        },
        trailingContent = trailingContent
    )
}
