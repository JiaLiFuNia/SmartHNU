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

public val WeatherIcon._2032: ImageVector
    get() {
        if (__2032 != null) {
            return __2032!!
        }
        __2032 = Builder(name = "_2032", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.5f, 6.0f)
                curveToRelative(0.0f, -0.355f, -0.046f, -0.7f, -0.133f, -1.027f)
                arcToRelative(2.5f, 2.5f, 0.0f, true, false, -2.84f, -2.84f)
                arcTo(4.005f, 4.005f, 0.0f, false, false, 5.5f, 6.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, true, false, 4.88f, 3.903f)
                arcTo(4.002f, 4.002f, 0.0f, false, false, 13.5f, 6.0f)
                close()
                moveTo(14.5f, 2.5f)
                arcTo(1.5f, 1.5f, 0.0f, false, true, 12.965f, 4.0f)
                arcTo(4.02f, 4.02f, 0.0f, false, false, 11.5f, 2.535f)
                lineTo(11.5f, 2.5f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 3.0f, 0.0f)
                close()
                moveTo(10.06f, 8.948f)
                arcTo(5.009f, 5.009f, 0.0f, false, false, 6.503f, 6.1f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, 3.56f, 2.847f)
                close()
                moveTo(9.5f, 11.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, true, true, -8.0f, 0.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, false, true, 8.0f, 0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(5.005f, 8.605f)
                curveToRelative(-0.326f, 0.197f, -0.553f, 0.635f, -0.496f, 0.997f)
                lineToRelative(0.345f, 2.198f)
                horizontalLineToRelative(1.078f)
                lineToRelative(0.358f, -2.277f)
                arcToRelative(0.846f, 0.846f, 0.0f, false, false, -0.408f, -0.853f)
                lineToRelative(-0.07f, -0.042f)
                curveToRelative(-0.256f, -0.156f, -0.55f, -0.176f, -0.807f, -0.023f)
                close()
                moveTo(5.4f, 13.5f)
                arcToRelative(0.6f, 0.6f, 0.0f, true, false, 0.0f, -1.2f)
                arcToRelative(0.6f, 0.6f, 0.0f, false, false, 0.0f, 1.2f)
                close()
            }
        }
        .build()
        return __2032!!
    }

private var __2032: ImageVector? = null
