package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
fun SuggestChip(
    onClick: () -> Unit,
    text: String,
    type: SuggestChipType,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val containerColor = when (type) {
        SuggestChipType.INFO -> MiuixTheme.colorScheme.tertiaryContainer
        SuggestChipType.ERROR -> MiuixTheme.colorScheme.errorContainer
    }
    val textColor = when (type) {
        SuggestChipType.INFO -> MiuixTheme.colorScheme.onTertiaryContainer
        SuggestChipType.ERROR -> MiuixTheme.colorScheme.error
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.defaultColors(containerColor),
        onClick = { onClick() },
        pressFeedbackType = PressFeedbackType.Sink,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            top.yukonga.miuix.kmp.basic.Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier
                    .weight(1f),
                color = textColor,
            )
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

enum class SuggestChipType {
    INFO,
    ERROR
}