package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SettingItemCard(
    modifier: Modifier,
    label: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (label != null)
            SmallTitle(text = label, insideMargin = PaddingValues(start = 12.dp, bottom = 8.dp, top = 16.dp))
        Card(
            modifier = Modifier,
            color = MiuixTheme.colorScheme.surface
        ) {
            content()
        }
    }
}