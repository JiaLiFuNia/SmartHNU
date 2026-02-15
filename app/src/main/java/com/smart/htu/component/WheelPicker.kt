package com.smart.htu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.filter
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.math.abs

@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialSelectedItem: T,
    modifier: Modifier = Modifier,
    visibleCount: Int = 5,
    itemHeight: Dp = 52.dp,
    formatItem: (T) -> String = { it.toString() },
    onSelectedItemChange: (T) -> Unit
) {
    require(visibleCount % 2 == 1)
    require(items.isNotEmpty())

    val halfVisible = visibleCount / 2
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }

    val infiniteCount = Int.MAX_VALUE
    val anchor = infiniteCount / 2

    val listState = rememberLazyListState()

    val flingBehavior = rememberSnapFlingBehavior(listState)

    LaunchedEffect(items, initialSelectedItem) {
        if (items.isEmpty()) return@LaunchedEffect
        val realIndex = items.indexOf(initialSelectedItem).coerceAtLeast(0)
        val targetIndex = anchor - anchor % items.size + realIndex
        listState.scrollToItem(targetIndex - halfVisible)
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleCount)
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = infiniteCount,
                key = { it }
            ) { index ->

                val realIndex = index % items.size
                val item = items[realIndex]

                val layoutInfo = listState.layoutInfo
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

                val normalized = itemInfo?.let {
                    val itemCenter = it.offset + it.size / 2
                    val distance = abs(center - itemCenter).toFloat()
                    (distance / itemHeightPx).coerceIn(0f, 1f)
                } ?: 1f

                val scale = 1f - normalized * 0.25f
                val alpha = 1f - normalized * 0.6f

                val textColor = lerp(
                    MiuixTheme.colorScheme.primary,
                    MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    normalized
                )

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatItem(item),
                        style = MiuixTheme.textStyles.title2.copy(
                            fontSize = 22.sp,
                            color = textColor
                        )
                    )
                }
            }
        }

        // 渐变遮罩
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.White,
                        0.3f to Color.Transparent,
                        0.7f to Color.Transparent,
                        1f to Color.White
                    )
                )
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val layoutInfo = listState.layoutInfo
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val closest = layoutInfo.visibleItemsInfo.minByOrNull {
                    abs((it.offset + it.size / 2) - center)
                }

                closest?.let {
                    val newItem = items[it.index % items.size]
                    onSelectedItemChange(newItem)
                }
            }
    }

}


