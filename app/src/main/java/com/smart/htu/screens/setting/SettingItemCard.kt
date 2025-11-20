package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle

@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    label: String? = null,
    titlePaddingValues: PaddingValues = PaddingValues(start = 12.dp, bottom = 8.dp, top = 16.dp),
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (label != null)
            SmallTitle(
                text = label,
                insideMargin = titlePaddingValues
            )
        Card {
            content()
        }
    }
}