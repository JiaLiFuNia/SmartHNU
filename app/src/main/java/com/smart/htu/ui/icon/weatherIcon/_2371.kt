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

public val WeatherIcon._2371: ImageVector
    get() {
        if (__2371 != null) {
            return __2371!!
        }
        __2371 = Builder(name = "_2371", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 16.0f)
                arcTo(8.0f, 8.0f, 0.0f, true, true, 8.0f, 0.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, true, 0.0f, 16.0f)
                close()
                moveTo(8.0f, 14.7f)
                arcTo(6.7f, 6.7f, 0.0f, true, false, 8.0f, 1.3f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, false, 0.0f, 13.4f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.408f, 3.5f)
                horizontalLineToRelative(3.378f)
                curveToRelative(0.164f, 0.0f, 0.264f, 0.195f, 0.174f, 0.342f)
                lineTo(9.1f, 6.25f)
                arcToRelative(0.108f, 0.108f, 0.0f, false, false, 0.032f, 0.145f)
                curveToRelative(0.015f, 0.01f, 0.032f, 0.014f, 0.05f, 0.014f)
                horizontalLineToRelative(1.956f)
                curveToRelative(0.26f, 0.0f, 0.394f, 0.332f, 0.216f, 0.535f)
                lineTo(5.572f, 13.5f)
                lineTo(6.95f, 8.449f)
                arcToRelative(0.11f, 0.11f, 0.0f, false, false, -0.016f, -0.091f)
                arcToRelative(0.1f, 0.1f, 0.0f, false, false, -0.034f, -0.03f)
                arcToRelative(0.092f, 0.092f, 0.0f, false, false, -0.043f, -0.012f)
                horizontalLineToRelative(-2.0f)
                arcToRelative(0.282f, 0.282f, 0.0f, false, true, -0.147f, -0.041f)
                arcToRelative(0.307f, 0.307f, 0.0f, false, true, -0.109f, -0.115f)
                arcToRelative(0.333f, 0.333f, 0.0f, false, true, -0.004f, -0.314f)
                lineToRelative(2.501f, -4.151f)
                arcToRelative(0.352f, 0.352f, 0.0f, false, true, 0.31f, -0.195f)
                close()
            }
        }
        .build()
        return __2371!!
    }

private var __2371: ImageVector? = null
