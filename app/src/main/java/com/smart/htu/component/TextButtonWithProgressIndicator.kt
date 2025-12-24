package com.smart.htu.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonColors
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.TextButtonColors

@Composable
fun TextButtonWithProgressIndicator(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    textColors: TextButtonColors = ButtonDefaults.textButtonColors()
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !isLoading && enabled,
        colors = colors
    ) {
        AnimatedVisibility(
            visible = isLoading
        ) {
            InfiniteProgressIndicator(
                modifier = Modifier.padding(end = 8.dp),
                size = 16.dp,
                color = textColors.disabledTextColor
            )
        }
        top.yukonga.miuix.kmp.basic.Text(
            textAlign = TextAlign.Center,
            text = text,
            color = if (isLoading) textColors.disabledTextColor else textColors.textColor
        )
    }
}