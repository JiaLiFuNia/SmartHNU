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

public val WeatherIcon._2031: ImageVector
    get() {
        if (__2031 != null) {
            return __2031!!
        }
        __2031 = Builder(name = "_2031", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(4.418f, 8.755f)
                lineToRelative(0.532f, -0.356f)
                verticalLineTo(2.5f)
                arcToRelative(1.3f, 1.3f, 0.0f, false, true, 2.6f, 0.0f)
                verticalLineToRelative(0.9f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, false, 1.2f, 0.0f)
                verticalLineToRelative(-0.9f)
                arcToRelative(2.5f, 2.5f, 0.0f, false, false, -5.0f, 0.0f)
                verticalLineToRelative(5.258f)
                arcToRelative(4.5f, 4.5f, 0.0f, true, false, 5.0f, 0.0f)
                verticalLineTo(6.6f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, false, -1.2f, 0.0f)
                verticalLineToRelative(1.799f)
                lineToRelative(0.532f, 0.356f)
                arcToRelative(3.3f, 3.3f, 0.0f, true, true, -3.665f, 0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.25f, 7.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, 0.5f)
                verticalLineToRelative(2.063f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, false, 1.0f, 0.0f)
                lineTo(6.75f, 7.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, false, -0.5f, -0.5f)
                close()
                moveTo(8.25f, 5.0f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, true, 0.6f, -0.6f)
                horizontalLineToRelative(4.8f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, true, 0.0f, 1.2f)
                horizontalLineToRelative(-4.8f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, true, -0.6f, -0.6f)
                close()
            }
        }
        .build()
        return __2031!!
    }

private var __2031: ImageVector? = null
