package com.smart.htu.screens.news.newsView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smart.htu.R
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun ImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    val hazeState = rememberHazeState()
    Box(modifier = Modifier.fillMaxSize()) {
        val model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
        // 背景模糊图层
        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .hazeEffect(HazeMaterials.ultraThin()) {
                    backgroundColor = Color.Transparent
                    this.blurEnabled = blurEnabled
                    this.drawContentBehind = drawContentBehind
                    this.blurRadius = 50.dp
                },
            contentScale = ContentScale.FillBounds
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.3f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = model,
                    contentDescription = "预览图片",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = onDownload,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(0.2f)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.download_24px),
                        contentDescription = "下载图片",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(0.2f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭预览",
                        tint = Color.White
                    )
                }
            }
        }
    }
}