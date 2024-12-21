package com.smart.htu.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val horizontal = 8
private const val vertical = 12

private val PreferenceTitleVariant: TextStyle
    @Composable get() = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)

private val PreferenceTitle
    @Composable get() = MaterialTheme.typography.titleMedium

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier =
        Modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = enabled,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick,
        ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()

            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(24.dp),
                    )
                }

                is Painter -> {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(24.dp),
                    )
                }

                is Int -> {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(50.dp)
                            .clip(CircleShape),
                    )
                }
            }
            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(
                        horizontal = if (icon == null && leadingIcon == null) 8.dp else 0.dp
                    )
                    .padding(end = 8.dp)
            ) {
                PreferenceItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty())
                    PreferenceItemDescription(text = description, enabled = enabled)
            }
            trailingIcon?.let {
                /*VerticalDivider(
                    modifier =
                    Modifier
                        .height(32.dp)
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    thickness = 1.dp,
                )*/
                trailingIcon.invoke()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemVariant(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: () -> Unit = {},
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier =
        Modifier.combinedClickable(
            enabled = enabled,
            onClick = onClick,
            onClickLabel = onClickLabel,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp),
                )
            }
            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = if (icon == null) 12.dp else 0.dp)
                    .padding(end = 8.dp)
            ) {
                PreferenceItemTitle(text = title, enabled = enabled)
                if (description != null) {
                    PreferenceItemDescription(text = description, enabled = enabled)
                }
            }
        }
    }
}

@Composable
fun PreferenceSingleChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    onClick: () -> Unit,
) {
    Surface(modifier = Modifier.selectable(selected = selected, onClick = onClick)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = PreferenceTitleVariant,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
                modifier = Modifier
                    .padding()
                    .clearAndSetSemantics {},
            )
        }
    }
}

@Composable
internal fun PreferenceItemTitle(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 2,
    style: TextStyle = PreferenceTitle,
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onBackground,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = style,
        overflow = overflow,
    )
}

@Composable
internal fun PreferenceItemDescription(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = style,
        overflow = overflow,
    )
}

@Composable
fun rememberThumbContent(
    isChecked: Boolean,
    checkedIcon: ImageVector = Icons.Outlined.Check,
): (@Composable () -> Unit)? =
    remember(isChecked, checkedIcon) {
        if (isChecked) {
            {
                Icon(
                    imageVector = checkedIcon,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    }

@Composable
fun PreferenceSwitchVariant(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isChecked: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: (() -> Unit) = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier =
        Modifier.toggleable(
            value = isChecked,
            enabled = enabled,
            onValueChange = { onClick() },
            indication = LocalIndication.current,
            interactionSource = interactionSource,
        )
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp)
                .padding(start = if (icon == null) 12.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                PreferenceItemTitle(text = title, enabled = enabled, style = PreferenceTitleVariant)
                if (!description.isNullOrEmpty())
                    PreferenceItemDescription(text = description, enabled = enabled)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                interactionSource = interactionSource,
                modifier = Modifier.padding(start = 20.dp, end = 6.dp),
                enabled = enabled,
                thumbContent = thumbContent,
            )
        }
    }
}

@Composable
fun PreferenceSwitch(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    @DrawableRes iconRes: Int? = null,
    enabled: Boolean = true,
    isChecked: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: (() -> Unit) = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier = Modifier
            .toggleable(
                value = isChecked,
                enabled = enabled,
                onValueChange = { onClick() },
                indication = LocalIndication.current,
                interactionSource = interactionSource,
            ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp)
                .padding(start = if (icon == null && iconRes == null) 12.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp)
                )
            }
            iconRes?.let {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                PreferenceItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty())
                    PreferenceItemDescription(text = description, enabled = enabled)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                interactionSource = interactionSource,
                modifier = Modifier.padding(start = 20.dp, end = 6.dp),
                enabled = enabled,
                thumbContent = thumbContent,
            )
        }
    }
}

@Composable
fun PreferenceSwitchWithDivider(
    title: String,
    description: String? = null,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    isSwitchEnabled: Boolean = enabled,
    isChecked: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: (() -> Unit) = {},
    onChecked: () -> Unit = {},
) {
    Surface(
        modifier =
        Modifier.clickable(
            enabled = enabled,
            onClick = onClick
        ),
        color = Color.Transparent
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                PreferenceItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty())
                    PreferenceItemDescription(text = description, enabled = enabled)
            }
            VerticalDivider(
                modifier =
                Modifier
                    .height(32.dp)
                    .padding(horizontal = 8.dp)
                    .width(1f.dp)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
            Switch(
                checked = isChecked,
                onCheckedChange = { onChecked() },
                modifier =
                Modifier
                    .padding(horizontal = 6.dp)
                    .semantics { contentDescription = title },
                enabled = isSwitchEnabled,
                thumbContent = thumbContent,
            )
        }
    }
}

