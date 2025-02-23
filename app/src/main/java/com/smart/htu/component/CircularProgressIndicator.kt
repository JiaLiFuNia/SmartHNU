package com.smart.htu.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LazyItemScope.CircularProgressIndicator() {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularWavyProgressIndicator(modifier = Modifier.padding(top = 88.dp))
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircularProgressIndicator(modifier: Modifier, size: Dp = 36.dp) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularWavyProgressIndicator(modifier = Modifier.size(size))
    }
}