package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingItemCard(
    modifier: Modifier,
    label: String? = null,
    cardElevation: CardElevation? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (label != null)
            PreferenceSubtitle(text = label)
        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun SettingItemCardPreview() {
    SettingItemCard(
        label = "显示",
        modifier = Modifier
    ) {
        Column {
            PreferenceSwitch(
                title = "动态颜色",
                description = "将壁纸颜色应用于主题",
                isChecked = true,
                onClick = {
                }
            )
            PreferenceSwitch(
                title = "深色主题",
                description = "自动切换夜间模式",
                isChecked = true,
                onClick = {
                }
            )
        }
    }
}