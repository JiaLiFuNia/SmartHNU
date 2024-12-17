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

public val WeatherIcon._2364: ImageVector
    get() {
        if (__2364 != null) {
            return __2364!!
        }
        __2364 = Builder(name = "_2364", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.0f, 16.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.0f, -6.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, false, 0.0f, 6.0f)
                close()
                moveTo(12.533f, 11.706f)
                curveToRelative(-0.024f, -0.212f, 0.192f, -0.393f, 0.467f, -0.393f)
                reflectiveCurveToRelative(0.491f, 0.181f, 0.467f, 0.393f)
                lineToRelative(-0.211f, 1.857f)
                horizontalLineToRelative(-0.512f)
                lineToRelative(-0.21f, -1.857f)
                close()
                moveTo(13.378f, 14.313f)
                arcToRelative(0.375f, 0.375f, 0.0f, true, true, -0.75f, 0.0f)
                arcToRelative(0.375f, 0.375f, 0.0f, false, true, 0.75f, 0.0f)
                close()
                moveTo(1.0f, 0.0f)
                lineTo(0.0f, 0.0f)
                verticalLineToRelative(16.0f)
                horizontalLineToRelative(1.0f)
                lineTo(1.0f, 7.752f)
                curveToRelative(0.75f, 0.836f, 2.0f, 1.135f, 4.703f, 0.431f)
                curveTo(8.228f, 7.525f, 11.0f, 9.0f, 11.0f, 9.0f)
                lineTo(11.0f, 1.81f)
                curveTo(8.12f, 0.65f, 6.717f, 1.003f, 4.57f, 1.54f)
                lineToRelative(-0.303f, 0.075f)
                curveTo(1.945f, 2.193f, 1.0f, 1.0f, 1.0f, 1.0f)
                lineTo(1.0f, 0.0f)
                close()
                moveTo(4.0f, 3.0f)
                lineTo(8.0f, 2.5f)
                verticalLineToRelative(4.0f)
                lineTo(4.0f, 7.0f)
                lineTo(4.0f, 3.0f)
                close()
            }
        }
        .build()
        return __2364!!
    }

private var __2364: ImageVector? = null
