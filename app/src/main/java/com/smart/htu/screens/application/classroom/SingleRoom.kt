package com.smart.htu.screens.application.classroom

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SingleRoom(
    label: String,
    formerPeriodBusyState: Boolean,
    latterPeriodBusyState: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Surface(
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        modifier = modifier
            .height(50.dp),
        color = MiuixTheme.colorScheme.surfaceContainer,
        onClick = {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = if (formerPeriodBusyState) MiuixTheme.colorScheme.disabledSecondaryVariant
                            else MiuixTheme.colorScheme.surfaceContainer
                        )
                )
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = if (latterPeriodBusyState) MiuixTheme.colorScheme.disabledSecondaryVariant
                            else MiuixTheme.colorScheme.surfaceContainer
                        )
                )
            }
            Text(
                text = label,
                maxLines = 1,
                color = if (formerPeriodBusyState && latterPeriodBusyState) MiuixTheme.colorScheme.disabledOnSurface
                else MiuixTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .basicMarquee(
                        repeatDelayMillis = 2_000,
                    )
            )
        }
    }
}