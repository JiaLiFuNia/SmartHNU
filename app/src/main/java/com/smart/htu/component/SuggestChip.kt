package com.smart.htu.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@Composable
fun SuggestChip(
    onClick: () -> Unit,
    onActionClick: () -> Unit,
    text: String,
    type: SuggestChipType,
    visibility: State<Boolean>,
    modifier: Modifier = Modifier,
    icon: Any? = Icons.Outlined.Close,
) {
    AnimatedVisibility(visible = visibility.value) {
        top.yukonga.miuix.kmp.basic.Surface(
            shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
            color = if (type == SuggestChipType.ERROR) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
            onClick = onClick,
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (type == SuggestChipType.ERROR) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    IconButton(
                        onClick = { onActionClick() },
                        modifier = Modifier.size(25.dp)
                    ) {
                        when (icon) {
                            is Int -> Icon(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                tint = if (type == SuggestChipType.ERROR) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )

                            is ImageVector -> Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (type == SuggestChipType.ERROR) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}

enum class SuggestChipType {
    INFO,
    ERROR
}