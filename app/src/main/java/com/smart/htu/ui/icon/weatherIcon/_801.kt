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

public val WeatherIcon._801: ImageVector
    get() {
        if (__801 != null) {
            return __801!!
        }
        __801 = Builder(name = "_801", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(9.0f, 0.0f)
                curveToRelative(-0.265f, 0.0f, -0.53f, 0.013f, -0.795f, 0.04f)
                arcToRelative(7.985f, 7.985f, 0.0f, false, false, -0.631f, 0.094f)
                curveToRelative(-0.043f, 0.008f, -0.087f, 0.011f, -0.13f, 0.02f)
                arcToRelative(7.998f, 7.998f, 0.0f, false, false, 0.0f, 15.692f)
                curveToRelative(0.043f, 0.009f, 0.087f, 0.012f, 0.13f, 0.02f)
                curveToRelative(0.208f, 0.037f, 0.417f, 0.073f, 0.631f, 0.094f)
                arcTo(8.0f, 8.0f, 0.0f, true, false, 9.0f, 0.0f)
                close()
                moveTo(9.0f, 15.5f)
                arcToRelative(7.46f, 7.46f, 0.0f, false, true, -1.668f, -0.188f)
                arcToRelative(8.497f, 8.497f, 0.0f, false, false, 0.0f, -14.623f)
                arcTo(7.5f, 7.5f, 0.0f, true, true, 9.0f, 15.5f)
                close()
            }
        }
        .build()
        return __801!!
    }

private var __801: ImageVector? = null
