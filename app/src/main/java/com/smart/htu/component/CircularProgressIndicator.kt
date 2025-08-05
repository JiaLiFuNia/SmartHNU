package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.ProgressIndicatorDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LazyItemScope.CircularProgressIndicator(
    size: Dp = ProgressIndicatorDefaults.DefaultInfiniteProgressIndicatorSize,
    loadingText: String = "正在加载..."
) {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfiniteProgressIndicator(
                modifier = Modifier.padding(top = 88.dp),
                size = size
            )
            Text(
                text = loadingText,
                modifier = Modifier.padding(top = 8.dp),
                style = MiuixTheme.textStyles.subtitle.copy(
                    color = Color.Gray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = ProgressIndicatorDefaults.DefaultInfiniteProgressIndicatorSize,
    loadingText: String = "正在加载..."
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfiniteProgressIndicator(size = size)
            Text(
                text = loadingText,
                modifier = Modifier.padding(top = 8.dp),
                style = MiuixTheme.textStyles.subtitle.copy(
                    color = Color.Gray
                )
            )
        }
    }
}