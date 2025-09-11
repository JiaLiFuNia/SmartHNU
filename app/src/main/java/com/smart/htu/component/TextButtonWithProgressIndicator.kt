package com.smart.htu.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun TextButtonWithProgressIndicator(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !isLoading && enabled,
        colors = ButtonDefaults.buttonColors(
            color = MiuixTheme.colorScheme.primaryContainer,
            disabledColor = MiuixTheme.colorScheme.disabledPrimaryButton
        )
    ) {
        AnimatedVisibility(
            visible = isLoading
        ) {
            InfiniteProgressIndicator(
                modifier = Modifier.padding(end = 8.dp),
                size = 16.dp,
                color = MiuixTheme.colorScheme.disabledOnPrimaryButton
            )
        }
        top.yukonga.miuix.kmp.basic.Text(
            textAlign = TextAlign.Center,
            text = text,
            color = if (isLoading) MiuixTheme.colorScheme.disabledOnPrimaryButton else MiuixTheme.colorScheme.primary
        )
    }
}