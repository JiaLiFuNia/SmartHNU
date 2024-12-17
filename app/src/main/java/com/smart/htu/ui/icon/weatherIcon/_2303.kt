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

public val WeatherIcon._2303: ImageVector
    get() {
        if (__2303 != null) {
            return __2303!!
        }
        __2303 = Builder(name = "_2303", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.003f, 4.0f)
                arcToRelative(0.933f, 0.933f, 0.0f, false, false, -0.934f, 0.933f)
                verticalLineToRelative(1.45f)
                lineToRelative(-1.256f, -0.725f)
                arcToRelative(0.933f, 0.933f, 0.0f, true, false, -0.933f, 1.617f)
                lineTo(6.136f, 8.0f)
                lineToRelative(-1.256f, 0.725f)
                arcToRelative(0.933f, 0.933f, 0.0f, true, false, 0.933f, 1.617f)
                lineToRelative(1.256f, -0.725f)
                verticalLineToRelative(1.45f)
                arcToRelative(0.933f, 0.933f, 0.0f, false, false, 1.867f, 0.0f)
                verticalLineToRelative(-1.45f)
                lineToRelative(1.256f, 0.725f)
                arcToRelative(0.933f, 0.933f, 0.0f, true, false, 0.933f, -1.617f)
                lineTo(9.87f, 8.0f)
                lineToRelative(1.256f, -0.725f)
                arcToRelative(0.933f, 0.933f, 0.0f, true, false, -0.933f, -1.617f)
                lineToRelative(-1.256f, 0.725f)
                verticalLineToRelative(-1.45f)
                arcTo(0.933f, 0.933f, 0.0f, false, false, 8.003f, 4.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(10.384f, 0.455f)
                lineToRelative(5.14f, 5.154f)
                arcToRelative(0.705f, 0.705f, 0.0f, false, true, 0.182f, 0.68f)
                lineToRelative(-1.889f, 7.047f)
                arcToRelative(0.704f, 0.704f, 0.0f, false, true, -0.497f, 0.497f)
                lineToRelative(-7.028f, 1.893f)
                arcToRelative(0.688f, 0.688f, 0.0f, false, true, -0.677f, -0.181f)
                lineToRelative(-5.14f, -5.154f)
                arcToRelative(0.705f, 0.705f, 0.0f, false, true, -0.18f, -0.679f)
                lineToRelative(1.888f, -7.047f)
                arcToRelative(0.705f, 0.705f, 0.0f, false, true, 0.496f, -0.498f)
                lineTo(9.707f, 0.274f)
                arcToRelative(0.693f, 0.693f, 0.0f, false, true, 0.677f, 0.181f)
                close()
                moveTo(6.322f, 14.263f)
                lineToRelative(6.245f, -1.683f)
                lineToRelative(1.678f, -6.263f)
                lineToRelative(-4.567f, -4.58f)
                lineToRelative(-6.245f, 1.684f)
                lineToRelative(-1.678f, 6.262f)
                lineToRelative(4.567f, 4.58f)
                close()
            }
        }
        .build()
        return __2303!!
    }

private var __2303: ImageVector? = null
