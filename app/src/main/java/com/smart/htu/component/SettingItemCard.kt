package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SettingItemCard(
    themeMode: Int,
    modifier: Modifier,
    label: String? = null,
    cardElevation: CardElevation? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (label != null)
            SmallTitle(text = label, insideMargin = PaddingValues(start = 12.dp, bottom = 8.dp, top = 16.dp))
        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = if (themeMode == 0) MiuixTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            content()
        }
    }
}