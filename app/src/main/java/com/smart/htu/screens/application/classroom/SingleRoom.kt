package com.smart.htu.screens.application.classroom

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@Composable
fun SingleRoom(
    themeMode: Int,
    label: String,
    state: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    top.yukonga.miuix.kmp.basic.Surface(
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        modifier = modifier
            .height(50.dp),
        color = when(state) {
            true -> if (themeMode == 0) MiuixTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer
            false -> if (themeMode == 0) MiuixTheme.colorScheme.disabledSecondaryVariant  else MaterialTheme.colorScheme.surfaceContainer
        },
        onClick = {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                maxLines = 1,
                color = if (state) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onBackground.copy(0.38f),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
        }
    }
}