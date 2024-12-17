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

public val WeatherIcon._807: ImageVector
    get() {
        if (__807 != null) {
            return __807!!
        }
        __807 = Builder(name = "_807", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.555f, 0.154f)
                curveToRelative(-0.042f, -0.009f, -0.085f, -0.012f, -0.129f, -0.02f)
                arcTo(7.981f, 7.981f, 0.0f, false, false, 9.795f, 0.04f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 0.0f, 15.92f)
                arcToRelative(8.12f, 8.12f, 0.0f, false, false, 0.631f, -0.094f)
                curveToRelative(0.043f, -0.008f, 0.087f, -0.011f, 0.13f, -0.02f)
                arcToRelative(7.998f, 7.998f, 0.0f, false, false, 0.0f, -15.692f)
                horizontalLineToRelative(-0.001f)
                close()
                moveTo(10.668f, 15.312f)
                arcToRelative(7.499f, 7.499f, 0.0f, true, true, 0.0f, -14.623f)
                arcToRelative(8.497f, 8.497f, 0.0f, false, false, 0.0f, 14.623f)
                close()
            }
        }
        .build()
        return __807!!
    }

private var __807: ImageVector? = null
