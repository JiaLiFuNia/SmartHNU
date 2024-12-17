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

public val WeatherIcon._2111: ImageVector
    get() {
        if (__2111 != null) {
            return __2111!!
        }
        __2111 = Builder(name = "_2111", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 9.8f)
                arcToRelative(1.8f, 1.8f, 0.0f, true, true, 0.0f, -3.6f)
                arcToRelative(1.8f, 1.8f, 0.0f, false, true, 0.0f, 3.6f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(14.0f, 8.0f)
                curveToRelative(0.0f, 1.167f, -3.59f, 3.5f, -6.0f, 3.5f)
                reflectiveCurveTo(2.0f, 9.167f, 2.0f, 8.0f)
                curveToRelative(0.0f, -1.167f, 3.59f, -3.5f, 6.0f, -3.5f)
                reflectiveCurveToRelative(6.0f, 2.333f, 6.0f, 3.5f)
                close()
                moveTo(12.75f, 7.789f)
                lineTo(12.751f, 7.786f)
                lineTo(12.747f, 7.794f)
                arcToRelative(0.11f, 0.11f, 0.0f, false, false, 0.003f, -0.005f)
                close()
                moveTo(12.491f, 7.889f)
                curveToRelative(-0.278f, -0.286f, -0.71f, -0.623f, -1.245f, -0.949f)
                curveTo(10.14f, 6.267f, 8.875f, 5.812f, 8.0f, 5.812f)
                reflectiveCurveToRelative(-2.14f, 0.455f, -3.246f, 1.128f)
                curveToRelative(-0.536f, 0.326f, -0.967f, 0.663f, -1.245f, 0.948f)
                curveToRelative(-0.04f, 0.04f, -0.074f, 0.078f, -0.104f, 0.112f)
                curveToRelative(0.03f, 0.034f, 0.064f, 0.071f, 0.104f, 0.112f)
                curveToRelative(0.278f, 0.285f, 0.71f, 0.622f, 1.245f, 0.948f)
                curveTo(5.86f, 9.733f, 7.125f, 10.187f, 8.0f, 10.187f)
                reflectiveCurveToRelative(2.14f, -0.454f, 3.246f, -1.127f)
                curveToRelative(0.536f, -0.326f, 0.967f, -0.663f, 1.245f, -0.948f)
                curveToRelative(0.04f, -0.04f, 0.075f, -0.078f, 0.104f, -0.112f)
                arcToRelative(2.783f, 2.783f, 0.0f, false, false, -0.104f, -0.112f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(16.0f, 8.0f)
                arcTo(8.0f, 8.0f, 0.0f, true, true, 0.0f, 8.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, true, 16.0f, 0.0f)
                close()
                moveTo(14.7f, 8.0f)
                arcToRelative(6.67f, 6.67f, 0.0f, false, false, -1.352f, -4.037f)
                lineToRelative(-9.385f, 9.385f)
                arcTo(6.7f, 6.7f, 0.0f, false, false, 14.7f, 8.0f)
                close()
                moveTo(12.315f, 2.874f)
                arcToRelative(6.7f, 6.7f, 0.0f, false, false, -9.44f, 9.44f)
                lineToRelative(9.44f, -9.44f)
                close()
            }
        }
        .build()
        return __2111!!
    }

private var __2111: ImageVector? = null
