package com.smart.htu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun LazyItemScope.EmptyContent(
    text: String,
    image: ImageVector? = null
) {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (image != null) {
                val painter = rememberVectorPainter(image = image)
                Image(
                    painter = painter,
                    contentDescription = "null",
                    modifier = Modifier
                        .fillMaxWidth(0.56f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MiuixTheme.textStyles.subtitle.copy(
                    color = Color.Gray
                )
            )
        }

    }
}

@Composable
fun EmptyContent(
    text: String,
    image: ImageVector? = null,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (image != null) {
                val painter = rememberVectorPainter(image = image)
                Image(
                    painter = painter,
                    contentDescription = "null",
                    modifier = Modifier
                        .fillMaxWidth(0.56f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MiuixTheme.textStyles.subtitle.copy(
                    color = Color.Gray
                )
            )
        }
    }
}