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

public val WeatherIcon._2366: ImageVector
    get() {
        if (__2366 != null) {
            return __2366!!
        }
        __2366 = Builder(name = "_2366", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.502f, 6.62f)
                curveToRelative(-0.026f, -0.226f, 0.205f, -0.42f, 0.498f, -0.42f)
                curveToRelative(0.293f, 0.0f, 0.524f, 0.194f, 0.498f, 0.42f)
                lineTo(8.273f, 8.6f)
                horizontalLineToRelative(-0.546f)
                lineToRelative(-0.225f, -1.98f)
                close()
                moveTo(8.403f, 9.4f)
                arcToRelative(0.4f, 0.4f, 0.0f, true, true, -0.8f, 0.0f)
                arcToRelative(0.4f, 0.4f, 0.0f, false, true, 0.8f, 0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.086f, 0.0f)
                curveTo(8.174f, 0.5f, 5.28f, 1.667f, 3.408f, 3.5f)
                arcToRelative(6.261f, 6.261f, 0.0f, false, false, 0.0f, 9.0f)
                arcToRelative(6.493f, 6.493f, 0.0f, false, false, 2.52f, 1.533f)
                lineTo(3.92f, 16.0f)
                curveToRelative(3.913f, -0.5f, 6.807f, -1.667f, 8.679f, -3.5f)
                arcToRelative(6.274f, 6.274f, 0.0f, false, false, 0.0f, -9.0f)
                arcToRelative(6.495f, 6.495f, 0.0f, false, false, -2.52f, -1.533f)
                lineTo(12.085f, 0.0f)
                close()
                moveTo(11.0f, 8.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -6.0f, 0.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, true, 6.0f, 0.0f)
                close()
            }
        }
        .build()
        return __2366!!
    }

private var __2366: ImageVector? = null
