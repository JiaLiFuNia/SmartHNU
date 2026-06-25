package com.smart.htu.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smart.htu.screens.application.ApplicationEntity
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MediumAppCard(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    content: ApplicationEntity,
    onClick: () -> Unit
) {
    Surface(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CardDefaults.CornerRadius),
        color = if (enabled) MiuixTheme.colorScheme.surfaceContainer
        else MiuixTheme.colorScheme.disabledSecondaryVariant
    ) {
        val iconColor = MiuixTheme.colorScheme.onPrimary
        val iconBackgroundColor = if (enabled) Color(content.iconColor!!.color) else
            Color(content.iconColor!!.color).copy(0.38f)
        val titleColor =
            if (enabled) MiuixTheme.colorScheme.onBackground else MiuixTheme.colorScheme.disabledOnSecondaryVariant

        BasicComponent(
            startAction = {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = content.icon ?: content.outlinedIcon),
                        contentDescription = "icon",
                        modifier = Modifier.size(20.dp),
                        tint = iconColor
                    )
                }
            },
            enabled = enabled
        ) {
            Text(
                text = stringResource(content.label),
                fontSize = MiuixTheme.textStyles.headline1.fontSize,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
        }
    }
}