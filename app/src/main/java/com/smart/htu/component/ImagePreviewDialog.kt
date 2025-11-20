package com.smart.htu.component

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
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.smart.htu.R
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState

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
        val painter = rememberAsyncImagePainter(model)
        val zoomableState = rememberZoomableState()
        val imageState = rememberZoomableImageState(zoomableState)

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
            contentScale = ContentScale.FillHeight
        )

        androidx.compose.material3.CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.3f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                ZoomableAsyncImage(
                    model = imageUrl,
                    contentDescription = "预览图片",
                    state = imageState,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )


                /*AsyncImage(
                    model = model,
                    contentDescription = "预览图片",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    onState = {}
                )*/

                IconButton(
                    onClick = onDownload,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(
                            0.2f
                        )
                    ),
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
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(
                            0.2f
                        )
                    ),
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