@Composable
fun PreferencesHintCard(
    title: String = "Title ".repeat(2),
    description: String? = "Description text ".repeat(3),
    icon: Any? = null,
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
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp)
            )

            is Int -> Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(40.dp)
            )

            is Painter -> Image(
                painter = icon,
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
                .padding(start = if (icon == null) 12.dp else 0.dp, end = 12.dp)
        ) {
            with(MaterialTheme) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = PreferenceTitleVariant
                )
                if (description != null)
                    Text(
                        text = description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.bodyMedium,
                    )
            }
        }
    }
}

@Composable
fun PreferencesCautionCard(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp),
            )
        }
        iconRes?.let {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp),
            )
        }
        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(start = if (icon == null && iconRes == null) 12.dp else 0.dp, end = 12.dp)
        ) {
            with(MaterialTheme) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = PreferenceTitleVariant,
                )
                if (description != null)
                    Text(
                        text = description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.bodyMedium,
                    )
            }
        }
    }
}

@Composable
fun PreferenceSwitchWithContainer(
    title: String,
    icon: ImageVector? = null,
    isChecked: Boolean,
    thumbContent: @Composable (() -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .toggleable(
                value = isChecked,
                onValueChange = { onClick() },
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(start = if (icon == null) 12.dp else 0.dp, end = 12.dp)
        ) {
            Text(
                text = title,
                maxLines = 2,
                style = PreferenceTitleVariant,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Switch(
            checked = isChecked,
            interactionSource = interactionSource,
            onCheckedChange = null,
            modifier = Modifier.padding(start = 12.dp, end = 6.dp),
            thumbContent = thumbContent,
        )
    }
}

@Composable
fun CreditItem(
    title: String,
    license: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                with(MaterialTheme) {
                    Text(
                        text = title,
                        maxLines = 1,
                        style = typography.titleMedium
                    )
                    license?.let {
                        Text(
                            text = it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun TemplateItem(
    label: String = "",
    template: String? = null,
    selected: Boolean = false,
    isMultiSelectEnabled: Boolean = false,
    checked: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Surface(
        modifier =
        Modifier.run {
            if (!isMultiSelectEnabled)
                then(
                    this.combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                )
            else {
                then(this.toggleable(value = checked, onValueChange = onCheckedChange))
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isMultiSelectEnabled) {
                Checkbox(
                    modifier = Modifier.clearAndSetSemantics {},
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                with(MaterialTheme) {
                    Text(
                        text = label,
                        maxLines = 1,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface,
                    )
                    template?.let {
                        Text(
                            text = it,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = typography.bodyMedium,
                        )
                    }
                }
            }

            AnimatedVisibility(!isMultiSelectEnabled) {
                Row {
                    VerticalDivider(
                        modifier =
                        Modifier
                            .height(32.dp)
                            .padding(horizontal = 12.dp)
                            .align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp,
                    )
                    RadioButton(
                        modifier = Modifier.semantics { contentDescription = label },
                        selected = selected,
                        onClick = onSelect,
                    )
                }
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

@Composable
fun PreferenceInfo(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector = Icons.Outlined.Info,
    @DrawableRes iconRes: Int? = null,
    applyPaddings: Boolean = true,
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .run {
                if (applyPaddings) padding(horizontal = 16.dp, vertical = 16.dp) else this
            }
    ) {
        if (iconRes == null)
            Icon(
                modifier = Modifier.padding(),
                imageVector = icon,
                contentDescription = null
            )
        else
            Icon(painter = painterResource(id = iconRes), contentDescription = null)
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

data class SelectionItem<T>(val label: String, val value: T)

@Composable
fun <T> DropdownListItem(
    value: T?,
    leadingImageVector: Int? = null,
    trailingImageVector: Int? = null,
    headlineText: String,
    selections: List<SelectionItem<T>>,
    onValueChanged: (index: Int, value: T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    BasicListItem(
        headlineText = headlineText,
        supportingText = selections.find { it.value == value }?.label ?: "",
        onClick = { expanded.value = true },
        leadingImageVector = leadingImageVector,
        trailingContent = {
            if (trailingImageVector != null)
                Icon(
                    painter = painterResource(id = trailingImageVector),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
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
fun BasicListItem(
    modifier: Modifier = Modifier,
    headlineText: String? = null,
    supportingText: String? = null,
    leadingImageVector: Int? = null,
    leadingImageVectorModifier: Modifier = Modifier,
    leadingPainter: Painter? = null,
    leadingContent: @Composable() (() -> Unit)? = null,
    leadingText: String? = null,
    trailingContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
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
                Icon(
                    painterResource(id = leadingImageVector),
                    contentDescription = null,
                    modifier = leadingImageVectorModifier
                )
            } else if (leadingPainter != null) {
                Image(
                    painter = leadingPainter, contentDescription = null, modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .size(24.dp)
                )
            } else if (leadingContent != null) {
                leadingContent()
            }
        },
        trailingContent = trailingContent
    )
}
