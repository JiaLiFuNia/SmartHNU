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

public val WeatherIcon._2001: ImageVector
    get() {
        if (__2001 != null) {
            return __2001!!
        }
        __2001 = Builder(name = "_2001", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.0f, 7.0f)
                horizontalLineToRelative(6.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, false, -0.943f, -1.333f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, -0.943f, -0.334f)
                arcTo(2.0f, 2.0f, 0.0f, true, true, 8.0f, 8.0f)
                horizontalLineTo(2.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.0f, -1.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.079f, 6.375f)
                arcTo(2.5f, 2.5f, 0.0f, false, true, 16.0f, 7.0f)
                curveToRelative(0.0f, 1.397f, -1.24f, 2.5f, -2.5f, 2.5f)
                lineTo(0.5f, 9.5f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, 0.0f, -1.0f)
                horizontalLineToRelative(13.0f)
                curveToRelative(0.74f, 0.0f, 1.5f, -0.688f, 1.5f, -1.5f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, false, -2.953f, -0.375f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, -0.968f, -0.25f)
                close()
                moveTo(2.5f, 10.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 3.0f, 10.0f)
                horizontalLineToRelative(8.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, true, true, -1.886f, 2.667f)
                arcToRelative(0.5f, 0.5f, 0.0f, true, true, 0.943f, -0.334f)
                arcTo(1.0f, 1.0f, 0.0f, true, false, 11.0f, 11.0f)
                lineTo(3.0f, 11.0f)
                arcToRelative(0.5f, 0.5f, 0.0f, false, true, -0.5f, -0.5f)
                close()
                moveTo(2.642f, 0.394f)
                arcToRelative(0.164f, 0.164f, 0.0f, false, false, -0.284f, 0.0f)
                lineTo(0.022f, 4.444f)
                arcToRelative(0.162f, 0.162f, 0.0f, false, false, 0.142f, 0.244f)
                horizontalLineToRelative(4.672f)
                arcToRelative(0.162f, 0.162f, 0.0f, false, false, 0.142f, -0.243f)
                lineTo(2.642f, 0.395f)
                close()
                moveTo(2.111f, 1.734f)
                curveToRelative(-0.02f, -0.176f, 0.16f, -0.328f, 0.389f, -0.328f)
                curveToRelative(0.23f, 0.0f, 0.41f, 0.152f, 0.39f, 0.328f)
                lineTo(2.712f, 3.28f)
                horizontalLineToRelative(-0.426f)
                lineTo(2.11f, 1.734f)
                close()
                moveTo(2.815f, 3.906f)
                arcToRelative(0.312f, 0.312f, 0.0f, true, true, -0.625f, 0.0f)
                arcToRelative(0.312f, 0.312f, 0.0f, false, true, 0.625f, 0.0f)
                close()
            }
        }
        .build()
        return __2001!!
    }

private var __2001: ImageVector? = null
