package com.smart.htu.screens.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.blur.Backdrop
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    label: String? = null,
    titlePaddingValues: PaddingValues = PaddingValues(start = 12.dp, bottom = 8.dp, top = 16.dp),
    backdrop: Backdrop? = null,
    blurRadius: Float = BlurDefaults.BlurRadius,
    noiseCoefficient: Float = BlurDefaults.NoiseCoefficient,
    cardBlend: List<BlendColorEntry> = emptyList(),
    brightness: Float = 0f,
    contrast: Float = 1f,
    saturation: Float = 1f,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (label != null)
            SmallTitle(
                text = label,
                insideMargin = titlePaddingValues
            )
        Card(
            modifier = Modifier
                .then(
                    if (backdrop != null) {
                        Modifier
                            .textureBlur(
                                backdrop = backdrop,
                                shape = RoundedCornerShape(16.dp),
                                blurRadius = blurRadius,
                                noiseCoefficient = noiseCoefficient,
                                colors = BlurColors(
                                    blendColors = cardBlend,
                                    brightness = brightness,
                                    contrast = contrast,
                                    saturation = saturation,
                                ),
                            )
                    } else {
                        Modifier
                    },
                ),
            colors = CardDefaults.defaultColors(
                if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.surfaceContainer,
                if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.onSurfaceContainer,
            ),
        ) {
            content()
        }
    }
}