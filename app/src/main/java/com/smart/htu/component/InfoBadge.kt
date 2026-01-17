package com.smart.htu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun InfoBadge(
    text: String,
    color: Color = MiuixTheme.colorScheme.primary
) {
    if (text.isNotEmpty())
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = text,
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .basicMarquee(
                        repeatDelayMillis = 2_000,
                    ),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary)
            )
        }
}