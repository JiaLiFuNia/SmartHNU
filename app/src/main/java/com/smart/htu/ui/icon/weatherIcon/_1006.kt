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

public val WeatherIcon._1006: ImageVector
    get() {
        if (__1006 != null) {
            return __1006!!
        }
        __1006 = Builder(name = "_1006", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.497f, 3.077f)
                reflectiveCurveTo(7.525f, 0.722f, 4.845f, 0.023f)
                arcToRelative(0.794f, 0.794f, 0.0f, false, false, -0.956f, 0.568f)
                lineTo(0.024f, 15.0f)
                arcToRelative(0.81f, 0.81f, 0.0f, false, false, 0.544f, 0.968f)
                arcToRelative(0.811f, 0.811f, 0.0f, false, false, 1.0f, -0.554f)
                lineToRelative(1.671f, -6.23f)
                lineToRelative(12.373f, -4.817f)
                arcToRelative(0.696f, 0.696f, 0.0f, false, false, -0.115f, -1.291f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.756f, 7.71f)
                arcToRelative(0.296f, 0.296f, 0.0f, false, false, -0.512f, 0.0f)
                lineTo(7.04f, 15.0f)
                arcToRelative(0.292f, 0.292f, 0.0f, false, false, 0.256f, 0.438f)
                horizontalLineToRelative(8.41f)
                arcToRelative(0.292f, 0.292f, 0.0f, false, false, 0.256f, -0.437f)
                lineTo(11.756f, 7.71f)
                close()
                moveTo(10.8f, 10.12f)
                curveToRelative(-0.036f, -0.317f, 0.287f, -0.59f, 0.7f, -0.59f)
                curveToRelative(0.412f, 0.0f, 0.736f, 0.273f, 0.7f, 0.59f)
                lineToRelative(-0.316f, 2.785f)
                horizontalLineToRelative(-0.768f)
                lineTo(10.8f, 10.12f)
                close()
                moveTo(12.067f, 14.03f)
                arcToRelative(0.563f, 0.563f, 0.0f, true, true, -1.125f, 0.0f)
                arcToRelative(0.563f, 0.563f, 0.0f, false, true, 1.125f, 0.0f)
                close()
            }
        }
        .build()
        return __1006!!
    }

private var __1006: ImageVector? = null
