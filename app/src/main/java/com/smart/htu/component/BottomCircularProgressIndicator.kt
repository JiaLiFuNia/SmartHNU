package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme


@Composable
fun BottomCircularProgressIndicator(
    loadingState: Boolean,
    loadingText: String = "正在加载..."
) {
    OverlayDialog(
        show = loadingState,
        onDismissRequest = {},
        content = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    InfiniteProgressIndicator(
                        color = MiuixTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = loadingText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )
}