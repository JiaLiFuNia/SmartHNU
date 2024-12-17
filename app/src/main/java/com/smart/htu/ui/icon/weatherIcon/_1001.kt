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

public val WeatherIcon._1001: ImageVector
    get() {
        if (__1001 != null) {
            return __1001!!
        }
        __1001 = Builder(name = "_1001", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(9.21f, 1.491f)
                curveToRelative(-3.757f, -0.066f, -6.613f, 2.537f, -6.88f, 5.705f)
                curveToRelative(-0.292f, 3.494f, 2.107f, 4.947f, 2.428f, 5.232f)
                curveTo(2.853f, 12.4f, 0.585f, 10.28f, 0.204f, 8.296f)
                arcToRelative(0.104f, 0.104f, 0.0f, false, false, -0.1f, -0.085f)
                arcToRelative(0.103f, 0.103f, 0.0f, false, false, -0.103f, 0.114f)
                curveToRelative(0.403f, 3.526f, 3.405f, 6.2f, 6.79f, 6.186f)
                curveToRelative(3.604f, -0.016f, 6.518f, -2.147f, 6.89f, -5.646f)
                curveToRelative(0.35f, -3.295f, -2.108f, -5.008f, -2.424f, -5.292f)
                curveToRelative(2.023f, 0.02f, 4.162f, 2.15f, 4.54f, 4.133f)
                curveToRelative(0.008f, 0.048f, 0.05f, 0.084f, 0.098f, 0.085f)
                arcToRelative(0.102f, 0.102f, 0.0f, false, false, 0.1f, -0.071f)
                arcToRelative(0.103f, 0.103f, 0.0f, false, false, 0.004f, -0.043f)
                curveToRelative(-0.406f, -3.521f, -3.405f, -6.126f, -6.788f, -6.185f)
                close()
                moveTo(8.0f, 9.503f)
                arcTo(1.502f, 1.502f, 0.0f, true, true, 8.0f, 6.5f)
                arcToRelative(1.502f, 1.502f, 0.0f, false, true, 0.0f, 3.004f)
                close()
            }
        }
        .build()
        return __1001!!
    }

private var __1001: ImageVector? = null
