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

public val WeatherIcon._802: ImageVector
    get() {
        if (__802 != null) {
            return __802!!
        }
        __802 = Builder(name = "_802", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 0.0f)
                curveToRelative(-0.356f, 0.003f, -0.712f, 0.03f, -1.065f, 0.079f)
                arcToRelative(7.992f, 7.992f, 0.0f, false, false, -0.354f, 15.788f)
                horizontalLineToRelative(0.001f)
                curveToRelative(0.468f, 0.087f, 0.942f, 0.131f, 1.418f, 0.133f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 8.0f, 0.0f)
                close()
                moveTo(8.0f, 15.5f)
                arcToRelative(6.76f, 6.76f, 0.0f, false, true, -0.725f, -0.04f)
                arcTo(24.01f, 24.01f, 0.0f, false, false, 8.5f, 7.5f)
                arcTo(25.67f, 25.67f, 0.0f, false, false, 7.593f, 0.514f)
                curveTo(7.734f, 0.504f, 7.868f, 0.5f, 8.0f, 0.5f)
                arcToRelative(7.5f, 7.5f, 0.0f, true, true, 0.0f, 15.0f)
                close()
            }
        }
        .build()
        return __802!!
    }

private var __802: ImageVector? = null
