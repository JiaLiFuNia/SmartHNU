package com.smart.htu.ui.icon.weatherIcon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.smart.htu.ui.icon.WeatherIcon

public val WeatherIcon._1018: ImageVector
    get() {
        if (__1018 != null) {
            return __1018!!
        }
        __1018 = Builder(name = "_1018", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.86f, 12.447f)
                arcToRelative(2.807f, 2.807f, 0.0f, true, false, 0.0f, -5.614f)
                arcToRelative(2.807f, 2.807f, 0.0f, false, false, 0.0f, 5.614f)
                close()
                moveTo(7.86f, 5.676f)
                curveToRelative(0.973f, 0.0f, 1.636f, 0.475f, 1.833f, 0.628f)
                curveToRelative(0.013f, -0.169f, 0.026f, -0.382f, 0.027f, -0.628f)
                curveToRelative(0.015f, -2.353f, -0.956f, -5.15f, -1.856f, -5.15f)
                curveToRelative(-0.678f, 0.0f, -1.849f, 2.306f, -1.849f, 5.15f)
                curveToRelative(0.0f, 0.211f, 0.016f, 0.412f, 0.027f, 0.617f)
                arcToRelative(3.018f, 3.018f, 0.0f, false, true, 1.818f, -0.617f)
                close()
                moveTo(4.528f, 11.776f)
                arcToRelative(3.022f, 3.022f, 0.0f, false, true, -0.372f, -1.901f)
                arcToRelative(8.77f, 8.77f, 0.0f, false, false, -0.558f, 0.29f)
                curveTo(1.553f, 11.328f, -0.384f, 13.567f, 0.066f, 14.347f)
                curveToRelative(0.339f, 0.587f, 2.921f, 0.449f, 5.384f, -0.973f)
                curveToRelative(0.183f, -0.106f, 0.349f, -0.22f, 0.52f, -0.332f)
                arcToRelative(3.016f, 3.016f, 0.0f, false, true, -1.442f, -1.266f)
                close()
                moveTo(12.399f, 10.014f)
                arcToRelative(9.015f, 9.015f, 0.0f, false, false, -0.547f, -0.285f)
                arcToRelative(3.021f, 3.021f, 0.0f, false, true, -0.375f, 1.883f)
                arcToRelative(3.013f, 3.013f, 0.0f, false, true, -1.46f, 1.273f)
                curveToRelative(0.175f, 0.114f, 0.344f, 0.23f, 0.53f, 0.338f)
                curveToRelative(2.463f, 1.422f, 5.046f, 1.559f, 5.388f, 0.967f)
                curveToRelative(0.45f, -0.779f, -1.492f, -3.015f, -3.536f, -4.176f)
                close()
            }
        }
        .build()
        return __1018!!
    }

private var __1018: ImageVector? = null
