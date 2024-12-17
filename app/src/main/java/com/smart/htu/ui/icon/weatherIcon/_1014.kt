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

public val WeatherIcon._1014: ImageVector
    get() {
        if (__1014 != null) {
            return __1014!!
        }
        __1014 = Builder(name = "_1014", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.058f, 0.0f)
                horizontalLineToRelative(5.403f)
                curveToRelative(0.264f, 0.0f, 0.422f, 0.311f, 0.279f, 0.548f)
                lineToRelative(-2.975f, 3.85f)
                arcToRelative(0.173f, 0.173f, 0.0f, false, false, 0.05f, 0.232f)
                arcToRelative(0.147f, 0.147f, 0.0f, false, false, 0.08f, 0.024f)
                horizontalLineToRelative(3.13f)
                curveToRelative(0.416f, 0.0f, 0.63f, 0.53f, 0.345f, 0.855f)
                lineTo(4.12f, 16.0f)
                lineToRelative(2.203f, -8.082f)
                arcToRelative(0.177f, 0.177f, 0.0f, false, false, -0.025f, -0.146f)
                arcToRelative(0.159f, 0.159f, 0.0f, false, false, -0.055f, -0.048f)
                arcToRelative(0.148f, 0.148f, 0.0f, false, false, -0.07f, -0.018f)
                horizontalLineTo(2.976f)
                arcToRelative(0.451f, 0.451f, 0.0f, false, true, -0.236f, -0.067f)
                arcToRelative(0.49f, 0.49f, 0.0f, false, true, -0.173f, -0.183f)
                arcToRelative(0.532f, 0.532f, 0.0f, false, true, -0.006f, -0.503f)
                lineTo(6.56f, 0.311f)
                curveTo(6.66f, 0.119f, 6.85f, 0.0f, 7.057f, 0.0f)
                close()
            }
        }
        .build()
        return __1014!!
    }

private var __1014: ImageVector? = null
