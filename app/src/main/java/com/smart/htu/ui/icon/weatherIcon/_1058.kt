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

public val WeatherIcon._1058: ImageVector
    get() {
        if (__1058 != null) {
            return __1058!!
        }
        __1058 = Builder(name = "_1058", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
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
                moveTo(12.935f, 10.602f)
                curveToRelative(-0.085f, 0.0f, -0.141f, -0.08f, -0.103f, -0.15f)
                lineToRelative(1.267f, -2.3f)
                curveToRelative(0.038f, -0.07f, -0.018f, -0.152f, -0.103f, -0.152f)
                lineTo(11.47f, 8.0f)
                arcToRelative(0.234f, 0.234f, 0.0f, false, false, -0.122f, 0.031f)
                arcToRelative(0.253f, 0.253f, 0.0f, false, false, -0.092f, 0.092f)
                lineToRelative(-2.235f, 4.22f)
                curveToRelative(-0.067f, 0.125f, 0.035f, 0.27f, 0.189f, 0.27f)
                horizontalLineToRelative(2.272f)
                curveToRelative(0.08f, 0.0f, 0.135f, 0.07f, 0.11f, 0.139f)
                lineToRelative(-1.182f, 3.11f)
                curveToRelative(-0.044f, 0.11f, 0.115f, 0.188f, 0.198f, 0.1f)
                lineToRelative(4.765f, -5.189f)
                curveToRelative(0.062f, -0.067f, 0.01f, -0.171f, -0.088f, -0.171f)
                horizontalLineToRelative(-2.35f)
                close()
            }
        }
        .build()
        return __1058!!
    }

private var __1058: ImageVector? = null
