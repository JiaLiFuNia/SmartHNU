package com.smart.htu.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Download

@Composable
fun ImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    val backdrop = rememberBlurBackdrop(true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
    ) {
        val model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
        val painter = rememberAsyncImagePainter(model)
        val zoomableState = rememberZoomableState()
        val imageState = rememberZoomableImageState(zoomableState)

        // 背景模糊图层
        /*AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .hazeEffect(HazeMaterials.ultraThin(MiuixTheme.colorScheme.surface)) {
                    backgroundColor = Color.Transparent
                    this.blurEnabled = blurEnabled
                    this.drawContentBehind = drawContentBehind
                    this.blurRadius = 50.dp
                },
            contentScale = ContentScale.FillHeight
        )*/

        top.yukonga.miuix.kmp.basic.CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )

        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
            ZoomableAsyncImage(
                model = imageUrl,
                contentDescription = "预览图片",
                state = imageState,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }

        IconButton(
            onClick = onDownload,
            backgroundColor = Color.Black.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(vertical = 32.dp, horizontal = 16.dp)

        ) {
            Icon(
                imageVector = MiuixIcons.Download,
                contentDescription = "下载图片",
                tint = Color.White
            )
        }
        IconButton(
            onClick = onDismiss,
            backgroundColor = Color.Black.copy(alpha = 0.9f),
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