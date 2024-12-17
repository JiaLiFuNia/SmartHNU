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

public val WeatherIcon._2105: ImageVector
    get() {
        if (__2105 != null) {
            return __2105!!
        }
        __2105 = Builder(name = "_2105", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.5f, 2.75f)
                arcToRelative(2.5f, 2.5f, 0.0f, false, false, -2.421f, 1.875f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, 0.968f, 0.25f)
                arcTo(1.5f, 1.5f, 0.0f, false, true, 15.0f, 5.25f)
                curveToRelative(0.0f, 0.812f, -0.76f, 1.5f, -1.5f, 1.5f)
                lineTo(0.5f, 6.75f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, 0.0f, 1.0f)
                horizontalLineToRelative(13.0f)
                curveToRelative(1.26f, 0.0f, 2.5f, -1.103f, 2.5f, -2.5f)
                arcToRelative(2.5f, 2.5f, 0.0f, false, false, -2.5f, -2.5f)
                close()
                moveTo(0.5f, 9.25f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, 0.0f, 1.0f)
                horizontalLineToRelative(11.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -0.943f, 1.333f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.943f, 0.334f)
                arcTo(2.0f, 2.0f, 0.0f, true, false, 11.5f, 9.25f)
                lineTo(0.5f, 9.25f)
                close()
            }
        }
        .build()
        return __2105!!
    }

private var __2105: ImageVector? = null
