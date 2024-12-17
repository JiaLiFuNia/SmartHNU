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

public val WeatherIcon._2307: ImageVector
    get() {
        if (__2307 != null) {
            return __2307!!
        }
        __2307 = Builder(name = "_2307", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(1.0f, 0.0f)
                lineTo(0.0f, 0.0f)
                verticalLineToRelative(16.0f)
                horizontalLineToRelative(1.0f)
                verticalLineToRelative(-1.0f)
                lineToRelative(8.0f, -3.5f)
                lineTo(1.0f, 8.0f)
                lineToRelative(8.0f, -3.5f)
                lineTo(1.0f, 1.0f)
                lineTo(1.0f, 0.0f)
                close()
                moveTo(13.17f, 0.473f)
                arcToRelative(0.197f, 0.197f, 0.0f, false, false, -0.34f, 0.0f)
                lineToRelative(-2.804f, 4.86f)
                curveToRelative(-0.075f, 0.13f, 0.02f, 0.292f, 0.17f, 0.292f)
                horizontalLineToRelative(5.607f)
                arcToRelative(0.194f, 0.194f, 0.0f, false, false, 0.17f, -0.291f)
                lineTo(13.17f, 0.473f)
                close()
                moveTo(12.533f, 2.081f)
                curveToRelative(-0.024f, -0.212f, 0.192f, -0.393f, 0.467f, -0.393f)
                reflectiveCurveToRelative(0.491f, 0.181f, 0.467f, 0.393f)
                lineToRelative(-0.211f, 1.857f)
                horizontalLineToRelative(-0.512f)
                lineToRelative(-0.21f, -1.857f)
                close()
                moveTo(13.378f, 4.688f)
                arcToRelative(0.375f, 0.375f, 0.0f, true, true, -0.75f, 0.0f)
                arcToRelative(0.375f, 0.375f, 0.0f, false, true, 0.75f, 0.0f)
                close()
            }
        }
        .build()
        return __2307!!
    }

private var __2307: ImageVector? = null
