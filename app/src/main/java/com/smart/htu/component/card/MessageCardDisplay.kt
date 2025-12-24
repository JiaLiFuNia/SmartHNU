package com.smart.htu.component.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MessageCardDisplay(
    modifier: Modifier,
    message: List<SingleInfo>,
    labelOnTop: Boolean = true,
) {
    top.yukonga.miuix.kmp.basic.Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            message.groupBy { it.rowIndex }.forEach { row ->
                Row {
                    row.value.forEach {
                        InfoItem(
                            labelOnTop = labelOnTop,
                            title = it.label,
                            content = it.content,
                            leadingIcon = it.leadingIcon,
                            modifier = Modifier.weight((1.0 / row.value.size.toFloat()).toFloat()),
                            rightContent = it.rightContent,
                        )
                    }
                }
            }

        }
    }
}

data class SingleInfo(
    val label: String,
    val content: String,
    val leadingIcon: ImageVector? = null,
    val rightContent: (@Composable () -> Unit)? = null,
    val rowIndex: Int,
)

@Composable
fun InfoItem(
    rightContent: (@Composable () -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    title: String,
    content: String,
    onClick: (() -> Unit)? = null,
    labelOnTop: Boolean = true,
    modifier: Modifier
) {
    // BasicComponent()
    Row(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                }
            )
            .heightIn(min = 56.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            top.yukonga.miuix.kmp.basic.Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (labelOnTop) {
                top.yukonga.miuix.kmp.basic.Text(
                    text = title,
                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    maxLines = 1
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = content,
                    fontSize = MiuixTheme.textStyles.headline1.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = MiuixTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            } else {
                top.yukonga.miuix.kmp.basic.Text(
                    text = content,
                    fontSize = MiuixTheme.textStyles.headline1.fontSize,
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = title,
                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    maxLines = 1
                )
            }
        }
        if (rightContent != null) {
            Spacer(modifier = Modifier.width(8.dp))
            rightContent()
        }
    }
}