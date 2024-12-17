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

public val WeatherIcon._803: ImageVector
    get() {
        if (__803 != null) {
            return __803!!
        }
        __803 = Builder(name = "_803", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 0.0f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 0.0f, 16.0f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 8.0f, 0.0f)
                close()
                moveTo(8.0f, 15.0f)
                arcToRelative(6.73f, 6.73f, 0.0f, false, true, -0.948f, -0.072f)
                arcToRelative(0.486f, 0.486f, 0.0f, false, true, -0.24f, -0.106f)
                arcTo(8.838f, 8.838f, 0.0f, false, true, 3.962f, 8.0f)
                arcTo(8.868f, 8.868f, 0.0f, false, true, 6.76f, 1.22f)
                arcToRelative(0.702f, 0.702f, 0.0f, false, true, 0.359f, -0.157f)
                curveToRelative(0.292f, -0.04f, 0.586f, -0.062f, 0.881f, -0.063f)
                arcToRelative(7.0f, 7.0f, 0.0f, false, true, 0.0f, 14.0f)
                close()
            }
        }
        .build()
        return __803!!
    }

private var __803: ImageVector? = null
