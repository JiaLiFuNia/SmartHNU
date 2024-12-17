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

public val WeatherIcon._2029: ImageVector
    get() {
        if (__2029 != null) {
            return __2029!!
        }
        __2029 = Builder(name = "_2029", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.857f, 6.354f)
                arcToRelative(0.2f, 0.2f, 0.0f, false, true, -0.217f, -0.21f)
                lineTo(9.005f, 0.0f)
                lineToRelative(-5.77f, 8.647f)
                arcToRelative(0.2f, 0.2f, 0.0f, false, false, 0.186f, 0.31f)
                lineToRelative(2.738f, -0.28f)
                arcToRelative(0.2f, 0.2f, 0.0f, false, true, 0.22f, 0.21f)
                lineTo(6.002f, 16.0f)
                lineToRelative(6.761f, -9.65f)
                arcToRelative(0.2f, 0.2f, 0.0f, false, false, -0.18f, -0.314f)
                lineToRelative(-3.726f, 0.319f)
                close()
            }
        }
        .build()
        return __2029!!
    }

private var __2029: ImageVector? = null
