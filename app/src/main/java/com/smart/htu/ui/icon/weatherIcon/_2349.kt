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

public val WeatherIcon._2349: ImageVector
    get() {
        if (__2349 != null) {
            return __2349!!
        }
        __2349 = Builder(name = "_2349", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 2.875f)
                lineTo(14.3f, 13.8f)
                lineTo(1.7f, 13.8f)
                lineTo(8.0f, 2.875f)
                close()
                moveTo(8.455f, 1.261f)
                arcToRelative(0.526f, 0.526f, 0.0f, false, false, -0.91f, 0.0f)
                lineTo(0.07f, 14.224f)
                curveToRelative(-0.2f, 0.346f, 0.052f, 0.776f, 0.454f, 0.776f)
                horizontalLineToRelative(14.952f)
                curveToRelative(0.402f, 0.0f, 0.654f, -0.43f, 0.454f, -0.776f)
                lineTo(8.455f, 1.26f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(9.955f, 7.84f)
                curveToRelative(-0.819f, 0.267f, -1.091f, 0.981f, -1.0f, 1.428f)
                curveToRelative(-0.637f, -0.714f, -0.637f, -1.518f, -0.546f, -2.768f)
                curveToRelative(-1.909f, 0.714f, -1.454f, 2.857f, -1.545f, 3.571f)
                curveToRelative(-0.455f, -0.446f, -0.546f, -1.339f, -0.546f, -1.339f)
                curveTo(5.773f, 9.0f, 5.5f, 9.714f, 5.5f, 10.34f)
                curveToRelative(0.0f, 1.429f, 1.182f, 2.411f, 2.545f, 2.411f)
                curveToRelative(1.364f, 0.0f, 2.455f, -1.071f, 2.455f, -2.41f)
                curveToRelative(0.0f, -0.983f, -0.545f, -1.25f, -0.545f, -2.5f)
                close()
            }
        }
        .build()
        return __2349!!
    }

private var __2349: ImageVector? = null
