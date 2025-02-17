package com.smart.htu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smart.htu.R

data class SelectionItem<T>(val label: String, val value: T)

@Composable
fun BasicListItem(
    modifier: Modifier = Modifier,
    headlineText: String? = null,
    supportingText: String? = null,
    leadingIcon: Any? = null,
    trailingContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
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
            when (leadingIcon) {
                is ImageVector -> Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )

                is Painter -> Icon(
                    painter = leadingIcon,
                    contentDescription = null
                )

                is Int -> Image(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            }
        },
        trailingContent = trailingContent
    )
}

@Composable
fun CommonListItem(
    headlineText: String,
    supportingText: String? = null,
    leadingIcon: Any? = null,
    onClick: (() -> Unit)? = null,
) {
    BasicListItem(
        headlineText = headlineText,
        supportingText = supportingText,
        leadingIcon = leadingIcon,
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        onClick = onClick
    )
}


@Composable
fun SwitchListItem(
    value: Boolean,
    leadingIcon: Any? = null,
    headlineText: String,
    supportingText: String? = null,
    onValueChanged: (value: Boolean) -> Unit,
    switchEnable: Boolean = true
) {
    BasicListItem(
        leadingIcon = leadingIcon,
        headlineText = headlineText,
        supportingText = supportingText,
        trailingContent = {
            Switch(
                enabled = switchEnable,
                checked = value,
                onCheckedChange = {
                    onValueChanged(it)
                }
            )
        }
    ) {
        onValueChanged(!value)
    }
}

@Composable
fun <T> DropdownListItem(
    value: T?,
    leadingIcon: Any? = null,
    headlineText: String,
    selections: List<SelectionItem<T>>,
    onValueChanged: (index: Int, value: T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    BasicListItem(
        headlineText = headlineText,
        supportingText = selections.find { it.value == value }?.label ?: "",
        onClick = { expanded.value = true },
        leadingIcon = leadingIcon,
        trailingContent = {
            Icon(
                painter = painterResource(id = R.drawable.outline_unfold_more_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                shape = RoundedCornerShape(15.dp),
            ) {
                selections.forEachIndexed { index, selection ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .background(
                                if (selection.value == value)
                                    MaterialTheme.colorScheme.surfaceVariant
                                else
                                    Color.Transparent,
                            ),
                        text = { Text(selection.label) },
                        onClick = {
                            expanded.value = false
                            onValueChanged(index, selection.value)
                        },
                        trailingIcon = {
                            if (selection.value == value)
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "check",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun PreferencesCard(
    headlineText: String,
    supportingText: String,
    leadingIcon: Any? = null,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(containerColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (leadingIcon) {
            is ImageVector -> Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
            )

            is Int -> Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
            )

            is Painter -> Image(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .clip(CircleShape)
                    .size(50.dp)
            )
        }
        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(start = if (leadingIcon == null) 12.dp else 0.dp, end = 12.dp)
        ) {
            with(MaterialTheme) {
                Text(
                    text = headlineText,
                    maxLines = 1
                )
                Text(
                    text = supportingText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.bodyMedium,
                )
            }
        }
    }
}


@Composable
fun PreferenceSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, top = 20.dp, bottom = 8.dp),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = text,
        modifier = modifier.padding(contentPadding),
        color = color,
        style = MaterialTheme.typography.labelLarge,
    )
}